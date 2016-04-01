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
package org.testah.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestPlan
public class TestWebElementWrapper extends BrowserTestPlan {

    private final String baseUrl = "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/clicks.html";

    @Before
    public void setup() {
        TS.browser().goTo(baseUrl);
        TS.util().pause(2000L, "Since using git redirect need to wait a little while");

    }

    @Ignore
    @TestCase
    @Test
    public void TestWebElementAsserts() {
        final AbstractWebElementWrapper e = TS.browser().getWebelement(By.id("enclosed-image"));
        e.assertAttributeExists("src");
        e.assertAttributeValue("src", "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif");
        e.assertFound();
    }

    @Ignore
    @TestCase
    @Test
    public void TestWebElementVerify() {
        final AbstractWebElementWrapper e = TS.browser().getWebelement(By.id("enclosed-image"));

        TS.asserts()
                .isTrue(e.verifytAttributeValue("src", "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif"));
        TS.asserts().isTrue(e.verifyFound());
        TS.asserts().isFalse(e.verifyNotfound());
        TS.asserts().isFalse(e.verifytAttributeValue("src", "shouldNotFind"));
    }

    @Ignore
    @TestCase
    @Test
    public void TestWebElementWithin() {
        TS.browser().goToAndWaitForTitle(
                "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/xhtmlTest.html",
                "XHTML Test Page");
        TS.browser().getWebelement(By.id("username")).assertFound().click().clearText().assertAttributeValue("value", "").typeText("hello")
                .assertAttributeValue("value", "hello").assertTypeText("cool");
    }

    @Ignore
    @TestCase
    @Test
    public void TestWithin() {
        TS.browser().goToAndWaitForTitle(
                "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/xhtmlTest.html",
                "XHTML Test Page");
        TS.browser().getWebelement(By.cssSelector("div.content")).getElementWithIn(By.id("username")).assertFound().assertTypeText("cool1");
    }

}
