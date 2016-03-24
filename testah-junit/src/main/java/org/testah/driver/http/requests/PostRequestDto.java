package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.testah.TS;

public class PostRequestDto extends AbstractRequestDto {

    public PostRequestDto(final String uri) {
        super(new HttpPost(uri), "POST");
    }

    public PostRequestDto(final String uri, final String payload) {
        super(new HttpPost(uri), "POST");
        setPayload(payload);
    }

    public PostRequestDto(final String uri, final Object payload) {
        super(new HttpPost(uri), "POST");
        setPayload(payload);
    }

    public PostRequestDto(final String uri, final HttpEntity payload) {
        super(new HttpPost(uri), "POST");
        setPayload(payload);
    }

    public AbstractRequestDto setPayload(final String payload) {
        try {
            return setPayload(new StringEntity(payload));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractRequestDto setPayload(final Object payload) {
        try {
            return setPayload(new StringEntity(TS.util().toJson(payload)));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractRequestDto setPayload(final HttpEntity payload) {
        try {
            if (null != payload) {
                httpEntity = payload;
                ((HttpPost) httpRequestBase).setEntity(payload);
            } else {
                TS.log().warn("payload was null so ignoring");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

}
