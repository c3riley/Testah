package org.testah.driver.http.requests;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpHead;

public class HeadRequestDto extends AbstractRequestDto<HeadRequestDto> {

    public HeadRequestDto(final String uri) {
        super(new HttpHead(uri), "HEAD");
    }

    @Override
    protected HeadRequestDto setEntity(final HttpEntity payload) {
        throw new NotImplementedException("Head per Http spec cannot have a payload");
    }
}
