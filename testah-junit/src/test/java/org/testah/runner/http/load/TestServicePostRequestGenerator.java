package org.testah.runner.http.load;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.runner.performance.TestDataGenerator;

import com.google.common.collect.Lists;

public class TestServicePostRequestGenerator extends TestServiceClient implements TestDataGenerator {

    @Override
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests(int chunkSize, int numberOfChunks) throws Exception {
        List<AbstractRequestDto<?>> postRequests = new ArrayList<>();
        int totalRequests = chunkSize * numberOfChunks;

        final int requestsPerStatusCode = 3;
        int requestCount = 0;
        String[] payloads = {"xxxAxxx", "xxxBxxx", "xxxCxxx", "xxxDxxx"};
        done:
        for (int request = 0; request < requestsPerStatusCode; request++) {
            for (String payload : payloads) {
                postRequests.add(new PostRequestDto(getUrlPost()).setPayload(payload));
                requestCount++;
                if (requestCount > totalRequests) {
                    break done;
                }
            }
        }

        List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ArrayList<>();
        for (List<AbstractRequestDto<?>> postRequestSublist : Lists.partition(postRequests, chunkSize)) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<AbstractRequestDto<?>>(postRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception {
        return new URL(getUrlPost()).getHost();
    }
}
