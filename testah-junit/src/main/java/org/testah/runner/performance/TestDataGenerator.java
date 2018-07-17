package org.testah.runner.performance;

import org.testah.driver.http.requests.AbstractRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TestDataGenerator {
    private List<AbstractRequestDto<?>> requestList = new ArrayList<>();
    private final int totalRequests;
    private final int chunkSize;

    public TestDataGenerator(int chunkSize, int numberOfChunks) {
        totalRequests = chunkSize * numberOfChunks;
        this.chunkSize = chunkSize;
    }

    public abstract List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() throws Exception;

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
