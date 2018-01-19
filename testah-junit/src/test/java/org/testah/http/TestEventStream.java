package org.testah.http;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.testah.MessageDto;
import org.testah.TS;
import org.testah.driver.http.eventstream.ConsumeEventStream;
import org.testah.driver.http.eventstream.ICloseStream;
import org.testah.driver.http.requests.PostRequestDto;

public class TestEventStream {

    @Test
    public void testWithDto() throws Exception {
        final String id = TS.http().doRequest(new PostRequestDto("https://api.dev1.eagleinvsys.com/reports-svc/v1/report",
                "{\"parameters\":{\"accountId\":\"15085\",\"asOfDate\":\"20171231\",\"benchmarkId\":\"25175\",\"benchmarkName\":"
                        + "\"S&P/TSX CAPPED COMPOSITE INDEX\",\"returnType\":\"Gross\"},\"reportType\":\"PRR_PDF\"}").withJson())
                .getResponse().get("id").textValue();

        new ConsumeEventStream("https://api.dev1.eagleinvsys.com/reports-svc/v1/report/subscription/" + id)
                .setCloseStream(new ICloseStream<MessageDto>() {
                    @Override
                    public boolean shouldCloseStream(final MessageDto data) {
                        return data.getType()
                                .equalsIgnoreCase("SUCCESS");
                    }
                }).consume(MessageDto.class);
    }

    @Test
    public void testWithString() throws Exception {
        final String id = TS.http().doRequest(new PostRequestDto("https://api.dev1.eagleinvsys.com/reports-svc/v1/report",
                "{\"parameters\":{\"accountId\":\"15085\",\"asOfDate\":\"20171231\",\"benchmarkId\":\"25175\","
                        + "\"benchmarkName\":\"S&P/TSX CAPPED COMPOSITE INDEX\",\"returnType\":\"Gross\"},\"reportType\":\"PRR_PDF\"}")
                .withJson()).getResponse().get("id").textValue();

        new ConsumeEventStream("https://api.dev1.eagleinvsys.com/reports-svc/v1/report/subscription/" + id).consume();
    }

    @Test
    public void testWithJsonNode() throws Exception {
        final String id = TS.http().doRequest(new PostRequestDto("https://api.dev1.eagleinvsys.com/reports-svc/v1/report",
                "{\"parameters\":{\"accountId\":\"15085\",\"asOfDate\":\"20171231\",\"benchmarkId\":\"25175\",\"benchmarkName\":"
                        + "\"S&P/TSX CAPPED COMPOSITE INDEX\",\"returnType\":\"Gross\"},\"reportType\":\"PRR_PDF\"}")
                .withJson()).getResponse().get("id").textValue();

        new ConsumeEventStream("https://api.dev1.eagleinvsys.com/reports-svc/v1/report/subscription/" + id).consumeJson();
    }

    @Test
    public void testWithJsonNodeClose() throws Exception {
        final String id = TS.http().doRequest(new PostRequestDto("https://api.dev1.eagleinvsys.com/reports-svc/v1/report",
                "{\"parameters\":{\"accountId\":\"15085\",\"asOfDate\":\"20171231\",\"benchmarkId\":\"25175\","
                       + "\"benchmarkName\":\"S&P/TSX CAPPED COMPOSITE INDEX\",\"returnType\":\"Gross\"},\"reportType\":\"PRR_PDF\"}")
                .withJson()).getResponse().get("id").textValue();

        new ConsumeEventStream("https://api.dev1.eagleinvsys.com/reports-svc/v1/report/subscription/" + id)
                .setCloseStream(new ICloseStream<JsonNode>() {
                    @Override
                    public boolean shouldCloseStream(final JsonNode data) {
                        return data.get("type").textValue()
                                .equalsIgnoreCase("SUCCESS");
                    }
                }).consumeJson();
    }

}

