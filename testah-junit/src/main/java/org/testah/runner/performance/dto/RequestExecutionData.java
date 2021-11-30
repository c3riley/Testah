package org.testah.runner.performance.dto;

public class RequestExecutionData
{
    long elapsedTimeMillis;
    long startTimeMillis;

    public RequestExecutionData(long elapsedTimeMillis, long startTimeMillis)
    {
        this.elapsedTimeMillis = elapsedTimeMillis;
        this.startTimeMillis = startTimeMillis;
    }

    public long getElapsedTimeMillis()
    {
        return elapsedTimeMillis;
    }

    public long getStartTimeMillis()
    {
        return startTimeMillis;
    }
}
