package org.testah.driver.http.requests;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.testah.TS;
import org.testah.framework.dto.StepActionDto;
import org.testah.framework.enums.TestStepActionType;

public abstract class AbstractRequestDto {

    protected String              uri                 = null;
    protected List<Header>        headers             = null;
    protected int                 expectedStatus      = 200;
    protected CredentialsProvider credentialsProvider = null;
    protected HttpRequestBase     httpRequestBase     = null;
    protected HttpEntity          httpEntity          = null;
    protected String              httpMethod          = null;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(final String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    protected AbstractRequestDto(final HttpRequestBase httpRequestBase, final String httpMethod) {
        this.httpRequestBase = httpRequestBase;
        this.httpMethod = httpMethod;
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

    public abstract AbstractRequestDto setPayload(final String payload);

    public abstract AbstractRequestDto setPayload(final HttpEntity payload);

    public abstract AbstractRequestDto setPayload(final Object payload);

    public String getPayloadString() {
        try {
            if (null != getHttpEntity()) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public AbstractRequestDto print() {
        TS.log().debug(httpRequestBase.getMethod() + " " + uri);
        return this;
    }

    public AbstractRequestDto printComplete() {
        TS.log().debug("###########");
        TS.log().debug("# Request " + httpMethod);
        TS.log().debug("# URI: " + getUri());
        if (null != getCredentialsProvider()) {
            TS.log().debug("# Credential: " + TS.util().toJson(getCredentialsProvider()));
        }
        TS.log().debug("# Expected Status: " + getExpectedStatus());
        TS.log().debug("# Headers: " + (null == headers ? "" : Arrays.toString(headers.toArray())));
        TS.log().debug("###########");
        if (null != getPayloadString()) {
            TS.log().debug("# payload: (see below)");
            System.out.println(getPayloadString());
            TS.log().debug("###########");
        }
        return this;
    }

    public StepActionDto createRequestInfoStep() {
        StepActionDto stepAction = null;
        stepAction = StepActionDto.createInfo("REQUEST: " + this.getHttpMethod() + " - Uri: " + getUri(),
                "Expected Status: " + getExpectedStatus() + " - Headers: "
                        + (null == headers ? "" : Arrays.toString(headers.toArray())),
                getPayloadStringEscaped(), false).setTestStepActionType(TestStepActionType.HTTP_REQUEST);
        printComplete();
        return stepAction;
    }

    public String getPayloadStringEscaped() {
        return escapeHtml(getPayloadString());
    }

}
