package org.testah.runner.http.load;

import com.google.common.collect.Lists;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.http.load.request.SimpleGetRestRequest;
import org.testah.runner.performance.TestDataGenerator;

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
    public ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> generateRequests()
    {
        while (addRequest(simpleGetRestRequest.next())) {
        }

        ConcurrentLinkedQueue<ConcurrentLinkedQueue<AbstractRequestDto<?>>> concurrentLinkedQueues = new ConcurrentLinkedQueue<>();
        for (List<AbstractRequestDto<?>> getRequestSublist : Lists.partition(getRequestList(), getChunkSize())) {
            concurrentLinkedQueues.add(new ConcurrentLinkedQueue<>(getRequestSublist));
        }
        return concurrentLinkedQueues;
    }

    @Override
    public String getDomain()
    {
        return "localhost";
    }
}
