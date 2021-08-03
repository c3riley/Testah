package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.PostRestRequest;
import org.testah.runner.performance.TestDataGenerator;

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
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() throws Exception {
        PostRestRequest postRestRequest = new PostRestRequest(stringList, longList);
        while (addRequest(postRestRequest.next())) {
        }

        List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ArrayList<>();
        for (List<AbstractRequestDto<?>> postRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<AbstractRequestDto<?>>(postRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception {
        return new URL(new TestServiceClient().getUrlPost()).getHost();
    }
}
