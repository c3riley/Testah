package org.testah.runner.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testah.TS;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.performance.dto.RequestExecutionDuration;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ElasticSearchResponseTimesPublisher implements ExecutionStatsPublisher {

    public static final String TYPE_CHUNK_OF_REQUESTS = "chunk";
    public static final String TYPE_SINGLE_REQUEST = "single";

    private static final String urlPathUpload = "/%s/%s/_bulk";
    private static final String bulkCreateString = String.format("%s%n", "{\"index\": {}}");
    private static final String elasticSearchUploadMsg = "Upload to elasticsearch at %s completed with status : %d";

    // Always server time (GMT)
    private static final ZoneId zoneId = ZoneId.of("GMT");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private Long startTime;
    private Long endTime;
    private String baseUrl;
    private String username;
    private String password;
    private String index;
    private String type;
    private Boolean verbose = false;
    private TestRunProperties runProps;

    /**
     * Constructor.
     *
     * @param baseUrl  domain URL
     * @param index    Elasticsearch index
     * @param type     Elasticsearch type
     * @param username username for basic authentication
     * @param password password for basic authentication
     * @param runProps test execution properties such as service name test class and method etc
     */
    public ElasticSearchResponseTimesPublisher(String baseUrl,
                                               String index,
                                               String type,
                                               String username,
                                               String password,
                                               TestRunProperties runProps) {
        this.password = password;
        this.index = index;
        this.baseUrl = baseUrl;
        this.username = username;
        this.runProps = runProps;
        this.type = type;
    }

    /**
     * Get the LocalDateTime as a String formatted so that Elasticsearch recognizes it as time stamp.
     *
     * @param dateTime   the LocalDateTime object
     * @param zoneOffset the time zone offset to the time used by elasticsearch, e.g. UTC; null if already
     *                   using the right time zone
     * @return LocalDateTime as string
     */
    public static String getDateTimeString(LocalDateTime dateTime, ZoneOffset zoneOffset) {
        if (zoneOffset != null) {
            return ZonedDateTime.of(dateTime,
                    ZoneId.systemDefault()).withZoneSameInstant(zoneOffset).toLocalDateTime().format(dateTimeFormatter);
        } else {
            return dateTime.format(dateTimeFormatter);
        }
    }

    /**
     * Get the date time string in the given time zone for the given epoch time.
     *
     * @param milliseconds epoch time in milliseconds
     * @param zoneId       time zone id
     * @return elastic search formatted time string
     */
    public static String getDateTimeString(long milliseconds, ZoneId zoneId) {
        return Instant.ofEpochMilli(milliseconds).atZone(zoneId).toLocalDateTime().format(dateTimeFormatter);
    }

    /**
     * Push the data to Elasticsearch.
     *
     * @see org.testah.runner.performance.ExecutionStatsPublisher#push(java.util.List)
     */
    @Override
    public void push(List<ResponseDto> responses) throws Exception {
        // reset the start and end time for a chunk of requests so that the elapsed time is computed properly
        startTime = 0L;
        endTime = 0L;

        // Always use server time (GMT)
        final String collectionTime = getDateTimeString(LocalDateTime.now(zoneId), null);
        StringBuilder payloadBuilder = new StringBuilder();
        // cannot do pretty print here, the whole object must be one line
        ObjectMapper mapper = new ObjectMapper();
        responses.stream().forEach(response -> {
            try {
                setStartTime(response.getStart());
                setEndTime(response.getEnd());
                payloadBuilder
                        .append(bulkCreateString)
                        .append(String.format("%s%n", mapper.writeValueAsString(
                                new RequestExecutionDuration(TYPE_SINGLE_REQUEST)
                                        .setCollectionTime(collectionTime)
                                        .setDomain(runProps.getDomain())
                                        .setDuration(response.getDuration())
                                        .setService(runProps.getServiceUnderTest())
                                        .setTestClass(runProps.getTestClass())
                                        .setTestMethod(runProps.getTestMethod())
                                        // Always use server time (GMT)
                                        .setTimestamp(getDateTimeString(response.getStart(), zoneId))
                                        .setStatusCode(response.getStatusCode())
                        )));
            } catch (JsonProcessingException e) {
                TS.log().info(e);
            }
        });

        payloadBuilder
                .append(bulkCreateString)
                .append(String.format("%s%n", mapper.writeValueAsString(
                        new RequestExecutionDuration(TYPE_CHUNK_OF_REQUESTS)
                                .setTimestamp(collectionTime)
                                .setDomain(runProps.getDomain())
                                .setDuration(endTime - startTime)
                                .setService(runProps.getServiceUnderTest())
                                .setTestClass(runProps.getTestClass())
                                .setTestMethod(runProps.getTestMethod()))
                ));

        PostRequestDto postRequestDto = new PostRequestDto(getUploadUrl(), payloadBuilder.toString());
        postRequestDto.setBasicAuthCredentials(username, password).withJson();
        ResponseDto response = TS.http().doRequest(postRequestDto, verbose);
        TS.log().info(String.format(elasticSearchUploadMsg, getUploadUrl(), response.getStatusCode()));
    }

    @Override
    public void cleanup() {
        // no post processing required
    }

    private void setStartTime(final Long startTime) {
        if (0L == this.startTime || this.startTime > startTime) {
            this.startTime = startTime;
        }
    }

    private void setEndTime(final Long endTime) {
        if (0L == this.endTime || this.endTime < endTime) {
            this.endTime = endTime;
        }
    }

    /**
     * Get the upload URL to Elasticsearch.
     *
     * @return the upload url to Elasticsearch
     */
    public String getUploadUrl() {
        return baseUrl + getUrlPathUpload();
    }

    /**
     * Get the upload URL to Elasticsearch.
     *
     * @return the upload url to Elasticsearch
     */
    public String getUrlPathUpload() {
        return String.format(urlPathUpload, index, type);
    }

    /**
     * Get the domain URL.
     *
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set the domain URL.
     *
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Get the verbosity setting. If true each service request and response is logged.
     *
     * @return the verbosity
     */
    public Boolean getVerbose() {
        return verbose;
    }

    /**
     * Set the verbosity. If true each service request and response is logged.
     *
     * @param verbose the verbose to set
     * @return this object
     */
    public ElasticSearchResponseTimesPublisher setVerbose(Boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Get the Elasticsearch index.
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Set the Elasticsearch index.
     *
     * @param index the index to set
     * @return this object
     */
    public ElasticSearchResponseTimesPublisher setIndex(String index) {
        this.index = index;
        return this;
    }
}
