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
     * @param runProps              properties that describe the test execution
     * @param publishers            0 or more ExecutionStatsPublisher, e.g. to push the statistics to Elasticsearch
     * @throws Exception when HTTP request generation fails
     */
    public void executeTest(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps, ExecutionStatsPublisher... publishers)
        throws Exception
    {
        List<ResponseDto> responses;
        runProps.setDomain(loadTestDataGenerator.getDomain());

        final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
        while (System.currentTimeMillis() < runProps.getStopTime()) {
            List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues =
                loadTestDataGenerator.generateRequests(runProps.getChunkSize(), runProps.getNumberOfChunks());
            for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues) {
                try {
                    responses = akkaRunner.runAndReport(runProps.getNumberOfAkkaThreads(), concurrentLinkedQueue, runProps.isVerbose());

                    if (publishers != null && publishers.length > 0) {
                        for (ExecutionStatsPublisher publisher : publishers) {
                            publisher.push(responses);
                        }
                    }

                    // Take care of open sockets
                    System.gc();

                    Thread.sleep(runProps.getMillisBetweenChunks());
                    if (System.currentTimeMillis() >= runProps.getStopTime()) {
                        return;
                    }
                } catch (Throwable t) {
                    TS.log().warn("Exception while running tests!", t);
                }
            }
        }
    }
}
