/*
#
# Copyright (c) 2014-2016 Cazena, Inc., as an unpublished work.
# This notice does not imply unrestricted or public access to these
# materials which are a trade secret of Cazena, Inc. or its
# subsidiaries or affiliates (together referred to as "Cazena"), and
# which may not be copied, reproduced, used, sold or transferred to any
# third party without Cazena's prior written consent.
#
# All rights reserved.
*/
package org.testah.http;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;
import org.testah.framework.testPlan.TestConfiguration;

@ContextHierarchy({ @ContextConfiguration(classes = TestConfiguration.class) })
@TestPlan(description = "test Http")
public class TestHttp extends HttpTestPlan {

	@TestCase
	@Test
	public void testWith2Gets() throws ClientProtocolException, IOException {
		step("Got to google");
		final HttpWrapperV1 http = new HttpWrapperV1();
		http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.google.com"));
		step("go to google again");
		http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.google.com"));

	}

	@Ignore
	@TestCase(tags = "test")
	@Test()
	public void postWithNUll() throws ClientProtocolException, IOException {
		TS.http().doPost("http://www.google.com", null);
	}

	@TestCase
	@Test
	public void downloadAndUnZip() throws ClientProtocolException, IOException {
		final ResponseDto dto = TS.http().doGet(
				"https://github.com/c3riley/maven-repository/raw/master/org/testah/testah-junit/0.0.1/testah-junit-0.0.1.jar");
		final File jar = dto.saveToFile(new File(TS.params().getOutput(), "test.jar"));
		TS.asserts().isTrue(jar.exists());
		final Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.JAR);
		archiver.extract(jar, jar.getParentFile());
	}

	@Ignore
	@TestCase
	@Test
	public void postWithObject() throws ClientProtocolException, IOException {
		TS.http().doPost("http://www.google.com", new TestCaseDto());
	}
}
