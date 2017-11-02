package org.testah.runner.http.load;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.performance.TestDataGenerator;

import com.google.common.collect.Lists;

public class TestServiceGetRequestGenerator extends TestServiceClient implements TestDataGenerator {

    @Override
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests(int chunkSize, int numberOfChunks) throws Exception {
        List<AbstractRequestDto<?>> getRequests = new ArrayList<>();
        int totalRequests = chunkSize * numberOfChunks;

        final int requestsPerStatusCode = 3;
        int requestCount = 0;
        int[] statusCodes = {200, 300, 400, 500};
        done:
        for (int request = 0; request < requestsPerStatusCode; request++) {
            for (int statusCode : statusCodes) {
                getRequests.add(new GetRequestDto(getUrlGet(statusCode)));
                requestCount++;
                if (requestCount > totalRequests) {
                    break done;
                }
            }
        }

        List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ArrayList<>();
        for (List<AbstractRequestDto<?>> getRequestSublist : Lists.partition(getRequests, chunkSize)) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<AbstractRequestDto<?>>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws MalformedURLException {
        return new URL(getUrlGet(200)).getHost();
    }
}
