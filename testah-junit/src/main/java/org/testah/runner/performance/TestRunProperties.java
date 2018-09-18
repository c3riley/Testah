package org.testah.runner.performance;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testah.TS;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestRunProperties {
    protected final Integer defaultNumberOfChunks = 2500;
    protected final Long defaultRunDuration = 48 * 3600 * 1000L;
    protected final Integer defaultChunkSize = 10;
    protected final Integer defaultNumberOfAkkaThreads = 3;
    protected final Long defaultMillisBetweenChunks = 3000L;
    private Integer numberOfChunks;
    private Long runDuration = null;
    private Duration runDurationAsDuration = null;
    private Long stopTime = null;
    private LocalDateTime stopDateTime = null;
    private Integer chunkSize;
    private Integer numberOfAkkaThreads;
    private Long millisBetweenChunks;
    private boolean isVerbose = false;
    private String serviceUnderTest;
    private String domain;
    private String testClass;
    private String testMethod;

    protected TestRunProperties(String serviceUnderTest, String testClass, String testMethod) {
        this.serviceUnderTest = serviceUnderTest;
        this.testClass = testClass;
        this.testMethod = testMethod;
    }

    /**
     * Constructor.
     *
     * @param serviceUnderTest    name of tested service
     * @param testClass           test plan name
     * @param testMethod          test method name
     * @param numberOfAkkaThreads number of Akka threads
     * @param millisBetweenChunks time to pause between chunks
     */
    public TestRunProperties(
            String serviceUnderTest,
            String testClass,
            String testMethod,
            int numberOfAkkaThreads,
            long millisBetweenChunks) {
        this.serviceUnderTest = serviceUnderTest;
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        this.millisBetweenChunks = millisBetweenChunks;
        this.runDuration = this.defaultRunDuration;
    }

    /**
     * Get the number of chunks of requests to be generated.
     *
     * @return the numberOfChunks
     */
    public int getNumberOfChunks() {
        return numberOfChunks;
    }

    /**
     * Set the number of chunks of requests to be generated.
     *
     * @param numberOfChunks the number of chunks as String
     * @return this object
     */
    public TestRunProperties setNumberOfChunks(String numberOfChunks) {
        return setNumberOfChunks(Integer.parseInt(numberOfChunks));
    }

    /**
     * Set the number of chunks of requests to be generated.
     *
     * @param numberOfChunks the number of chunks to set
     * @return this object
     */
    public TestRunProperties setNumberOfChunks(int numberOfChunks) {
        TS.log().info("Setting numberOfChunks to " + numberOfChunks);
        this.numberOfChunks = numberOfChunks;
        return this;
    }

    /**
     * Get the duration in milliseconds for the test to run.
     *
     * @return the runDuration
     */
    public long getRunDuration() {
        return runDuration;
    }

    /**
     * Set the duration in milliseconds for the test to run.
     *
     * @param runDuration the run duration as a String
     * @return this object
     */
    public TestRunProperties setRunDuration(String runDuration) {
        return setRunDuration(Long.parseLong(runDuration));
    }

    /**
     * Set the duration in milliseconds for the test to run.
     *
     * @param runDuration the run duration to set
     * @return this object
     */
    public TestRunProperties setRunDuration(long runDuration) {
        TS.log().info("Setting runDuration to " + runDuration);
        this.runDuration = runDuration;
        return this;
    }

    /**
     * Get the number of requests in one chunk of requests.
     *
     * @return the chunkSize
     */
    public int getChunkSize() {
        return chunkSize;
    }

    /**
     * Set the number of requests in one chunk of requests.
     *
     * @param chunkSize the chunk size as a String
     * @return this object
     */
    public TestRunProperties setChunkSize(String chunkSize) {
        return setChunkSize(Integer.parseInt(chunkSize));
    }

    /**
     * Set the number of requests in one chunk of requests.
     *
     * @param chunkSize the chunkSize to set
     * @return this object
     */
    public TestRunProperties setChunkSize(int chunkSize) {
        TS.log().info("Setting chunkSize to " + chunkSize);
        this.chunkSize = chunkSize;
        return this;
    }

    /**
     * Get the number of Akka threads.
     *
     * @return the numberOfAkkaThreads
     */
    public int getNumberOfAkkaThreads() {
        return numberOfAkkaThreads;
    }

    /**
     * Set the number of Akka threads.
     *
     * @param numberOfAkkaThreads the number of Akka threads as a String
     * @return this object
     */
    public TestRunProperties setNumberOfAkkaThreads(String numberOfAkkaThreads) {
        return setNumberOfAkkaThreads(Integer.parseInt(numberOfAkkaThreads));
    }

    /**
     * Set the number of Akka threads.
     *
     * @param numberOfAkkaThreads the number of Akka threads to set
     * @return this object
     */
    public TestRunProperties setNumberOfAkkaThreads(int numberOfAkkaThreads) {
        TS.log().info("Setting numberOfAkkaThreads to " + numberOfAkkaThreads);
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        return this;
    }

    /**
     * Get the pause time in milliseconds between chunks of requests.
     *
     * @return the milliseconds between chunks
     */
    public long getMillisBetweenChunks() {
        return millisBetweenChunks;
    }

    /**
     * Set the pause time in milliseconds between chunks of requests.
     *
     * @param millisBetweenChunks the milliseconds between chunks as a String
     * @return this object
     */
    public TestRunProperties setMillisBetweenChunks(String millisBetweenChunks) {
        return setMillisBetweenChunks(Long.parseLong(millisBetweenChunks));
    }

    /**
     * Set the pause time in milliseconds between chunks of requests.
     *
     * @param millisBetweenChunks the milliseconds between chunks to set
     * @return this object
     */
    public TestRunProperties setMillisBetweenChunks(long millisBetweenChunks) {
        TS.log().info("Setting millisBetweenChunks to " + millisBetweenChunks);
        this.millisBetweenChunks = millisBetweenChunks;
        return this;
    }

    /**
     * Get the verbosity setting for making HTTP requests.
     *
     * @return the isVerbose
     */
    public boolean isVerbose() {
        return isVerbose;
    }

    /**
     * Set the verbosity setting for making HTTP requests.
     *
     * @param isVerbose the isVerbose to set
     * @return this object
     */
    public TestRunProperties setVerbose(boolean isVerbose) {
        TS.log().info("Setting isVerbose to " + isVerbose);
        this.isVerbose = isVerbose;
        return this;
    }

    /**
     * Get the name of the tested service.
     *
     * @return the serviceUnderTest
     */
    public String getServiceUnderTest() {
        return serviceUnderTest;
    }

    /**
     * Get the name of the executing test plan/test class.
     *
     * @return the testClass
     */
    public String getTestClass() {
        return testClass;
    }

    /**
     * Get the executing test method.
     *
     * @return the testMethod
     */
    public String getTestMethod() {
        return testMethod;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * Set the domain for the service under test in the test run properties.
     *
     * @param domain service domain
     * @return this object
     */
    public TestRunProperties setDomain(String domain) {
        TS.log().info("Setting domain to " + domain);
        this.domain = domain;
        return this;
    }

    public String getRuntime() {
        return runDurationAsDuration.toString();
    }

    @Override
    public String toString() {
        runDurationAsDuration = Duration.ofMillis(runDuration);
        getStopDateTime();
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Get the time as LocalDateTime until the test will run.
     *
     * @return the stopDateTime
     */
    public LocalDateTime getStopDateTime() {
        if (stopDateTime == null) {
            stopDateTime = Instant.ofEpochMilli(getStopTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return stopDateTime;
    }

    /**
     * Get the time in milliseconds until the test will run.
     *
     * @return the stopTime
     */
    public long getStopTime() {
        if (stopTime == null) {
            stopTime = System.currentTimeMillis() + runDuration;
        }
        return stopTime;
    }

    /**
     * Set the time in milliseconds until the test will run.
     *
     * @param stopTime the stopTime to set
     * @return this object
     */
    public TestRunProperties setStopTime(long stopTime) {
        TS.log().info("Setting stopTime to " + stopTime);
        this.stopTime = stopTime;
        return this;
    }
}

