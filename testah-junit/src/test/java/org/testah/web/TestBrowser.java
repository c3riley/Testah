package org.testah.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.client.enums.BrowserType;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.driver.web.browser.FirefoxBrowser;
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

    private final String baseUrl = "https://www.google.com/";
    private final String baseTitle = "Google";
    private final String cssSelectorPath = "input[value='Google Search']";

    @Before
    public void setup() {
        TS.browser().goTo(baseUrl);
    }

    @After
    public void tearDown() {
        TS.browser().close();
    }

    @TestCase
    @Test
    public void testPageTitle() {
        TS.browser().getWebElement(By.name("q"), 30).waitTillIsDisplayed();
        TS.browser().waitForTitle(baseTitle, 20);
        TS.asserts().equalsTo(baseTitle, TS.browser().getTitle());
        TS.browser().assertTitle(baseTitle);
    }

    @TestCase
    @Test
    public void testPageUtl() {
        TS.asserts().equalsTo(baseUrl, TS.browser().getUrl());
        TS.browser().assertUrl(baseUrl);
    }

    @TestCase
    @Test
    public void testScreenShot() {
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
    public void testGetDriver() {
        TS.asserts().notNull(TS.browser().getDriver());
        TS.asserts().equalsTo(baseTitle, TS.browser().getDriver().getTitle());
    }

    @TestCase
    @Test
    public void testGetJavaScriptValue() {
        final String rtn = TS.browser().getJavaScriptValue("return 'cool';");
        TS.asserts().equalsTo(rtn, "cool");
    }

    @TestCase
    @Test
    public void testElementWaitTime() {
        final int rtn = TS.browser().getElementWaitTime();
        TS.asserts().equalsTo(10, rtn);
        TS.browser().setElementWaitTime(2);
        TS.asserts().equalsTo(2, TS.browser().getElementWaitTime());
        TS.browser().setElementWaitTime(10);
    }

    @TestCase
    @Test
    public void testGetCapabilities() {
        TS.asserts().notNull(TS.browser().getCapabilities());
        TS.asserts().equalsTo(BrowserType.getBrowserType(TS.browser().getCapabilities().getBrowserName()),
            TS.params().getBrowser());
    }

    @TestCase
    @Test
    public void testWebElements() {
        final List<AbstractWebElementWrapper> list = TS.browser().getWebElements(By.cssSelector(cssSelectorPath));
        TS.asserts().notNull(list);
        TS.asserts().isGreaterThan("At least one element.",1, list.size());
        TS.asserts().notNull(list.get(0));
        TS.asserts().equalsTo(By.cssSelector(cssSelectorPath), list.get(0).getBy());
        TS.asserts().notNull(list.get(0).getDriverWebElement());
        TS.asserts().notNull("type value is not null", list.get(0).getAttribute("type"));
    }

    @TestCase
    @Test
    public void testWebElement() {
        final AbstractWebElementWrapper e = TS.browser().getWebElement(By.cssSelector(cssSelectorPath));
        TS.asserts().notNull(e);
        TS.asserts().equalsTo(By.cssSelector(cssSelectorPath), e.getBy());
        TS.asserts().notNull(e.getDriverWebElement());
        TS.asserts().notNull("type value is not null", e.getAttribute("type"));
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

    @Test
    @TestCase(name = "test3")
    public void testChrome() {

        final AbstractBrowser<GoogleChromeBrowser> b = new GoogleChromeBrowser().start();
        b.start().getDriver().get("http://www.google.com");
        b.goToAndWaitForTitleToChange("http://www.google.com");
        b.close();

    }

    @Test
    @TestCase(name = "test2", runTypes = "FIREFOX")
    public void testFirefox() {

        final FirefoxBrowser b = new FirefoxBrowser();
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
