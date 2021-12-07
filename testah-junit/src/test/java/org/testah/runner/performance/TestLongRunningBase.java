package org.testah.runner.performance;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.collections4.ListUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.testah.runner.HttpAkkaRunner;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class TestLongRunningBase extends AbstractLongRunningTest {
    protected static final String baseUrl = "http://localhost:";
    protected static final List<Integer> statusCodes =
        ListUtils.unmodifiableList(Arrays.asList(
            HttpStatus.SC_OK,
            HttpStatus.SC_MULTIPLE_CHOICES,
            HttpStatus.SC_BAD_REQUEST,
            HttpStatus.SC_INTERNAL_SERVER_ERROR
        ));
    protected static final int numberOfChunks = 8;
    protected static final int chunkSize = 1;
    protected static final int nthreads = 2;
    protected static final long millisBetweenChunks = 2000L;
    protected static final long runDuration = 10000L;
    protected static final String serviceUnderTest = "ServiceUnderTest";
    protected static final String username = "elastic";
    protected static final String password = "changeme";
    protected static final String index = "testah";
    protected static final String type = "load";
    protected static final String requestRegexNoDataIndexEmpty = ".*\\{\"index\": \\{\\}\\}.*";
    protected static final String requestRegexNoDataDuration0 = ".*\"duration\":0.*";
    protected static final String requestRegexIndexCounter = "(.*index.*\\n.*\\n)";
    protected static final String requestRegexSingle =
        ".*\\{\"statusCode\":[2345]00,\"duration\":\\d+," +
            "\"collectionTime\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"domain\":\"%s\",\"service\":\"ServiceUnderTest\",\"testClass\":\"%s\"," +
            "\"testMethod\":\"%s\",\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"aggregation\":\"single\"\\}.*";
    protected static final String requestRegexSingleStatus =
        ".*\\{\"statusCode\":%d,\"duration\":\\d+," +
            "\"collectionTime\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"domain\":\"%s\",\"service\":\"ServiceUnderTest\",\"testClass\":\"%s\"," +
            "\"testMethod\":\"%s\",\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\"," +
            "\"aggregation\":\"single\"\\}.*";
    protected static final String requestRegexChunk =
        ".*\\{\"duration\":\\d+,\"domain\":\"%s\",\"service\":\"ServiceUnderTest\"," +
            "\"testClass\":\"%s\",\"testMethod\":\"%s\"," +
            "\"timestamp\":\"\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d\",\"aggregation\":\"chunk\"\\}.*";

    @Before
    public void setup() {
        HttpAkkaRunner.reset();
    }

    protected void setupWiremock(WireMockRule wireMockRule,
                               ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher,
                               String domain,
                               String testClass,
                               String testMethod) {
        wireMockRule.stubFor(post(urlEqualTo(elasticSearchExecutionStatsPublisher.getUrlPathUpload()))
            .withRequestBody(matching(requestRegexIndexCounter))
            .withRequestBody(matching(String.format(requestRegexSingle, domain, testClass, testMethod)))
            .withRequestBody(matching(String.format(requestRegexChunk, domain,  testClass, testMethod)))
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
}
