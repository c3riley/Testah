package org.testah.driver.http.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.util.unittest.dtotest.DtoTest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

public class PostRequestDtoTest {

    PostRequestDto postEmptyList;
    PostRequestDto postWithData;

    /**
     * Sets .
     */
    @Before
    public void setup() {
        postEmptyList = new PostRequestDto("https://postman-echo.com/post", "[]");
        HashMap<String, String> hash = new HashMap<>();
        hash.put("test1", "1");
        hash.put("test2", "2");
        postWithData = new PostRequestDto("https://postman-echo.com/post", hash);
    }

    @Test
    public void setEntity() {
        postEmptyList.setEntity(null);
        HttpPost post = (HttpPost) postEmptyList.getHttpRequestBase();
        Assert.assertNull(post.getEntity());
    }

    @Test
    public void getHttpMethod() {
        Assert.assertEquals(postEmptyList.getHttpMethod(), "POST");
    }

    @Test
    public void setHttpMethod() {
        Assert.assertEquals(postEmptyList.getHttpMethod(), "POST");
        postEmptyList.setHttpMethod("NOT_POST");
        Assert.assertEquals(postEmptyList.getHttpMethod(), "NOT_POST");
    }

    @Test
    public void getSelf() {
        Assert.assertEquals(postEmptyList, postEmptyList.getSelf());
    }

    @Test
    public void setUri() {
        postEmptyList.setUri("test");
        Assert.assertEquals("test", postEmptyList.getUri());
    }

