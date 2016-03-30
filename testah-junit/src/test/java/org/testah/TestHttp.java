package org.testah;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.client.dto.TestCaseDto;
import org.testah.framework.testPlan.HttpTestPlan;
import org.testah.framework.testPlan.TestConfiguration;

@ContextHierarchy({ @ContextConfiguration(classes = TestConfiguration.class) })
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

        http.doPost("http://www.google.com", null);

    }

    @TestCase(tags="test")
    @Test
    public void postWithNUll() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", null);
    }

    @TestCase
    @Test
    public void postWithEmptyString() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", "");
    }

    @TestCase
    @Test
    public void postWithObject() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", new TestCaseDto());
    }
}
