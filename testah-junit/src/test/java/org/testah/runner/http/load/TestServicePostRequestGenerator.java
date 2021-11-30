package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.PostRestRequest;
import org.testah.runner.performance.RequestQueueWrapper;
import org.testah.runner.performance.TestDataGenerator;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestServicePostRequestGenerator extends TestDataGenerator {
    private final List<String> stringList = Arrays.asList("xxxAxxx", "xxxBxxx", "xxxCxxx", "xxxDxxx");
    private final List<Long> longList = Arrays.asList(1L, 2L);

    public TestServicePostRequestGenerator(int chunkSize, int numberOfChunks) {
        super(chunkSize, numberOfChunks);
    }

    @Override
    public ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() {
        PostRestRequest postRestRequest = new PostRestRequest(stringList, longList);
        while (addRequest(postRestRequest.next())) {
        }

        ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ConcurrentLinkedQueue<>();
        for (List<AbstractRequestDto<?>> postRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<>(postRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception {
        return new URL(new TestServiceClient().getUrlPost()).getHost();
    }
}
