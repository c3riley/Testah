package org.testah.runner.performance;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.requests.AbstractRequestDto;

public interface TestDataGenerator {
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests(int chunkSize, int numberOfChunks) throws Exception;
}
