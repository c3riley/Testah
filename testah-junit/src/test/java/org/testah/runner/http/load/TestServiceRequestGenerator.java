package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.performance.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestServiceRequestGenerator extends TestServiceClient implements TestDataGenerator {

    @Override
    public List<ConcurrentLinkedQueue<GetRequestDto>> generateRequests(int chunkSize, int numberOfChunks) throws Exception {
        List<GetRequestDto> getRequests = new ArrayList<>();
        int totalRequests = chunkSize * numberOfChunks;

        final int requestsPerStatusCode = 3;
        int requestCount = 0;
        int[] statusCodes = {200, 300, 400, 500};
        done:
        for (int request = 0; request < requestsPerStatusCode; request++) {
            for (int statusCode : statusCodes) {
                getRequests.add(new GetRequestDto(getUrl(statusCode)));
                requestCount++;
                if (requestCount > totalRequests) {
                    break done;
                }
            }
        }

        List<ConcurrentLinkedQueue<GetRequestDto>> concurrentLinkedQueues = new ArrayList<>();
        for (List<GetRequestDto> getRequestSublist : Lists.partition(getRequests, chunkSize)) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<GetRequestDto>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

}
