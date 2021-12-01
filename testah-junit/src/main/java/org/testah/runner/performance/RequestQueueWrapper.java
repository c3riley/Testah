package org.testah.runner.performance;

import org.testah.driver.http.requests.AbstractRequestDto;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestQueueWrapper
{
    final ConcurrentLinkedQueue<AbstractRequestDto<?>> queue;
    final long millisBetweenChunks;
    long stopTime;
    final long id;

    /**
     * Wrapper for RequestQueue, holds the time between chunks and an ID in addition
     * to the queue of REST requests.
     * @param queue queue of REST requests
     * @param millisBetweenChunks milli seconds between sending chunks of requests
     * @param id identifier for this request queue
     */
    public RequestQueueWrapper(ConcurrentLinkedQueue<AbstractRequestDto<?>> queue, long millisBetweenChunks, long id) {
        this.queue = queue;
        this.millisBetweenChunks = millisBetweenChunks;
        this.id = id;
    }

    public ConcurrentLinkedQueue<AbstractRequestDto<?>> getQueue()
    {
        return queue;
    }

    public long getMillisBetweenChunks()
    {
        return millisBetweenChunks;
    }

    public long getId()
    {
        return id;
    }

    public void setStopTime(long stopTime)
    {
        this.stopTime = stopTime;
    }

    public long getStopTime()
    {
        return stopTime;
    }
}
