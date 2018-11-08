package org.testah.driver.http.requests;

import org.apache.tools.ant.taskdefs.Delete;
import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

public class DeleteRequestDtoTest {

    @Test
    public void testConstructorOnlyUrl() {
        DeleteRequestDto delete = new DeleteRequestDto("https://postman-echo.com/delete");
        Assert.assertEquals("https://postman-echo.com/delete",
            TS.http().doRequest(delete).assertStatus().getResponse().get("url").textValue());
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorUrlAndPayloadAsObject() {
        DeleteRequestDto delete = new DeleteRequestDto("https://postman-echo.com/delete");
        delete.setPayload("this is not allowed");
    }

}