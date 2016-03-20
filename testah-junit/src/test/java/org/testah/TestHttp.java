package org.testah;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.testPlan.HttpTestPlan;

@TestMeta
public class TestHttp extends HttpTestPlan {

	@TestMeta
	@Test
	public void t2() throws ClientProtocolException, IOException {
		final HttpWrapperV1 http = new HttpWrapperV1();
		http.setHttpClient().preformRequestWithAssert(new GetRequestDto("http://www.google.com")).print();
		http.setHttpClient().preformRequestWithAssert(new GetRequestDto("http://www.google.com")).printStatus();

	}
}
