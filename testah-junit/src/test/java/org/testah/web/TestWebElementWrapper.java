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

    @TestCase
    @Test
    public void happy() {
        TS.asserts().isTrue(true);
    }

    @Ignore
    @TestCase
    @Test
    public void TestWebElementAsserts() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("enclosed-image"));
        e.assertAttributeExists("src");
        e.assertAttributeValue("src",
                "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif");
        e.assertFound();
    }

    @Ignore
    @TestCase
    @Test
    public void TestWebElementVerify() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("enclosed-image"));

        TS.asserts().isTrue(e.verifytAttributeValue("src",
                "https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif"));
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
        TS.browser().getWebElement(By.id("username")).assertFound().click().clearText()
                .assertAttributeValue("value", "").typeText("hello").assertAttributeValue("value", "hello")
                .assertTypeText("cool");
    }

    @Ignore
    @TestCase
    @Test
    public void TestWithin() {
        TS.browser().goToAndWaitForTitle(
                "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/xhtmlTest.html",
                "XHTML Test Page");
        TS.browser().getWebElement(By.cssSelector("div.content")).getElementWithIn(By.id("username")).assertFound()
                .assertTypeText("cool1");
    }

}
