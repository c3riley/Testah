package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.ExecData;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.testah.runner.http.load.HttpActor.getReceivedCount;
import static org.testah.runner.http.load.HttpActor.getSentCount;

public abstract class AbstractLongRunningTest {
    private long notificationCount = 0;
    private static final long maxNotificationCount = 100L;
    private static final long defaultMillisPauseExecution = 1000L;
    private long millisPauseExecution;
    private long millisBetweenNotifications;
    private long stopTime;

    /**
     * Execute the HTTP requests, gather and publish the statistics.
     *
     * @param loadTestDataGenerator the class that generates the requests
     * @param runProps              properties that describe the test execution
     * @param publishers            0 or more ExecutionStatsPublisher, e.g. to push the statistics to Elasticsearch
     * @return execution data
     * @throws Exception when HTTP request generation fails
     */
    public ExecData executeTest(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps,
                                ExecutionStatsPublisher... publishers) throws Exception {
        runProps.setDomain(loadTestDataGenerator.getDomain());

        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        runProps.setStopTime(System.currentTimeMillis() + runProps.getRunDuration());
        millisPauseExecution = runProps.getMillisPauseExecution(defaultMillisPauseExecution);
        millisBetweenNotifications = Math.max(millisPauseExecution, runProps.getRunDuration() / maxNotificationCount);

        LinkedBlockingQueue<ResponseDto> responses = new LinkedBlockingQueue<>();

        LoadTestSequenceDto step = new LoadTestSequenceDto().fillUndefined(runProps);

        ExecData execDataDto = new ExecData().withStart(Instant.now());
        DateTime startDateTime = DateTime.now();
        stopTime = step.getStopTimeMillis(startDateTime);
        try {
            StepRequestQueueWrapper stepRequestQueueWrapper =
                new StepRequestQueueWrapper(
                    loadTestDataGenerator,
                    new LoadTestSequenceDto().fillUndefined(runProps),
                    startDateTime);
            akkaRunner.runAndReport(responses, step.getThreads(), step.getSenders(), stepRequestQueueWrapper, runProps.isVerbose());

            while (System.currentTimeMillis() < stopTime) {
                List<ResponseDto> responseDtoList = new ArrayList<>();
                akkaRunner.updateResponseQueue(responses);
                responses.drainTo(responseDtoList);
                if (publishers != null && publishers.length > 0 && step.getIsPublish()) {
                    for (ExecutionStatsPublisher publisher : publishers) {
                        publisher.push(responseDtoList);
                    }
                }

                pause();
            }
        } catch (Throwable t) {
            TS.log().warn("Exception while running tests!", t);
        } finally {
            akkaRunner.terminateActorSystems();
            execDataDto.withStop(Instant.now());
            execDataDto.withSendCount(getSentCount());
            execDataDto.withReceiveCount(getReceivedCount());
            TS.log().warn(String.format("Requests sent/received = %d/%d", getSentCount(), getReceivedCount()));

            if (publishers != null && publishers.length > 0)
            {
                for (ExecutionStatsPublisher publisher : publishers)
                {
                    publisher.cleanup();
                }
            }
        }
        return execDataDto;
    }

    private void pause() throws InterruptedException
    {
        // Take care of open sockets
        System.gc();

        Thread.sleep(millisPauseExecution);
        if (System.currentTimeMillis() > millisBetweenNotifications * notificationCount) {
            notificationCount++;
            TS.log().info("Execution Time left (ms): " + (stopTime - System.currentTimeMillis()));
        }
    }
}
