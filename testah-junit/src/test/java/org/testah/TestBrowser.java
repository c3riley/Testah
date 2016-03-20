package org.testah;

import org.junit.Test;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestMeta(name = "testplan3")
public class TestBrowser extends BrowserTestPlan {

	@TestMeta()
	@Test
	public void TestPageTitle() {

		TS.asserts().equals("", TS.browser().getTitle());
		TS.browser().assertTitle("");
	}

}
