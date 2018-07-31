package org.testah.runner.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        LoadTestSequenceDto.PARAM_STEP,
        LoadTestSequenceDto.PARAM_THREADS,
        LoadTestSequenceDto.PARAM_CHUNK_SIZE,
        LoadTestSequenceDto.PARAM_DURATION_MINUTES
})
public class LoadTestSequenceDto {

    static final String PARAM_DURATION_MINUTES = "durationMinutes";
    static final String PARAM_CHUNK_SIZE = "chunkSize";
    static final String PARAM_THREADS = "threads";
    static final String PARAM_STEP = "step";
    @JsonProperty(PARAM_STEP)
    private Integer step;
    @JsonProperty(PARAM_THREADS)
    private Integer threads;
    @JsonProperty(PARAM_CHUNK_SIZE)
    private Integer chunkSize;
    @JsonProperty(PARAM_DURATION_MINUTES)
    private Integer durationMinutes;

    @JsonProperty(PARAM_STEP)
    public Integer getStep() {
        return step;
    }

    @JsonProperty(PARAM_STEP)
    public void setStep(Integer step) {
        this.step = step;
    }

    public LoadTestSequenceDto withStep(Integer step) {
        this.step = step;
        return this;
    }

    @JsonProperty(PARAM_THREADS)
    public Integer getThreads() {
        return threads;
    }

    @JsonProperty(PARAM_THREADS)
    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public LoadTestSequenceDto withThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public Integer getChunkSize() {
        return chunkSize;
    }

    @JsonProperty(PARAM_CHUNK_SIZE)
    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public LoadTestSequenceDto withChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    @JsonProperty(PARAM_DURATION_MINUTES)
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LoadTestSequenceDto withDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }
}
