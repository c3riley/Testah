package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
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

    public AbstractRequestDto setPayload(String payload) {
        try {
            if (null == payload) {
                payload = "";
            }
            return setPayload(new StringEntity(payload));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractRequestDto setPayload(final Object payload) {
        try {
            if (null == payload) {
                TS.log().warn("payload was null so setting to empty string");
                return setPayload(new StringEntity(""));
            } else {
                return setPayload(new StringEntity(TS.util().toJson(payload)));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractRequestDto setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                ((HttpPatch) httpRequestBase).setEntity(payload);
            } else {
                TS.log().warn("payload was null so setting to empty string");
                ((HttpPatch) httpRequestBase).setEntity(new StringEntity(""));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    protected AbstractRequestDto getSelf() {
        return this;
    }

}
