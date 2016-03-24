package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPatch;
import org.testah.TS;

public class PatchRequestDto extends AbstractRequestDto {

    public PatchRequestDto(final String uri) {
        super(new HttpPatch(uri), "PATCH");
    }

    public PatchRequestDto(final String uri, final String payload) {
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

    public AbstractRequestDto setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                ((HttpPatch) httpRequestBase).setEntity(payload);
            } else {
                TS.log().warn("payload was null so ignoring");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

}
