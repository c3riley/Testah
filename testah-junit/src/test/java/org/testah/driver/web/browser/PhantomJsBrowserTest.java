package org.testah.driver.web.browser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import java.io.IOException;

import static org.junit.Assert.*;

public class PhantomJsBrowserTest {

    @Test
    public void getDriverBinaryTest() throws IOException {
        PhantomJsBrowser phantomJsBrowser = new PhantomJsBrowser();
        phantomJsBrowser.getDriverBinary();
        phantomJsBrowser.start().getDriver().get("http://www.google.com");
        phantomJsBrowser.goToAndWaitForTitleToChange("http://www.google.com");
        phantomJsBrowser.close();
    }

}