package org.testah.driver.http.response;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.framework.cli.Cli;
import org.testah.framework.dto.StepAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

public class ResponseDto {

	private int statusCode = -1;
	private String statusText = null;
	@JsonIgnore
	private String responseBody = null;
	private Header[] headers = null;
	private Header[] footers = null;
	private byte[] responseBytes = null;
	private String url = null;
	private Long start = 0L;
	private Long end = 0L;
	private String requestType = null;
	private AbstractRequestDto requestUsed = null;

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
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public JsonNode getResponse() {
		try {
			return TS.util().getMap().readTree(responseBody);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T getResponse(final Class<T> valueType) {
		try {
			return TS.util().getMap().readValue(responseBody, valueType);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T getResponse(final TypeReference<T> valueType) {
		try {
			return TS.util().getMap().readValue(responseBody, valueType);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
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
		return print(false);
	}

	public ResponseDto print(final boolean shortResponseBody) {
		return print(shortResponseBody, 500);
	}

	public ResponseDto print(final boolean shortResponseBody, final int truncate) {
		TS.log().debug(Cli.BAR_SHORT);
		TS.log().debug(Cli.BAR_WALL + "Response");
		TS.log().debug(Cli.BAR_WALL + "URI: " + getUrl());
		TS.log().debug(Cli.BAR_WALL + "Status: " + getStatusCode() + " [ " + getStatusText() + " ]");
		TS.log().debug(Cli.BAR_WALL + "Headers: " + Arrays.toString(getHeaders()));
		TS.log().debug(Cli.BAR_SHORT);
		TS.log().debug(Cli.BAR_WALL + "Body: (see below)");
		if (shortResponseBody) {
			System.out.println(StringUtils.abbreviate(getResponseBody(), truncate));
		} else {
			System.out.println(getResponseBody());
		}
		return this;

	}

	public StepActionDto addAsInfoStep() {
		return createResponseInfoStep(true, true, 2000);
	}

	public StepActionDto createResponseInfoStep(final boolean shortResponseBody, final boolean escapdeBody,
			final int truncate) {
		StepActionDto stepAction = null;
		if (shortResponseBody) {
			stepAction = StepAction
					.createInfo(this.getRequestType() + " - Uri: " + getUrl(),
							"Status: " + getStatusCode() + " [ " + getStatusText() + " ]",
							StringUtils.abbreviate(getResponseBody(escapdeBody), truncate), false)
					.setTestStepActionType(TestStepActionType.HTTP_REQUEST);
		} else {
			stepAction = StepAction.createInfo(this.getRequestType() + " - Uri: " + getUrl(),
					"Status: " + getStatusCode() + " [ " + getStatusText() + " ]", getResponseBody(escapdeBody), false)
					.setTestStepActionType(TestStepActionType.HTTP_REQUEST);

		}
		print(shortResponseBody, truncate);
		return stepAction;
	}

	public String getResponseBody(final boolean escape) {
		if (escape) {
			return escapeHtml(getResponseBody());
		} else {
			return getResponseBody();
		}
	}

	public ResponseDto printStatus() {
		TS.log().debug(Cli.BAR_SHORT);
		TS.log().debug(Cli.BAR_WALL + "Response");
		TS.log().debug(Cli.BAR_WALL + "URI: " + getUrl());
		TS.log().debug(Cli.BAR_WALL + "Status: " + getStatusCode() + " [ " + getStatusText() + " ]");
		TS.log().debug(Cli.BAR_SHORT);
		return this;
	}

	public String toStringStatus() {
		return new StringBuilder("Uri:").append(getUrl()).append("\nStatus: ").append(statusCode).append(" [ ")
				.append(statusText).append(" ]").toString();
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

	public String getRequestType() {
		return requestType;
	}

	public ResponseDto setRequestType(final String requestType) {
		this.requestType = requestType;
		return this;
	}

	public AbstractRequestDto getRequestUsed() {
		return requestUsed;
	}

	public void setRequestUsed(final AbstractRequestDto requestUsed) {
		this.requestUsed = requestUsed;
	}
}
