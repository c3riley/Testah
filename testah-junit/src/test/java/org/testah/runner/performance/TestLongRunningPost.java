package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.testah.TS;
import org.testah.runner.http.load.TestServicePostRequestGenerator;
import org.testah.runner.performance.dto.ExecData;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.testah.util.PortUtil.getFreePort;

public class TestLongRunningPost extends TestLongRunningBase {
    private static final int port = getFreePort();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    @Test
    public void testPost() throws Exception {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elasticSearchUrl = baseUrl + port;
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
            new ElasticSearchResponseTimesPublisher(elasticSearchUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        String domain = new TestServicePostRequestGenerator(chunkSize, numberOfChunks).getDomain();
        setupWiremock(wireMockRule, elasticSearchExecutionStatsPublisher, domain, testClass, testMethod);

        ExecData execData = executeTest(new TestServicePostRequestGenerator(chunkSize, numberOfChunks),
            runProps.setVerbose(true).setRunDuration(runDuration),
            elasticSearchExecutionStatsPublisher,
            chunkStatsLogPublisher);

        TS.asserts().isGreaterThan("check run duration", runDuration, Duration.between(execData.getStart(), execData.getStop()).toMillis());
        TS.asserts().isGreaterThan("some messages should be received", 1, execData.getReceiveCount());
        TS.asserts().isGreaterThan("the send count should be larger or equal the receive count",
            execData.getReceiveCount(), execData.getSendCount(), true);

        // verify data was at least published once
        verify(postRequestedFor(urlEqualTo("/testah/load/_bulk"))
            .withRequestBody(matching(requestRegexIndexCounter))
            .withRequestBody(matching(String.format(requestRegexSingle, domain,  testClass, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain,  testClass, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain,  testClass, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain,  testClass, testMethod)))
            .withRequestBody(matching(String.format(requestRegexChunk, domain,  testClass, testMethod)))
        );
    }
}
