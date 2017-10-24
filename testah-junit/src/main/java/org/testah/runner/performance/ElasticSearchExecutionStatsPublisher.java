package org.testah.runner.performance;

import org.testah.TS;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.framework.report.performance.dto.ChunkStats;

public class ElasticSearchExecutionStatsPublisher implements ExecutionStatsPublisher {

    private String baseUrl;
    private String username;
    private String password;
    private static final String urlPathUpload = "/%s/json";
    private String index = "testah";

    /**
     * Constructor.
     * @param baseUrl  domain URL
     * @param username  username for basic authentication
     * @param password  password for basic authentication
     */
    public ElasticSearchExecutionStatsPublisher(String baseUrl, String username, String password) {
        this.password = password;
        this.baseUrl = baseUrl;
        this.username = username;
    }

    /* (non-Javadoc)
     * @see org.testah.runner.performance.ExecutionStatsPublisher#push(org.testah.framework.report.performance.dto.ChunkStats)
     */
    @Override
    public void push(ChunkStats chunkStats) throws Exception {
        String payload = TS.util().getMap().writeValueAsString(chunkStats);
        PostRequestDto postRequestDto = new PostRequestDto(getUploadUrl(), payload);
        postRequestDto.setBasicAuthCredentials(username, password).withJson();
        TS.log().info("Upload to elasticsearch completed with status : " + TS.http().doRequest(postRequestDto, false).getStatusCode());
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
}