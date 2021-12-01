package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class StepRequestQueueWrapper
{
    final ConcurrentLinkedQueue<RequestQueueWrapper> requestQueueWrapperQueue;
    final DateTime startTime;
    final TestDataGenerator loadTestDataGenerator;
    final LoadTestSequenceDto step;
    private volatile AtomicLong id = new AtomicLong();

    /**
     * Constructor of wrapper for queue of RequestQueueWrapper.
     * @param loadTestDataGenerator data generator
     * @param step                  runtime data for this step
     * @param startTime             start time of this step
     */
    public StepRequestQueueWrapper(TestDataGenerator loadTestDataGenerator, LoadTestSequenceDto step, DateTime startTime) {
        this.requestQueueWrapperQueue = new ConcurrentLinkedQueue<>();
        this.startTime = startTime;
        this.loadTestDataGenerator = loadTestDataGenerator;
        this.step = step;
        id.set(0);
    }

    /**
     * Add information from the LoadTestSequenceDto to each RequestQueueWrapper element.
     * @param step LoadTestSequenceDto
     * @return queue of RequestQueueWrapper
     * @throws Exception when generation of requests fails
     */
    public ConcurrentLinkedQueue<RequestQueueWrapper> fillQueue(LoadTestSequenceDto step) throws Exception
    {
        ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> queueOfRequestQueues = loadTestDataGenerator.generateRequests();
        for (ConcurrentLinkedQueue<AbstractRequestDto<?>> requestQueue : queueOfRequestQueues) {
            requestQueueWrapperQueue.add(new RequestQueueWrapper(requestQueue, step.getMillisBetweenChunks(), id.incrementAndGet()));
        }
        return requestQueueWrapperQueue;
    }

    /**
     * Get the next request queue wrapper.
     * @return wrapper for RequestQueue
     * @throws Exception if request generation fails
     */
    public RequestQueueWrapper getNext() throws Exception
    {
        if (requestQueueWrapperQueue.size() == 0) {
            fillQueue(step);
        }
        return requestQueueWrapperQueue.poll();
    }

    public long getMillisBetweenChunks()
    {
        return step.getMillisBetweenChunks();
    }

    public long getStopTime()
    {
        return step.getStopTimeMillis(startTime);
    }
}
