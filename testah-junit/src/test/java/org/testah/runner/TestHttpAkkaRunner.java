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
package org.testah.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;

public class TestHttpAkkaRunner {

    @Ignore
    @Test
    public void test() {
        final HttpAkkaRunner a = new HttpAkkaRunner();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }

}
