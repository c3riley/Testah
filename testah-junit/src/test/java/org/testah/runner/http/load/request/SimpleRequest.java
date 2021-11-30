package org.testah.runner.http.load.request;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.performance.AbstractPerformanceRestRequest;

import java.util.Random;

public class SimpleRequest extends AbstractPerformanceRestRequest
{
    public static final String PATH_DELAYED = "/delayed";
    public static final String PATH_NORMAL = "/normal";

    final int outlierFrequency;
    final String baseUrl;
    final String fastUrlString;
    final String slowUrlString;
    final Random random = new Random();

    private long count;

    /**
     * Constructor.
     * @param baseUrl           URL to which to send the request to.
     * @param outlierFrequency  frequency at which the delayed request is made:
     *                          every outlierFrequency is a slow response request
     */
    public SimpleRequest(String baseUrl, int outlierFrequency)
    {
        this.baseUrl = baseUrl;
        this.outlierFrequency = outlierFrequency;
        fastUrlString = baseUrl + PATH_NORMAL;
        slowUrlString = baseUrl + PATH_DELAYED;
        // first outlier at some random invocation count, then every outlierFrequency invocation
        count = outlierFrequency == 0 ? 0 : random.nextInt(outlierFrequency);
    }

    @Override
    public AbstractRequestDto<?> next()
    {
        count++;
        // if outlierFrequency == 0 no outliers at all
        if (outlierFrequency != 0 && count % outlierFrequency == 0)
        {
            return new GetRequestDto(slowUrlString);
        } else
        {
            return new GetRequestDto(fastUrlString);
        }
    }
}
