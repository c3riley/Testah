package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;

public class DeleteRequestDto extends AbstractRequestDto<DeleteRequestDto> {

    public DeleteRequestDto(final String uri) {
        super(new HttpDelete(uri), "DELETE");
    }

    public DeleteRequestDto setPayload(final String payload) {
        return this;
    }

    public DeleteRequestDto setPayload(final HttpEntity payload) {
        return this;
    }

    public DeleteRequestDto setPayload(final Object payload) {
        return this;
    }

}
