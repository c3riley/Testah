package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.runner.http.load.TestTimingGetRequestGenerator;
import org.testah.runner.performance.dto.SequenceExecData;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testah.runner.http.load.request.SimpleRequest.PATH_DELAYED;
import static org.testah.runner.http.load.request.SimpleRequest.PATH_NORMAL;
import static org.testah.util.PortUtil.getFreePort;

public class SenderThreadLoadTest extends AbstractLoadTest
{
    private static final int longResponseTime = 4000;
    private static final int shortResponseTime = 200;
    private static final int numberOfChunks = 900;
    private static final int chunkSize = 2;
    private static final String serviceUnderTest = "SenderThreadLoadTest";

    WireMockServer wm;

    /**
     * Setup of test: configure and start the Wiremock server.
     */
    @Before
    public void setupBase()
    {
        wm = new WireMockServer(options().port(getFreePort()));
        wm.start();
        wm.stubFor(get(PATH_DELAYED).willReturn(aResponse().withStatus(HttpStatus.SC_ACCEPTED).withFixedDelay(longResponseTime)));
        wm.stubFor(get(PATH_NORMAL).willReturn(aResponse().withStatus(HttpStatus.SC_OK).withFixedDelay(shortResponseTime)));
    }

    @After
    public void teardown() {
        wm.stop();
    }

    @Test
    public void test() throws Exception
    {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod);
        TestTimingGetRequestGenerator testTimingGetRequestGenerator =
            new TestTimingGetRequestGenerator(wm.baseUrl(), 0, chunkSize, numberOfChunks);
        ExecutionTimePublisher executionTimePublisher = new ExecutionTimePublisher();
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();
        initialize(testTimingGetRequestGenerator, runProps, executionTimePublisher, chunkStatsLogPublisher);
        SequenceExecData sequenceExecData = runTest(getRunStepFile(this.getClass()));
        TS.asserts().isLessThan("", 60, sequenceExecData.getByStepCount(0).getReceiveCount());
        TS.asserts().isLessThan("", 60, sequenceExecData.getByStepCount(0).getSendCount());
        TS.asserts().isGreaterThan("", 180, sequenceExecData.getByStepCount(1).getReceiveCount());
        TS.asserts().isGreaterThan("", 180, sequenceExecData.getByStepCount(1).getSendCount());
        executionTimePublisher.getStartTimes();
    }
}
