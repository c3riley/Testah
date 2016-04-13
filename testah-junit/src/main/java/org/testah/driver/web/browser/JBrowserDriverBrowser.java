package org.testah.driver.web.browser;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;

public class JBrowserDriverBrowser extends AbstractBrowser {

	public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
		return new JBrowserDriver(capabilities);
	}

	public AbstractBrowser getDriverBinay() {
		return this;
	}

	public AbstractBrowser startService() {
		return this;
	}

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

	public AbstractBrowser stopService() throws IOException {
		return this;
	}

}
