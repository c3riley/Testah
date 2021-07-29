package org.testah.runner.http.load;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.AbstractLongRunningTest;
import org.testah.runner.performance.ChunkStatsLogPublisher;
import org.testah.runner.performance.ElasticSearchResponseTimesPublisher;
import org.testah.runner.performance.TestRunProperties;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

public class TestLongRunning extends AbstractLongRunningTest {
    private static final Integer[] statusCodes =
        {HttpStatus.SC_OK, HttpStatus.SC_MULTIPLE_CHOICES, HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_INTERNAL_SERVER_ERROR};
    private static final int numberOfChunks = 8;
    private static final int chunkSize = 4;
    private static final int nthreads = 2;
    private static final long millisBetweenChunks = 2000L;
    private static final long runDuration = 10000L;
    private static final String serviceUnderTest = "ServiceUnderTest";
    private static final String baseUrl = "http://localhost:9200";
    private static final String username = "elastic";
    private static final String password = "changeme";
    private static final String index = "testah";
    private static final String type = "load";
    private static final String requestRegexNoDataIndexEmpty = ".*\\{\"index\": \\{\\}\\}.*";
    private static final String requestRegexNoDataDuration0 = ".*\"duration\":0.*";
    private static final String requestRegexIndexCounter = "(.*index.*\\n.*\\n)";
    private static final String requestRegexSingle =
        ".*\\{\"statusCode\":[2345]00,\"duration\":\\d+," +
            "\"collectionTime\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"domain\":\"%s\",\"service\":\"ServiceUnderTest\",\"testClass\":\"TestLongRunning\"," +
            "\"testMethod\":\"%s\",\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"aggregation\":\"single\"\\}.*";
    private static final String requestRegexSingleStatus =
        ".*\\{\"statusCode\":%d,\"duration\":\\d+," +
            "\"collectionTime\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"domain\":\"%s\",\"service\":\"ServiceUnderTest\",\"testClass\":\"TestLongRunning\"," +
            "\"testMethod\":\"%s\",\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"aggregation\":\"single\"\\}.*";
    private static final String requestRegexChunk =
            ".*\\{\"duration\":\\d+,\"domain\":\"%s\",\"service\":\"ServiceUnderTest\"," +
            "\"testClass\":\"TestLongRunning\",\"testMethod\":\"%s\"," +
            "\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\",\"aggregation\":\"chunk\"\\}.*";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9200);

    @Before
    public void setup() {
        HttpAkkaRunner.reset();
    }

    private void setupWiremock(ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher, String domain,
                               String testMethod) {
        wireMockRule.stubFor(post(urlEqualTo(elasticSearchExecutionStatsPublisher.getUrlPathUpload()))
            .withRequestBody(matching(requestRegexIndexCounter))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testMethod)))
            .withRequestBody(matching(String.format(requestRegexChunk, domain, testMethod)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("<response>Some content</response>")));
        wireMockRule.stubFor(post(urlEqualTo(elasticSearchExecutionStatsPublisher.getUrlPathUpload()))
            .withRequestBody(matching(requestRegexNoDataIndexEmpty))
            .withRequestBody(matching(requestRegexNoDataDuration0))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("<response>No Data</response>")));
    }

    @Test
    public void testGet() throws Exception {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
            new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        String domain = new TestServiceGetRequestGenerator(chunkSize, numberOfChunks).getDomain();
        setupWiremock(elasticSearchExecutionStatsPublisher, domain, testMethod);

        executeTest(new TestServiceGetRequestGenerator(chunkSize, numberOfChunks),
            runProps.setVerbose(true).setRunDuration(runDuration),
            elasticSearchExecutionStatsPublisher,
            chunkStatsLogPublisher);

        for (int status : statusCodes) {
            verify(postRequestedFor(urlEqualTo("/testah/load/_bulk"))
                .withRequestBody(matching(requestRegexIndexCounter))
                .withRequestBody(matching(String.format(requestRegexSingleStatus, status, domain, testMethod)))
            );
        }
    }

    @Test
    public void testPost() throws Exception {
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        TestRunProperties runProps =
                new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, millisBetweenChunks);
        ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
                new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        String domain = new TestServicePostRequestGenerator(chunkSize, numberOfChunks).getDomain();
        setupWiremock(
            elasticSearchExecutionStatsPublisher,
            domain,
            testMethod);

        executeTest(new TestServicePostRequestGenerator(chunkSize, numberOfChunks),
                runProps.setVerbose(true).setRunDuration(runDuration),
                elasticSearchExecutionStatsPublisher,
                chunkStatsLogPublisher);
        // verify datawas at least published once
        verify(postRequestedFor(urlEqualTo("/testah/load/_bulk"))
            .withRequestBody(matching(requestRegexIndexCounter))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testMethod)))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testMethod)))
            .withRequestBody(matching(String.format(requestRegexChunk, domain, testMethod)))
        );
    }
}
