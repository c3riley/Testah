package org.testah.driver.http;

import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;

import java.util.Arrays;

/**
 * The type Http wrapper v 2.
 * Sets important timeouts that is not set by default in V1.
 * Also turns off ExpectContinueEnabled which will cause issues if used with Zap proxy
 */
public class HttpWrapperV2 extends AbstractHttpWrapper {

    /**
     * The constant DEFAULT_TIMEOUT.
     */
    public static final int DEFAULT_TIMEOUT = 60000;

    /**
     * Instantiates a new Http wrapper v 2.
     */
    public HttpWrapperV2() {
        super();
        setDefaultConnectionTimeout(DEFAULT_TIMEOUT);
        initRequestConfig();
    }

    protected AbstractHttpWrapper getSelf() {
        return this;
    }

    @Override
    public AbstractHttpWrapper setDefaultConnectionTimeout(final int defaultConnectionTimeout) {
        super.setDefaultConnectionTimeout(defaultConnectionTimeout);
        return initRequestConfig();
    }

    private AbstractHttpWrapper initRequestConfig() {
        return initRequestConfig(null);
    }

    private AbstractHttpWrapper initRequestConfig(final Integer timeout) {

        final RequestConfig.Builder rcb = getRequestConfigDefaultBuilder();

        if (null != timeout) {
            rcb.setSocketTimeout(timeout).setConnectTimeout(timeout);
        }

        return setRequestConfig(rcb.build());
    }

}
