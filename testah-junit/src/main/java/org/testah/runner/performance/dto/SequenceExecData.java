package org.testah.runner.performance.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SequenceExecData
{
    List<ExecData> execDataDtos = new ArrayList<>();

    public SequenceExecData add(int stepId, ExecData execData)
    {
        execDataDtos.add(execData.withStepId(stepId));
        return this;
    }

    public ExecData getByStepCount(int stepCount)
    {
        return execDataDtos.get(stepCount);
    }

    public int getStepCount()
    {
        return execDataDtos.size();
    }

    public long getReceiveCount()
    {
        return execDataDtos.stream().mapToLong(ExecData::getReceiveCount).sum();
    }

    public long getSendCount()
    {
        return execDataDtos.stream().mapToLong(ExecData::getSendCount).sum();
    }

    public Instant getStart()
    {
        return execDataDtos.get(0).getStart();
    }

    public Instant getStop()
    {
        return getByStepCount(getStepCount() - 1).getStop();
    }
}
