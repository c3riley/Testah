package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.testah.runner.http.load.TestServiceGetRequestGenerator;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.testah.util.PortUtil.getFreePort;

public class TestLongRunningGet extends TestLongRunningBase {
    private static final int port = getFreePort();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    @Test
    public void testGet() throws Exception {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elasticSearchUrl = baseUrl + port;
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
            new ElasticSearchResponseTimesPublisher(elasticSearchUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        String domain = new TestServiceGetRequestGenerator(chunkSize, numberOfChunks).getDomain();
        setupWiremock(wireMockRule, elasticSearchExecutionStatsPublisher, domain, testClass, testMethod);

        executeTest(new TestServiceGetRequestGenerator(chunkSize, numberOfChunks),
            runProps.setVerbose(false).setRunDuration(runDuration),
            elasticSearchExecutionStatsPublisher,
            chunkStatsLogPublisher);

        for (int status : statusCodes) {
            verify(postRequestedFor(urlEqualTo("/testah/load/_bulk"))
                .withRequestBody(matching(requestRegexIndexCounter))
                .withRequestBody(matching(String.format(requestRegexSingleStatus, status, domain, testClass, testMethod)))
            );
        }
    }
}
