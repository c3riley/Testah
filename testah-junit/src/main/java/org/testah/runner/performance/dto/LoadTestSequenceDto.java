package org.testah.runner.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.testah.runner.performance.TestRunProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadTestSequenceDto {

    static final String PARAM_DURATION_MINUTES = "durationMinutes";
    static final String PARAM_CHUNK_SIZE = "chunkSize";
    static final String PARAM_THREADS = "threads";
    static final String IS_PUBLISH = "isPublish";
    static final String PARAM_STEP = "step";
    static final String IS_VERBOSE = "isVerbose";
    static final String MILLIS_BETWEEN_CHUNKS = "millisBetweenChunks";
    static final String NUMBER_OF_CHUNKS = "numberOfChunks";

    @JsonProperty(PARAM_STEP)
    private Integer step;
    @JsonProperty(PARAM_THREADS)
    private Integer threads;
    @JsonProperty(PARAM_CHUNK_SIZE)
    private Integer chunkSize;
    @JsonProperty(PARAM_DURATION_MINUTES)
    private Integer durationMinutes;
    @JsonProperty(IS_PUBLISH)
    private boolean isPublish = true;
    @JsonProperty(IS_VERBOSE)
    private Boolean isVerbose;
    @JsonProperty(MILLIS_BETWEEN_CHUNKS)
    private Long millisBetweenChunks;
    @JsonProperty(NUMBER_OF_CHUNKS)
    private Integer numberOfChunks;

    @JsonProperty(PARAM_STEP)
    public Integer getStep() {
        return step;
    }

    @JsonProperty(PARAM_STEP)
    public LoadTestSequenceDto setStep(Integer step) {
        this.step = step;
        return this;
    }

    @JsonProperty(PARAM_THREADS)
    public Integer getThreads() {
        return threads;
    }

    @JsonProperty(PARAM_THREADS)
    public LoadTestSequenceDto setThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public Integer getChunkSize() {
        return chunkSize;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public LoadTestSequenceDto setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    @JsonProperty(NUMBER_OF_CHUNKS)
    public Integer getNumberOfChunks() {
        return numberOfChunks;
    }

    @JsonProperty(NUMBER_OF_CHUNKS)
    public LoadTestSequenceDto setNumberOfChunks(Integer numberOfChunks) {
        this.numberOfChunks = numberOfChunks;
        return this;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public LoadTestSequenceDto setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
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
     * Fill missing properties from TestRunProperties.
     * @param runProperties TestRunProperties containing default values
     * @return this object instance
     */
    public LoadTestSequenceDto fillUndefined(TestRunProperties runProperties)
    {
        threads = threads == null ? runProperties.getNumberOfAkkaThreads() : threads;
        chunkSize = chunkSize == null ? runProperties.getChunkSize() : chunkSize;
        durationMinutes = durationMinutes == null ? runProperties.getRunDurationMinutes() : durationMinutes;
        millisBetweenChunks = millisBetweenChunks == null ? runProperties.getMillisBetweenChunks() : millisBetweenChunks;
        isVerbose = isVerbose == null ? runProperties.isVerbose() : isVerbose;
        numberOfChunks = numberOfChunks == null ? runProperties.getNumberOfChunks() : numberOfChunks;
        return this;
    }
}
