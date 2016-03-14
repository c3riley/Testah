package org.testah.driver.http.response;

import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.testah.TS;

public class ResponseDto {
    
    private int      statusCode    = -1;
    private String   statusText    = null;
    private String   responseBody  = null;
    private Header[] headers       = null;
    private Header[] footers       = null;
    private byte[]   responseBytes = null;
    private String   url           = null;
    private Long     start         = 0L;
    private Long     end           = 0L;

    public ResponseDto() {
    
    }
    
    public ResponseDto(final int statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseDto assertStatus(final int expectedStatus) {
        TS.asserts().equals("assertStatus", expectedStatus, statusCode);
        return this;
    }
    
    public ResponseDto assertResponseBodyContains(final String expectedContents) {
        TS.asserts().notNull("assertResponseBodyContains", responseBody);
        TS.asserts().isTrue("assertResponseBodyContains responseBody[" + responseBody + "] expected to contain["
                + expectedContents + "]", responseBody.contains(expectedContents));
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public Header[] getFooters() {
        return footers;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public String getUrl() {
        return url;
    }
    
    public ResponseDto setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    public ResponseDto setStatusText(final String statusText) {
        this.statusText = statusText;
        return this;
    }
    
    public ResponseDto setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
        return this;
    }
    
    public ResponseDto setResponseBody(final HttpEntity responseBody) {
        try {
            this.responseBody = EntityUtils.toString(responseBody);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public ResponseDto setHeaders(final Header[] headers) {
        this.headers = headers;
        return this;
    }
    
    public ResponseDto setFooters(final Header[] footers) {
        this.footers = footers;
        return this;
    }
    
    public ResponseDto setResponseBytes(final byte[] responseBytes) {
        this.responseBytes = responseBytes;
        return this;
    }
    
    public ResponseDto setUrl(final String url) {
        this.url = url;
        return this;
    }
    
    public ResponseDto print() {
        TS.log().debug(toString());
        return this;
    }
    
    public ResponseDto printStatus() {
        TS.log().debug(toStringStatus());
        return this;
    }

    public String toStringStatus() {
        return new StringBuilder("Status: ").append(statusCode).append("[").append(statusText).append("]").toString();
    }
    
    public String toString() {
        return new StringBuilder(toStringStatus()).append("\nResponse:\n").append(getResponseBody())
                .append("\nHeaders:\n").append(Arrays.toString(getHeaders())).toString();
    }
    
    public Long getStart() {
        return start;
    }
    
    public ResponseDto setStart() {
        return setStart(System.currentTimeMillis());
    }
    
    public ResponseDto setStart(final Long start) {
        this.start = start;
        return this;
    }
    
    public Long getEnd() {
        return end;
    }
    
    public ResponseDto setEnd() {
        return setEnd(System.currentTimeMillis());
    }
    
    public ResponseDto setEnd(final Long end) {
        this.end = end;
        return this;
    }

    public Long getDuration() {
        return (end - start);
    }
}
