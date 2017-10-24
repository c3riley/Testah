package org.testah.runner.http.load;

import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.runner.performance.AbstractLongRunningTest;
import org.testah.runner.performance.ElasticSearchExecutionStatsPublisher;
import org.testah.runner.performance.TestRunProperties;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TestLongRunning extends AbstractLongRunningTest {

    @Test
    @TestCase()
    public void test() throws Exception {
        final int numberOfChunks = 8;
        final int chunkSize = 4;
        final int nthreads = 2;
        final long millisBetweenChunks = 1000L;
        String baseUrl = "http://localhost:9200";
        String username = "elastic";
        String password = "changeme";
        String testClass = this.getClass().getSimpleName();
        String testMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
        String serviceUnderTest = "ServiceUnderTest";
        executeTest(new TestServiceRequestGenerator(),
            new TestRunProperties(serviceUnderTest, testClass, testMethod, nthreads, chunkSize, numberOfChunks, millisBetweenChunks)
                .setVerbose(true)
                .setExpectedStatusCodes(Arrays.stream(new Integer[] {200, 300, 400, 500}).collect(Collectors.toSet()))
                .setRunDuration(1000L * 10), new ElasticSearchExecutionStatsPublisher(baseUrl, username, password)
        );
    }
}
