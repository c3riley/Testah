package org.testah.web;

import org.junit.Ignore;
import org.junit.Test;
import org.testah.driver.web.browser.JBrowserDriverBrowser;

public class JBrowserDriverTest {

    @Ignore
    @Test
    public void test() {
        final JBrowserDriverBrowser j = new JBrowserDriverBrowser();
        j.start();
        j.goTo("http://www.google.com");
        j.assertTitle("Google");
    }

}
