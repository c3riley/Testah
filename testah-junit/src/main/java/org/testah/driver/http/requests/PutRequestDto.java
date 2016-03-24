package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.testah.TS;

public class PutRequestDto extends AbstractRequestDto {

    public PutRequestDto(final String uri) {
        super(new HttpPut(uri), "PUT");
    }

    public PutRequestDto(final String uri, final String payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    public PutRequestDto(final String uri, final Object payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    public PutRequestDto(final String uri, final HttpEntity payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    public AbstractRequestDto setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                ((HttpPut) httpRequestBase).setEntity(payload);
            } else {
                TS.log().warn("payload was null so ignoring");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

}
