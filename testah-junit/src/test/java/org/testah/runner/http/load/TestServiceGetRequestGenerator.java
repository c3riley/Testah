package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.GetRestRequest;
import org.testah.runner.performance.TestDataGenerator;

import java.net.URL;
import java.util.ArrayList;
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
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() throws Exception {
        while (addRequest(getRestRequest.next())) {
        }

        List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ArrayList<>();
        for (List<AbstractRequestDto<?>> getRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<AbstractRequestDto<?>>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception {
        return new URL(new TestServiceClient().getUrlGet(200)).getHost();
    }
}
