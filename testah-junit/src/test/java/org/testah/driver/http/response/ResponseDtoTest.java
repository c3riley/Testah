package org.testah.driver.http.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.report.asserts.AssertFile;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class ResponseDtoTest {

    private static GetRequestDto get;
    private static ResponseDto getResponse;

    @BeforeClass
    public static void setUp() throws Exception {
        get = new GetRequestDto("https://postman-echo.com/get");
        getResponse = TS.http().doRequest(get);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void assertStatusTest() {
        getResponse.assertStatus();
    }

    @Test(expected = AssertionError.class)
    public void assertStatus1Test() {
        getResponse.assertStatus(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getRequestUsedTest() {
        Assert.assertEquals(get, getResponse.getRequestUsed());
    }

    @Test
    public void setRequestUsedTest() {
        ResponseDto response = new ResponseDto();
        Assert.assertEquals(null, response.getRequestUsed());
        response.setRequestUsed(get);
        Assert.assertEquals(get, response.getRequestUsed());
    }

    @Test
    public void assertResponseBodyContainsTest() {
        getResponse.assertResponseBodyContains("gzip,deflate");
        try {
            getResponse.assertResponseBodyContains("gz555ip,deflate");
            Assert.assertTrue("Assert Did not Fail as Expected", false);
        } catch (final AssertionError assertionError) {
            Assert.assertTrue("Assert Failed as Expected", true);
        }
    }


    @Test
    public void getFootersTest() {
    }

    @Test
    public void setFootersTest() {
    }

    @Test
    public void saveToFileTest() {
    }

    @Test
    public void saveToFile1Test() {
    }

    @Test
    public void getResponseBytesTest() {
        assertThat(
            getResponse.getResponseBody()
                .getBytes(Charset.forName("UTF8")), equalTo(getResponse.getResponseBytes()));
    }

    @Test
    public void setResponseBytesTest() {
        ResponseDto response = new ResponseDto();
        final String body = "This is a test";
        response.setResponseBytes(body.getBytes(Charset.forName("UTF8")));
        assertThat(body.getBytes(Charset.forName("UTF8")), equalTo(response.getResponseBytes()));
    }

    @Test
    public void setResponseBodyTest() {
        ResponseDto response = new ResponseDto();
        final String body = "This is a test";
        response.setResponseBody(body);
        assertThat(body, response.getResponseBody(), equalTo(response.getResponseBody()));
    }

    @Test
    public void setResponseBody1Test() throws UnsupportedEncodingException {
        ResponseDto response = new ResponseDto();
        final HttpEntity body = new StringEntity("This is a test");
        response.setResponseBody(body);
        assertThat("This is a test",
            equalTo(response.getResponseBody()));
    }

    @Test
    public void getResponseAsDtoTest() {

        PostManDto dto = getResponse.getResponse(PostManDto.class);
        assertThat("https://postman-echo.com/get",
            equalTo(dto.getUrl()));
        assertThat(dto.getHeaders().size(),
            greaterThanOrEqualTo(5));
        assertThat(dto.getHeaders().entrySet().stream().filter(
            header -> {
                TS.log().debug(header);
                return header.getKey().equals("host") &&
                    header.getValue().equals("postman-echo.com");
            }).findFirst().isPresent(),
            equalTo(true));
        assertThat(dto.getArgs().size(),
            equalTo(0));
    }

    @Test
    public void getResponseAsTypeReferenceTest() {
        HashMap<String, Object> dto = getResponse.getResponse(new TypeReference<HashMap<String, Object>>() {
        });
        assertThat("https://postman-echo.com/get",
            equalTo(dto.get("url")));
    }

    @Test
    public void getResponseAsJsonNodeTest() {
        JsonNode jsonNode = getResponse.getResponse();
        assertThat("https://postman-echo.com/get",
            equalTo(jsonNode.get("url").textValue()));
    }

    @Test
    public void printTest() {
        TS.asserts().assertSystemOutContains(
            () ->
            {
                getResponse.print();
            }, "DEBUG - ResponseDto               - # Response",
            "DEBUG - ResponseDto               - # URI: https://postman-echo.com/get",
            "DEBUG - ResponseDto               - # Status: 200 [ OK ]",
            "DEBUG - ResponseDto               - # Headers: ",
            "DEBUG - ResponseDto               - =========================================",
            "DEBUG - ResponseDto               - # Body: (see below)",
            "{\"args\":{},\"headers\":{");

    }

    @Test
    public void print1Test() {
        TS.asserts().assertSystemOutContains(
            () ->
            {
                getResponse.print(true);
            }, "DEBUG - ResponseDto               - # Response",
            "DEBUG - ResponseDto               - # URI: https://postman-echo.com/get",
            "DEBUG - ResponseDto               - # Status: 200 [ OK ]",
            "DEBUG - ResponseDto               - # Headers: ",
            "DEBUG - ResponseDto               - =========================================",
            "DEBUG - ResponseDto               - # Body: (see below)",
            "{\"args\":{},\"headers\":{");
    }

    @Test
    public void print2Test() {
        TS.asserts().assertSystemOutContains(
            () -> {
                getResponse.print(true, 10);
            }, "DEBUG - ResponseDto               - # Response",
            "DEBUG - ResponseDto               - # URI: https://postman-echo.com/get",
            "DEBUG - ResponseDto               - # Status: 200 [ OK ]",
            "DEBUG - ResponseDto               - # Headers: ",
            "DEBUG - ResponseDto               - =========================================",
            "DEBUG - ResponseDto               - # Body: (see below)",
            "{\"args\":{}...");
    }

    @Test
    public void getUrlTest() {
        assertThat("https://postman-echo.com/get",
            equalTo(getResponse.getUrl()));
    }

    @Test
    public void setUrlTest() {
        ResponseDto response = new ResponseDto();
        final String url = "http://thisisatest.test";
        response.setUrl(url);
        assertThat(url,
            equalTo(response.getUrl()));
    }

    @Test
    public void getStatusCodeTest() {
        assertThat(SC_OK,
            equalTo(getResponse.getStatusCode()));
    }

    @Test
    public void setStatusCodeTest() {
        ResponseDto response = new ResponseDto();
        final int status = SC_NOT_FOUND;
        response.setStatusCode(status);
        assertThat(status,
            equalTo(response.getStatusCode()));
    }

    @Test
    public void getStatusTextTest() {
        assertThat("OK",
            equalTo(getResponse.getStatusText()));
    }

    @Test
    public void setStatusTextTest() {
        ResponseDto response = new ResponseDto();
        final String status = "NOT FOUND";
        response.setStatusText(status);
        assertThat(status,
            equalTo(response.getStatusText()));
    }

    @Test
    public void getHeadersTest() {
        assertThat(getResponse.getHeaders().length,
            greaterThanOrEqualTo(6));

        assertThat(Arrays.stream(getResponse.getHeaders())
                .filter(header -> {
                    return header.getName().equals("Content-Type") &&
                        header.getValue().equals("application/json; charset=utf-8");
                }).findFirst().isPresent(),
            is(true));

        assertThat("application/json; charset=utf-8", equalTo(getResponse.getHeaderHash().get("Content-Type")));
    }

    @Test
    public void setHeadersTest() {
        ResponseDto response = new ResponseDto();
        final Header[] headers = new Header[]{new BasicHeader("", "")};
        response.setHeaders(headers);
        assertThat(response.getHeaders(),
            equalTo(headers));
    }

    @Test
    public void getResponseBodyTest() {
        assertThat(getResponse.getResponseBody(), containsString(
            "{\"args\":{},\"headers\":{"
        ));

    }

    @Test
    public void getResponseBody1Test() {
        assertThat(getResponse.getResponseBody(true), containsString(
            "{&quot;args&quot;:{},&quot;headers&quot;:{"
        ));
    }

    @Test
    public void addAsInfoStepTest() {
    }

    @Test
    public void createResponseInfoStepTest() {
        StepActionDto stepActionDto = getResponse
            .createResponseInfoStep(true, true, 100);
        assertThat(stepActionDto.toString(),
            startsWith("{\"testStepActionType\":\"HTTP_REQUEST\",\"description\":null," +
                "\"message1\":\"GET - Uri: https:\\/\\/postman-echo.com\\/get\",\"message2\":" +
                "\"Status: 200 [ OK ]\",\"message3\":\"{&quot;args&quot;:{},&quot;headers&quot;:"));
    }

    @Test
    public void createResponseInfoStep1Test() {
        StepActionDto original = new StepActionDto();
        StepActionDto stepActionDto = getResponse
            .createResponseInfoStep(true, true, 100, original);
        assertThat(stepActionDto, is(original));
        assertThat(stepActionDto.toString(),
            startsWith("{\"testStepActionType\":\"HTTP_REQUEST\",\"description\":null," +
                "\"message1\":\"GET - Uri: https:\\/\\/postman-echo.com\\/get\",\"message2\":" +
                "\"Status: 200 [ OK ]\",\"message3\":\"{&quot;args&quot;:{},&quot;headers&quot;:"));

    }

    @Test
    public void getRequestTypeTest() {
        assertThat(getResponse.getRequestType(),
            equalTo("GET"));
    }

    @Test
    public void setRequestTypeTest() {
        ResponseDto response = new ResponseDto();
        final String requestType = "TEST";
        response.setRequestType(requestType);
        assertThat(response.getRequestType(),
            equalTo(requestType));
    }

    @Test
    public void writeResponseInfoFileTest() {

        new AssertFile(getResponse.writeResponseInfoFile(true)).contentContains(
            "{\"args\":{},\"headers\":{",
            "\"host\":\"postman-echo.com\",",
            "\"accept-encoding\":\"gzip,deflate\",");

    }

    @Test
    public void writeResponseInfoFileTestWithFalse() {
        assertThat(getResponse.writeResponseInfoFile(false),
            equalTo(null));
    }

    @Test
    public void printStatusTest() {
        TS.asserts().assertSystemOutContains(() -> {
            getResponse.printStatus();
        }, "# Status: 200 [ OK ]");
    }

    @Test
    public void toStringStatusTest() {
        assertThat(getResponse.toStringStatus(),
            equalTo("Uri:https://postman-echo.com/get\nStatus: 200 [ OK ]"));
    }

    @Test
    public void getStartTest() {
        assertThat(getResponse.getStart(),
            greaterThan(0L));
        assertThat(getResponse.getEnd(),
            greaterThan(getResponse.getStart()));
        assertThat(getResponse.getDuration(),
            equalTo(getResponse.getEnd() - getResponse.getStart()));
    }

    @Test
    public void setStartTest() {
        ResponseDto response = new ResponseDto();
        response.setStart(10L);
        response.setEnd(20L);
        assertThat(response.getStart(),
            equalTo(10L));
        assertThat(response.getEnd(),
            equalTo(20L));
        assertThat(response.getDuration(),
            equalTo(response.getEnd() - response.getStart()));

        response.setStart();
        TS.util().pause(10L);
        response.setEnd();
        assertThat(response.getStart(),
            greaterThan(0L));
        assertThat(response.getEnd(),
            greaterThan(response.getStart()));
        assertThat(response.getDuration(),
            equalTo(response.getEnd() - response.getStart()));

    }

    @Test
    public void getHeaderValueTest() {
        assertThat(getResponse.getHeaderValue("Content-Type"),
            equalTo("application/json; charset=utf-8"));
    }

}