    @Test
    public void withJson() {
        postEmptyList.withJson();
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Content-Type") && header.getValue()
                .equals("application/json")).isPresent());
    }

    @Test
    public void addHeader() {
        postEmptyList.addHeader(new BasicHeader("cool", "test"));
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("cool") && header.getValue()
                .equals("test")).isPresent());
    }

    @Test
    public void addHeader1() {
        postEmptyList.addHeader("test1", "1");
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("test1") && header.getValue().equals("1")).isPresent());
    }

    @Test
    public void withJsonUTF8() {
        postEmptyList.withJsonUTF8();
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Content-Type") && header.getValue()
                .equals("application/json; charset=UTF-8")).isPresent());
    }

    @Test
    public void withFormUrlEncoded() {
        postEmptyList.withFormUrlEncoded();
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Content-Type") && header.getValue()
                .equals("application/x-www-form-urlencoded")).isPresent());
    }

    @Test
    public void headersTest() {
        Assert.assertEquals(0, postWithData.getHeaders().size());
        Assert.assertEquals(0, postWithData.getHeadersArray().length);
        postWithData.setHeaders(null);
        Assert.assertEquals(0, postWithData.getHeaders().size());
        Assert.assertEquals(0, postWithData.getHeadersArray().length);
        postWithData.setHeaders(new ArrayList<>());
        Assert.assertEquals(0, postWithData.getHeaders().size());
        Assert.assertEquals(0, postWithData.getHeadersArray().length);
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("testName", "testValue"));
        postWithData.setHeaders(headers);
        Assert.assertEquals(1, postWithData.getHeaders().size());
        Assert.assertEquals("testValue",
            postWithData.getHeaders().get(0).getValue());
        Assert.assertEquals(1, postWithData.getHeadersArray().length);
        Assert.assertEquals("testValue",
            postWithData.getHeadersArray()[0].getValue());
    }


    @Test
    public void expectedStatus() {
        Assert.assertEquals(SC_OK, postWithData.getExpectedStatus());
        postWithData.setExpectedStatus(SC_NOT_FOUND);
        Assert.assertEquals(SC_NOT_FOUND, postWithData.getExpectedStatus());
    }

    @Test
    public void getCredentialsProvider() {
    }

    @Test
    public void setCredentialsProvider() {
    }

    @Test
    public void setBasicAuthCredentials() {
        Assert.assertNull(postWithData.getCredentialsProvider());
        postWithData.setBasicAuthCredentials("user", "password", AuthScope.ANY);
        CredentialsProvider credentialsProvider = postWithData.getCredentialsProvider();
        Assert.assertEquals("{<any realm>=[principal: user]}", credentialsProvider.toString());
    }

    @Test
    public void setBasicAuthCredentials1() {
        Assert.assertNull(postWithData.getCredentialsProvider());
        postWithData.setBasicAuthCredentials("user", "password");
        CredentialsProvider credentialsProvider = postWithData.getCredentialsProvider();
        Assert.assertEquals("{<any realm>=[principal: user]}", credentialsProvider.toString());
    }

    @Test
    public void addBasicAuth() throws Exception {
        postWithData.addBasicAuth("User1", "password");
        Assert.assertTrue(postWithData.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Authorization") &&
                header.getValue().equals("Basic VXNlcjE6cGFzc3dvcmQ=")
        ).isPresent());
    }

    @Test
    public void getterSetterTest() throws Exception {
        DtoTest test = new DtoTest();
        test.addToAnnotationsToIgnore(JsonIgnore.class);
        test.addToIgnoredGetFields("getCredentialsProvider");
        test.addToIgnoredGetFields("withFormUrlEncoded");
        test.addToIgnoredGetFields("getHeadersArray");
        test.addToIgnoredGetFields("getHttpEntity");
        test.addToIgnoredGetFields("getHttpRequestBase");
        test.addToIgnoredGetFields("withJson");
        test.addToIgnoredGetFields("withJsonUTF8");
        test.addToIgnoredGetFields("getPayloadString");
        test.addToIgnoredGetFields("getPayloadStringEscaped");

        test.testGettersAndSetters(postWithData);

    }

    @Test
    public void addBasicAuthHeader() {
        postWithData.addBasicAuthHeader("User1", "password");
        Assert.assertTrue(postWithData.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Authorization") &&
                header.getValue().equals("Basic VXNlcjE6cGFzc3dvcmQ=")
        ).isPresent());
    }

    @Test
    public void addBasicAuthHeader1() {
        TS.destroyMaskValueMap();
        postWithData.addBasicAuthHeader("User1", "password", true);
        Assert.assertTrue(postWithData.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Authorization") &&
                header.getValue().equals("Basic VXNlcjE6cGFzc3dvcmQ=")
        ).isPresent());

        TS.asserts().assertSystemOutContains(() -> postWithData.printComplete(), "Basic VXNlcjE6cGFzc3dvcmQ=");
        Assert.assertEquals(3, TS.getMaskValues().size());
        Assert.assertNotNull(TS.getMaskValues().get("User1"));
        Assert.assertNotNull(TS.getMaskValues().get("password"));
        Assert.assertNotNull(TS.getMaskValues().get("VXNlcjE6cGFzc3dvcmQ="));
    }

    @Test
    public void addBasicAuthHeaderWithMask() {
        TS.destroyMaskValueMap();
        postWithData.addBasicAuthHeaderWithMask("User1", "password");
        Assert.assertTrue(postWithData.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Authorization") &&
                header.getValue().equals("Basic VXNlcjE6cGFzc3dvcmQ=")
        ).isPresent());

        TS.asserts().assertSystemOutContains(() -> postWithData.printComplete(), "Basic VXNlcjE6cGFzc3dvcmQ=");
        Assert.assertEquals(3, TS.getMaskValues().size());
        Assert.assertNotNull(TS.getMaskValues().get("User1"));
        Assert.assertNotNull(TS.getMaskValues().get("password"));
        Assert.assertNotNull(TS.getMaskValues().get("VXNlcjE6cGFzc3dvcmQ="));
    }

    @Test
    public void getHttpEntity() {
    }

    @Test
    public void getHttpRequestBase() {
    }

    @Test
    public void setHttpRequestBase() {
    }

    @Test
    public void setHttpEntity() {
    }

    @Test
    public void getUri() {
        Assert.assertEquals("https://postman-echo.com/post", postWithData.getUri());
    }

    @Test
    public void setEntity1() {
    }

    @Test
    public void setPayload() {
    }

    @Test
    public void setPayload1() {
    }

    @Test
    public void setPayload2() {
    }

    @Test
    public void setPayload3() {
    }

    @Test
    public void setContentType() {
        postEmptyList.setContentType("html");
        Assert.assertTrue(postEmptyList.getHeaders().stream().findFirst().filter(
            header -> header.getName().equals("Content-Type") && header.getValue()
                .equals("html")).isPresent());
    }

    @Test
    public void getPayloadString() {
        Assert.assertEquals("[]", postEmptyList.getPayloadString());
        Assert.assertEquals("{" + System.lineSeparator() +
            "  \"test2\" : \"2\"," + System.lineSeparator() +
            "  \"test1\" : \"1\"" + System.lineSeparator() +
            "}", postWithData.getPayloadString());
    }

    @Test
    public void print() {
        TS.asserts()
            .assertSystemOutContains(() ->
            {
                postWithData.print();
            }, "DEBUG - AbstractRequestDto        - POST https://postman-echo.com/post");
    }


    @Test
    public void printComplete() {
        TS.asserts().assertSystemOutContains(() -> {
                postWithData.printComplete();
            },
            "DEBUG - AbstractRequestDto        - # Request POST",
            "DEBUG - AbstractRequestDto        - # URI: https://postman-echo.com/post",
            "DEBUG - AbstractRequestDto        - # Expected Status: 200",
            "DEBUG - AbstractRequestDto        - # Headers: []",
            "DEBUG - AbstractRequestDto        - =========================================",
            "DEBUG - AbstractRequestDto        - # payload: (see below)",
            "{" + System.lineSeparator() +
                "  \"test2\" : \"2\"," + System.lineSeparator() +
                "  \"test1\" : \"1\"" + System.lineSeparator() +
                "}",
            "DEBUG - AbstractRequestDto        - ========================================="
        );
    }

    @Test
    public void createRequestInfoStep() {
    }

    @Test
    public void getPayloadStringEscaped() {
    }

    @Test
    public void isAutoAssert() {
    }

    @Test
    public void setAutoAssert() {
    }

    @Test
    public void setUploadFile() {
    }

    @Test
    public void setUploadFile1() {
    }

    @Test
    public void setUpload() {
    }

    @Test
    public void setUpload1() {
    }

    @Test
    public void setUpload2() {
    }

    @Test
    public void setUploadResourceFile() {
    }

    @Test
    public void isTruncateResponseBodyInReport() {
    }

    @Test
    public void setTruncateResponseBodyInReport() {
    }

    @Test
    public void getTruncateResponseBodyInReportBy() {
    }

    @Test
    public void setTruncateResponseBodyInReportBy() {
    }

    @Test
    public void equalsVerbose() {
    }

    @Test
    public void hashCodeTest() {
    }

    @Test
    public void equalsTest() {
    }

    @Test
    public void cloneTest() {
    }

    @Test
    public void toStringTest() {
    }

    @Test
    public void toString1() {
    }

    @Test
    public void toJson() {
    }

    @Test
    public void getClassPath() {
    }

    @Test
    public void getToStringListOfFieldsToExclude() {
    }

    @Test
    public void setToStringListOfFieldsToExclude() {
    }

    @Test
    public void assertEquals() {
    }

    @Test
    public void assertEquals1() {
    }

    @Test
    public void getAdditionalProperties() {
    }

    @Test
    public void setAdditionalProperty() {
    }

    @Test
    public void toStringStyle() {
        Assert.assertEquals(ToStringStyle.JSON_STYLE, postWithData.getToStringStyle());
        postWithData.setToStringStyle(ToStringStyle.SIMPLE_STYLE);
        Assert.assertEquals(ToStringStyle.SIMPLE_STYLE, postWithData.getToStringStyle());
    }

    @Test
    public void testConstructorOnlyUrl() {
        PostRequestDto post = new PostRequestDto("https://postman-echo.com/post");
        Assert.assertNull(TS.http().doRequest(post).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsObject() {
        final List<String> payload = new ArrayList<>();
        payload.add("this is a test");
        PostRequestDto post = new PostRequestDto("https://postman-echo.com/post", payload);
        Assert.assertEquals("[ \"this is a test\" ]", TS.http().doRequest(post).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsString() {
        final String payload = "This is a test";
        PostRequestDto post = new PostRequestDto("https://postman-echo.com/post", "This is a test");
        Assert.assertEquals(payload, TS.http().doRequest(post).assertStatus().getResponse().get("data").textValue());
    }

    @Test
    public void testConstructorUrlAndPayloadAsByteArray() {
        final String payload = "This is a test";
        PostRequestDto post = new PostRequestDto("https://postman-echo.com/post",
            payload.getBytes(Charset.forName("UTF8")));
        JsonNode json = TS.http().doRequest(post).assertStatus().getResponse();
        Assert.assertEquals("application/octet-stream", json.get("headers").get("content-type").textValue());
        Assert.assertEquals("Buffer", json.get("data").get("type").textValue());
        Assert.assertEquals("[84,104,105,115,32,105,115,32,97,32,116,101,115,116]", json.get("data").get("data").toString());
    }

    @Test
    public void testConstructorUrlAndPayloadAsHttpEntity() throws UnsupportedEncodingException {
        final String payload = "This is a test";
        HttpEntity entity = new StringEntity(payload);
        PostRequestDto post = new PostRequestDto("https://postman-echo.com/post", entity);
        JsonNode json = TS.http().doRequest(post).assertStatus().getResponse();
        Assert.assertEquals("text/plain; charset=ISO-8859-1", json.get("headers").get("content-type").textValue());
        Assert.assertEquals(payload, json.get("data").textValue());
    }
}