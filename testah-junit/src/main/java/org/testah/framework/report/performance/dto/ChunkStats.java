package org.testah.framework.report.performance.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.testah.runner.http.load.HttpAkkaStats;
import org.testah.runner.performance.ElasticSearchExecutionStatsPublisher;
import org.testah.runner.performance.TestRunProperties;

public class ChunkStats {

    private String testClass;
    private String testMethod;
    private String serviceName;
    private long elapsedTime;
    private String timeStamp;
    private Set<Integer> statusCodes;
    private StatsDetails overallStats;
    private Map<Integer, StatsDetails> statsByStatusCode;

    /**
     * Constructor for holder of statistical data of the the execution of a chunk of requests.
     *
     * @param runProps properties to execute the long running test
     * @param stats    overall execution data for a chunk of requests
     */
    public ChunkStats(TestRunProperties runProps, HttpAkkaStats stats) {
        setTimeStamp(ElasticSearchExecutionStatsPublisher.getDateTimeString(LocalDateTime.now()));
        setServiceName(runProps.getServiceUnderTest());
        setClassName(runProps.getTestClass());
        setMethodName(runProps.getTestMethod());
        setElapsedTime(stats.getDuration());
        setStatusCodes(stats.getStatsDurationPerStatus().keySet());
        overallStats = new StatsDetails(stats);
        if (stats.getStatsDurationPerStatus().size() > 1) {
            statsByStatusCode = new HashMap<Integer, StatsDetails>();
            for (Entry<Integer, DescriptiveStatistics> entry : stats.getStatsDurationPerStatus().entrySet()) {
                statsByStatusCode.put(entry.getKey(), new StatsDetails(entry.getValue()));
            }
        }
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Collect the statistics per status code.
     *
     * @param elapsedTime time to execute the chunk of requests
     * @return this object
     */
    public ChunkStats setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    /**
     * Get the overall statistics.
     *
     * @return the set of encountered status codes
     */
    public StatsDetails getOverallStats() {
        return overallStats;
    }

    /**
     * Set the overall statistics.
     *
     * @param overallStats overall statistics
     * @return this object
     */
    public ChunkStats setOverallStats(StatsDetails overallStats) {
        this.overallStats = overallStats;
        return this;
    }

    /**
     * Get the statistics per status code.
     *
     * @return the set of encountered status codes
     */
    public Map<Integer, StatsDetails> getStatsByStatusCode() {
        return statsByStatusCode;
    }

    /**
     * Collect the statistics per status code.
     *
     * @param statsByStatusCode map of statistics per status code
     * @return this object
     */
    public ChunkStats setStatsByStatusCode(Map<Integer, StatsDetails> statsByStatusCode) {
        this.statsByStatusCode = statsByStatusCode;
        return this;
    }

    /**
     * Get the status codes.
     *
     * @return the set of encountered status codes
     */
    public Set<Integer> getStatusCodes() {
        return statusCodes;
    }

    /**
     * Record the status codes that were returned.
     *
     * @param statusCodes status codes
     * @return this object
     */
    public ChunkStats setStatusCodes(Set<Integer> statusCodes) {
        this.statusCodes = statusCodes;
        return this;
    }

    /**
     * Get the timestamp the data was sent to Elasticsearch.
     *
     * @return the timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Record the time the data was sent to Elasticsearch.
     *
     * @param timeStamp the timeStamp to set
     * @return this object
     */
    public ChunkStats setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    /**
     * Get the name of the test plan/test class.
     *
     * @return the testName
     */
    public String getTestName() {
        return testClass;
    }

    /**
     * Set the name of the test plan/test class.
     *
     * @param testClass the test plan/test class name to set
     * @return this object
     */
    public ChunkStats setTestName(String testClass) {
        this.testClass = testClass;
        return this;
    }

    /**
     * Get the name of the tested service.
     *
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Set the name of the tested service.
     *
     * @param serviceName the serviceName to set
     * @return this object
     */
    public ChunkStats setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /**
     * Get the name to the test plan/test class.
     *
     * @return the className
     */
    public String getClassName() {
        return testClass;
    }

    /**
     * Set the name to the test plan/test class.
     *
     * @param className the className to set
     * @return this object
     */
    public ChunkStats setClassName(String className) {
        this.testClass = className;
        return this;
    }

    /**
     * Get the name of the test method/test case.
     *
     * @return the methodName
     */
    public String getMethodName() {
        return testMethod;
    }

    /**
     * Set the name of the test method/test case.
     *
     * @param methodName the methodName to set
     * @return this object
     */
    public ChunkStats setMethodName(String methodName) {
        this.testMethod = methodName;
        return this;
    }
}
