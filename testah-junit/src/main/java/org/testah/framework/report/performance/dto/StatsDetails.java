package org.testah.framework.report.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.testah.runner.http.load.HttpAkkaStats;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder( {"count", "mean", "pct90", "std", "elapsed", "short", "long"})
public class StatsDetails {

    private static final double percentile90 = 90.0;

    @JsonProperty("count")
    private Long count;
    @JsonProperty("mean")
    private Long mean;
    @JsonProperty("pct90")
    private Long pct90;
    @JsonProperty("std")
    private Long std;
    @JsonProperty("min")
    private Long min;
    @JsonProperty("max")
    private Long max;

    public StatsDetails() {
        // default constructor
    }

    /**
     * Constructor for holder of basic overall statistical execution of a chunk of requests.
     * @param stats overall execution data for a chunk of requests
     */
    public StatsDetails(HttpAkkaStats stats) {
        this.setMean(stats.getAvgDuration()).setCount((long) stats.getTotalResponses())
            .setMax(stats.getLongestDuration()).setMin(stats.getShortestDuration())
            .setPct90((long) stats.getStatsDuration().getPercentile(percentile90))
            .setStd((long) stats.getStatsDuration().getStandardDeviation());
    }

    /**
     * Constructor for holder of basic overall statistical execution of a chunk of requests.
     * @param stats execution data for a chunk of requests related to a status code
     */
    public StatsDetails(DescriptiveStatistics stats) {
        this.setMean((long) stats.getMean()).setCount(stats.getN())
            .setMax((long) stats.getMax()).setMin((long) stats.getMin())
            .setPct90((long) stats.getPercentile(percentile90))
            .setStd((long) stats.getStandardDeviation());
    }

    @JsonProperty("count")
    public Long getCount() {
        return count;
    }

    @JsonProperty("count")
    public StatsDetails setCount(Long count) {
        this.count = count;
        return this;
    }

    @JsonProperty("mean")
    public Long getMean() {
        return mean;
    }

    @JsonProperty("mean")
    public StatsDetails setMean(Long mean) {
        this.mean = mean;
        return this;
    }

    @JsonProperty("pct90")
    public Long getPct90() {
        return pct90;
    }

    @JsonProperty("pct90")
    public StatsDetails setPct90(Long pct90) {
        this.pct90 = pct90;
        return this;
    }

    @JsonProperty("std")
    public Long getStd() {
        return std;
    }

    @JsonProperty("std")
    public StatsDetails setStd(Long std) {
        this.std = std;
        return this;
    }

    @JsonProperty("min")
    public Long getMin() {
        return min;
    }

    @JsonProperty("min")
    public StatsDetails setMin(Long min) {
        this.min = min;
        return this;
    }

    @JsonProperty("max")
    public Long getMax() {
        return max;
    }

    @JsonProperty("max")
    public StatsDetails setMax(Long max) {
        this.max = max;
        return this;
    }
}