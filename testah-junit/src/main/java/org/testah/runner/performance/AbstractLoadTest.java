package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.ExecData;
import org.testah.runner.performance.dto.LoadTestSequenceDto;
import org.testah.runner.performance.dto.SequenceExecData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractLoadTest
{
    private static final String RUN_LOG_MESSAGE =
        "Executing step %d of %d with: threads=%d, chunksize=%d, duration=%s, millisBetweenChunks=%d, publish=%b";
    private final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
    private TestDataGenerator loadTestDataGenerator;
    private TestRunProperties runProps;
    private List<ExecutionStatsPublisher> publishers;

    public static String getRunStepFile(Class<?> testClass)
    {
        return testClass.getCanonicalName().replaceAll("\\.", "/") + ".json";
    }

    protected void initialize(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps, ExecutionStatsPublisher... publishers)
        throws Exception
    {
        this.loadTestDataGenerator = loadTestDataGenerator;
        this.runProps = runProps;
        this.runProps.setDomain(loadTestDataGenerator.getDomain());
        this.publishers = Arrays.asList(publishers);
    }

    protected SequenceExecData runTest(String resourceFile) throws Exception
    {
        SequenceExecData sequenceExecData = new SequenceExecData();
        LoadTestSequence loadTestSequence = new LoadTestSequence(resourceFile);
        loadTestSequence.getSteps(runProps).forEach(step ->
        {
            TS.log().info(
                String.format(RUN_LOG_MESSAGE,
                    step.getStep(),
                    loadTestSequence.size(),
                    step.getThreads(),
                    step.getChunkSize(),
                    step.getStepRunDurationString(),
                    step.getMillisBetweenChunks(),
                    step.getIsPublish()));

            try
            {
                if (publishers != null && publishers.size() > 0)
                {
                    for (ExecutionStatsPublisher publisher : publishers)
                    {
                        publisher.beforeTestSequenceStep(step);
                    }
                }
                // execute the step and record execution data
                sequenceExecData.add(step.getStep(), executeStep(step));
            } catch (Exception e)
            {
                TS.log().info(String.format("Caught exception in step %d.", step.getStep()), e);
            } finally
            {
                if (publishers != null && publishers.size() > 0 && step.getIsPublish())
                {
                    for (ExecutionStatsPublisher publisher : publishers)
                    {
                        publisher.afterTestSequenceStep(step);
                    }
                }
            }
        });
        return sequenceExecData;
    }

    /**
     * Execute the HTTP requests, gather and publish the statistics. A concrete test may have multiple
     * calls to ramp up, steady level and ramp down.
     *
     * @param step LoadTestSequenceDto for this step
     * @throws Exception when HTTP request generation fails
     * @return execution data
     */
    public ExecData executeStep(LoadTestSequenceDto step)
        throws Exception
    {
        long stopTime = step.getStopTimeMillis(DateTime.now());
        loadTestDataGenerator.init(step.getChunkSize(), step.getNumberOfChunks());
        LinkedBlockingQueue<ResponseDto> responses = new LinkedBlockingQueue<>();
        List<ResponseDto> responseDtoList = new ArrayList<>();
        long sentRequests = 0;

        ExecData execDataDto = new ExecData().withStart(Instant.now());
        try
        {
            while (System.currentTimeMillis() < stopTime)
            {
                List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues =
                    loadTestDataGenerator.generateRequests();
                for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues)
                {
                    sentRequests += concurrentLinkedQueue.size();
                    responseDtoList.clear();
                    try
                    {
                        akkaRunner.runAndReport(responses, step.getThreads(), concurrentLinkedQueue, step.getIsVerbose());
                        responses.drainTo(responseDtoList);
                        if (publishers != null && publishers.size() > 0 && step.getIsPublish())
                        {
                            for (ExecutionStatsPublisher publisher : publishers)
                            {
                                publisher.push(responseDtoList);
                            }
                        }

                        // Take care of open sockets
                        System.gc();

                        Thread.sleep(step.getMillisBetweenChunks());
                        if (System.currentTimeMillis() >= stopTime)
                        {
                            break;
                        }
                    } catch (Throwable t)
                    {
                        TS.log().warn("Exception while running tests!", t);
                    }
                }
            }
        } finally
        {
            execDataDto.withStop(Instant.now());
            execDataDto.withSendCount(sentRequests);
            execDataDto.withReceiveCount(akkaRunner.getReceiveCount());
            TS.log().info(String.format("Requests sent/received = %d/%d", sentRequests, akkaRunner.getReceiveCount()));
            akkaRunner.terminateActorSystems();
            // do not carry over the received count from the previous step
            akkaRunner.resetReceiveCount();

            if (publishers != null && publishers.size() > 0)
            {
                for (ExecutionStatsPublisher publisher : publishers)
                {
                    publisher.cleanup();
                }
            }
        }
        return execDataDto;
    }
}
