package org.testah.driver.http.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GetRequestDtoTest {

    @Test
    public void testConstructorOnlyUrl() {
        GetRequestDto get = new GetRequestDto("https://postman-echo.com/get");
        Assert.assertEquals("https://postman-echo.com/get",
            TS.http().doRequest(get).assertStatus().getResponse().get("url").textValue());
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorUrlAndPayloadAsObject() {
        GetRequestDto get = new GetRequestDto("https://postman-echo.com/get");
        get.setPayload("this is not allowed");
    }

}