package org.testah.runner.http.load;

import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.runner.performance.AbstractLongRunningTest;
import org.testah.runner.performance.ChunkStatsLogPublisher;
import org.testah.runner.performance.TestRunProperties;

//import org.testah.runner.performance.ElasticSearchResponseTimesPublisher;

public class TestLongRunning extends AbstractLongRunningTest {
    // private static final String baseUrl = "http://localhost:9200";
    // private static final String username = "elastic";
    // private static final String password = "changeme";
    // private static final String index = "testah";
    // private static final String type = "load";

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
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks);
        //ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
        //new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        executeTest(new TestServiceGetRequestGenerator(),
            runProps
                .setVerbose(true)
                .setRunDuration(1000L * 10),
            //elasticSearchExecutionStatsPublisher,
            chunkStatsLogPublisher);
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
        TestRunProperties runProps =
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks);
        //ElasticSearchResponseTimesPublisher elasticSearchExecutionStatsPublisher =
        //new ElasticSearchResponseTimesPublisher(baseUrl, index, type, username, password, runProps).setVerbose(true);
        ChunkStatsLogPublisher chunkStatsLogPublisher = new ChunkStatsLogPublisher();

        executeTest(new TestServicePostRequestGenerator(),
            runProps
                .setVerbose(true)
                .setRunDuration(1000L * 10),
            //elasticSearchExecutionStatsPublisher,
            chunkStatsLogPublisher);
    }
}
