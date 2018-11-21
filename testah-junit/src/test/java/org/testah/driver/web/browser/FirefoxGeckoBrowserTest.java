package org.testah.driver.web.browser;

import org.junit.Assume;
import org.junit.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testah.TS;
import org.testah.framework.cli.Params;

import static org.junit.Assert.*;

public class FirefoxGeckoBrowserTest {

    @EnabledOnOs(OS.WINDOWS)
    @Test
    public void happyPathTest() {
        Assume.assumeTrue("Only run on windows", Params.isWindows());
        FirefoxGeckoBrowser browser = new FirefoxGeckoBrowser();
        browser.getDriverBinary();
        browser.start().getDriver().get("http://www.google.com");
        browser.goToAndWaitForTitleToChange("http://www.google.com");
        browser.close();
    }

}