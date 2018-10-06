package org.testah.driver.http.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PatchRequestDtoTest {

    @Test
    public void testConstructorOnlyUrl() {
        PatchRequestDto patch = new PatchRequestDto("https://postman-echo.com/patch");
        Assert.assertNull(TS.http().doRequest(patch).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsObject() {
        final List<String> payload = new ArrayList<>();
        payload.add("this is a test");
        PatchRequestDto patch = new PatchRequestDto("https://postman-echo.com/patch", payload);
        Assert.assertEquals("[ \"this is a test\" ]", TS.http().doRequest(patch).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsString() {
        final String payload = "This is a test";
        PatchRequestDto patch = new PatchRequestDto("https://postman-echo.com/patch", "This is a test");
        Assert.assertEquals(payload, TS.http().doRequest(patch).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsByteArray() {
        final String payload = "This is a test";
        PatchRequestDto patch = new PatchRequestDto("https://postman-echo.com/patch",
            payload.getBytes(Charset.forName("UTF8")));
        JsonNode json = TS.http().doRequest(patch).assertStatus().getResponse();
        Assert.assertEquals("application/octet-stream", json.get("headers").get("content-type").textValue());
        Assert.assertEquals("Buffer", json.get("data").get("type").textValue());
        Assert.assertEquals("[84,104,105,115,32,105,115,32,97,32,116,101,115,116]", json.get("data").get("data").toString());
    }

    @Test
    public void testConstructorUrlAndPayloadAsHttpEntity() throws UnsupportedEncodingException {
        final String payload = "This is a test";
        HttpEntity entity = new StringEntity(payload);
        PatchRequestDto patch = new PatchRequestDto("https://postman-echo.com/patch", entity);
        JsonNode json = TS.http().doRequest(patch).assertStatus().getResponse();
        Assert.assertEquals("text/plain; charset=ISO-8859-1", json.get("headers").get("content-type").textValue());
        Assert.assertEquals(payload, json.get("data").textValue());
    }

}