package org.testah.http;

import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.requests.*;

public class TestHttpCalls {


    @Test
    public void doGet() {
        TS.http().doGet("https://postman-echo.com/get?foo1=bar1&foo2=bar2").assertStatus();

        TS.http().doRequest(new GetRequestDto("https://postman-echo.com/get")).assertStatus();
    }

    @Test
    public void doDelete() {
        TS.http().doDelete("https://postman-echo.com/delete").assertStatus();

        TS.http().doRequest(new DeleteRequestDto("https://postman-echo.com/delete")).assertStatus();
    }

    @Test
    public void doPost() {
        TS.http().doPost("https://postman-echo.com/post",
                "foo1=bar1&foo2=bar2").assertStatus();

        TS.http().doRequest(new PostRequestDto("https://postman-echo.com/post",
                "foo1=bar1&foo2=bar2")).assertStatus();
    }

    @Test
    public void doPut() {
        TS.http().doPut("https://postman-echo.com/put",
                "foo1=bar1&foo2=bar2").assertStatus();

        TS.http().doRequest(new PutRequestDto("https://postman-echo.com/put",
                "foo1=bar1&foo2=bar2")).assertStatus();
    }

    @Test
    public void doPatch() {
        TS.http().doRequest(new PatchRequestDto("https://postman-echo.com/patch",
                "foo1=bar1&foo2=bar2")).assertStatus();
    }

}
