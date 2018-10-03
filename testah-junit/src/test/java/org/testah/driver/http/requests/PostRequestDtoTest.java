package org.testah.driver.http.requests;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PostRequestDtoTest {

    PostRequestDto postEmptyList;
    PostRequestDto postWithData;

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
        postWithData.setEntity(null);
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
    public void getHeadersArray() {
    }

    @Test
    public void setHeaders() {
    }

    @Test
    public void getExpectedStatus() {
    }

    @Test
    public void setExpectedStatus() {
    }

    @Test
    public void getCredentialsProvider() {
    }

    @Test
    public void setCredentialsProvider() {
    }

    @Test
    public void setBasicAuthCredentials() {
    }

    @Test
    public void setBasicAuthCredentials1() {
    }

    @Test
    public void addBasicAuth() {
    }

    @Test
    public void addBasicAuthHeader() {
    }

    @Test
    public void addBasicAuthHeader1() {
    }

    @Test
    public void addBasicAuthHeaderWithMask() {
    }

    @Test
    public void getHttpEntity() {
    }

    @Test
    public void getHttpRequestBase() {
    }

    @Test
    public void getHeaders() {
    }

    @Test
    public void setHttpRequestBase() {
    }

    @Test
    public void setHttpEntity() {
    }

    @Test
    public void getUri() {
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
    }

    @Test
    public void getPayloadString() {
    }

    @Test
    public void print() {
    }

    @Test
    public void printComplete() {
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
    public void isAllowUnknown() {
    }

    @Test
    public void setAllowUnknown() {
    }

    @Test
    public void getToStringStyle() {
    }

    @Test
    public void setToStringStyle() {
    }
}