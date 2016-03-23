package org.testah;

import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestPlan(name = "testplan3")
public class TestBrowser extends BrowserTestPlan {

    @TestCase()
    @Test
    public void TestPageTitle() {
        step("");
        TS.asserts().equals("45", TS.browser().getTitle());
        TS.browser().assertTitle("");
    }

}
