package org.testah.runner.performance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.io.IOException;
import java.util.List;

import static org.testah.runner.performance.AbstractLoadTest.getRunStepFile;

public class LoadTestSequenceTest
{
    TestRunProperties runProps;

    @Before
    public void setup() {
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
            Assert.assertEquals(true, step.getIsPublish());
            Assert.assertEquals(true, step.getIsVerbose());
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
        for (LoadTestSequenceDto step : steps) {
            istep++;
            Assert.assertEquals(istep * 100, step.getChunkSize().intValue());
            Assert.assertEquals(istep * 10, step.getDurationMinutes().intValue());
            Assert.assertEquals(istep, step.getStep().intValue());
            Assert.assertEquals(istep, step.getThreads().intValue());
        }
        Assert.assertEquals(false, steps.get(0).getIsPublish());
        Assert.assertEquals(true, steps.get(0).getIsVerbose());
        Assert.assertEquals(true, steps.get(1).getIsPublish());
        Assert.assertEquals(false, steps.get(1).getIsVerbose());
        Assert.assertEquals(true, steps.get(2).getIsPublish());
        Assert.assertEquals(100, steps.get(2).getMillisBetweenChunks().intValue());
        Assert.assertEquals(false, steps.get(2).getIsVerbose());
        Assert.assertEquals(true, steps.get(3).getIsPublish());
        Assert.assertEquals(false, steps.get(3).getIsVerbose());
        Assert.assertEquals(false, steps.get(4).getIsPublish());
        Assert.assertEquals(true, steps.get(4).getIsVerbose());
        Assert.assertEquals(false, steps.get(4).getIsPublish());
        Assert.assertEquals(10, steps.get(4).getMillisBetweenChunks().intValue());
        Assert.assertEquals(true, steps.get(5).getIsPublish());
        Assert.assertEquals(false, steps.get(5).getIsVerbose());
    }
}
