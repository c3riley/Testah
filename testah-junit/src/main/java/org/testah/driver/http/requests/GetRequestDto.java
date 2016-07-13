package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

public class GetRequestDto extends AbstractRequestDto {

    public GetRequestDto(final String uri) {
        super(new HttpGet(uri), "GET");
    }

    public AbstractRequestDto setPayload(final String payload) {
        return this;
    }

    public AbstractRequestDto setPayload(final HttpEntity payload) {
        return this;
    }

    public AbstractRequestDto setPayload(final Object payload) {
        return this;
    }

    protected AbstractRequestDto getSelf() {
        return this;
    }

}
