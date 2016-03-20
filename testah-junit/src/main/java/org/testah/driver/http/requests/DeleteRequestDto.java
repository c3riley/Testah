package org.testah.driver.http.requests;

import org.apache.http.client.methods.HttpDelete;

public class DeleteRequestDto extends AbstractRequestDto {

	public DeleteRequestDto(final String uri) {
		super(new HttpDelete(uri), "DELETE");
	}

}
