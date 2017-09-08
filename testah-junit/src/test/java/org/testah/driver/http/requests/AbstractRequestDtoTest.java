package org.testah.driver.http.requests;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.dto.base.TestAbstractDtoBaseDto;

public class AbstractRequestDtoTest {

    public static final String url = "http://www.google.com";
    public static final String payloadString = "This is a payload String";

    @Test
    public void testPostRequestDto() {
        final PostRequestDto post = new PostRequestDto(url);
        post.setPayload(payloadString);
        Assert.assertEquals(payloadString, post.getPayloadString());
        post.setPayload(new TestAbstractDtoBaseDto().setValue(payloadString));
        Assert.assertTrue(post.getPayloadString().contains("This is a payload"));
    }

}
