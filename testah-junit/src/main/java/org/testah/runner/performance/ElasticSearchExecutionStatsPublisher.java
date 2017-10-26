package org.testah.runner.performance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testah.TS;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.framework.report.performance.dto.ChunkStats;

public class ElasticSearchExecutionStatsPublisher implements ExecutionStatsPublisher {

    private static final String urlPathUpload = "/%s/json";
    private String baseUrl;
    private String username;
    private String password;
    private String index;
    private Boolean verbose = false;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Constructor.
     * @param baseUrl   domain URL
     * @param index     Elasticsearch index
     * @param username  username for basic authentication
     * @param password  password for basic authentication
     */
    public ElasticSearchExecutionStatsPublisher(String baseUrl, String index, String username, String password) {
        this.password = password;
        this.index = index;
        this.baseUrl = baseUrl;
        this.username = username;
    }

    /**
     * Push the data to Elasticsearch.
     * @see org.testah.runner.performance.ExecutionStatsPublisher#push(org.testah.framework.report.performance.dto.ChunkStats)
     */
    @Override
    public void push(ChunkStats chunkStats) throws Exception {
        String payload = TS.util().getMap().writeValueAsString(chunkStats);
        PostRequestDto postRequestDto = new PostRequestDto(getUploadUrl(), payload);
        postRequestDto.setBasicAuthCredentials(username, password).withJson();
        TS.log().info("Upload to elasticsearch completed with status : " + TS.http().doRequest(postRequestDto, verbose).getStatusCode());
    }

    /**
     * Get the domain URL.
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set the domain URL.
     * @param baseUrl
     *            the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Get the upload URL to Elasticsearch.
     * @return the upload url to Elasticsearch
     */
    public String getUploadUrl() {
        return baseUrl + String.format(urlPathUpload, index);
    }

    /**
     * Get the verbosity setting. If true each service request and response is logged.
     * @return the verbosity
     */
    public Boolean getVerbose() {
        return verbose;
    }

    /**
     * Set the verbosity. If true each service request and response is logged.
     * @param verbose the verbose to set
     * @return this object
     */
    public ElasticSearchExecutionStatsPublisher setVerbose(Boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Get the Elasticsearch index.
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * Set the Elasticsearch index.
     * @param index the index to set
     * @return this object
     */
    public ElasticSearchExecutionStatsPublisher setIndex(String index) {
        this.index = index;
        return this;
    }

    /**
     * Get the LocalDateTime as a String formatted so that Elasticsearch recognizes it as time stamp.
     * @param dateTime the LocalDateTime object
     * @return LocalDateTime as string
     */
    public static String getDateTimeString(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }
}