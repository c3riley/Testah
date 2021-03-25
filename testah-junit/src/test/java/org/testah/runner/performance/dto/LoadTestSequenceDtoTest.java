package org.testah.runner.performance.dto;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;
import org.testah.runner.performance.TestRunProperties;

import java.io.IOException;

public class LoadTestSequenceDtoTest
{
    @Test
    public void testDto()
    {
        LoadTestSequenceDto loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setChunkSize(11).setDurationMinutes(12).setStep(13).setThreads(14)
                .setIsPublish(false).setIsVerbose(true).setMillisBetweenChunks(111L);
        Assert.assertEquals(11, loadTestSequenceDto.getChunkSize().intValue());
        Assert.assertEquals(12, loadTestSequenceDto.getDurationMinutes().intValue());
        Assert.assertEquals(13, loadTestSequenceDto.getStep().intValue());
        Assert.assertEquals(14, loadTestSequenceDto.getThreads().intValue());
        Assert.assertEquals(111, loadTestSequenceDto.getMillisBetweenChunks().intValue());
        Assert.assertEquals(true, loadTestSequenceDto.getIsVerbose());
        Assert.assertEquals(false, loadTestSequenceDto.getIsPublish());
    }
}

