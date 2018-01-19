package org.testah.http;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.testah.driver.http.HttpAsynchWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

import java.util.concurrent.ExecutionException;

@TestPlan
public class TestAysnchHttp {

    @TestCase
    @Test
    public void basicTest() throws InterruptedException, ExecutionException {
        try (final HttpAsynchWrapperV1 http = new HttpAsynchWrapperV1()) {
            final HttpResponse response = http.doRequestAysnch(new GetRequestDto("http://www.google.com"), true).get();
            Assert.assertThat(response, is(notNullValue()));
            Assert.assertThat(response.getStatusLine().getStatusCode(), is(200));
        }
    }
}
