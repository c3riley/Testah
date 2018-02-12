package org.testah.runner.performance;

import java.util.List;

import org.testah.driver.http.response.ResponseDto;

public interface ExecutionStatsPublisher {

    public void push(List<ResponseDto> responses) throws Exception;

    public void cleanup();
}
