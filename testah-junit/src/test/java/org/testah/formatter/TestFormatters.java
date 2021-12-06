package org.testah.formatter;

import org.junit.After;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.BrowserTestPlan;

@TestPlan(
    platforms = "SimpleTestPlatforms",
    components = "SimpleTestComponents",
    devices = "SimpleTestDevices",
    relatedIds = "TEST_ID",
    relatedLinks = "HTTP_TEST_LINK",
    tags = "TEST_TAG",
    runTypes = "TEST_SVR",
    name = "SERVICE_TEST",
    description = "THIS IS A JUST TEST")
public class TestFormatters extends BrowserTestPlan {

    @Test
    @TestCase(description = "THIS IS A JUST TEST")
    public void test1() {
        TS.browser().goTo("http://www.google.com");
        TS.asserts().isFalse(false);
    }

    //    @KnownProblem(linkedIds = "test")
    //    @Test(expected = AssertionError.class)
    //    @TestCase(description = "THIS IS A JUST TEST")
    //    public void test2() {
    //        TS.browser().goTo("http://www.google.com");
    //        TS.asserts().isFalse(true);
    //    }

    @After
    public void tearDown() {
        TS.browser().close();
    }

}
