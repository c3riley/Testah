package org.testah.driver.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

public class PostRequestDto extends AbstractRequestDto {

	public PostRequestDto(final String uri) {
		super(new HttpPost(uri), "POST");
	}

	public PostRequestDto(final String uri, final String payload) {
		super(new HttpPost(uri), "POST");
		setPayload(payload);
	}

	public PostRequestDto(final String uri, final HttpEntity payload) {
		super(new HttpPost(uri), "POST");
		setPayload(payload);
	}

}
