package org.testah.http;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.testah.driver.http.HttpAsyncWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@TestPlan
public class TestAsyncHttp {

    @TestCase
    @Test
    public void basicTest() throws InterruptedException, ExecutionException {
        try (final HttpAsyncWrapperV1 http = new HttpAsyncWrapperV1()) {
            final HttpResponse response = http.doRequestAsync(new GetRequestDto("https://www.google.com"), true).get();
            Assert.assertThat(response, is(notNullValue()));
            Assert.assertThat(response.getStatusLine().getStatusCode(), is(200));
        }
    }
}
