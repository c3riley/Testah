package org.testah.driver.web.browser;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;


/**
 * The Class JBrowserDriverBrowser.
 */
public class JBrowserDriverBrowser extends AbstractBrowser {

	/* (non-Javadoc)
	 * @see org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.selenium.remote.DesiredCapabilities)
	 */
	public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
		return new JBrowserDriver(capabilities);
	}

	/* (non-Javadoc)
	 * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinay()
	 */
	public AbstractBrowser getDriverBinay() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.testah.driver.web.browser.AbstractBrowser#startService()
	 */
	public AbstractBrowser startService() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.testah.driver.web.browser.AbstractBrowser#createCapabilities()
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

	/* (non-Javadoc)
	 * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
	 */
	public AbstractBrowser stopService() throws IOException {
		return this;
	}

}
