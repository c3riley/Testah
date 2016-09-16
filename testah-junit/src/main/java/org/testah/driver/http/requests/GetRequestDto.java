package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

public class GetRequestDto extends AbstractRequestDto<GetRequestDto> {

    public GetRequestDto(final String uri) {
        super(new HttpGet(uri), "GET");
    }

    public GetRequestDto setPayload(final String payload) {
        return this;
    }

    public GetRequestDto setPayload(final HttpEntity payload) {
        return this;
    }

    public GetRequestDto setPayload(final Object payload) {
        return this;
    }

}
