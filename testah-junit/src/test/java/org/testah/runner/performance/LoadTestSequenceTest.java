package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.io.IOException;
import java.util.List;

import static org.testah.runner.performance.AbstractLoadTest.getRunStepFile;
import static org.testah.runner.performance.TestRunProperties.DEFAULT_NUMBER_OF_SENDER_THREADS;

public class LoadTestSequenceTest
{
    TestRunProperties runProps;

    /**
     * Setup test.
     */
    @Before
    public void setup()
    {
        String testedSvc = "serviceUnderTest";
        String testClass = "testClass";
        String testMethod = "testMethod";
        int numThreads = 2;
        long millisBetweenChunks = 100;
        runProps = new TestRunProperties(testedSvc, testClass, testMethod, numThreads, millisBetweenChunks);
    }

    @Test
    public void testLoadDtoResourcePath() throws IOException
    {
        LoadTestSequence loadTestSequence = new LoadTestSequence("org/testah/runner/performance/dto/runconfig1.json");
        List<LoadTestSequenceDto> steps = loadTestSequence.getSteps(runProps.setVerbose(true));
        int listSize = 4;
        Assert.assertEquals(listSize, steps.size());
        int istep = 0;
        for (LoadTestSequenceDto step : steps)
        {
            istep++;
            Assert.assertEquals(istep * 100, step.getChunkSize().intValue());
            Assert.assertEquals(istep * 10, step.getDurationMinutes().intValue());
            Assert.assertEquals(istep, step.getStep().intValue());
            Assert.assertEquals(istep, step.getThreads().intValue());
            Assert.assertTrue(step.getIsPublish());
            Assert.assertTrue(step.getIsVerbose());
            Assert.assertEquals(DEFAULT_NUMBER_OF_SENDER_THREADS, step.getSenders());
        }
    }

    @Test
    public void testLoadDtoResourceClass() throws IOException
    {
        LoadTestSequence loadTestSequence = new LoadTestSequence(getRunStepFile(this.getClass()));
        List<LoadTestSequenceDto> steps = loadTestSequence.getSteps(runProps.setVerbose(false));

        int listSize = 6;
        Assert.assertEquals(listSize, steps.size());
        int istep = 0;
        for (LoadTestSequenceDto step : steps)
        {
            istep++;
            Assert.assertEquals(istep * 100, step.getChunkSize().intValue());
            Assert.assertEquals(istep * 10, step.getDurationMinutes().intValue());
            Assert.assertEquals(istep, step.getStep().intValue());
            Assert.assertEquals(istep, step.getThreads().intValue());
        }
        Assert.assertFalse(steps.get(0).getIsPublish());
        Assert.assertTrue(steps.get(0).getIsVerbose());
        Assert.assertTrue(steps.get(1).getIsPublish());
        Assert.assertFalse(steps.get(1).getIsVerbose());
        Assert.assertTrue(steps.get(2).getIsPublish());
        Assert.assertEquals(100, steps.get(2).getMillisBetweenChunks().intValue());
        Assert.assertFalse(steps.get(2).getIsVerbose());
        Assert.assertTrue(steps.get(3).getIsPublish());
        Assert.assertFalse(steps.get(3).getIsVerbose());
        Assert.assertFalse(steps.get(4).getIsPublish());
        Assert.assertTrue(steps.get(4).getIsVerbose());
        Assert.assertFalse(steps.get(4).getIsPublish());
        Assert.assertEquals(10, steps.get(4).getMillisBetweenChunks().intValue());
        Assert.assertTrue(steps.get(5).getIsPublish());
        Assert.assertFalse(steps.get(5).getIsVerbose());
    }

    @Test
    public void testLoadDtoDuration() throws IOException
    {
        final DateTime now = DateTime.now();
        runProps.setRunDuration(257 * 1000L);
        LoadTestSequence loadTestSequence = new LoadTestSequence("org/testah/runner/performance/dto/runconfig2.json");
        List<LoadTestSequenceDto> steps = loadTestSequence.getSteps(runProps.setVerbose(true));
        Assert.assertNull(steps.get(0).getDurationSeconds());
        Assert.assertEquals(3, steps.get(0).getDurationMinutes().intValue());
        Assert.assertEquals(180 * 1000L, steps.get(0).getStopTimeMillis(now) - now.getMillis());
        Assert.assertEquals(17, steps.get(1).getDurationSeconds().intValue());
        Assert.assertEquals(1, steps.get(1).getDurationMinutes().intValue());
        Assert.assertEquals(77 * 1000L, steps.get(1).getStopTimeMillis(now) - now.getMillis());
        Assert.assertEquals(23, steps.get(2).getDurationSeconds().intValue());
        Assert.assertNull(steps.get(2).getDurationMinutes());
        Assert.assertEquals(23 * 1000L, steps.get(2).getStopTimeMillis(now) - now.getMillis());
        Assert.assertEquals(187, steps.get(3).getDurationSeconds().intValue());
        Assert.assertEquals(1, steps.get(3).getDurationMinutes().intValue());
        Assert.assertEquals(247 * 1000L, steps.get(3).getStopTimeMillis(now) - now.getMillis());
        Assert.assertEquals(257, steps.get(4).getDurationSeconds().intValue());
        Assert.assertNull(steps.get(4).getDurationMinutes());
        Assert.assertEquals(257 * 1000L, steps.get(4).getStopTimeMillis(now) - now.getMillis());
        Assert.assertEquals(237, steps.get(5).getSenders().intValue());
    }
}
