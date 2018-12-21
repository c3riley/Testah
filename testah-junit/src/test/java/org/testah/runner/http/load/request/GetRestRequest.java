package org.testah.runner.http.load.request;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.http.load.TestServiceClient;
import org.testah.runner.performance.AbstractPerformanceRestRequest;

import java.util.List;

public class GetRestRequest extends AbstractPerformanceRestRequest {
    private final List<Integer> statusCodes;
    private final TestServiceClient client = new TestServiceClient();

    public GetRestRequest(List<Integer> statusCodes) {
        super(statusCodes);
        this.statusCodes = statusCodes;
    }

    @Override
    public AbstractRequestDto<?> next() {
        List<Integer> tuple = tupleGenerator.nextTuple();
        return new GetRequestDto(client.getUrlGet(statusCodes.get(tuple.get(0))));
    }
}
