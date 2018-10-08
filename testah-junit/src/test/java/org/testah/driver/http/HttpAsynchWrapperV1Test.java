package org.testah.driver.http;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;

import java.util.concurrent.Future;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class HttpAsynchWrapperV1Test {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doRequestAsynchTest() throws Exception {
        try (HttpAsyncWrapperV1 client = new HttpAsyncWrapperV1()) {
            GetRequestDto get = new GetRequestDto("https://postman-echo.com/get");
            Future<HttpResponse> responseFuture = client.doRequestAsync(get, true);
            client.getResponseDtoFromFuture(responseFuture, get).assertStatus();
            responseFuture = client.doRequestAsync(get, true);
            client.getResponseDtoFromFuture(responseFuture, get).assertStatus();
        }
    }

    @Test
    public void getHttpAsyncClientTest() {
        try (HttpAsyncWrapperV1 client = new HttpAsyncWrapperV1()) {

            CloseableHttpAsyncClient http = client.getHttpAsyncClientBuilder().build();
            client.setHttpAsyncClient(http);
            assertThat(client.getHttpAsyncClient(), is(http));

        }
    }

    @Test
    public void getSelfTest() {
        try (HttpAsyncWrapperV1 client = new HttpAsyncWrapperV1()) {
            assertThat(client.getSelf(), is(client));
        }
    }
}