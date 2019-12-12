package org.testah.runner.performance.dto;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;

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

    @Test
    public void testLoadDto() throws IOException
    {
        String json = TS.util().getResourceAsString("/org/testah/runner/performance/dto/runconfig1.json");
        LoadTestSequenceDto[] loadTestSequence = TS.util().getMap().readValue(json, LoadTestSequenceDto[].class);
        int listSize = 4;
        Assert.assertEquals(listSize, loadTestSequence.length);
        for (int istep = 1; istep <= listSize; istep++)
        {
            Assert.assertEquals(istep * 100, loadTestSequence[istep - 1].getChunkSize().intValue());
            Assert.assertEquals(istep * 10, loadTestSequence[istep - 1].getDurationMinutes().intValue());
            Assert.assertEquals(istep, loadTestSequence[istep - 1].getStep().intValue());
            Assert.assertEquals(istep, loadTestSequence[istep - 1].getThreads().intValue());
            Assert.assertEquals(true, loadTestSequence[istep - 1].getIsPublish());
        }

        json = TS.util().getResourceAsString("/org/testah/runner/performance/dto/runconfig2.json");
        loadTestSequence = TS.util().getMap().readValue(json, LoadTestSequenceDto[].class);
        listSize = 6;
        Assert.assertEquals(listSize, loadTestSequence.length);
        for (int istep = 1; istep <= listSize; istep++)
        {
            Assert.assertEquals(istep * 100, loadTestSequence[istep - 1].getChunkSize().intValue());
            Assert.assertEquals(istep * 10, loadTestSequence[istep - 1].getDurationMinutes().intValue());
            Assert.assertEquals(istep, loadTestSequence[istep - 1].getStep().intValue());
            Assert.assertEquals(istep, loadTestSequence[istep - 1].getThreads().intValue());
        }
        Assert.assertEquals(false, loadTestSequence[0].getIsPublish());
        Assert.assertEquals(true, loadTestSequence[0].getIsVerbose());
        Assert.assertEquals(true, loadTestSequence[1].getIsPublish());
        Assert.assertEquals(false, loadTestSequence[1].getIsVerbose());
        Assert.assertEquals(true, loadTestSequence[2].getIsPublish());
        Assert.assertEquals(100, loadTestSequence[2].getMillisBetweenChunks().intValue());
        Assert.assertEquals(false, loadTestSequence[2].getIsVerbose());
        Assert.assertEquals(true, loadTestSequence[3].getIsPublish());
        Assert.assertEquals(false, loadTestSequence[3].getIsVerbose());
        Assert.assertEquals(false, loadTestSequence[4].getIsPublish());
        Assert.assertEquals(true, loadTestSequence[4].getIsVerbose());
        Assert.assertEquals(false, loadTestSequence[4].getIsPublish());
        Assert.assertEquals(10, loadTestSequence[4].getMillisBetweenChunks().intValue());
        Assert.assertEquals(true, loadTestSequence[5].getIsPublish());
        Assert.assertEquals(false, loadTestSequence[5].getIsVerbose());
    }
}

