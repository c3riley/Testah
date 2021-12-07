package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.GetRestRequest;
import org.testah.runner.performance.RequestQueueWrapper;
import org.testah.runner.performance.TestDataGenerator;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestServiceGetRequestGenerator extends TestDataGenerator {
    private final List<Integer> statusCodes = Arrays.asList(200, 300, 400, 500);
    private final GetRestRequest getRestRequest;

    public TestServiceGetRequestGenerator(int chunkSize, int numberOfChunks) {
        super(chunkSize, numberOfChunks);
        getRestRequest = new GetRestRequest(statusCodes);
    }

    @Override
    public ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() {
        while (addRequest(getRestRequest.next())) {
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
