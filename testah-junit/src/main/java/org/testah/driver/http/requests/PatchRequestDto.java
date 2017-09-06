package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPatch;

public class PatchRequestDto extends AbstractRequestDto<PatchRequestDto> {

    public PatchRequestDto(final String uri) {
        super(new HttpPatch(uri), "PATCH");
    }

    public PatchRequestDto(final String uri, final String payload) {
        super(new HttpPatch(uri), "PATCH");
        setPayload(payload);
    }

    public PatchRequestDto(final String uri, final byte[] payload) {
        super(new HttpPatch(uri), "PATCH");
        setPayload(payload);
    }

    public PatchRequestDto(final String uri, final Object payload) {
        super(new HttpPatch(uri), "PATCH");
        setPayload(payload);
    }

    public PatchRequestDto(final String uri, final HttpEntity payload) {
        super(new HttpPatch(uri), "PATCH");
        setPayload(payload);
    }

    @Override
    protected PatchRequestDto setEntity(final HttpEntity payload) {
        ((HttpPatch) httpRequestBase).setEntity(payload);
        return this;
    }
}
