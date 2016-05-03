/*
#
# Copyright (c) 2014-2016 Cazena, Inc., as an unpublished work.
# This notice does not imply unrestricted or public access to these
# materials which are a trade secret of Cazena, Inc. or its
# subsidiaries or affiliates (together referred to as "Cazena"), and
# which may not be copied, reproduced, used, sold or transferred to any
# third party without Cazena's prior written consent.
#
# All rights reserved.
*/
package org.testah.web;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.driver.web.browser.FirefoxBrowser;
import org.testah.driver.web.browser.GoogleChromeBrowser;
import org.testah.driver.web.browser.PhantomJsBrowser;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestPlan
public class TestBrowser extends BrowserTestPlan {

	private final String baseUrl = "http://htmlpreview.github.io/?https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/clicks.html";
	private final String baseTitle = "clicks";

	@Before
	public void setup() {
		TS.browser().goTo(baseUrl);
		TS.util().pause(2000L, "Since using git redirect need to wait a little while");
	}

	@TestCase
	@Test
	public void TestPageTitle() {

		TS.asserts().equals(baseTitle, TS.browser().getTitle());
		TS.browser().assertTitle(baseTitle);
	}

	@TestCase
	@Test
	public void TestPageUtl() {
		TS.asserts().equals(baseUrl, TS.browser().getUrl());
		TS.browser().assertUrl(baseUrl);
	}

	@TestCase
	@Test
	public void TestScreenShot() {
		final String screenshot = TS.browser().takeScreenShot();
		final File f = new File(screenshot);
		TS.asserts().notNull(f);
		step("step 2");
		TS.asserts().isTrue(f.exists());
		step("step 3");
		TS.asserts().isFalse(f.isDirectory());
		step("step 4");
		TS.asserts().isTrue(screenshot.endsWith(".png"));

	}

	@TestCase
	@Test
	public void TestGetDriver() {
		TS.asserts().notNull(TS.browser().getDriver());
		TS.asserts().equals(baseTitle, TS.browser().getDriver().getTitle());
	}

	@TestCase
	@Test
	public void TestGetJavaScriptValue() {
		final String rtn = TS.browser().getJavaScriptValue("return 'cool';");
		TS.asserts().equals(rtn, "cool");
	}

	@TestCase
	@Test
	public void TestElementWaitTime() {
		final int rtn = TS.browser().getElementWaitTime();
		TS.asserts().equals(10, rtn);
		TS.browser().setElementWaitTime(2);
		TS.asserts().equals(2, TS.browser().getElementWaitTime());
		TS.browser().setElementWaitTime(10);
	}

	@TestCase
	@Test
	public void TestGetCapabilities() {
		TS.asserts().notNull(TS.browser().getCapabilities());
		TS.asserts().equals("firefox", TS.browser().getCapabilities().getBrowserName());
	}

	@TestCase
	@Test
	public void TestWebElements() {
		final List<AbstractWebElementWrapper> lst = TS.browser().getWebElements(By.id("enclosed-image"));
		TS.asserts().notNull(lst);
		TS.asserts().equals(1, lst.size());
		TS.asserts().notNull(lst.get(0));
		TS.asserts().equals(By.id("enclosed-image"), lst.get(0).getBy());
		TS.asserts().notNull(lst.get(0).getDriverWebElement());
		TS.asserts().equals("https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif",
				lst.get(0).getAttribute("src"));
	}

	@TestCase
	@Test
	public void TestWebElement() {
		final AbstractWebElementWrapper e = TS.browser().getWebElement(By.id("enclosed-image"));
		TS.asserts().notNull(e);
		TS.asserts().equals(By.id("enclosed-image"), e.getBy());
		TS.asserts().notNull(e.getDriverWebElement());
		TS.asserts().equals("https://raw.githubusercontent.com/SeleniumHQ/selenium/master/common/src/web/icon.gif",
				e.getAttribute("src"));
	}

	@Ignore
	@Test
	@TestCase(name = "test3")
	public void testPhantomJs() {

		final AbstractBrowser b = new PhantomJsBrowser();
		b.start();
		b.getDriver().get("http://www.google.com");
		b.goToAndWaitForTitleToChange("http://www.google.com");
		b.close();
		TS.asserts().isTrue(false);

	}

	@Ignore
	@Test
	@TestCase(name = "test3")
	public void testChrome() {

		final AbstractBrowser b = new GoogleChromeBrowser();
		b.start();
		b.getDriver().get("http://www.google.com");
		b.goToAndWaitForTitleToChange("http://www.google.com");
		b.close();

	}

	@Test
	@TestCase(name = "test2")
	public void test2() {

		final AbstractBrowser b = new FirefoxBrowser();
		b.start();
		b.getDriver().get("http://www.google.com");
		b.goToAndWaitForTitleToChange("http://www.google.com");
		b.close();

	}

	@Test
	@TestCase(name = "testScreenshot")
	public void testScreenshot() {

		TS.browser().goTo("http://www.google.com").getHtml();
	}

}
