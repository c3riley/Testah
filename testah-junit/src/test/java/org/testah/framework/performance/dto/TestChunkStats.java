package org.testah.framework.performance.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.performance.dto.ChunkStats;
import org.testah.framework.report.performance.dto.StatsDetails;
import org.testah.runner.http.load.HttpAkkaStats;
import org.testah.runner.performance.TestRunProperties;

public class TestChunkStats {
    private static final long now = System.currentTimeMillis();
    private static final long elapsedTime = 660L;
    private static final Integer[] statusCodes = new Integer[] { 200, 300, 400, 500 };

    @Test
    public void test() {
        String serviceUnderTest = "ServiceUnderTest";
        String testClass = "TestClass";
        String testMethod = "TestMethod";
        int numberOfAkkaThreads = 5;
        int chunkSize = 5;
        int numberOfChunks = 5;
        long millisBetweenChunks = 1000L;

        List<ResponseDto> responses = getResponseList(generateResponseMap());
        TestRunProperties testRunProperties = new TestRunProperties(serviceUnderTest, testClass, testMethod, 
                        numberOfAkkaThreads, chunkSize, numberOfChunks, millisBetweenChunks);

        HttpAkkaStats httpAkkaStats = new HttpAkkaStats(responses);
        ChunkStats chunkStats = new ChunkStats(testRunProperties, httpAkkaStats);

        TS.asserts().equalsTo("tested service", serviceUnderTest, chunkStats.getServiceName());
        TS.asserts().equalsTo("test class", testClass, chunkStats.getClassName());
        TS.asserts().equalsTo("test method", testMethod, chunkStats.getMethodName());
        TS.asserts().equalsTo("elapased time", elapsedTime, chunkStats.getElapsedTime());
        TS.asserts().equalsTo("number of data points", Long.valueOf(responses.size()), chunkStats.getOverallStats().getCount());
        TS.asserts().equalsTo("longest duration", Long.valueOf(510), chunkStats.getOverallStats().getMax());
        TS.asserts().equalsTo("shortest duration", Long.valueOf(190), chunkStats.getOverallStats().getMin());
        TS.asserts().equalsTo("average duration", Long.valueOf(350), chunkStats.getOverallStats().getMean());
        TS.asserts().equalsTo("90th percentail", Long.valueOf(507), chunkStats.getOverallStats().getPct90());
        TS.asserts().equalsTo("standard deviation", Long.valueOf(117), chunkStats.getOverallStats().getStd());

        for (Integer statusCode : statusCodes) {
            StatsDetails statsDetails = chunkStats.getStatsByStatusCode().get(statusCode);
            TS.asserts().equalsTo("longest duration", Long.valueOf(statusCode + 10), statsDetails.getMax());
            TS.asserts().equalsTo("shortest duration", Long.valueOf(statusCode - 10), statsDetails.getMin());
            TS.asserts().equalsTo("average duration", Long.valueOf(statusCode), statsDetails.getMean());
            TS.asserts().equalsTo("90th percentail", Long.valueOf(statusCode + 10), statsDetails.getPct90());
            TS.asserts().equalsTo("standard deviation", Long.valueOf(10), statsDetails.getStd());
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
        Long[] durations = new Long[] { seed - 10L, seed, seed + 10L };
        return Arrays.stream(durations).map(duration -> new ResponseDto().setStatusCode(statusCode).setStart(startTime)
                        .setEnd(startTime + duration.longValue())).collect(Collectors.toList());
    }
}