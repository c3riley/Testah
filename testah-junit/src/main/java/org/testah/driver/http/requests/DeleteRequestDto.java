package org.testah.driver.http.requests;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;

public class DeleteRequestDto extends AbstractRequestDto<DeleteRequestDto> {

    public DeleteRequestDto(final String uri) {
        super(new HttpDelete(uri), "DELETE");
    }

    @Override
    protected DeleteRequestDto setEntity(final HttpEntity payload) {
        throw new NotImplementedException("Get per Http spec cannot have a payload");
    }

}
