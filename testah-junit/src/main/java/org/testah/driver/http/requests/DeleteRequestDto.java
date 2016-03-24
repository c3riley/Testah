package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.testah.TS;

public class DeleteRequestDto extends AbstractRequestDto {

    public DeleteRequestDto(final String uri) {
        super(new HttpDelete(uri), "DELETE");
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
