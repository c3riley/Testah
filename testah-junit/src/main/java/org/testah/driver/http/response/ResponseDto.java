package org.testah.driver.http.response;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.testah.framework.dto.base.AbstractDtoBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Class ResponseDto.
 */
public class ResponseDto extends AbstractDtoBase<ResponseDto> {

    /**
     * The status code.
     */
    private int statusCode = -1;

    /**
     * The status text.
     */
    private String statusText = null;

    /**
     * The response body.
     */
    @JsonIgnore
    private String responseBody = null;

    /**
     * The headers.
     */
    private Header[] headers = null;

    /**
     * The footers.
     */
    private Header[] footers = null;

    /**
     * The response bytes.
     */
    private byte[] responseBytes = null;

    /**
     * The url.
     */
    private String url = null;

    /**
     * The start.
     */
    private Long start = 0L;

    /**
     * The end.
     */
    private Long end = 0L;

    /**
     * The request type.
     */
    private String requestType = null;

    /**
     * The request used.
     */
    private AbstractRequestDto<?> requestUsed = null;

    private HashMap<String, String> headerHash = null;

    /**
     * Instantiates a new response dto.
     */
    public ResponseDto() {

    }

    /**
     * Instantiates a new response dto.
     *
     * @param statusCode the status code
     */
    public ResponseDto(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Assert status.
     *
     * @param expectedStatus the expected status
     * @return the response dto
     */
    public ResponseDto assertStatus(final int expectedStatus) {
        TS.asserts().equalsTo("assertStatus", expectedStatus, statusCode);
        return this;
    }

    /**
     * Assert status.
     *
     * @return the response dto
     */
    public ResponseDto assertStatus() {
        return assertStatus(getRequestUsed().getExpectedStatus());
    }

    /**
     * Assert response body contains.
     *
     * @param expectedContents the expected contents
     * @return the response dto
     */
    public ResponseDto assertResponseBodyContains(final String expectedContents) {
        TS.asserts().notNull("assertResponseBodyContains", responseBody);
        TS.asserts().isTrue("assertResponseBodyContains responseBody[" + responseBody + "] expected to contain["
            + expectedContents + "]", responseBody.contains(expectedContents));
        return this;
    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the status text.
     *
     * @return the status text
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Gets the response body.
     *
     * @return the response body
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Gets the response body.
     *
     * @param escape the escape
     * @return the response body
     */
    public String getResponseBody(final boolean escape) {
        if (escape) {
            return escapeHtml(getResponseBody());
        } else {
            return getResponseBody();
        }
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public Header[] getHeaders() {
        return headers.clone();
    }

    /**
     * Gets the footers.
     *
     * @return the footers
     */
    public Header[] getFooters() {
        return footers.clone();
    }

    /**
     * Gets the response bytes.
     *
     * @return the response bytes
     */
    public byte[] getResponseBytes() {
        return responseBytes.clone();
    }

    /**
     * Save to file.
     *
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public File saveToFile() throws IOException {
        return saveToFile(File.createTempFile("download", ".tmp", new File(TS.params().getOutput())));
    }

    /**
     * Save to file.
     *
     * @param downloadFile the download file
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public File saveToFile(final File downloadFile) throws IOException {
        try (
            FileOutputStream fileOuputStream = new FileOutputStream(downloadFile))
        {
            fileOuputStream.write(this.getResponseBytes());
            return downloadFile;
        }
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the status code
     * @return the response dto
     */
    public ResponseDto setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Sets the status text.
     *
     * @param statusText the status text
     * @return the response dto
     */
    public ResponseDto setStatusText(final String statusText) {
        this.statusText = statusText;
        return this;
    }

    /**
     * Sets the response body.
     *
     * @param responseBody the response body
     * @return the response dto
     */
    public ResponseDto setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    /**
     * Sets the response body.
     *
     * @param responseBody the response body
     * @return the response dto
     */
    public ResponseDto setResponseBody(final HttpEntity responseBody) {
        try {
            this.responseBody = EntityUtils.toString(responseBody);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public JsonNode getResponse() {
        try {
            return TS.util().getMap().readTree(responseBody);
        } catch (final Exception e) {
            TS.log().debug("Issue getting object from body: " + responseBody);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the response.
     *
     * @param <T>       the generic type
     * @param valueType the value type
     * @return the response
     */
    public <T> T getResponse(final Class<T> valueType) {
        try {
            TS.log().debug("Getting reesponse as " + valueType.getCanonicalName());
            return TS.util().getMap().readValue(responseBody, valueType);
        } catch (final Exception e) {
            TS.log().debug("Issue getting object from body: " + responseBody);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the response.
     *
     * @param <T>       the generic type
     * @param valueType the value type
     * @return the response
     */
    public <T> T getResponse(final TypeReference<T> valueType) {
        try {
            TS.log().debug("Getting response as TypeReference: " + valueType.toString());
            return TS.util().getMap().readValue(responseBody, valueType);
        } catch (final Exception e) {
            TS.log().debug("Issue getting object from body: " + responseBody);
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the headers.
     *
     * @param headers the headers
     * @return the response dto
     */
    public ResponseDto setHeaders(final Header[] headers) {
        this.headers = headers.clone();
        return this;
    }

    /**
     * Sets the footers.
     *
     * @param footers the footers
     * @return the response dto
     */
    public ResponseDto setFooters(final Header[] footers) {
        this.footers = footers.clone();
        return this;
    }

    /**
     * Sets the response bytes.
     *
     * @param responseBytes the response bytes
     * @return the response dto
     */
    public ResponseDto setResponseBytes(final byte[] responseBytes) {
        this.responseBytes = responseBytes.clone();
        return this;
    }

    /**
     * Sets the url.
     *
     * @param url the url
     * @return the response dto
     */
    public ResponseDto setUrl(final String url) {
        this.url = url;
        return this;
    }

    /**
     * Prints the.
     *
     * @return the response dto
     */
    public ResponseDto print() {
        return print(false);
    }

    /**
     * Prints the.
     *
     * @param shortResponseBody the short response body
     * @return the response dto
     */
    public ResponseDto print(final boolean shortResponseBody) {
        return print(shortResponseBody, 500);
    }

    /**
     * Prints the.
     *
     * @param shortResponseBody the short response body
     * @param truncate          the truncate
     * @return the response dto
     */
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
        TS.log().debug(Cli.BAR_SHORT);
        return this;

    }

    /**
     * Adds the as info step.
     *
     * @return the step action dto
     */
    public StepActionDto addAsInfoStep() {
        return createResponseInfoStep(true, true, 2000);
    }

    /**
     * Creates the response info step.
     *
     * @param shortResponseBody the short response body
     * @param escapdeBody       the escapde body
     * @param truncate          the truncate
     * @return the step action dto
     */
    public StepActionDto createResponseInfoStep(final boolean shortResponseBody, final boolean escapdeBody,
                                                final int truncate)
    {
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

    /**
     * Prints the status.
     *
     * @return the response dto
     */
    public ResponseDto printStatus() {
        TS.log().debug(Cli.BAR_SHORT);
        TS.log().debug(Cli.BAR_WALL + "Response");
        TS.log().debug(Cli.BAR_WALL + "URI: " + getUrl());
        TS.log().debug(Cli.BAR_WALL + "Status: " + getStatusCode() + " [ " + getStatusText() + " ]");
        TS.log().debug(Cli.BAR_SHORT);
        return this;
    }

    /**
     * To string status.
     *
     * @return the string
     */
    public String toStringStatus() {
        return new StringBuilder("Uri:").append(getUrl()).append("\nStatus: ").append(statusCode).append(" [ ")
            .append(statusText).append(" ]").toString();
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public Long getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @return the response dto
     */
    public ResponseDto setStart() {
        return setStart(System.currentTimeMillis());
    }

    /**
     * Sets the start.
     *
     * @param start the start
     * @return the response dto
     */
    public ResponseDto setStart(final Long start) {
        this.start = start;
        return this;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public Long getEnd() {
        return end;
    }

    /**
     * Sets the end.
     *
     * @return the response dto
     */
    public ResponseDto setEnd() {
        return setEnd(System.currentTimeMillis());
    }

    /**
     * Sets the end.
     *
     * @param end the end
     * @return the response dto
     */
    public ResponseDto setEnd(final Long end) {
        this.end = end;
        return this;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public Long getDuration() {
        return (end - start);
    }

    /**
     * Gets the request type.
     *
     * @return the request type
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the request type.
     *
     * @param requestType the request type
     * @return the response dto
     */
    public ResponseDto setRequestType(final String requestType) {
        this.requestType = requestType;
        return this;
    }

    /**
     * Gets the request used.
     *
     * @return the request used
     */
    public AbstractRequestDto<?> getRequestUsed() {
        return requestUsed;
    }

    /**
     * Sets the request used.
     *
     * @param requestUsed the new request used
     * @return the response dto
     */
    public ResponseDto setRequestUsed(final AbstractRequestDto<?> requestUsed) {
        this.requestUsed = requestUsed;
        return this;
    }

    /**
     * Get value of header.
     *
     * @param name header name.
     * @return value of header
     */
    public String getHeaderValue(final String name) {
        return getHeaderHash().get(name);
    }

    /**
     * Get the headers as a map.
     *
     * @return header map
     */
    public HashMap<String, String> getHeaderHash() {
        if (null == headerHash) {
            this.headerHash = new HashMap<>();
            for (final Header header : getHeaders()) {
                headerHash.put(header.getName(), header.getValue());
            }
        }
        return headerHash;
    }
}
