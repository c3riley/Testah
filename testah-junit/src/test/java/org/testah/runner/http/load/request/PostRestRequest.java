package org.testah.runner.http.load.request;

import java.util.List;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.runner.http.load.TestServiceClient;
import org.testah.runner.performance.AbstractPerformanceRestRequest;

public class PostRestRequest extends AbstractPerformanceRestRequest {
    private final List<String> stringList;
    private final List<Long> longList;
    private final TestServiceClient client = new TestServiceClient();

    /**
     * Constructor.
     * @param stringList list of strings
     * @param longList list of longs
     */
    public PostRestRequest(List<String> stringList, List<Long> longList) {
        super(stringList, longList);
        this.stringList = stringList;
        this.longList = longList;
    }

    @Override
    public AbstractRequestDto<?> next() {
        List<Integer> tuple = tupleGenerator.nextTuple();
        return new PostRequestDto(client.getUrlPost(), stringList.get(tuple.get(0)) + longList.get(tuple.get(1)));
    }
}
