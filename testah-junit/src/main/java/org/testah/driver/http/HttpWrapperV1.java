package org.testah.driver.http;

/**
 * HttpWrapperV1 is Deprecated, please change to use HttpWrapperV2
 */
@Deprecated
public class HttpWrapperV1 extends AbstractHttpWrapper {

    public HttpWrapperV1(){
        super();
        setRequestConfig(getRequestConfigDefaultBuilder().setExpectContinueEnabled(true).build());
    }

    protected AbstractHttpWrapper getSelf() {
        return this;
    }
}
