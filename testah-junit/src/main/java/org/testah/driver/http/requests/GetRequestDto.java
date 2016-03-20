package org.testah.driver.http.requests;

import org.apache.http.client.methods.HttpGet;

public class GetRequestDto extends AbstractRequestDto {

	public GetRequestDto(final String uri) {
		super(new HttpGet(uri), "GET");
	}

}
