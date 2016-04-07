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

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;
import org.testah.framework.testPlan.TestConfiguration;

@ContextHierarchy({ @ContextConfiguration(classes = TestConfiguration.class) })
@TestPlan
public class TestHttp extends HttpTestPlan {

    @TestCase
    @Test
    public void t2() throws ClientProtocolException, IOException {
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

    @Ignore
    @TestCase
    @Test
    public void postWithEmptyString() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", "");
    }

    @Ignore
    @TestCase
    @Test
    public void postWithObject() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", new TestCaseDto());
    }
}
