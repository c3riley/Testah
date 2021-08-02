package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.SimpleGetRestRequest;
import org.testah.runner.performance.TestDataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleGetRequestGenerator extends TestDataGenerator
{
    SimpleGetRestRequest simpleGetRestRequest;

    public SimpleGetRequestGenerator(String urlString)
    {
        super();
        simpleGetRestRequest = new SimpleGetRestRequest(urlString);
    }

    @Override
    public List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests() throws Exception
    {
        while (addRequest(simpleGetRestRequest.next())) {
        }

        List<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ArrayList<>();
        for (List<AbstractRequestDto<?>> getRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<AbstractRequestDto<?>>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain() throws Exception
    {
        return "localhost";
    }
}
