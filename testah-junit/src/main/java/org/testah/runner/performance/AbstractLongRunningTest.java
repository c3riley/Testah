package org.testah.runner.performance;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;

public abstract class AbstractLongRunningTest {

    /**
     * Execute the HTTP requests, gather and publish the statistics.
     *
     * @param loadTestDataGenerator the class that generates the requests
     * @param runProps properties that describe the test execution
     * @param publishers 0 or more ExecutionStatsPublisher, e.g. to push the statistics to Elasticsearch
     * @throws Exception when HTTP request generation fails
     */
    public void executeTest(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps,
            ExecutionStatsPublisher... publishers) throws Exception {
        List<ResponseDto> responses;
        runProps.setDomain(loadTestDataGenerator.getDomain());

        TS.log().info(runProps.toString());
        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        long timeleft = runProps.getStopTime() - System.currentTimeMillis();
        try {
            while (timeleft > 0) {
                List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = loadTestDataGenerator.generateRequests();
                for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues) {
                    try {
                        responses = akkaRunner.runAndReport(runProps.getNumberOfAkkaThreads(), concurrentLinkedQueue,
                                runProps.isVerbose());

                        if (publishers != null && publishers.length > 0) {
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
                            return;
                        }
                    } catch (Throwable t) {
                        TS.log().warn("Exception while running tests!", t);
                    }
                }
            }
        } finally {
            if (publishers != null && publishers.length > 0) {
                for (ExecutionStatsPublisher publisher : publishers) {
                    publisher.cleanup();
                }
            }
        }
    }
}
