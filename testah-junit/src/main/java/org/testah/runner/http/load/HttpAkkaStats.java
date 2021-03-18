package org.testah.runner.http.load;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.testah.driver.http.response.ResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpAkkaStats {

    private int totalResponses;
    private Long duration;
    private Long startTime = 0L;
    private Long endTime = 0L;
    private DescriptiveStatistics statsDuration = new DescriptiveStatistics();
    private Map<Integer, DescriptiveStatistics> statsDurationPerStatus = new HashMap<>();

    /**
     * Constructor. Takes the provided responses to generate execution statistics.
     *
     * @param responseQueue blocking queue of service responses
     */
    public HttpAkkaStats(LinkedBlockingQueue<ResponseDto> responseQueue) {
        List<ResponseDto> responseList = new ArrayList<>();
        responseQueue.drainTo(responseList);
        initHttpAkkaStats(responseList);
    }

    /**
     * Constructor. Takes the provided responses to generate execution statistics.
     *
     * @param responseList list of service responses
     */
    public HttpAkkaStats(final List<ResponseDto> responseList) {
        if (responseList == null) {
            throw new RuntimeException("responses is null and null is not allowed");
        }
        initHttpAkkaStats(responseList);
    }

    public void initHttpAkkaStats(final List<ResponseDto> responses) {
        this.totalResponses = responses.size();
        statsDuration = new DescriptiveStatistics();
        statsDurationPerStatus = new HashMap<>();

        for (final ResponseDto response : responses) {
            statsDuration.addValue(response.getDuration());
            if (!statsDurationPerStatus.keySet().contains(response.getStatusCode())) {
                statsDurationPerStatus.put(response.getStatusCode(), new DescriptiveStatistics());
            }
            statsDurationPerStatus.get(response.getStatusCode()).addValue(response.getDuration());
            setStartTime(response.getStart());
            setEndTime(response.getEnd());
        }
        duration = (endTime - startTime);
    }

    /**
     * Get the number of responses grouped by HTTP status code.
     *
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
     *
     * @return number of all responses
     */
    public int getTotalResponses() {
        return totalResponses;
    }

    /**
     * Get the time stamp in milliseconds of the first response.
     *
     * @return the start timestamp
     */
    public Long getStartTime() {
        return startTime;
    }

    private void setStartTime(final Long startTime) {
        if (0L == this.startTime || this.startTime > startTime) {
            this.startTime = startTime;
        }
    }

    /**
     * Get the time stamp in milliseconds of the last response.
     *
     * @return the last response time stamp
     */
    public Long getEndTime() {
        return endTime;
    }

    private void setEndTime(final Long endTime) {
        if (0L == this.endTime || this.endTime < endTime) {
            this.endTime = endTime;
        }
    }

    /**
     * Get the elapsed time in milliseconds for the requests.
     *
     * @return the elapsed time
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Get the average duration of a request.
     *
     * @return the mean duration in milliseconds
     */
    public Long getAvgDuration() {
        return (long) statsDuration.getMean();
    }

    /**
     * Get the shortest duration of a request.
     *
     * @return the shortest duration of a request
     */
    public Long getShortestDuration() {
        return (long) statsDuration.getMin();
    }

    /**
     * Get the longest duration of a request.
     *
     * @return the longest duration of a request
     */
    public Long getLongestDuration() {
        return (long) statsDuration.getMax();
    }

    /**
     * Get the list of durations of all the requests in the completed order.
     *
     * @return the list of all durations
     */
    public List<Long> getDurations() {
        List<Long> longs = new ArrayList<>();
        for (double duration : statsDuration.getValues()) {
            // the other way caused double boxing error.
            longs.add(Long.valueOf(String.valueOf(duration)));
        }
        return longs;
    }

    /**
     * Return the org.apache.commons.math3.stat.descriptive.DescriptiveStatistics object build from all durations for additional statistical data.
     *
     * @return DescriptiveStatistics object build from all durations
     */
    public DescriptiveStatistics getStatsDuration() {
        return statsDuration;
    }

    /**
     * Return the org.apache.commons.math3.stat.descriptive.DescriptiveStatistics object build for each HTTP code for additional statistical data.
     *
     * @return map DescriptiveStatistics of durations for each HTTP status code
     */
    public Map<Integer, DescriptiveStatistics> getStatsDurationPerStatus() {
        return statsDurationPerStatus;
    }
}
