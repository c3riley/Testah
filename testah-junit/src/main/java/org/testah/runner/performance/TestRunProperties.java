package org.testah.runner.performance;

import java.util.Set;

public class TestRunProperties {
    private Integer numberOfChunks = 2500;
    private Long runDuration = 48 * 3600 * 1000L;
    private Long stopTime = null;
    private Integer chunkSize = 10;
    private Integer numberOfAkkaThreads = 3;
    private Long millisBetweenChunks = 3000L;
    private boolean isVerbose = false;
    private String serviceUnderTest;
    private String testClass;
    private String testMethod;


    // for testing purposes only
    private Set<Integer> expectedStatusCodes = null;

    /**
     * Constructor.
     *
     * @param serviceUnderTest    name of tested service
     * @param testClass           test plan name
     * @param testMethod          test method name
     * @param numberOfAkkaThreads number of Akka threads
     * @param chunkSize           size of request chunks
     * @param numberOfChunks      number of chunks
     * @param millisBetweenChunks time to pause between chunks
     */
    public TestRunProperties(
        String serviceUnderTest,
        String testClass,
        String testMethod,
        int numberOfAkkaThreads,
        int chunkSize,
        int numberOfChunks,
        long millisBetweenChunks)
    {
        this.serviceUnderTest = serviceUnderTest;
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        this.chunkSize = chunkSize;
        this.numberOfChunks = numberOfChunks;
        this.millisBetweenChunks = millisBetweenChunks;
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
     * @param numberOfChunks the numberOfChunks to set
     * @return this object
     */
    public TestRunProperties setNumberOfChunks(int numberOfChunks) {
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
     * @param runDuration the runDuration to set
     * @return this object
     */
    public TestRunProperties setRunDuration(long runDuration) {
        this.runDuration = runDuration;
        return this;
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
        this.stopTime = stopTime;
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
     * @param chunkSize the chunkSize to set
     * @return this object
     */
    public TestRunProperties setChunkSize(int chunkSize) {
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
     * @param numberOfAkkaThreads the numberOfAkkaThreads to set
     * @return this object
     */
    public TestRunProperties setNumberOfAkkaThreads(int numberOfAkkaThreads) {
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        return this;
    }

    /**
     * Get the pause time in milliseconds between chunks of requests.
     *
     * @return the millisBetweenChunks
     */
    public long getMillisBetweenChunks() {
        return millisBetweenChunks;
    }

    /**
     * Set the pause time in milliseconds between chunks of requests.
     *
     * @param millisBetweenChunks the millisBetweenChunks to set
     * @return this object
     */
    public TestRunProperties setMillisBetweenChunks(long millisBetweenChunks) {
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
        this.isVerbose = isVerbose;
        return this;
    }

    /**
     * Get the expected status codes for the responses. This is for testing of the long running test functionality only.
     *
     * @return the expectedStatusCodes
     */
    public Set<Integer> getExpectedStatusCodes() {
        return expectedStatusCodes;
    }

    /**
     * Set the expected status codes for the responses. This is for testing of the long running test functionality only.
     *
     * @param expectedStatusCodes the expectedStatusCodes to set
     * @return this object
     */
    public TestRunProperties setExpectedStatusCodes(Set<Integer> expectedStatusCodes) {
        this.expectedStatusCodes = expectedStatusCodes;
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
}
