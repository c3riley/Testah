package org.testah.driver.web.browser;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

/**
 * The Class JBrowserDriverBrowser.
 */
public class JBrowserDriverBrowser extends AbstractBrowser<JBrowserDriverBrowser> {

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinary()
     */
    public JBrowserDriverBrowser getDriverBinary() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#startService()
     */
    public JBrowserDriverBrowser startService() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.selenium.remote.DesiredCapabilities)
     */
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        return new JBrowserDriver(capabilities);
    }

    /**
     * create Capabilities.
     *
     * @return DesiredCapabilities
     */
    public DesiredCapabilities createCapabilities() {
        final Settings.Builder builder = new Settings.Builder();

        builder.saveAttachments(true);
        builder.headless(true);
        builder.javascript(true);
        builder.traceConsole(true);
        builder.saveMedia(true);
        builder.warnConsole(true);
        return (DesiredCapabilities) builder.buildCapabilities();

    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
     */
    public JBrowserDriverBrowser stopService() throws IOException {
        return this;
    }

    @Override
    public AbstractBrowser<JBrowserDriverBrowser> logBrowserInfo() {
        // TODO Auto-generated method stub
        return null;
    }

}
