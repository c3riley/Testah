package org.testah.driver.http.requests;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

public class GetRequestDto extends AbstractRequestDto<GetRequestDto> {

    public GetRequestDto(final String uri) {
        super(new HttpGet(uri), "GET");
    }

    @Override
    protected GetRequestDto setEntity(final HttpEntity payload) {
        throw new NotImplementedException("Get per Http spec cannot have a payload");
    }
}
