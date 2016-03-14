package org.testah.driver.http.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.testah.TS;

public abstract class AbstractRequestDto {
    
    protected String              uri                 = null;
    protected List<Header>        headers             = null;
    protected int                 expectedStatus      = 200;
    protected CredentialsProvider credentialsProvider = null;
    protected HttpRequestBase     httpRequestBase     = null;
    protected HttpEntity          httpEntity          = null;
    
    protected AbstractRequestDto(final HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }

    public AbstractRequestDto withJson() {
        addHeader("content-type", "application/json");
        return this;
    }
    
    public AbstractRequestDto withJsonUTF8() {
        addHeader("content-type", "application/json; charset=UTF-8");
        return this;
    }

    public AbstractRequestDto withFormUrlEncoded() {
        addHeader("content-type", "application/x-www-form-urlencoded");
        return this;
    }
    
    public AbstractRequestDto addHeader(final String name, final String value) {
        addHeader(new BasicHeader(name, value));
        return this;
    }

    public AbstractRequestDto addHeader(final Header header) {
        if (null == headers) {
            headers = new ArrayList<Header>();
        }
        headers.add(header);
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Header[] getHeadersArray() {
        if (null == headers) {
            return null;
        }
        return headers.toArray(new Header[] {});
    }

    public AbstractRequestDto setHeaders(final List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public int getExpectedStatus() {
        return expectedStatus;
    }

    public AbstractRequestDto setExpectedStatus(final int expectedStatus) {
        this.expectedStatus = expectedStatus;
        return this;
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public AbstractRequestDto setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }
    
    public HttpRequestBase getHttpRequestBase() {
        return httpRequestBase;
    }
    
    public void setHttpRequestBase(final HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }
    
    public void setHttpEntity(final HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }
    
    public String getUri() {
        return httpRequestBase.getURI().toString();
    }

    public AbstractRequestDto print() {
        TS.log().debug(httpRequestBase.getMethod() + " " + uri);
        return this;
    }

    public AbstractRequestDto setPayload(final String payload) {
        try {
            return setPayload(new StringEntity(payload));
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public AbstractRequestDto setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                ((HttpPost) httpRequestBase).setEntity(payload);
            }
            else {
                TS.log().warn("payload was null so ignoreing");
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public String getPayloadString() {
        try {
            if (null != getHttpEntity()) {
                return EntityUtils.toString(httpEntity);
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
