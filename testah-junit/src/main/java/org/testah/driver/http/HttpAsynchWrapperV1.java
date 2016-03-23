package org.testah.driver.http;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.testah.driver.http.response.ResponseDto;

public class HttpAsynchWrapperV1 extends AbstractHttpWrapper {

    private CloseableHttpAsyncClient httpclient;

    public HttpAsynchWrapperV1 setHttpclient() {
        httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(getRequestConfig())
                .setMaxConnPerRoute(getDefaultMaxPerRoute()).setMaxConnTotal(getDefaultPoolSize()).build();
        return this;
    }
    
    public CloseableHttpAsyncClient getAsyncHttpClient() {
        return httpclient;
    }
    
    
    protected ResponseDto doExecute(final HttpClientContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
