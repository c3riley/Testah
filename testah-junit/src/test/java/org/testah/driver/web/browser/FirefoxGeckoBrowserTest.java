package org.testah.driver.web.browser;

import org.junit.Test;
import org.testah.TS;

import static org.junit.Assert.*;

public class FirefoxGeckoBrowserTest {

    @Test
    public void happyPathTest() {
        FirefoxGeckoBrowser browser = new FirefoxGeckoBrowser();
        browser.getDriverBinary();
        browser.start().getDriver().get("http://www.google.com");
        browser.goToAndWaitForTitleToChange("http://www.google.com");
        browser.close();
    }

}