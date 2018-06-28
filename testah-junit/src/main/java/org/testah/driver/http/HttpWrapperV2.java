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

    protected AbstractHttpWrapper getSelf() {
        return this;
    }

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

    private AbstractHttpWrapper initRequestConfig() {
        return initRequestConfig(null);
    }

    private AbstractHttpWrapper initRequestConfig(final Integer timeout) {

        final RequestConfig.Builder rcb = RequestConfig.custom();

        if (null != timeout) {
            rcb.setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(getDefaultConnectionTimeout());
        }

        rcb.setCookieSpec(CookieSpecs.DEFAULT).setExpectContinueEnabled(false)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC));

        return setRequestConfig(rcb.build());
    }

    @Override
    public AbstractHttpWrapper setDefaultConnectionTimeout(final int defaultConnectionTimeout) {
        super.setDefaultConnectionTimeout(defaultConnectionTimeout);
        return initRequestConfig();
    }

}
