package org.testah;

import org.junit.Test;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.driver.web.browser.FirefoxBrowser;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestMeta(name = "testplan3")
public class TestBrowser extends BrowserTestPlan {

	@Test
	@TestMeta(name = "test3")
	public void test3() {

		final AbstractBrowser b = new FirefoxBrowser();
		b.start();
		b.getDriver().get("http://www.google.com");

		b.goToAndWaitForTitle("http://www.google.com");
		b.getDriver().close();

	}

	@Test
	@TestMeta(name = "test2")
	public void test2() {

		final AbstractBrowser b = new FirefoxBrowser();
		b.start();
		b.getDriver().get("http://www.google.com");

		b.goToAndWaitForTitle("http://www.google.com");
		b.getDriver().close();

	}

}
