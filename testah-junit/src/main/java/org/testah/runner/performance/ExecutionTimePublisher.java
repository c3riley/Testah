package org.testah.runner.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.performance.dto.ChunkStats;
import org.testah.runner.http.load.HttpAkkaStats;
import org.testah.runner.performance.dto.LoadTestSequenceDto;
import org.testah.runner.performance.dto.RequestExecutionData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ExecutionTimePublisher implements ExecutionStatsPublisher {

    ConcurrentLinkedQueue<RequestExecutionData> executionData = new ConcurrentLinkedQueue<>();

    /**
     * Write stats to log.
     *
     * @see ExecutionStatsPublisher#push(List)
     */
    @Override
    public void push(List<ResponseDto> responses) {
        for (ResponseDto responseDto : responses) {
            executionData.add(new RequestExecutionData(responseDto.getDuration(), responseDto.getStart()));
        }
    }

    /**
     * Get the list of request execution times.
     * @return  the list of request execution times
     */
    public List<Long> getElapsedTimes() {
        return executionData.stream().map(RequestExecutionData::getElapsedTimeMillis)
            .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    public List<Long> getStartTimes() {
        return executionData.stream().map(RequestExecutionData::getStartTimeMillis).collect(Collectors.toList());
    }

    /**
     * Get the list of times between consecutive requests.
     * @return  list of times between consecutive requests
     */
    public List<Long> getStartTimeSpacing() {
        List<Long> startTimesSorted =
            executionData.stream().map(RequestExecutionData::getStartTimeMillis).sorted().collect(Collectors.toList());
        List<Long> spacingTimes = new ArrayList<>();
        for (int idx = 1; idx < startTimesSorted.size(); idx++) {
            spacingTimes.add(startTimesSorted.get(idx) - startTimesSorted.get(idx - 1));
        }
        return spacingTimes;
    }

    @Override
    public void cleanup() {
        // no post processing required
    }

    @Override
    public void beforeTestSequenceStep(LoadTestSequenceDto step) {
        // no post processing required
    }

    @Override
    public void afterTestSequenceStep(LoadTestSequenceDto step) {
        // no post processing required
    }
}
