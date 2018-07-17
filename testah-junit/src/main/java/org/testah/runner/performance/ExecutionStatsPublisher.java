package org.testah.runner.performance;

import org.testah.driver.http.response.ResponseDto;

import java.util.List;

public interface ExecutionStatsPublisher {

    public void push(List<ResponseDto> responses) throws Exception;

    public void cleanup();
}
