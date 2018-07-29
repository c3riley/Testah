package org.testah.runner.http.load;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.http.load.HttpAkkaStats;

import java.util.*;
import java.util.stream.Collectors;

public class TestHttpAkkaStats {
    private static final long now = System.currentTimeMillis();
    private static final long elapsedTime = 660L;
    private static final double delta = 0.001;
    private static final Integer[] statusCodes = new Integer[] {200, 300, 400, 500};

    /**
     * Verify that the responses are properly processed into instances of org.apache.commons.math3.stat.descriptive.DescriptiveStatistics.
     */
    @Test
    public void happyPath() {
        List<ResponseDto> responses = getResponseList(generateResponseMap());

        HttpAkkaStats httpAkkaStats = new HttpAkkaStats(responses);
        TS.asserts().equalsTo("longest duration", 510L, httpAkkaStats.getLongestDuration().longValue());
        TS.asserts().equalsTo("shortest duration", 190L, httpAkkaStats.getShortestDuration().longValue());
        TS.asserts().equalsTo("average duration", 350, httpAkkaStats.getAvgDuration().longValue());
        TS.asserts().equalsTo("start time", now, httpAkkaStats.getStartTime().longValue());
        TS.asserts().equalsTo("elapsed time", elapsedTime, httpAkkaStats.getDuration().longValue());
        TS.asserts().equalsTo("end time", now + elapsedTime, httpAkkaStats.getEndTime().longValue());

        DescriptiveStatistics descriptiveStatistics = httpAkkaStats.getStatsDuration();
        TS.asserts().equalsTo("longest duration", 510.0, descriptiveStatistics.getMax(), delta);
        TS.asserts().equalsTo("shortest duration", 190.0, descriptiveStatistics.getMin(), delta);
        TS.asserts().equalsTo("average duration", 350.0, descriptiveStatistics.getMean(), delta);
        TS.asserts().equalsTo("number of data points", responses.size(), descriptiveStatistics.getN());

        for (Integer statusCode : statusCodes) {
            TS.asserts().equalsTo("number of data points for status " + statusCode, 3,
                httpAkkaStats.getStatsDurationPerStatus().get(statusCode).getN());
            TS.asserts().equalsTo("average duration for status " + statusCode, statusCode.doubleValue(),
                httpAkkaStats.getStatsDurationPerStatus().get(statusCode).getMean());
        }
    }

    private List<ResponseDto> getResponseList(Map<Integer, List<ResponseDto>> map) {
        List<ResponseDto> list = new ArrayList<>();
        list.addAll(map.get(200));
        list.addAll(map.get(300));
        list.addAll(map.get(500));
        list.addAll(map.get(400));
        return list;
    }

    private Map<Integer, List<ResponseDto>> generateResponseMap() {
        long offset = 50;
        long startTime = now;
        Map<Integer, List<ResponseDto>> map = new HashMap<>();
        for (Integer statusCode : statusCodes) {
            map.put(statusCode, generateResponses(startTime, statusCode, statusCode.longValue()));
            startTime += offset;
        }
        return map;
    }

    private List<ResponseDto> generateResponses(long startTime, int statusCode, long seed) {
        Long[] durations = new Long[] {seed - 10L, seed, seed + 10L};
        return Arrays.stream(durations).map(duration ->
            new ResponseDto().setStatusCode(statusCode)
                .setStart(startTime).setEnd(startTime + duration.longValue())).collect(Collectors.toList());
    }
}
