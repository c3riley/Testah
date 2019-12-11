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
        loadTestSequenceDto.withChunkSize(11).withDurationMinutes(12).withStep(13).withThreads(14).withIsPublish(false);
        Assert.assertEquals(11, loadTestSequenceDto.getChunkSize().intValue());
        Assert.assertEquals(12, loadTestSequenceDto.getDurationMinutes().intValue());
        Assert.assertEquals(13, loadTestSequenceDto.getStep().intValue());
        Assert.assertEquals(14, loadTestSequenceDto.getThreads().intValue());
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
        Assert.assertEquals(true, loadTestSequence[1].getIsPublish());
        Assert.assertEquals(true, loadTestSequence[2].getIsPublish());
        Assert.assertEquals(true, loadTestSequence[3].getIsPublish());
        Assert.assertEquals(false, loadTestSequence[4].getIsPublish());
        Assert.assertEquals(true, loadTestSequence[5].getIsPublish());

    }
}

