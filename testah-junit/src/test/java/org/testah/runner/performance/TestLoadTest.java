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

import java.io.IOException;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testah.util.PortUtil.getFreePort;

@TestPlan(testType = TestType.AUTOMATED)
public class TestLoadTest extends AbstractLoadTest
{
    public static final int RESPONSE_TIME_MILLIS = 1;
    public static final String URL_PATH_GET = "/getpath";
    public static final String SERVICE_UNDER_TEST = "wiremocksvc";
    WireMockServer wm;

    /**
     * Setup of test: configure and start the Wiremock server.
     */
    @Before
    public void setup() throws IOException
    {
        HttpAkkaRunner.reset();
        wm = new WireMockServer(options().port(getFreePort()));
        wm.start();
        wm.stubFor(get(urlPathEqualTo(URL_PATH_GET)).willReturn(aResponse()
            .withStatus(HttpStatus.SC_ACCEPTED).withFixedDelay(RESPONSE_TIME_MILLIS)));
    }

    @TestCase
    @Test
    public void runLoadTest() throws Exception
    {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(SERVICE_UNDER_TEST, testClass, testMethod).setNumberOfChunks(10);
        TestDataGenerator simpleGetRequestGenerator = new SimpleGetRequestGenerator(wm.url(URL_PATH_GET));
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();
        initialize(simpleGetRequestGenerator, runProps, chunkStatsLogPublisher);
        SequenceExecData sequenceExecData = runTest(getRunStepFile(this.getClass()));

        // validation
        TS.asserts().isGreaterThan("Send count should be greater of equal to receive count",
            sequenceExecData.getSendCount(), sequenceExecData.getReceiveCount(), true);
        long totalMillis = Duration.between(sequenceExecData.getStart(), sequenceExecData.getStop()).toMillis();
        TS.asserts().isGreaterThan("total execution time", 40000L, totalMillis, true);
        TS.asserts().isLessThan("total execution time", 45000L, totalMillis, true);
        TS.asserts().equalsTo("step count", 4, sequenceExecData.getStepCount());
        long elapsedTime = 0L;
        for (int count = 0; count < 4; count++)
        {
            ExecData execData = sequenceExecData.getByStepCount(count);
            TS.asserts().equalsTo("step id", count + 1, execData.getStepId().intValue());
            elapsedTime += Duration.between(execData.getStart(), execData.getStop()).toMillis();
        }
        isBetweenNumbers(sequenceExecData.getByStepCount(0).getSendCount(), 9L, 11L);
        isBetweenNumbers(sequenceExecData.getByStepCount(1).getSendCount(), 17L, 23L);
        isBetweenNumbers(sequenceExecData.getByStepCount(2).getSendCount(), 36L, 44L);
        isBetweenNumbers(sequenceExecData.getByStepCount(3).getSendCount(), 32L, 48L);
        isBetweenNumbers(sequenceExecData.getSendCount(), 98L, 122L);
        isBetweenNumbers(sequenceExecData.getByStepCount(0).getReceiveCount(), 9L, 11L);
        isBetweenNumbers(sequenceExecData.getByStepCount(1).getReceiveCount(), 17L, 23L);
        isBetweenNumbers(sequenceExecData.getByStepCount(2).getReceiveCount(), 36L, 44L);
        isBetweenNumbers(sequenceExecData.getByStepCount(3).getReceiveCount(), 32L, 48L);
        isBetweenNumbers(sequenceExecData.getReceiveCount(), 98L, 122L);
        TS.asserts().isGreaterThan("total execution should be greater than time added elapsed times",
            elapsedTime, totalMillis, true);
    }

    /**
     * Verify the given number is between the provided lower and upper bound.
     * @param actualNumber  number to check
     * @param lowerBound    lower bound, actual number should be bigger or equal
     * @param upperBound    upper bound, actual number should be smaller or equal
     */
    public void isBetweenNumbers(final Long actualNumber, final Long lowerBound, final Long upperBound) {
        String message = String.format("Expect '%d' (lower bound) < '%d' (actual) < '%d' (upper bound)",
            lowerBound, actualNumber, upperBound);
        TS.asserts().isTrue(message, actualNumber <= upperBound && actualNumber >= lowerBound);
    }
}
