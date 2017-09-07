package org.testah.web;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.driver.web.browser.FirefoxGeckoBrowser;
import org.testah.driver.web.browser.GoogleChromeBrowser;
import org.testah.driver.web.browser.PhantomJsBrowser;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

import java.io.File;
import java.util.List;

@TestPlan
public class TestBrowser extends BrowserTestPlan {

    private final String baseUrl = "http://www.google.com/";
    private final String baseTitle = "Google";

    @Before
    public void setup() {
        TS.browser().goTo(baseUrl);
    }

    @TestCase
    @Test
    public void TestPageTitle() {
        TS.browser().getWebElement(By.name("q"), 30).waitTillIsDisplayed();
        TS.browser().waitForTitle(baseTitle, 20);
        TS.asserts().equalsTo(baseTitle, TS.browser().getTitle());
        TS.browser().assertTitle(baseTitle);
    }

    @TestCase
    @Test
    public void TestPageUtl() {
        TS.asserts().equalsTo(baseUrl, TS.browser().getUrl());
        TS.browser().assertUrl(baseUrl);
    }

    @TestCase
    @Test
    public void TestScreenShot() {
        final String screenshot = TS.browser().takeScreenShot();
        final File f = new File(TS.params().getOutput(), screenshot);
        TS.asserts().notNull(f);
        step("step 2");
        TS.asserts().isTrue(f.exists());
        step("step 3");
        TS.asserts().isFalse(f.isDirectory());
        step("step 4");
        TS.asserts().isTrue(screenshot.endsWith(".png"));

    }

    @TestCase
    @Test
    public void TestGetDriver() {
        TS.asserts().notNull(TS.browser().getDriver());
        TS.asserts().equalsTo(baseTitle, TS.browser().getDriver().getTitle());
    }

    @TestCase
    @Test
    public void TestGetJavaScriptValue() {
        final String rtn = TS.browser().getJavaScriptValue("return 'cool';");
        TS.asserts().equalsTo(rtn, "cool");
    }

    @TestCase
    @Test
    public void TestElementWaitTime() {
        final int rtn = TS.browser().getElementWaitTime();
        TS.asserts().equalsTo(10, rtn);
        TS.browser().setElementWaitTime(2);
        TS.asserts().equalsTo(2, TS.browser().getElementWaitTime());
        TS.browser().setElementWaitTime(10);
    }

    @TestCase
    @Test
    public void TestGetCapabilities() {
        TS.asserts().notNull(TS.browser().getCapabilities());
        TS.asserts().equalsTo("firefox", TS.browser().getCapabilities().getBrowserName());
    }

    @TestCase
    @Test
    public void TestWebElements() {
        final List<AbstractWebElementWrapper> lst = TS.browser().getWebElements(By.id("hplogo"));
        TS.asserts().notNull(lst);
        TS.asserts().equalsTo(1, lst.size());
        TS.asserts().notNull(lst.get(0));
        TS.asserts().equalsTo(By.id("hplogo"), lst.get(0).getBy());
        TS.asserts().notNull(lst.get(0).getDriverWebElement());
        TS.asserts().notNull("src value is not null",
                lst.get(0).getAttribute("src"));
    }

    @TestCase
    @Test
    public void TestWebElement() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("hplogo"));
        TS.asserts().notNull(e);
        TS.asserts().equalsTo(By.id("hplogo"), e.getBy());
        TS.asserts().notNull(e.getDriverWebElement());
        TS.asserts().notNull("src value is not null",
                e.getAttribute("src"));
    }

    @Ignore
    @Test
    @TestCase(name = "test3")
    public void testPhantomJs() {

        final PhantomJsBrowser b = new PhantomJsBrowser();
        b.start().getDriver().get("http://www.google.com");
        b.goToAndWaitForTitleToChange("http://www.google.com");
        b.close();
        TS.asserts().isTrue(false);

    }

    @Ignore
    @Test
    @TestCase(name = "test3")
    public void testChrome() {

        final AbstractBrowser<GoogleChromeBrowser> b = new GoogleChromeBrowser().start();
        b.start().getDriver().get("http://www.google.com");
        b.goToAndWaitForTitleToChange("http://www.google.com");
        b.close();

    }

    @Test
    @TestCase(name = "test2")
    public void test2() {

        final FirefoxGeckoBrowser b = new FirefoxGeckoBrowser();
        b.start().getDriver().get("http://www.google.com");
        b.goToAndWaitForTitleToChange("http://www.google.com");
        b.close();

    }

    @Test
    @TestCase(name = "testScreenshot")
    public void testScreenshot() {

        TS.browser().goTo("http://www.google.com").getHtml();
    }

}
