package org.testah.driver.http.requests;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

public class OptionRequestDtoTest {

    @Test
    public void testConstructorOnlyUrl() {
        OptionRequestDto option = new OptionRequestDto("https://postman-echo.com/option");
        Assert.assertEquals(
            "GET,HEAD,PUT,POST,DELETE,PATCH",
            TS.http().doRequest(option).assertStatus().getResponseBody());
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorUrlAndPayloadAsObject() {
        OptionRequestDto option = new OptionRequestDto("https://postman-echo.com/get");
        option.setPayload("this is not allowed");
    }

}