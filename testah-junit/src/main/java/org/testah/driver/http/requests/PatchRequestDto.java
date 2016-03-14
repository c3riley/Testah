package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPatch;

public class PatchRequestDto extends AbstractRequestDto {

    public PatchRequestDto(final String uri) {
        super(new HttpPatch(uri));
    }
    
    public PatchRequestDto(final String uri, final String payload) {
        super(new HttpPatch(uri));
        setPayload(payload);
    }
    
    public PatchRequestDto(final String uri, final HttpEntity payload) {
        super(new HttpPatch(uri));
        setPayload(payload);
    }
    
}
