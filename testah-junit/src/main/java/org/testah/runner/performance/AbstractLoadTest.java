package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.ExecData;
import org.testah.runner.performance.dto.LoadTestSequenceDto;
import org.testah.runner.performance.dto.SequenceExecData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.testah.runner.http.load.HttpActor.getReceivedCount;
import static org.testah.runner.http.load.HttpActor.getSentCount;
import static org.testah.runner.http.load.HttpActor.resetReceivedCount;
import static org.testah.runner.http.load.HttpActor.resetSentCount;

public abstract class AbstractLoadTest
{
    private static final String RUN_LOG_MESSAGE =
        "Executing step %d of %d with: threads=%d, chunkSize=%d, duration=%s, millisBetweenChunks=%d, publish=%b";
    private final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
    private TestDataGenerator loadTestDataGenerator;
    private TestRunProperties runProps;
    private List<ExecutionStatsPublisher> publishers;
    private static final long defaultMillisPauseExecution = 500L;
    private static final long maxNotificationCount = 20L;
    private long notificationCount = 0;

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
                akkaRunner.terminateActorSystems();
                if (publishers != null && publishers.size() > 0 && step.getIsPublish())
                {
                    for (ExecutionStatsPublisher publisher : publishers)
                    {
                        publisher.afterTestSequenceStep(step);
                    }
                }
            }
        });
        TS.log().info(String.format("Total number of requests sent/received = %d/%d",
            sequenceExecData.getSendCount(), sequenceExecData.getReceiveCount()));

        return sequenceExecData;
    }

    /**
     * Execute the HTTP requests, gather and publish the statistics. A concrete test may have multiple
     * calls to ramp up, steady level and ramp down.
     *
     * @param step LoadTestSequenceDto for this step
     * @return execution data
     */
    public ExecData executeStep(LoadTestSequenceDto step)
    {
        loadTestDataGenerator.init(step.getChunkSize(), step.getNumberOfChunks());
        LinkedBlockingQueue<ResponseDto> responses = new LinkedBlockingQueue<>();

        ExecData execDataDto = new ExecData().withStart(Instant.now());
        DateTime startDateTime = DateTime.now();
        long millisPauseExecution = runProps.getMillisPauseExecution(defaultMillisPauseExecution);
        long stopTime = step.getStopTimeMillis(startDateTime);
        long millisBetweenNotifications = Math.max(millisPauseExecution, runProps.getRunDuration() / maxNotificationCount);
        try
        {
            StepRequestQueueWrapper stepRequestQueueWrapper = new StepRequestQueueWrapper(loadTestDataGenerator, step, startDateTime);
            akkaRunner.runAndReport(responses, step.getThreads(), step.getSenders(), stepRequestQueueWrapper, step.getIsVerbose());
            while (System.currentTimeMillis() < stopTime)
            {
                List<ResponseDto> responseDtoList = new ArrayList<>();
                akkaRunner.updateResponseQueue(responses);
                responses.drainTo(responseDtoList);
                if (publishers != null && publishers.size() > 0 && step.getIsPublish())
                {
                    for (ExecutionStatsPublisher publisher : publishers)
                    {
                        publisher.push(responseDtoList);
                    }
                }
                pause(step.getStep(), millisPauseExecution, millisBetweenNotifications, stopTime);
                // Take care of open sockets
                System.gc();

                Thread.sleep(millisPauseExecution);
            }
        } catch (Throwable t)
        {
            TS.log().warn("Exception while running tests!", t);
        } finally
        {
            final long receiveCount = getReceivedCount();
            resetReceivedCount();
            final long sentCount = getSentCount();
            resetSentCount();

            execDataDto.withStop(Instant.now());
            execDataDto.withSendCount(sentCount);
            execDataDto.withReceiveCount(receiveCount);

            TS.log().info(String.format("Requests sent/received = %d/%d", sentCount, receiveCount));

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

    private void pause(int stepNumber, long millisPauseExecution, long millisBetweenNotifications, long stopTime)
        throws InterruptedException
    {
        // Take care of open sockets
        System.gc();

        Thread.sleep(millisPauseExecution);
        if (System.currentTimeMillis() > millisBetweenNotifications * notificationCount) {
            notificationCount++;
            TS.log().info(String.format("Step %d: execution time left (ms) = %d", stepNumber, stopTime - System.currentTimeMillis()));
        }
    }
}
