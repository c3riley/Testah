package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.http.load.SimpleGetRequestGenerator;
import org.testah.runner.performance.dto.ExecData;
import org.testah.runner.performance.dto.SequenceExecData;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestPlan(testType = TestType.AUTOMATED)
public class TestLoadTest extends AbstractLoadTest
{
    public static final int RESPONSE_TIME_MILLIS = 10;
    public static final String URL_PATH_GET = "/getpath";
    public static final String SERVICE_UNDER_TEST = "wiremocksvc";
    WireMockServer wm;

    @Before
    public void setup() {
        HttpAkkaRunner.reset();
        wm = new WireMockServer(options().port(2348));
        wm.start();
        wm.stubFor(get(URL_PATH_GET).willReturn(aResponse().withStatus(HttpStatus.SC_ACCEPTED).withFixedDelay(RESPONSE_TIME_MILLIS)));
    }

    @TestCase
    @Test
    public void runLoadTest() throws Exception
    {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(SERVICE_UNDER_TEST, testClass, testMethod).setNumberOfChunks(500);
        TestDataGenerator simpleGetRequestGenerator = new SimpleGetRequestGenerator(wm.url(URL_PATH_GET));
        initialize(simpleGetRequestGenerator, runProps);
        SequenceExecData sequenceExecData = runTest(getRunStepFile(this.getClass()));

        // validation
        TS.asserts().isGreaterThan("Send count should be greater of equal to receive count",
            sequenceExecData.getReceiveCount(), sequenceExecData.getSendCount(), true);
        long totalMillis = Duration.between(sequenceExecData.getStart(), sequenceExecData.getStop()).toMillis();
        TS.asserts().isGreaterThan("total execution time", 40000L, totalMillis, true);
        TS.asserts().isLessThan("total execution time", 45000L, totalMillis, true);
        TS.asserts().equalsTo("step count", 3, sequenceExecData.getStepCount());
        long elapsedTime = 0L;
        for (int count = 0; count < 3; count++)
        {
            ExecData execData = sequenceExecData.getByStepCount(count);
            TS.asserts().equalsTo("step id", count + 1, execData.getStepId().intValue());
            elapsedTime += Duration.between(execData.getStart(), execData.getStop()).toMillis();
        }
        TS.asserts().isGreaterThan("total execution should be greater than time added elapsed times",
            elapsedTime, totalMillis, true);
    }
}
