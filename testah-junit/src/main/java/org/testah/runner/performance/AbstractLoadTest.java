package org.testah.runner.performance;

import org.joda.time.DateTime;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractLoadTest {
    private static final String RUN_LOG_MESSAGE =
            "Executing step %d of %d with : threads=%d, chunksize=%d, duration=%d minutes, publish=%b";
    private final HttpAkkaRunner akkaRunner = HttpAkkaRunner.getInstance();
    private TestDataGenerator loadTestDataGenerator;
    private TestRunProperties runProps;
    private List<ExecutionStatsPublisher> publishers;

    protected void initialize(TestDataGenerator loadTestDataGenerator, TestRunProperties runProps, ExecutionStatsPublisher... publishers)
            throws Exception {
        this.loadTestDataGenerator = loadTestDataGenerator;
        this.runProps = runProps;
        this.runProps.setDomain(loadTestDataGenerator.getDomain());
        this.publishers = Arrays.asList(publishers);
    }

    protected void runTest(String resourceFile) throws Exception {
        LoadTestSequenceDto[] loadTestSequence =
                TS.util().getMap().readValue(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resourceFile),
                                Charset.forName("UTF-8")),
                        LoadTestSequenceDto[].class);
        Arrays.stream(loadTestSequence).forEach(step -> {
            TS.log().info(String.format(RUN_LOG_MESSAGE,
                    step.getStep(),
                    loadTestSequence.length,
                    step.getThreads(),
                    step.getChunkSize(),
                    step.getDurationMinutes(),
                    step.getIsPublish()));
            try {
                if (publishers != null && publishers.size() > 0) {
                    for (ExecutionStatsPublisher publisher : publishers) {
                        publisher.beforeTestSequenceStep(step);
                    }
                }
                executeStep(step.getThreads(), step.getChunkSize(), step.getDurationMinutes(), step.getIsPublish());
            } catch (Exception e) {
                TS.log().info(e);
            } finally {
                if (publishers != null && publishers.size() > 0 && step.getIsPublish()) {
                    for (ExecutionStatsPublisher publisher : publishers) {
                        publisher.afterTestSequenceStep(step);
                    }
                }
            }
        });
    }

    /**
     * Execute the HTTP requests, gather and publish the statistics. A concrete test may have multiple
     * calls to ramp up, steady level and ramp down.
     *
     * @param numThreads          number of Akka threads
     * @param chunkSize           number of bundled requests
     * @param timeIntervalMinutes time to run requests
     * @param isPublish set to false to not publish
     * @throws Exception when HTTP request generation fails
     */
    public void executeStep(int numThreads, int chunkSize, int timeIntervalMinutes, boolean isPublish) throws Exception {
        long stopTime = DateTime.now().plusMinutes(timeIntervalMinutes).getMillis();
        loadTestDataGenerator.init(chunkSize, runProps.getNumberOfChunks());
        List<ResponseDto> responses;

        while (System.currentTimeMillis() < stopTime) {
            List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues =
                    loadTestDataGenerator.generateRequests();
            for (ConcurrentLinkedQueue<AbstractRequestDto<?>> concurrentLinkedQueue : concurrentLinkedQueues) {
                try {
                    responses = akkaRunner.runAndReport(numThreads, concurrentLinkedQueue, runProps.isVerbose());

                    if (publishers != null && publishers.size() > 0 && isPublish) {
                        for (ExecutionStatsPublisher publisher : publishers) {
                            publisher.push(responses);
                        }
                    }

                    // Take care of open sockets
                    System.gc();

                    Thread.sleep(runProps.getMillisBetweenChunks());
                    if (System.currentTimeMillis() >= stopTime) {
                        return;
                    }
                } catch (Throwable t) {
                    TS.log().warn("Exception while running tests!", t);
                }
            }
        }
    }

    protected String getRunStepFile(Class<?> testClass) {
        return testClass.getCanonicalName().replaceAll("\\.", "/") + ".json";
    }

}
