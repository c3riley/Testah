package org.testah.driver.web.browser;

import org.junit.Assume;
import org.junit.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testah.framework.cli.Params;

import static org.junit.Assert.*;

public class FirefoxBrowserTest {

    @EnabledOnOs(OS.WINDOWS)
    @Test
    public void happyPathTest() {
        Assume.assumeTrue("Only run on windows", Params.isWindows());
        FirefoxBrowser browser = new FirefoxBrowser();
        browser.getDriverBinary();
        browser.start().getDriver().get("https://www.google.com");
        browser.goToAndWaitForTitleToChange("https://www.google.com");
        browser.close();
    }
}