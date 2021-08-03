package org.testah.runner.performance.dto;

import java.time.Instant;

public class ExecData
{
    Instant start;
    Instant stop;
    long sendCount;
    long receiveCount;
    Integer stepId = null;

    public Instant getStart()
    {
        return start;
    }

    public ExecData withStart(Instant start)
    {
        this.start = start;
        return this;
    }

    public Instant getStop()
    {
        return stop;
    }

    public ExecData withStop(Instant stop)
    {
        this.stop = stop;
        return this;
    }

    public long getSendCount()
    {
        return sendCount;
    }

    public ExecData withSendCount(long sendCount)
    {
        this.sendCount = sendCount;
        return this;
    }

    public long getReceiveCount()
    {
        return receiveCount;
    }

    public ExecData withReceiveCount(long receiveCount)
    {
        this.receiveCount = receiveCount;
        return this;
    }

    public Integer getStepId()
    {
        return stepId;
    }

    public ExecData withStepId(Integer stepId)
    {
        this.stepId = stepId;
        return this;
    }
}
