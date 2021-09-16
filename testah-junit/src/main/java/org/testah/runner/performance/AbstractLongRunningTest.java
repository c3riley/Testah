package org.testah.runner.performance;

import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.ExecData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractLongRunningTest {

    /**
     * Execute the HTTP requests, gather and publish the statistics.
     *
     * @param loadTestDataGenerator the class that generates the requests
     * @param runProps              properties that describe the test execution
     * @param publishers            0 or more ExecutionStatsPublisher, e.g. to push the statistics to Elasticsearch
     * @throws Exception when HTTP request generation fails
     * @return execution data
     */
    public ExecData executeTest(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps,
                                ExecutionStatsPublisher... publishers) throws Exception {
        runProps.setDomain(loadTestDataGenerator.getDomain());

        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        runProps.setStopTime(System.currentTimeMillis() + runProps.getRunDuration());
        TS.log().info(runProps.toString());
        long timeleft = runProps.getStopTime() - System.currentTimeMillis();
        LinkedBlockingQueue<ResponseDto> responses = new LinkedBlockingQueue<>();
        List<ResponseDto> responseDtoList = new ArrayList<>();
        long sentRequests = 0;

        ExecData execDataDto = new ExecData().withStart(Instant.now());
        try {
            while (timeleft > 0) {
                List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = loadTestDataGenerator.generateRequests();
                for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues) {
                    sentRequests += concurrentLinkedQueue.size();
                    responseDtoList.clear();
                    try {
                        akkaRunner.runAndReport(responses, runProps.getNumberOfAkkaThreads(), concurrentLinkedQueue,
                                runProps.isVerbose());
                        responses.drainTo(responseDtoList);
                        if (publishers != null && publishers.length > 0) {
                            for (ExecutionStatsPublisher publisher : publishers) {
                                publisher.push(responseDtoList);
                            }
                        }

                        // Take care of open sockets
                        System.gc();

                        Thread.sleep(runProps.getMillisBetweenChunks());
                        timeleft = runProps.getStopTime() - System.currentTimeMillis();
                        TS.log().info("Time left (ms): " + timeleft);
                        if (timeleft <= 0) {
                            break;
                        }
                    } catch (Throwable t) {
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
            akkaRunner.resetReceiveCount();
            akkaRunner.terminateActorSystems();
            if (publishers != null && publishers.length > 0) {
                for (ExecutionStatsPublisher publisher : publishers) {
                    publisher.cleanup();
                }
            }
        }
        return execDataDto;
    }
}
