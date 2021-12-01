package org.testah.runner.performance;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testah.TS;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestRunProperties
{
    public static final Integer DEFAULT_NUMBER_OF_CHUNKS = 2500;
    public static final Long DEFAULT_RUN_DURATION = 48 * 3600 * 1000L;
    public static final Integer DEFAULT_CHUNK_SIZE = 10;
    public static final Integer DEFAULT_NUMBER_OF_AKKA_THREADS = 3;
    public static final Integer DEFAULT_NUMBER_OF_SENDER_THREADS = 50;
    public static final Long DEFAULT_MILLIS_BETWEEN_CHUNKS = 3000L;
    private final String serviceUnderTest;
    private final String testClass;
    private final String testMethod;
    private Integer numberOfChunks;
    private Long runDuration;
    private Long stopTime = null;
    private LocalDateTime stopDateTime = null;
    private Integer chunkSize;
    private Integer numberOfAkkaThreads;
    private Integer numberOfSenderThreads;
    private Long millisBetweenChunks;
    private boolean isVerbose = false;
    private String domain;
    private String id;
    private Long millisPauseExecution;

    protected TestRunProperties(String serviceUnderTest, String testClass, String testMethod)
    {
        this.serviceUnderTest = serviceUnderTest;
        this.testClass = testClass;
        this.testMethod = testMethod;
        this.runDuration = DEFAULT_RUN_DURATION;
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
        long millisBetweenChunks)
    {
        this(serviceUnderTest, testClass, testMethod);
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        this.millisBetweenChunks = millisBetweenChunks;
    }

    /**
     * Get the number of chunks of requests to be generated.
     *
     * @return the numberOfChunks
     */
    public Integer getNumberOfChunks() {
        if (numberOfChunks == null || numberOfChunks < 1) {
            return DEFAULT_NUMBER_OF_CHUNKS;
        }
        return numberOfChunks;
    }

    /**
     * Set the number of chunks of requests to be generated.
     *
     * @param numberOfChunks the number of chunks as String
     * @return this object
     */
    public TestRunProperties setNumberOfChunks(String numberOfChunks)
    {
        return setNumberOfChunks(Integer.parseInt(numberOfChunks));
    }

    /**
     * Set the number of chunks of requests to be generated.
     *
     * @param numberOfChunks the number of chunks to set
     * @return this object
     */
    public TestRunProperties setNumberOfChunks(int numberOfChunks)
    {
        TS.log().info("Setting numberOfChunks to " + numberOfChunks);
        this.numberOfChunks = numberOfChunks;
        return this;
    }

    /**
     * Get the duration in milliseconds for the test to run.
     *
     * @return the runDuration
     */
    public long getRunDuration()
    {
        return runDuration;
    }

    /**
     * Set the duration in milliseconds for the test to run.
     *
     * @param runDuration the run duration as a String
     * @return this object
     */
    public TestRunProperties setRunDuration(String runDuration)
    {
        return setRunDuration(Long.parseLong(runDuration));
    }

    /**
     * Set the duration in milliseconds for the test to run.
     *
     * @param runDuration the run duration to set
     * @return this object
     */
    public TestRunProperties setRunDuration(long runDuration)
    {
        TS.log().info("Setting runDuration to " + runDuration);
        this.runDuration = runDuration;
        return this;
    }

    public Integer getRunDurationMinutes()
    {
        return Math.toIntExact(Duration.ofMillis(runDuration).toMinutes());
    }

    public Integer getRunDurationSeconds()
    {
        return Math.toIntExact(Duration.ofMillis(runDuration).getSeconds());
    }

    /**
     * Get the number of requests in one chunk of requests.
     *
     * @return the chunkSize
     */
    public Integer getChunkSize() {
        if (chunkSize == null || chunkSize < 1) {
            return DEFAULT_CHUNK_SIZE;
        }
        return chunkSize;
    }

    /**
     * Set the number of requests in one chunk of requests.
     *
     * @param chunkSize the chunk size as a String
     * @return this object
     */
    public TestRunProperties setChunkSize(String chunkSize)
    {
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
    public Integer getNumberOfAkkaThreads() {
        if (numberOfAkkaThreads == null || numberOfAkkaThreads < 1) {
            return DEFAULT_NUMBER_OF_AKKA_THREADS;
        }
        return numberOfAkkaThreads;
    }

    /**
     * Set the number of Akka threads.
     *
     * @param numberOfAkkaThreads the number of Akka threads as a String
     * @return this object
     */
    public TestRunProperties setNumberOfAkkaThreads(String numberOfAkkaThreads)
    {
        return setNumberOfAkkaThreads(Integer.parseInt(numberOfAkkaThreads));
    }

    /**
     * Set the number of Akka threads.
     *
     * @param numberOfAkkaThreads the number of Akka threads to set
     * @return this object
     */
    public TestRunProperties setNumberOfAkkaThreads(int numberOfAkkaThreads)
    {
        TS.log().info("Setting numberOfAkkaThreads to " + numberOfAkkaThreads);
        this.numberOfAkkaThreads = numberOfAkkaThreads;
        return this;
    }

    /**
     * Get the number of threads.
     * @return the number of threads
     */
    public Integer getNumberOfSenderThreads()
    {
        if (numberOfSenderThreads == null || numberOfSenderThreads < 1) {
            return DEFAULT_NUMBER_OF_SENDER_THREADS;
        }
        return numberOfSenderThreads;
    }

    public TestRunProperties setNumberOfSenderThreads(Integer numberOfSenderThreads)
    {
        this.numberOfSenderThreads = numberOfSenderThreads;
        return this;
    }

    /**
     * Get the pause time in milliseconds between chunks of requests.
     *
     * @return the milliseconds between chunks
     */
    public Long getMillisBetweenChunks() {
        if (millisBetweenChunks == null || millisBetweenChunks < 1) {
            return DEFAULT_MILLIS_BETWEEN_CHUNKS;
        }
        return millisBetweenChunks;
    }

    /**
     * Set the pause time in milliseconds between chunks of requests.
     *
     * @param millisBetweenChunks the milliseconds between chunks as a String
     * @return this object
     */
    public TestRunProperties setMillisBetweenChunks(String millisBetweenChunks)
    {
        return setMillisBetweenChunks(Long.parseLong(millisBetweenChunks));
    }

    /**
     * Set the pause time in milliseconds between chunks of requests.
     *
     * @param millisBetweenChunks the milliseconds between chunks to set
     * @return this object
     */
    public TestRunProperties setMillisBetweenChunks(long millisBetweenChunks)
    {
        TS.log().info("Setting millisBetweenChunks to " + millisBetweenChunks);
        this.millisBetweenChunks = millisBetweenChunks;
        return this;
    }

    /**
     * Get the verbosity setting for making HTTP requests.
     *
     * @return the isVerbose
     */
    public Boolean isVerbose()
    {
        return isVerbose;
    }

    /**
     * Set the verbosity setting for making HTTP requests.
     *
     * @param isVerbose the isVerbose to set
     * @return this object
     */
    public TestRunProperties setVerbose(boolean isVerbose)
    {
        TS.log().info("Setting isVerbose to " + isVerbose);
        this.isVerbose = isVerbose;
        return this;
    }

    /**
     * Get the identifier previously set for the test run.
     * The framework itself does not use it.
     *
     * @return the test run id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set an id for a test run. The framework does not use it.
     * The framework itself does not use it.
     *
     * @param id the test run id
     * @return this object
     */
    public TestRunProperties setId(String id)
    {
        this.id = id;
        return this;
    }

    /**
     * Get the name of the tested service.
     *
     * @return the serviceUnderTest
     */
    public String getServiceUnderTest()
    {
        return serviceUnderTest;
    }

    /**
     * Get the name of the executing test plan/test class.
     *
     * @return the testClass
     */
    public String getTestClass()
    {
        return testClass;
    }

    /**
     * Get the executing test method.
     *
     * @return the testMethod
     */
    public String getTestMethod()
    {
        return testMethod;
    }

    public String getDomain()
    {
        return domain;
    }

    /**
     * Set the domain for the service under test in the test run properties.
     *
     * @param domain service domain
     * @return this object
     */
    public TestRunProperties setDomain(String domain)
    {
        TS.log().info("Setting domain to " + domain);
        this.domain = domain;
        return this;
    }

    /**
     * Gets runtime.
     *
     * @return the runtime
     */
    public String getRuntime()
    {
        if (runDuration != null)
        {
            return Duration.ofMillis(runDuration).toString();
        }
        return null;
    }

    @Override
    public String toString()
    {
        getStopDateTime();
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Get the time as LocalDateTime until the test will run.
     *
     * @return the stopDateTime
     */
    public LocalDateTime getStopDateTime()
    {
        if (stopDateTime == null)
        {
            stopDateTime = Instant.ofEpochMilli(getStopTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return stopDateTime;
    }

    /**
     * Get the time in milliseconds until the test will run.
     *
     * @return the stopTime
     */
    public long getStopTime()
    {
        if (stopTime == null)
        {
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
    public TestRunProperties setStopTime(long stopTime)
    {
        TS.log().info("Setting stopTime to " + stopTime);
        this.stopTime = stopTime;
        return this;
    }

    /**
     * Get the time between executing two chunks in milliseconds, with a fallback number
     * if the value is not set.
     * @param defaultMillisPauseExecution milliseconds to pause while waiting for the step to finish
     * @return the time between execution two chunks in milliseconds
     */
    public long getMillisPauseExecution(long defaultMillisPauseExecution)
    {
        if (millisPauseExecution == null || millisPauseExecution <= 0L) {
            return defaultMillisPauseExecution;
        }
        return millisPauseExecution;
    }

    public TestRunProperties setMillisPauseExecution(Long millisPauseExecution)
    {
        this.millisPauseExecution = millisPauseExecution;
        return this;
    }
}

