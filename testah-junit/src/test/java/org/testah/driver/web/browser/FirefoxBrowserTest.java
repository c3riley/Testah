package org.testah.driver.web.browser;

import org.junit.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.Assert.*;

public class FirefoxBrowserTest {

    @EnabledOnOs(OS.WINDOWS)
    @Test
    public void happyPathTest() {
        FirefoxBrowser browser = new FirefoxBrowser();
        browser.getDriverBinary();
        browser.start().getDriver().get("http://www.google.com");
        browser.goToAndWaitForTitleToChange("http://www.google.com");
        browser.close();
    }
}