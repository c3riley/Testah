package org.testah.driver.web.browser;

import org.junit.Test;

import static org.junit.Assert.*;

public class GoogleChromeBrowserTest {

    @Test
    public void happyPathTest() {
        GoogleChromeBrowser browser = new GoogleChromeBrowser();
        browser.getDriverBinary();
        browser.start().getDriver().get("https://www.google.com");
        browser.goToAndWaitForTitleToChange("https://www.google.com");
        browser.close();
    }
}