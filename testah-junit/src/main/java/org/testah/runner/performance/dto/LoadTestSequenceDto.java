package org.testah.runner.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.testah.runner.performance.TestRunProperties;

import java.time.Duration;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadTestSequenceDto
{

    static final String PARAM_DURATION_MINUTES = "durationMinutes";
    static final String PARAM_DURATION_SECONDS = "durationSeconds";
    static final String PARAM_CHUNK_SIZE = "chunkSize";
    static final String PARAM_THREADS = "threads";
    static final String PARAM_SENDERS = "senders";
    static final String IS_PUBLISH = "isPublish";
    static final String PARAM_STEP = "step";
    static final String IS_VERBOSE = "isVerbose";
    static final String MILLIS_BETWEEN_CHUNKS = "millisBetweenChunks";
    static final String NUMBER_OF_CHUNKS = "numberOfChunks";

    @JsonProperty(PARAM_STEP)
    private Integer step;
    @JsonProperty(PARAM_THREADS)
    private Integer threads;
    @JsonProperty(PARAM_SENDERS)
    private Integer senders;
    @JsonProperty(PARAM_CHUNK_SIZE)
    private Integer chunkSize;
    @JsonProperty(PARAM_DURATION_MINUTES)
    private Integer durationMinutes;
    @JsonProperty(PARAM_DURATION_SECONDS)
    private Integer durationSeconds;
    @JsonProperty(IS_PUBLISH)
    private boolean isPublish = true;
    @JsonProperty(IS_VERBOSE)
    private Boolean isVerbose;
    @JsonProperty(MILLIS_BETWEEN_CHUNKS)
    private Long millisBetweenChunks;
    @JsonProperty(NUMBER_OF_CHUNKS)
    private Integer numberOfChunks;

    @JsonProperty(PARAM_STEP)
    public Integer getStep()
    {
        return step;
    }

    @JsonProperty(PARAM_STEP)
    public LoadTestSequenceDto setStep(Integer step)
    {
        this.step = step;
        return this;
    }

    @JsonProperty(PARAM_THREADS)
    public Integer getThreads()
    {
        return threads;
    }

    @JsonProperty(PARAM_THREADS)
    public LoadTestSequenceDto setThreads(Integer threads)
    {
        this.threads = threads;
        return this;
    }

    @JsonProperty(PARAM_SENDERS)
    public Integer getSenders()
    {
        return senders;
    }

    @JsonProperty(PARAM_SENDERS)
    public LoadTestSequenceDto setSenders(Integer senders)
    {
        this.senders = senders;
        return this;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public Integer getChunkSize()
    {
        return chunkSize;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public LoadTestSequenceDto setChunkSize(Integer chunkSize)
    {
        this.chunkSize = chunkSize;
        return this;
    }

    @JsonProperty(NUMBER_OF_CHUNKS)
    public Integer getNumberOfChunks()
    {
        return numberOfChunks;
    }

    @JsonProperty(NUMBER_OF_CHUNKS)
    public LoadTestSequenceDto setNumberOfChunks(Integer numberOfChunks)
    {
        this.numberOfChunks = numberOfChunks;
        return this;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public Integer getDurationMinutes()
    {
        return durationMinutes;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public LoadTestSequenceDto setDurationMinutes(Integer durationMinutes)
    {
        this.durationMinutes = durationMinutes;
        return this;
    }

    @JsonProperty(PARAM_DURATION_SECONDS)
    public Integer getDurationSeconds()
    {
        return durationSeconds;
    }

    @JsonProperty(PARAM_DURATION_SECONDS)
    public LoadTestSequenceDto setDurationSeconds(Integer durationSeconds)
    {
        this.durationSeconds = durationSeconds;
        return this;
    }

    @JsonProperty(IS_PUBLISH)
    public boolean getIsPublish()
    {
        return isPublish;
    }

    @JsonProperty(IS_PUBLISH)
    public LoadTestSequenceDto setIsPublish(boolean isPublish)
    {
        this.isPublish = isPublish;
        return this;
    }

    public Boolean getIsVerbose()
    {
        return isVerbose;
    }

    public LoadTestSequenceDto setIsVerbose(boolean isVerbose)
    {
        this.isVerbose = isVerbose;
        return this;
    }

    public Long getMillisBetweenChunks()
    {
        return millisBetweenChunks;
    }

    public LoadTestSequenceDto setMillisBetweenChunks(Long millisBetweenChunks)
    {
        this.millisBetweenChunks = millisBetweenChunks;
        return this;
    }

    /**
     * Get the test execution time as a Duration.
     * @return test execution time as a Duration
     */
    public Duration getStepRunDuration()
    {
        int minutes = durationMinutes == null ? 0 : durationMinutes;
        int seconds = durationSeconds == null ? 0 : durationSeconds;
        return Duration.ofMinutes(minutes).plusSeconds(seconds);
    }

    /**
     * Get the test duration as string for use in reports.
     * @return report formatted duration string
     */
    public String getStepRunDurationString()
    {
        Duration duration = getStepRunDuration();
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return String.format("%d minutes %d seconds", minutes, seconds);
    }

    public long getStopTimeMillis(DateTime dateTime)
    {
        return dateTime.plusMillis(Math.toIntExact(getStepRunDuration().toMillis())).getMillis();
    }

    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Fill missing properties from TestRunProperties.
     *
     * @param runProperties TestRunProperties containing default values
     * @return this object instance
     */
    public LoadTestSequenceDto fillUndefined(TestRunProperties runProperties)
    {
        threads = threads == null ? runProperties.getNumberOfAkkaThreads() : threads;
        senders = senders == null ? runProperties.getNumberOfSenderThreads() : senders;
        chunkSize = chunkSize == null ? runProperties.getChunkSize() : chunkSize;
        durationSeconds = (durationMinutes == null && durationSeconds == null) ? runProperties.getRunDurationSeconds() : durationSeconds;
        millisBetweenChunks = millisBetweenChunks == null ? runProperties.getMillisBetweenChunks() : millisBetweenChunks;
        isVerbose = isVerbose == null ? runProperties.isVerbose() : isVerbose;
        numberOfChunks = numberOfChunks == null ? runProperties.getNumberOfChunks() : numberOfChunks;
        return this;
    }
}
