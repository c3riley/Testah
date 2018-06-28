package org.testah.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

@KnownProblem
@TestPlan
public class TestWebElementWrapper extends BrowserTestPlan {

    private static final String SRC = "src";
    private static final String baseUrl =
            "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/clicks.html";

    /**
     * Test setup.
     */
    @Before
    public void setup() {
        TS.browser().goTo(baseUrl);
        TS.util().pause(2000L, "Since using git redirect need to wait a little while");

    }

    @TestCase
    @Test
    public void happy() {
        TS.asserts().isTrue(true);
    }

    @Ignore
    @TestCase
    @Test
    public void testWebElementAsserts() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("enclosed-image"));
        e.assertAttributeExists(SRC);
        e.assertAttributeValue(SRC,
                "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif");
        e.assertFound();
    }

    @Ignore
    @TestCase
    @Test
    public void testWebElementVerify() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("enclosed-image"));

        TS.asserts().isTrue(e.verifytAttributeValue(SRC,
                "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif"));
        TS.asserts().isTrue(e.verifyFound());
        TS.asserts().isFalse(e.verifyNotfound());
        TS.asserts().isFalse(e.verifytAttributeValue(SRC, "shouldNotFind"));
    }

    @Ignore
    @TestCase
    @Test
    public void testWebElementWithin() {
        TS.browser().goToAndWaitForTitle(
                "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/xhtmlTest.html",
                "XHTML Test Page");
        TS.browser().getWebElement(By.id("username")).assertFound().click().clearText()
                .assertAttributeValue("value", "").typeText("hello").assertAttributeValue("value", "hello")
                .assertTypeText("cool");
    }

    @Ignore
    @TestCase
    @Test
    public void testWithin() {
        TS.browser().goToAndWaitForTitle(
                "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/xhtmlTest.html",
                "XHTML Test Page");
        TS.browser().getWebElement(By.cssSelector("div.content")).getElementWithIn(By.id("username")).assertFound()
                .assertTypeText("cool1");
    }

}
