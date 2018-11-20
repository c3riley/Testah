package org.testah.driver.http.poller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.http.Header;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.util.unittest.dtotest.DtoTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * The type Http poller test.
 */
public class HttpPollerTest {

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test getter and setter.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetterAndSetter() throws Exception {
        DtoTest test = new DtoTest();
        test.addToAnnotationsToIgnore(JsonIgnore.class);
        test.testGettersAndSetters(new HttpPoller());
    }

    /**
     * Poll request only 1 attempt.
     */
    @Test
    public void pollRequestOnly1Attempt() {
        pollRequest(0);
    }

    /**
     * Poll request.
     *
     * @param return2 the return 2
     */
    public void pollRequest(int return2) {
        GetRequestDto request = new GetRequestDto("http://www.testah.com");
        AbstractHttpWrapper http = spy(HttpWrapperV2.class);
        doAnswer(getAnswer(
                "false", 200,
                "true", 200,
                return2, request))
                .when(http).doRequest(any(), anyBoolean(), anyBoolean());

        ResponseDto response = new HttpPoller().setHttp(http).pollRequest(
                request, getPollerCheck());

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.getResponseBody());

        verify(http, times(return2 + 1)).doRequest(any(), anyBoolean(), anyBoolean());
    }

    private Answer getAnswer(final String body1, final int status1, String body2,
                             final int status2, final int return2, final AbstractRequestDto<?> usedRequest) {
        return new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count++ >= return2) {
                    return new ResponseDto().setHeaders(new Header[]{}).setResponseBody(body2).setStatusCode(status2)
                            .setRequestUsed(usedRequest);
                }
                return new ResponseDto().setHeaders(new Header[]{}).setResponseBody(body1).setStatusCode(status1)
                        .setRequestUsed(usedRequest);
            }
        };
    }

    private HttpPollerCheck getPollerCheck() {
        return new HttpPollerCheck() {
            @Override
            public boolean isDone(ResponseDto response) {
                return response.getResponseBody().equalsIgnoreCase("true");
            }
        };
    }

    /**
     * Poll request only 3 attempt.
     */
    @Test
    public void pollRequestOnly3Attempt() {
        pollRequest(3);
    }

    /**
     * Poll request get wrong status code to start.
     */
    @Test(expected = java.lang.AssertionError.class)
    public void pollRequestGetWrongStatusCodeToStart() {
        GetRequestDto request = new GetRequestDto("http://www.testah.com");
        AbstractHttpWrapper http = spy(HttpWrapperV2.class);
        doAnswer(getAnswer(
                "false", 205,
                "true", 200,
                3, request))
                .when(http).doRequest(any(), anyBoolean(), anyBoolean());

        ResponseDto response = new HttpPoller().setHttp(http).pollRequest(
                request, getPollerCheck());

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.getResponseBody());

        verify(http, times(4)).doRequest(any(), anyBoolean(), anyBoolean());
    }

    /**
     * Poll request get wrong status code to after.
     */
    @Test(expected = java.lang.AssertionError.class)
    public void pollRequestGetWrongStatusCodeToAfter() {
        GetRequestDto request = new GetRequestDto("http://www.testah.com");
        AbstractHttpWrapper http = spy(HttpWrapperV2.class);
        doAnswer(getAnswer(
                "false", 200,
                "true", 205,
                3, request))
                .when(http).doRequest(any(), anyBoolean(), anyBoolean());

        ResponseDto response = new HttpPoller().setHttp(http).pollRequest(request, getPollerCheck());

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.getResponseBody());

        verify(http, times(4)).doRequest(any(), anyBoolean(), anyBoolean());
    }

    /**
     * Poll request get wrong status code to after ignore status check.
     */
    @Test()
    public void pollRequestGetWrongStatusCodeToAfterIgnoreStatusCheck() {
        GetRequestDto request = new GetRequestDto("http://www.testah.com");
        AbstractHttpWrapper http = spy(HttpWrapperV2.class);
        doAnswer(getAnswer(
                "false", 200,
                "true", 205,
                3, request)).when(http).doRequest(any(), anyBoolean(), anyBoolean());

        ResponseDto response = new HttpPoller().setStatusAssert(false).setHttp(http).pollRequest(
                request, getPollerCheck());

        Assert.assertEquals(205, response.getStatusCode());
        Assert.assertEquals("true", response.getResponseBody());

        verify(http, times(4)).doRequest(any(), anyBoolean(), anyBoolean());
    }

    /**
     * Poll and go over limit.
     */
    @Test(expected = AssertionError.class)
    public void pollAndGoOverLimit() {
        GetRequestDto request = new GetRequestDto("http://www.testah.com");
        AbstractHttpWrapper http = spy(HttpWrapperV2.class);
        doAnswer(getAnswer(
                "false", 200,
                "false", 200,
                3, request)).when(http).doRequest(any(), anyBoolean(), anyBoolean());

        ResponseDto response = new HttpPoller().setMaxPollIteration(5).setHttp(http).pollRequest(
                request, getPollerCheck());

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("true", response.getResponseBody());

        verify(http, times(5)).doRequest(any(), anyBoolean(), anyBoolean());
    }

}