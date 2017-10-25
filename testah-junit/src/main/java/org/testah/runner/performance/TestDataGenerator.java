package org.testah.runner.performance;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.requests.GetRequestDto;

public interface TestDataGenerator {
    public List<ConcurrentLinkedQueue<GetRequestDto>> generateRequests(int chunkSize, int numberOfChunks) throws Exception;
}
