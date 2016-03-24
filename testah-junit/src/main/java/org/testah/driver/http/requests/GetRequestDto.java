package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.testah.TS;

public class GetRequestDto extends AbstractRequestDto {

    public GetRequestDto(final String uri) {
        super(new HttpGet(uri), "GET");
    }

    public AbstractRequestDto setPayload(String payload) {
        TS.log().debug("No Op");
        return this;
    }

    public AbstractRequestDto setPayload(HttpEntity payload) {
        TS.log().debug("No Op");
        return this;
    }

    public AbstractRequestDto setPayload(Object payload) {
        TS.log().debug("No Op");
        return this;
    }

}
