package org.testah.runner.performance.dto;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.testah.runner.performance.TestRunProperties;

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
        Assert.assertTrue(loadTestSequenceDto.getIsVerbose());
        Assert.assertFalse(loadTestSequenceDto.getIsPublish());
    }

    @Test
    public void getStepRunDurationString()
    {
        LoadTestSequenceDto loadTestSequenceDto;

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationMinutes(12);
        Assert.assertNull(loadTestSequenceDto.getDurationSeconds());
        Assert.assertEquals("12 minutes 0 seconds", loadTestSequenceDto.getStepRunDurationString());

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationSeconds(13);
        Assert.assertNull(loadTestSequenceDto.getDurationMinutes());
        Assert.assertEquals("0 minutes 13 seconds", loadTestSequenceDto.getStepRunDurationString());

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationSeconds(185);
        Assert.assertEquals("3 minutes 5 seconds", loadTestSequenceDto.getStepRunDurationString());

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationMinutes(5).setDurationSeconds(67);
        Assert.assertEquals("6 minutes 7 seconds", loadTestSequenceDto.getStepRunDurationString());
    }

    @Test
    public void getStopTimeMillis()
    {
        DateTime now = DateTime.now();
        LoadTestSequenceDto loadTestSequenceDto;

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationMinutes(12);
        Assert.assertEquals(now.plusMinutes(12).getMillis(), loadTestSequenceDto.getStopTimeMillis(now));

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationSeconds(185);
        Assert.assertEquals(now.plusSeconds(185).getMillis(), loadTestSequenceDto.getStopTimeMillis(now));

        loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setDurationMinutes(5).setDurationSeconds(37);
        Assert.assertEquals(now.plusMinutes(5).plusSeconds(37).getMillis(), loadTestSequenceDto.getStopTimeMillis(now));
    }

    @Test
    public void numberOfChunks()
    {
        int numberOfChunks = 111;
        LoadTestSequenceDto loadTestSequenceDto = new LoadTestSequenceDto();
        loadTestSequenceDto.setNumberOfChunks(numberOfChunks);
        Assert.assertEquals(numberOfChunks, loadTestSequenceDto.getNumberOfChunks().intValue());
    }

    @Test
    public void fillUndefined()
    {
        final int numberOfAkkaThreads = 13;
        final int numberOfChunks = 111;
        final int chunkSize = 32;
        final long millisBetweenChunks = 123L;
        final int runDurationSeconds = 1800;
        final long runDurationMillis = runDurationSeconds * 1000L;
        final boolean isVerbose = false;
        final String serviceUnderTest = "myService";
        final String testClass = "myTestClass";
        final String testMethod = "myTestMethod";

        LoadTestSequenceDto loadTestSequenceDto = new LoadTestSequenceDto();
        Assert.assertNull(loadTestSequenceDto.getThreads());
        Assert.assertNull(loadTestSequenceDto.getChunkSize());
        Assert.assertNull(loadTestSequenceDto.getDurationMinutes());
        Assert.assertNull(loadTestSequenceDto.getDurationSeconds());
        Assert.assertNull(loadTestSequenceDto.getIsVerbose());
        Assert.assertNull(loadTestSequenceDto.getMillisBetweenChunks());
        Assert.assertNull(loadTestSequenceDto.getNumberOfChunks());

        TestRunProperties runProperties =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, numberOfAkkaThreads, millisBetweenChunks);
        runProperties
            .setRunDuration(runDurationMillis)
            .setNumberOfChunks(numberOfChunks)
            .setChunkSize(chunkSize)
            .setVerbose(isVerbose);

        loadTestSequenceDto.fillUndefined(runProperties);
        Assert.assertEquals(numberOfAkkaThreads, loadTestSequenceDto.getThreads().intValue());
        Assert.assertEquals(chunkSize, loadTestSequenceDto.getChunkSize().intValue());
        Assert.assertEquals(numberOfChunks, loadTestSequenceDto.getNumberOfChunks().intValue());
        Assert.assertEquals(millisBetweenChunks, loadTestSequenceDto.getMillisBetweenChunks().intValue());
        Assert.assertNull(loadTestSequenceDto.getDurationMinutes());
        Assert.assertEquals(runDurationSeconds, loadTestSequenceDto.getDurationSeconds().intValue());
        Assert.assertEquals(isVerbose, loadTestSequenceDto.getIsVerbose());

        runProperties
            .setNumberOfAkkaThreads(numberOfAkkaThreads + 1)
            .setChunkSize(chunkSize + 1)
            .setNumberOfChunks(numberOfChunks + 1)
            .setRunDuration(runDurationMillis + 1L)
            .setMillisBetweenChunks(millisBetweenChunks + 1)
            .setVerbose(!isVerbose);

        loadTestSequenceDto.fillUndefined(runProperties);
        Assert.assertEquals(numberOfAkkaThreads, loadTestSequenceDto.getThreads().intValue());
        Assert.assertEquals(chunkSize, loadTestSequenceDto.getChunkSize().intValue());
        Assert.assertEquals(numberOfChunks, loadTestSequenceDto.getNumberOfChunks().intValue());
        Assert.assertEquals(millisBetweenChunks, loadTestSequenceDto.getMillisBetweenChunks().intValue());
        Assert.assertNull(loadTestSequenceDto.getDurationMinutes());
        Assert.assertEquals(runDurationSeconds, loadTestSequenceDto.getDurationSeconds().intValue());
        Assert.assertEquals(isVerbose, loadTestSequenceDto.getIsVerbose());
    }
}

