package org.testah.runner.performance;

import org.testah.driver.http.requests.AbstractRequestDto;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface TestDataGenerator {
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests(int chunkSize, int numberOfChunks) throws Exception;

    public String getDomain() throws Exception;
}
