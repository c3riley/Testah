package org.testah.runner.performance;

import org.junit.Before;
import org.junit.Test;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.asserts.AssertStrings;
import org.testah.util.unittest.dtotest.SystemOutCapture;

import java.util.ArrayList;
import java.util.List;

public class ChunkStatsLogPublisherTest {

    ChunkStatsLogPublisher chunkStatsLogPublisher;

    @Before
    public void setup() {
        chunkStatsLogPublisher = new ChunkStatsLogPublisher();
    }

    @Test(expected = RuntimeException.class)
    public void pushNull() throws Exception {
        testPush(null, "");
    }

    @Test()
    public void pushEmptyList() throws Exception {
        testPush(new ArrayList<ResponseDto>(), "INFO  - ChunkStatsLogPublisher    - " +
                "{\"elapsedTime\":0,\"statusCodes\":[],\"overallStats\":{\"count\":0,\"mean\":0,\"pct90\":0,\"std" +
                "\":0,\"min\":0,\"max\":0},\"statsByStatusCode\":null}");
    }

    @Test()
    public void pushOneResponseList() throws Exception {
        List<ResponseDto> responses = new ArrayList<>();
        responses.add(new ResponseDto(200).setResponseBody("This is a test"));
        testPush(responses, "INFO  - ChunkStatsLogPublisher    - " +
                "{\"elapsedTime\":0,\"statusCodes\":[200],\"overallStats\":{\"count\":1,\"mean\":0,\"pct90\":0,\"std" +
                "\":0,\"min\":0,\"max\":0},\"statsByStatusCode\":null}");
    }

    private void testPush(final List<ResponseDto> responses, final String expectedContains) throws Exception {
        String content = "";
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            chunkStatsLogPublisher.push(responses);
            content = systemOutCapture.getSystemOut();
        }
        new AssertStrings(content).contains(expectedContains);
    }

    @Test
    public void cleanup() {
        chunkStatsLogPublisher.cleanup();
    }
}
