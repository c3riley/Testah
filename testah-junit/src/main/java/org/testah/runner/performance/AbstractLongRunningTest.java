package org.testah.runner.performance;

import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;

import java.time.Duration;
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
     */
    public void executeTest(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps,
                            ExecutionStatsPublisher... publishers) throws Exception {
        runProps.setDomain(loadTestDataGenerator.getDomain());

        TS.log().info(runProps.toString());
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        long timeleft = runProps.getStopTime() - System.currentTimeMillis();
        LinkedBlockingQueue<ResponseDto> responseQueue = new LinkedBlockingQueue<>();
        List<ResponseDto> responses;
        long sentRequests = 0;

        try {
            while (timeleft > 0) {
                List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = loadTestDataGenerator.generateRequests();
                for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues) {
                    sentRequests += concurrentLinkedQueue.size();
                    try {
                        akkaRunner.runAndReport(responseQueue, runProps.getNumberOfAkkaThreads(), concurrentLinkedQueue,
                                runProps.isVerbose());

                        responses = akkaRunner.updateResponseQueue(responseQueue);
                        if (publishers != null && publishers.length > 0 && responses.size() > 0) {
                            for (ExecutionStatsPublisher publisher : publishers) {
                                publisher.push(responses);
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
            akkaRunner.waitForResponses(responseQueue, sentRequests, Duration.ofSeconds(5).toMillis());
            responses = akkaRunner.updateResponseQueue(responseQueue);
            if (publishers != null && publishers.length > 0 && responses.size() > 0) {
                for (ExecutionStatsPublisher publisher : publishers) {
                    publisher.push(responses);
                }
            }
        } finally {
            TS.log().info(String.format("Requests sent/received = %d/%d", sentRequests, akkaRunner.getReceiveCount()));
            akkaRunner.terminateActorSystems();
            if (publishers != null && publishers.length > 0) {
                for (ExecutionStatsPublisher publisher : publishers) {
                    publisher.cleanup();
                }
            }
        }
    }
}
