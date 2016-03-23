package org.testah;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan
public class TestHttp extends HttpTestPlan {

    @TestCase
    @Test
    public void t2() throws ClientProtocolException, IOException {
        step("cool");
        final HttpWrapperV1 http = new HttpWrapperV1();
        http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.google.com"));
        step("cool");
        http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.2google.com"));

    }
}
