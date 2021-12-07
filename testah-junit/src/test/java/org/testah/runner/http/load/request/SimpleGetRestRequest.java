package org.testah.runner.http.load.request;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.performance.AbstractPerformanceRestRequest;

public class SimpleGetRestRequest extends AbstractPerformanceRestRequest
{
    private final String urlString;
    private int count = 1;

    public SimpleGetRestRequest(String urlString)
    {
        this.urlString = urlString;
    }

    @Override
    public AbstractRequestDto<?> next()
    {
        return new GetRequestDto(urlString + "?" + count++);
    }
}
