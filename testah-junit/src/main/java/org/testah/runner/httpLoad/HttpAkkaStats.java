package org.testah.runner.httpLoad;

import org.testah.driver.http.response.ResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HttpAkkaStats {

    private final HashMap<Integer, Integer> statusCodes;
    private final int totalResponses;
    private Long startTime = 0L;
    private Long endTime = 0L;
    private final Long duration;
    private final Long avgDuration;
    private final Long shortestDuration;
    private final Long longestDuration;
    private final TreeSet<Long> durations;
    private final HashMap<Integer, TreeSet<Long>> durationsForStatus;
    private final HashMap<Integer, Long> avgDurationsForStatus;

    public HttpAkkaStats(final List<ResponseDto> responses) {
        this.avgDurationsForStatus = new HashMap<Integer, Long>();
        this.durationsForStatus = new HashMap<Integer, TreeSet<Long>>();
        this.totalResponses = responses.size();
        this.statusCodes = new HashMap<Integer, Integer>();
        this.durations = new TreeSet<Long>();
        Long sumDurations = 0L;
        Long sumDurationsStatus = 0L;
        Integer statusCodeCtr;
        Long responseDuration = 0L;
        for (final ResponseDto r : responses) {
            responseDuration = r.getDuration();
            statusCodeCtr = statusCodes.get(r.getStatusCode());
            sumDurationsStatus = avgDurationsForStatus.get(r.getStatusCode());
            if (null == statusCodeCtr) {
                durationsForStatus.put(r.getStatusCode(), new TreeSet<Long>());
                statusCodeCtr = 0;
                sumDurationsStatus = 0L;
            }
            durationsForStatus.get(r.getStatusCode()).add(responseDuration);
            statusCodes.put(r.getStatusCode(), ++statusCodeCtr);
            avgDurationsForStatus.put(r.getStatusCode(), sumDurationsStatus + responseDuration);
            setStartTime(r.getStart());
            setEndTime(r.getEnd());
            durations.add(responseDuration);
            sumDurations += responseDuration;
        }
        this.shortestDuration = durations.first();
        this.longestDuration = durations.last();
        this.avgDuration = sumDurations / totalResponses;
        this.duration = (endTime - startTime);
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

    public HashMap<Integer, Integer> getStatusCodes() {
        return statusCodes;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getAvgDuration() {
        return avgDuration;
    }

    public Long getShortestDuration() {
        return shortestDuration;
    }

    public Long getLongestDuration() {
        return longestDuration;
    }

    public TreeSet<Long> getDurations() {
        return durations;
    }

}
