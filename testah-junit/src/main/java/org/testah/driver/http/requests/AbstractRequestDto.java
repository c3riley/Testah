package org.testah.driver.http.requests;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
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

	public String getHttpMethod() {
		return httpMethod;
	}

	public AbstractRequestDto setHttpMethod(final String httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

	protected AbstractRequestDto(final HttpRequestBase httpRequestBase, final String httpMethod) {
		this.httpRequestBase = httpRequestBase;
		this.httpMethod = httpMethod;
	}

	public AbstractRequestDto withJson() {
		addHeader("Content-Type", "application/json");
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

	public AbstractRequestDto setBasicAuthCredentials(final String userName, final String password,
			final AuthScope authScope) {
		credentialsProvider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
		credentialsProvider.setCredentials(authScope, creds);
		return this;
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
		return this;
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
