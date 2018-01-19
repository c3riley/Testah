package org.testah.framework.report.performance.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.testah.runner.http.load.HttpAkkaStats;

public class ChunkStats {

    private long elapsedTime;
    private Set<Integer> statusCodes;
    private StatsDetails overallStats;
    private Map<Integer, StatsDetails> statsByStatusCode;

    /**
     * Constructor for holder of statistical data of the the execution of a chunk of requests.
     *
     * @param stats    overall execution data for a chunk of requests
     */
    public ChunkStats(HttpAkkaStats stats) {
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
}
