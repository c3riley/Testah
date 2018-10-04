package org.testah.driver.http.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.StringEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PutRequestDtoTest {

   @Test
    public void testConstructorOnlyUrl() {
        PutRequestDto put = new PutRequestDto("https://postman-echo.com/put");
        Assert.assertNull(TS.http().doRequest(put).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsObject() {
        final List<String> payload = new ArrayList<>();
        payload.add("this is a test");
        PutRequestDto put = new PutRequestDto("https://postman-echo.com/put", payload);
        Assert.assertEquals("[ \"this is a test\" ]", TS.http().doRequest(put).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsString() {
        final String payload = "This is a test";
        PutRequestDto put = new PutRequestDto("https://postman-echo.com/put", "This is a test");
        Assert.assertEquals(payload, TS.http().doRequest(put).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsByteArray() {
        final String payload = "This is a test";
        PutRequestDto put = new PutRequestDto("https://postman-echo.com/put",
            payload.getBytes(Charset.forName("UTF8")));
        JsonNode json = TS.http().doRequest(put).assertStatus().getResponse();
        Assert.assertEquals("application/octet-stream", json.get("headers").get("content-type").textValue());
        Assert.assertEquals("Buffer", json.get("data").get("type").textValue());
        Assert.assertEquals("[84,104,105,115,32,105,115,32,97,32,116,101,115,116]", json.get("data").get("data").toString());
    }

    @Test
    public void testConstructorUrlAndPayloadAsHttpEntity() throws UnsupportedEncodingException {
        final String payload = "This is a test";
        HttpEntity entity = new StringEntity(payload);
        PutRequestDto put = new PutRequestDto("https://postman-echo.com/put", entity);
        JsonNode json = TS.http().doRequest(put).assertStatus().getResponse();
        Assert.assertEquals("text/plain; charset=ISO-8859-1", json.get("headers").get("content-type").textValue());
        Assert.assertEquals(payload, json.get("data").textValue());
    }

}