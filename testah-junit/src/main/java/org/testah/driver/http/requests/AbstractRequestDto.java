package org.testah.driver.http.requests;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.driver.http.HttpAuthUtil;
import org.testah.framework.cli.Cli;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * The Class AbstractRequestDto.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractRequestDto<T> extends AbstractDtoBase<AbstractRequestDto<T>> {

    /**
     * The uri.
     */
    protected String uri = null;

    /**
     * The headers.
     */
    protected List<Header> headers = new ArrayList<>();

    /**
     * The expected status.
     */
    protected int expectedStatus = SC_OK;

    /**
     * The credentials provider.
     */
    protected CredentialsProvider credentialsProvider = null;

    /**
     * The http request base.
     */
    protected HttpRequestBase httpRequestBase = null;

    /**
     * The http entity.
     */
    protected HttpEntity httpEntity = null;

    /**
     * The http method.
     */
    protected String httpMethod = null;

    /**
     * The auto assert.
     */
    protected boolean autoAssert = false;

    /**
     * if the Http driver should use the short response in the report and truncate it.
     * Default is true, and will use truncateResponseBodyInReportBy to tell how much to truncate it by
     */
    private boolean truncateResponseBodyInReport = true;

    /**
     * if truncateResponseBodyInReport is true, this number will tell how much to truncate the response by.
     */
    private int truncateResponseBodyInReportBy = TS.params().getDefaultResponseTruncate();

    /**
     * Instantiates a new abstract request dto.
     *
     * @param httpRequestBase the http request base
     * @param httpMethod      the http method
     */
    protected AbstractRequestDto(final HttpRequestBase httpRequestBase, final String httpMethod) {
        this.httpRequestBase = httpRequestBase;
        this.httpMethod = httpMethod;
        if (httpRequestBase != null && httpRequestBase.getURI() != null) {
            this.uri = httpRequestBase.getURI().toString();
        }
    }

    /**
     * Gets the http method.
     *
     * @return the http method
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * Sets the http method.
     *
     * @param httpMethod the http method
     * @return the abstract request dto
     */
    public T setHttpMethod(final String httpMethod) {
        this.httpMethod = httpMethod;
        return getSelf();
    }

    /**
     * Gets the self.
     *
     * @return the self
     */
    @SuppressWarnings("unchecked")
    protected T getSelf() {
        return (T) this;
    }

    /**
     * Sets the uri.
     *
     * @param uri the uri
     * @return the abstract request dto
     */
    public T setUri(final String uri) {
        try {
            httpRequestBase.setURI(new URI(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.uri = uri;
        return getSelf();
    }

    /**
     * With json.
     *
     * @return the abstract request dto
     */
    public T withJson() {
        addHeader("Content-Type", "application/json");
        return getSelf();
    }

    /**
     * Adds the header.
     *
     * @param name  the name
     * @param value the value
     * @return the abstract request dto
     */
    public T addHeader(final String name, final String value) {
        addHeader(new BasicHeader(name, value));
        return getSelf();
    }

    /**
     * Adds the header.
     *
     * @param header the header
     * @return the abstract request dto
     */
    public T addHeader(final Header header) {
        headers.add(header);
        return getSelf();
    }

    /**
     * With json UTF 8.
     *
     * @return the abstract request dto
     */
    @SuppressWarnings("checkstyle:abbreviationaswordinnamecheck")
    public T withJsonUTF8() {
        addHeader("Content-Type", "application/json; charset=UTF-8");
        return getSelf();
    }

    /**
     * With form url encoded.
     *
     * @return the abstract request dto
     */
    public T withFormUrlEncoded() {
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        return getSelf();
    }

    /**
     * Gets the headers array.
     *
     * @return the headers array
     */
    public Header[] getHeadersArray() {
        return headers.toArray(new Header[]{});
    }

    /**
     * Sets the headers.
     *
     * @param headers the headers
     * @return the abstract request dto
     */
    public T setHeaders(final List<Header> headers) {
        if (headers == null) {
            TS.log().debug("Clearing headers on request as null was sent in, headers cannot be null");
            this.headers.clear();
        } else {
            this.headers = headers;
        }
        return getSelf();
    }

    /**
     * Gets the expected status.
     *
     * @return the expected status
     */
    public int getExpectedStatus() {
        return expectedStatus;
    }

    /**
     * Sets the expected status.
     *
     * @param expectedStatus the expected status
     * @return the abstract request dto
     */
    public T setExpectedStatus(final int expectedStatus) {
        this.expectedStatus = expectedStatus;
        return getSelf();
    }

    /**
     * Gets the credentials provider.
     *
     * @return the credentials provider
     */
    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    /**
     * Sets the credentials provider.
     *
     * @param credentialsProvider the credentials provider
     * @return the abstract request dto
     */
    public T setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return getSelf();
    }

    /**
     * Sets the basic auth credentials.
     *
     * @param userName the user name
     * @param password the password
     * @return the abstract http wrapper
     */
    public T setBasicAuthCredentials(final String userName, final String password) {
        return setBasicAuthCredentials(userName, password, AuthScope.ANY);
    }

    /**
     * Sets the basic auth credentials.
     *
     * @param userName  the user name
     * @param password  the password
     * @param authScope the auth scope
     * @return the abstract request dto
     */
    public T setBasicAuthCredentials(final String userName, final String password,
                                     final AuthScope authScope) {
        credentialsProvider = new BasicCredentialsProvider();
        final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
        credentialsProvider.setCredentials(authScope, creds);
        return getSelf();
    }

    /**
     * Adds the basic auth.
     *
     * @param userName the user name
     * @param password the password
     * @return the abstract request dto
     */
    public T addBasicAuth(final String userName, final String password) {
        return addHeader(new HttpAuthUtil().setUserName(userName).setPassword(password).useEncodingUtf8().createBasicAuthHeader());
    }

    /**
     * Adds the basic auth header.
     *
     * @param userName the user name
     * @param password the password
     * @return the abstract request dto
     */
    public T addBasicAuthHeader(final String userName, final String password) {
        return addBasicAuthHeader(userName, password, false);
    }

    public T addBasicAuthHeader(final String userName, final String password, final boolean useMask) {
        return addHeader(new HttpAuthUtil().setUserName(userName).setPassword(password).useEncodingIso()
            .setUseMask(useMask).createBasicAuthHeader());
    }

    /**
     * Adds the basic auth header with mask.
     *
     * @param userName the user name
     * @param password the password
     * @return the abstract request dto
     */
    public T addBasicAuthHeaderWithMask(final String userName, final String password) {
        return addBasicAuthHeader(userName, password, true);
    }

    /**
     * Gets the http entity.
     *
     * @return the http entity
     */
    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    /**
     * Gets the http request base.
     *
     * @return the http request base
     */
    public HttpRequestBase getHttpRequestBase() {
        if (null != headers && null != httpRequestBase.getAllHeaders() && httpRequestBase.getAllHeaders().length == 0) {
            for (final Header header : getHeaders()) {
                httpRequestBase.addHeader(header);
            }
        }
        return httpRequestBase;
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * Sets the http request base.
     *
     * @param httpRequestBase the http request base
     * @return the abstract request dto
     */
    public T setHttpRequestBase(final HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
        return getSelf();
    }

    /**
     * Sets the http entity.
     *
     * @param httpEntity the http entity
     * @return the abstract request dto
     */
    public T setHttpEntity(final HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return getSelf();
    }

    /**
     * Gets the uri.
     *
     * @return the uri
     */
    public String getUri() {
        return httpRequestBase.getURI().toString();
    }

    /**
     * Sets entity.
     *
     * @param payload the payload
     * @return the entity
     */
    protected abstract T setEntity(final HttpEntity payload);

    /**
     * Sets payload.
     *
     * @param payload the payload
     * @return the payload
     */
    public T setPayload(String payload) {
        try {
            if (null == payload) {
                payload = "";
            }
            return setPayload(new StringEntity(payload));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets payload.
     *
     * @param payload the payload
     * @return the payload
     */
    public T setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                setEntity(payload);
            } else {
                TS.log().warn("payload was null so setting to empty string");
                setEntity(new StringEntity(""));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return getSelf();
    }

    /**
     * Sets payload.
     *
     * @param payload the payload
     * @return the payload
     */
    public T setPayload(final Object payload) {
        try {
            if (null == payload) {
                TS.log().warn("payload was null so setting to empty string");
                return setPayload(new StringEntity(""));
            } else {
                return setPayload(new StringEntity(TS.util().toJson(payload)));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets payload.
     *
     * @param payload the payload
     * @return the payload
     */
    public T setPayload(final byte[] payload) {
        setContentType("application/octet-stream");
        return setPayload(new ByteArrayEntity(payload));
    }

    /**
     * Sets the content type.
     *
     * @param value the value
     * @return the abstract request dto
     */
    public T setContentType(final String value) {
        addHeader("Content-Type", value);
        return getSelf();
    }

    /**
     * Gets the payload string.
     *
     * @return the payload string
     */
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

    /**
     * Prints the.
     *
     * @return the abstract request dto
     */
    public T print() {
        TS.log().debug(httpRequestBase.getMethod() + " " + uri);
        return getSelf();
    }

    /**
     * Prints the complete.
     *
     * @return the abstract request dto
     */
    public T printComplete() {
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

    /**
     * Creates the request info step.
     *
     * @return the step action dto
     */
    public StepActionDto createRequestInfoStep() {
        StepActionDto stepAction = TS.step().action().createInfo("REQUEST: " + this.getHttpMethod() + " - Uri: " + getUri(),
            "Expected Status: " + getExpectedStatus() + " - Headers: " + (null == headers ? ""
                : Arrays.toString(headers.toArray())),
            getPayloadStringEscaped(), false).setTestStepActionType(TestStepActionType.HTTP_REQUEST);
        printComplete();
        return stepAction;
    }

    /**
     * Gets the payload string escaped.
     *
     * @return the payload string escaped
     */
    public String getPayloadStringEscaped() {
        return escapeHtml(getPayloadString());
    }

    /**
     * Checks if is auto assert.
     *
     * @return true, if is auto assert
     */
    public boolean isAutoAssert() {
        return autoAssert;
    }

    /**
     * Sets the auto assert.
     *
     * @param autoAssert the new auto assert
     * @return the t
     */
    public T setAutoAssert(final boolean autoAssert) {
        this.autoAssert = autoAssert;
        return getSelf();
    }

    /**
     * Sets the upload file.
     *
     * @param payload the payload
     * @return the abstract request dto
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public T setUploadFile(final String payload) throws IOException {
        File file = new File(payload);
        TS.asserts().assertFileExists(file);
        return setUpload(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
    }

    /**
     * Sets the upload file.
     *
     * @param payload the payload
     * @return the abstract request dto
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public T setUploadFile(final File payload) throws IOException {
        TS.asserts().assertFileExists(payload);
        return setUpload(FileUtils.readFileToString(payload, Charset.forName("UTF-8")));
    }

    /**
     * Sets the upload.
     *
     * @param payload the payload
     * @return the abstract request dto
     */
    public T setUpload(final String payload) {
        return setUpload(payload, "UTF-8");
    }

    /**
     * Sets the upload.
     *
     * @param payload the payload
     * @param charset the charset
     * @return the abstract request dto
     */
    public T setUpload(final String payload, final String charset) {
        return setUpload(payload.getBytes(Charset.forName(charset)));
    }

    /**
     * Sets the upload.
     *
     * @param payload the payload
     * @return the abstract request dto
     */
    public T setUpload(final byte[] payload) {
        setPayload(new ByteArrayEntity(payload));
        setContentType("application/octet-stream");
        return getSelf();
    }

    /**
     * Sets the upload resource file.
     *
     * @param pathToResource the path to resource
     * @return the abstract request dto
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public T setUploadResourceFile(final String pathToResource) throws IOException {
        final String fileContent = TS.util().getResourceAsString(pathToResource);
        TS.asserts().notNull("setUploadResourceFile make sure value is not null", fileContent);
        return setUpload(fileContent.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Is truncate response body in report boolean.
     * If the boolean is true, but also the truncateResponseBodyInReportBy needs to be set to 0 or more, else it will
     * turn off the truncation, and this will return false.
     *
     * @return the boolean
     */
    public boolean isTruncateResponseBodyInReport() {
        return truncateResponseBodyInReport && truncateResponseBodyInReportBy > 0;
    }

    public T setTruncateResponseBodyInReport(boolean truncateResponseBodyInReport) {
        this.truncateResponseBodyInReport = truncateResponseBodyInReport;
        return getSelf();
    }

    public int getTruncateResponseBodyInReportBy() {
        return truncateResponseBodyInReportBy;
    }

    public T setTruncateResponseBodyInReportBy(int truncateResponseBodyInReportBy) {
        this.truncateResponseBodyInReportBy = truncateResponseBodyInReportBy;
        return getSelf();
    }
}
