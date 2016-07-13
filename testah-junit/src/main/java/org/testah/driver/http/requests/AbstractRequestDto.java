package org.testah.driver.http.requests;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.internal.Base64Encoder;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.framework.cli.Cli;
import org.testah.framework.dto.StepAction;

public abstract class AbstractRequestDto {

    protected String uri = null;
    protected List<Header> headers = null;
    protected int expectedStatus = 200;
    protected CredentialsProvider credentialsProvider = null;
    protected HttpRequestBase httpRequestBase = null;
    protected HttpEntity httpEntity = null;
    protected String httpMethod = null;

    protected abstract AbstractRequestDto getSelf();

    public String getHttpMethod() {
        return httpMethod;
    }

    public AbstractRequestDto setHttpMethod(final String httpMethod) {
        this.httpMethod = httpMethod;
        return getSelf();
    }

    public AbstractRequestDto setUri(final String uri) {
        this.uri = uri;
        return getSelf();
    }

    protected AbstractRequestDto(final HttpRequestBase httpRequestBase, final String httpMethod) {
        this.httpRequestBase = httpRequestBase;
        this.httpMethod = httpMethod;
    }

    public AbstractRequestDto withJson() {
        addHeader("Content-Type", "application/json");
        return getSelf();
    }

    public AbstractRequestDto withJsonUTF8() {
        addHeader("content-type", "application/json; charset=UTF-8");
        return getSelf();
    }

    public AbstractRequestDto withFormUrlEncoded() {
        addHeader("content-type", "application/x-www-form-urlencoded");
        return getSelf();
    }

    public AbstractRequestDto addHeader(final String name, final String value) {
        addHeader(new BasicHeader(name, value));
        return getSelf();
    }

    public AbstractRequestDto addHeader(final Header header) {
        if (null == headers) {
            headers = new ArrayList<Header>();
        }
        headers.add(header);
        return getSelf();
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
        return getSelf();
    }

    public int getExpectedStatus() {
        return expectedStatus;
    }

    public AbstractRequestDto setExpectedStatus(final int expectedStatus) {
        this.expectedStatus = expectedStatus;
        return getSelf();
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public AbstractRequestDto setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return getSelf();
    }

    public AbstractRequestDto setBasicAuthCredentials(final String userName, final String password,
            final AuthScope authScope) {
        credentialsProvider = new BasicCredentialsProvider();
        final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
        credentialsProvider.setCredentials(authScope, creds);
        return getSelf();
    }

    public AbstractRequestDto addBasicAuth(final String userName, final String password) {
        final String encoding = Base64.encodeBase64String("test1:test1".getBytes(Charset.forName("UTF-8")));
        addHeader("Authorization", "Basic " + encoding);
        return getSelf();
    }

    public AbstractRequestDto addBasicAuthHeader(final String userName, final String password) {
        final String encoding = new Base64Encoder()
                .encode((userName + ":" + password).getBytes(Charset.forName("ISO-8859-1")));
        addHeader(new BasicHeader("Authorization", "Basic " + encoding));
        return getSelf();
    }

    public AbstractRequestDto addBasicAuthHeaderWithMask(final String userName, final String password) {
        TS.addMask(userName);
        TS.addMask(password);
        return addBasicAuthHeader(userName, password);
    }

    /**
     * Sets the basic auth credentials.
     *
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @return the abstract http wrapper
     */
    public AbstractRequestDto setBasicAuthCredentials(final String userName, final String password) {
        return setBasicAuthCredentials(userName, password, AuthScope.ANY);
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public HttpRequestBase getHttpRequestBase() {
        if (null != headers && null != httpRequestBase.getAllHeaders() && httpRequestBase.getAllHeaders().length == 0) {
            for (final Header header : getHeaders()) {
                httpRequestBase.addHeader(header);
            }
        }
        return httpRequestBase;
    }

    public AbstractRequestDto setHttpRequestBase(final HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
        return getSelf();
    }

    public AbstractRequestDto setHttpEntity(final HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return getSelf();
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
        return getSelf();
    }

    public AbstractRequestDto printComplete() {
        TS.log().debug(Cli.BAR_SHORT);
        TS.log().debug(Cli.BAR_WALL + "Request " + httpMethod);
        TS.log().debug(Cli.BAR_WALL + "URI: " + getUri());
        if (null != getCredentialsProvider()) {
            TS.log().debug(Cli.BAR_WALL + "Credential: " + TS.util().toJson(getCredentialsProvider()));
        }
        TS.log().debug(Cli.BAR_WALL + "Expected Status: " + getExpectedStatus());
        TS.log().debug(Cli.BAR_WALL + "Headers: " + (null == headers ? "" : Arrays.toString(headers.toArray())));
        TS.log().debug(Cli.BAR_SHORT);
        if (null != getPayloadString()) {
            TS.log().debug(Cli.BAR_WALL + "payload: (see below)");
            System.out.println(getPayloadString());
            TS.log().debug(Cli.BAR_SHORT);
        }
        return getSelf();
    }

    public StepActionDto createRequestInfoStep() {
        StepActionDto stepAction = null;
        stepAction = StepAction.createInfo("REQUEST: " + this.getHttpMethod() + " - Uri: " + getUri(),
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
