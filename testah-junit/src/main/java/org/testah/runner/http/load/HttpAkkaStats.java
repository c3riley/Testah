package org.testah.runner.http.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.testah.driver.http.response.ResponseDto;

import com.google.common.primitives.Doubles;

public class HttpAkkaStats {

    private final int totalResponses;
    private Long startTime = 0L;
    private Long endTime = 0L;
    private final Long duration;

    private DescriptiveStatistics statsDuration = new DescriptiveStatistics();
    private Map<Integer, DescriptiveStatistics> statsDurationPerStatus = new HashMap<>();

    public HttpAkkaStats(final List<ResponseDto> responses) {
        this.totalResponses = responses.size();
        statsDuration = new DescriptiveStatistics();
        statsDurationPerStatus = new HashMap<>();

        for (final ResponseDto r : responses) {
            statsDuration.addValue(r.getDuration());
            if (!statsDurationPerStatus.keySet().contains(r.getStatusCode())) {
                statsDurationPerStatus.put(r.getStatusCode(), new DescriptiveStatistics());
            }
            statsDurationPerStatus.get(r.getStatusCode()).addValue(r.getDuration());
            setStartTime(r.getStart());
            setEndTime(r.getEnd());
        }
        duration = (endTime - startTime);
    }

    private void setStartTime(final Long startTime) {
        if (0L == this.startTime || this.startTime > startTime) {
            this.startTime = startTime;
        }
    }

    private void setEndTime(final Long endTime) {
        if (0L == this.endTime || this.endTime < endTime) {
            this.endTime = endTime;
        }
    }

    /**
     * Get the number of responses grouped by HTTP status code.
     * @return map of number of responses per status code
     */
    public Map<Integer, Integer> getStatusCodes() {
        Map<Integer, Integer> map = new HashMap<>();
        statsDurationPerStatus.entrySet().stream().forEach(entry -> {
            map.put(entry.getKey(), Long.valueOf(entry.getValue().getN()).intValue());
        });
        return map;
    }

    /**
     * Get the number of registered responses.
     * @return number of all responses
     */
    public int getTotalResponses() {
        return totalResponses;
    }

    /**
     * Get the time stamp in milliseconds of the first response.
     * @return the start timestamp
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Get the time stamp in milliseconds of the last response.
     * @return the last response time stamp
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * Get the elapsed time in milliseconds for the requests.
     * @return the elapsed time
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Get the average duration of a request.
     * @return the mean duration in milliseconds
     */
    public Long getAvgDuration() {
        return (long) statsDuration.getMean();
    }

    /**
     * Get the shortest duration of a request.
     * @return the shortest duration of a request
     */
    public Long getShortestDuration() {
        return (long) statsDuration.getMin();
    }

    /**
     * Get the longest duration of a request.
     * @return the longest duration of a request
     */
    public Long getLongestDuration() {
        return (long) statsDuration.getMax();
    }

    /**
     * Get the list of durations of all the requests in the completed order.
     * @return the list of all durations
     */
    public List<Long> getDurations() {
        return Doubles.asList(statsDuration.getValues()).stream().map(val -> new Double(val).longValue()).collect(Collectors.toList());
    }

    /**
     * Return the org.apache.commons.math3.stat.descriptive.DescriptiveStatistics object build from all durations for additional statistical data.
     * @return DescriptiveStatistics object build from all durations
     */
    public DescriptiveStatistics getStatsDuration() {
        return statsDuration;
    }

    /**
     * Return the org.apache.commons.math3.stat.descriptive.DescriptiveStatistics object build for each HTTP code for additional statistical data.
     * @return map DescriptiveStatistics of durations for each HTTP status code
     */
    public Map<Integer, DescriptiveStatistics> getStatsDurationPerStatus() {
        return statsDurationPerStatus;
    }
}
