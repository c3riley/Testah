package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;

/**
 * The type Put request dto.
 */
public class PutRequestDto extends AbstractRequestDto<PutRequestDto> {

    /**
     * Instantiates a new Put request dto.
     *
     * @param uri the uri
     */
    public PutRequestDto(final String uri) {
        super(new HttpPut(uri), "PUT");
    }

    /**
     * Instantiates a new Put request dto.
     *
     * @param uri     the uri
     * @param payload the payload
     */
    public PutRequestDto(final String uri, final String payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    /**
     * Instantiates a new Put request dto.
     *
     * @param uri     the uri
     * @param payload the payload
     */
    public PutRequestDto(final String uri, final byte[] payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    /**
     * Instantiates a new Put request dto.
     *
     * @param uri     the uri
     * @param payload the payload
     */
    public PutRequestDto(final String uri, final Object payload) {
        super(new HttpPut(uri), "PUT");
        if (payload instanceof byte[]) {
            this.setUpload((byte[]) payload);
        } else {
            setPayload(payload);
        }
    }

    /**
     * Instantiates a new Put request dto.
     *
     * @param uri     the uri
     * @param payload the payload
     */
    public PutRequestDto(final String uri, final HttpEntity payload) {
        super(new HttpPut(uri), "PUT");
        setPayload(payload);
    }

    @Override
    protected PutRequestDto setEntity(final HttpEntity payload) {
        ((HttpPut) httpRequestBase).setEntity(payload);
        return this;
    }
}
