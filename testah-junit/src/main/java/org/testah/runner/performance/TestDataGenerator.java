package org.testah.runner.performance;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TestDataGenerator {
    private List<AbstractRequestDto<?>> requestList = new ArrayList<>();
    // total requests and chunk size typically change during a load test and should not be final
    private int totalRequests;
    private int chunkSize;

    /**
     * No argument constructor. Properties to be set dynamically.
     */
    public TestDataGenerator() {
    }

    /**
     * Constructor for long-running tests where the parameters are fixed during execution.
     *
     * @param chunkSize      size of a chunk of requests
     * @param numberOfChunks number of chunks
     */
    public TestDataGenerator(int chunkSize, int numberOfChunks) {
        init(chunkSize, numberOfChunks);
    }

    /**
     * Allow dynamic setting of parameters.
     *
     * @param chunkSize      size of a chunk of requests
     * @param numberOfChunks number of chunks
     */
    public void init(int chunkSize, int numberOfChunks) {
        totalRequests = chunkSize * numberOfChunks;
        this.chunkSize = chunkSize;
    }

    public abstract ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() throws Exception;

    public abstract String getDomain() throws Exception;

    /**
     * Add a request to the request list.
     *
     * @param request http request
     * @return true if more requests need to be added to reach the requested number of requests
     */
    public boolean addRequest(AbstractRequestDto<?> request) {
        boolean keepGoing = false;
        if (requestList.size() < totalRequests) {
            keepGoing = true;
            requestList.add(request);
        }
        return keepGoing;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public List<AbstractRequestDto<?>> getRequestList() {
        return requestList;
    }

    /**
     * Set the list of requests.
     *
     * @param requestList the requestList to set
     */
    protected void setRequestList(List<AbstractRequestDto<?>> requestList) {
        this.requestList = requestList;
    }
}
