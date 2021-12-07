package org.testah.driver.web.browser;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.cli.Params;

import java.io.IOException;

import static org.junit.Assert.*;

public class PhantomJsBrowserTest {

    @Test
    public void getDriverBinaryTest() throws IOException {
        Assume.assumeTrue("Only run on windows", Params.isWindows());
        PhantomJsBrowser phantomJsBrowser = new PhantomJsBrowser();
        phantomJsBrowser.getDriverBinary();
        phantomJsBrowser.start().getDriver().get("https://www.google.com");
        phantomJsBrowser.goToAndWaitForTitleToChange("https://www.google.com");
        phantomJsBrowser.close();
    }

}