package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestPlan;
import org.testah.runner.http.load.TestTimingGetRequestGenerator;

import java.util.Comparator;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testah.runner.http.load.request.SimpleRequest.PATH_DELAYED;
import static org.testah.runner.http.load.request.SimpleRequest.PATH_NORMAL;
import static org.testah.util.PortUtil.getFreePort;

@TestPlan(testType = TestType.AUTOMATED)
public class SlowLongRunningTest extends AbstractLongRunningTest
{
    public static final int LONG_RESPONSE_TIME = 4000;
    public static final int SHORT_RESPONSE_TIME = 10;
    protected static final int numberOfChunks = 900;
    protected static final int chunkSize = 5;
    protected static final int numThreads = 1;
    protected static final long millisBetweenChunks = 970L;
    protected static final long runDuration = 10000L;
    protected static final String serviceUnderTest = "SlowLongRunningTest";

    WireMockServer wm;

    /**
     * Setup of test: configure and start the Wiremock server.
     */
    @Before
    public void setupBase()
    {
        // setup Mock Rest Service
        wm = new WireMockServer(options().port(getFreePort()));
        wm.start();
        wm.stubFor(get(PATH_DELAYED).willReturn(aResponse().withStatus(HttpStatus.SC_ACCEPTED).withFixedDelay(LONG_RESPONSE_TIME)));
        wm.stubFor(get(PATH_NORMAL).willReturn(aResponse().withStatus(HttpStatus.SC_OK).withFixedDelay(SHORT_RESPONSE_TIME)));
    }

    @After
    public void teardown()
    {
        wm.stop();
    }

    @Test
    public void runTest() throws Exception
    {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, numThreads, millisBetweenChunks)
                .setNumberOfSenderThreads(10)
                .setVerbose(false);

        TestTimingGetRequestGenerator testTimingGetRequestGenerator =
            new TestTimingGetRequestGenerator(wm.baseUrl(), 10, chunkSize, numberOfChunks);

        ExecutionTimePublisher executionTimePublisher = new ExecutionTimePublisher();
        executeTest(
            testTimingGetRequestGenerator,
            runProps.setVerbose(false).setRunDuration(runDuration),
            executionTimePublisher);

        executionTimePublisher.getStartTimeSpacing().forEach(
            period -> TS.asserts().isLessThan("Check limit on period between requests.", 2100, period));
        TS.log().info(String.format("Sorted spacing:%n%s", executionTimePublisher.getStartTimeSpacing().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())));
        TS.asserts().isGreaterThan("Expect at requests with long execution time.", 0,
            executionTimePublisher.getElapsedTimes().stream().filter(responseTime -> responseTime >= LONG_RESPONSE_TIME).count());
    }
}
