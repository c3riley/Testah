package org.testah.runner.performance;

import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.util.List;

public interface ExecutionStatsPublisher {

    /**
     * Publish the responses.
     * @param responses service request responses
     * @throws Exception propagate any uncaught exception
     */
    public void push(List<ResponseDto> responses) throws Exception;

    /**
     * Finalize the publishing of results, e.g. close connections, etc.
     */
    public void cleanup();

    /**
     * When running performance/stress tests, there may be tasks that need to be executed before each step.
     */
    public void beforeTestSequenceStep(LoadTestSequenceDto step);

    /**
     * When running performance/stress tests, there may be tasks that need to be executed after each step.
     */
    public void afterTestSequenceStep(LoadTestSequenceDto step);
}
