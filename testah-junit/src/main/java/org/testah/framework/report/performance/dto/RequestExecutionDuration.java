package org.testah.framework.report.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;;

@JsonInclude(Include.NON_NULL)
public class RequestExecutionDuration {

    private Integer statusCode;
    private Long duration;
    private String collectionTime;
    private String domain;
    private String service;
    private String testClass;
    private String testMethod;
    private String timestamp;
    private String aggregation;

    /**
     * Constructor.
     * @param aggregation either 'chunk' or 'single'
     */
    public RequestExecutionDuration(String aggregation) {
        this.aggregation = aggregation;
    }

    /**
     * Get the time stamp for the request.
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the time stamp for the request.
     * @param timestamp the timestamp to set
     * @return this object
     */
    public RequestExecutionDuration setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Get the collection time stamp. This can be used to group the response times for a chunk of requests.
     * @return the collection time
     */
    public String getCollectionTime() {
        return collectionTime;
    }

    /**
     * Set the collection time stamp. This is the time when the data for one chunk of requests was collected.
     * @param timestamp the collection time to set
     * @return this object
     */
    public RequestExecutionDuration setCollectionTime(String timestamp) {
        this.collectionTime = timestamp;
        return this;
    }

    /**
     * Get the name of the service to which the requests are sent.
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * Set the name of the service to which the requests are sent.
     * @param service the service to set
     * @return this object
     */
    public RequestExecutionDuration setService(String service) {
        this.service = service;
        return this;
    }

    /**
     * Get the domain name of the service url to which the requests are sent.
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Set the domain name of the service url to which the requests are sent.
     * @param domain the domain to set
     * @return this object
     */
    public RequestExecutionDuration setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * Get the name of the test class/plan.
     * @return the testClass
     */
    public String getTestClass() {
        return testClass;
    }

    /**
     * Set the name of the test class/plan.
     * @param testClass the testClass to set
     * @return this object
     */
    public RequestExecutionDuration setTestClass(String testClass) {
        this.testClass = testClass;
        return this;
    }

    /**
     * Get the name of the test method/case.
     * @return the testMethod
     */
    public String getTestMethod() {
        return testMethod;
    }

    /**
     * Set the name of the test method/case.
     * @param testMethod the testMethod to set
     * @return this object
     */
    public RequestExecutionDuration setTestMethod(String testMethod) {
        this.testMethod = testMethod;
        return this;
    }

    /**
     * Get the request status code.
     * @return the statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Set the request status code.
     * @param statusCode the statusCode to set
     * @return this object
     */
    public RequestExecutionDuration setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Get the duration of the request.
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Set the duration of the request.
     * @param duration the duration to set
     * @return this object
     */
    public RequestExecutionDuration setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @return the type
     */
    public String getAggregation() {
        return aggregation;
    }
}
