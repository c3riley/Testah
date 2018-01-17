package org.testah.driver.http.requests;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpOptions;

public class OptionRequestDto extends AbstractRequestDto<OptionRequestDto> {

    public static final String methodName = "OPTIONS";

    public OptionRequestDto(final String uri) {
        super(new HttpOptions(uri), methodName);
    }

    @Override
    protected OptionRequestDto setEntity(final HttpEntity payload) {
        throw new NotImplementedException("Options per Http spec cannot have a payload");
    }
}
