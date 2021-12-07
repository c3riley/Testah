package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.SimpleRequest;
import org.testah.runner.performance.TestDataGenerator;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestTimingGetRequestGenerator extends TestDataGenerator {
    private final List<Integer> statusCodes = Arrays.asList(200, 300, 400, 500);
    private final SimpleRequest simpleRequest;

    public TestTimingGetRequestGenerator(String baseUrl, int outlierFrequency, int chunkSize, int numberOfChunks) {
        super(chunkSize, numberOfChunks);
        simpleRequest = new SimpleRequest(baseUrl, outlierFrequency);
    }

    @Override
    public ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() {
        while (addRequest(simpleRequest.next())) {
        }

        ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ConcurrentLinkedQueue<>();
        for (List<AbstractRequestDto<?>> getRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception {
        return new URL(new TestServiceClient().getUrlGet(200)).getHost();
    }
}
