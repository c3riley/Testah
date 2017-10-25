package org.testah.runner.http.load;

import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.runner.performance.AbstractLongRunningTest;
import org.testah.runner.performance.TestRunProperties;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestLongRunning extends AbstractLongRunningTest {
    // private final String baseUrl = "http://localhost:9200";
    // private final String username = "elastic";
    // private final String password = "changeme";

    @Test
    @TestCase()
    public void testGet() throws Exception {
        final int numberOfChunks = 8;
        final int chunkSize = 4;
        final int nthreads = 2;
        final long millisBetweenChunks = 1000L;
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        String serviceUnderTest = "ServiceUnderTest";
        executeTest(new TestServiceGetRequestGenerator(),
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks)
                .setVerbose(true)
                .setExpectedStatusCodes(Arrays.stream(new Integer[] {200, 300, 400, 500}).collect(Collectors.toSet()))
                .setRunDuration(1000L * 10));
        // new ElasticSearchExecutionStatsPublisher(baseUrl, username, password));
    }

    @Test
    @TestCase()
    public void testPost() throws Exception {
        final int numberOfChunks = 8;
        final int chunkSize = 4;
        final int nthreads = 2;
        final long millisBetweenChunks = 1000L;
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        String serviceUnderTest = "ServiceUnderTest";
        executeTest(new TestServicePostRequestGenerator(),
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks)
                .setVerbose(true)
                .setRunDuration(1000L * 10));
        // new ElasticSearchExecutionStatsPublisher(baseUrl, username, password));
    }
}
