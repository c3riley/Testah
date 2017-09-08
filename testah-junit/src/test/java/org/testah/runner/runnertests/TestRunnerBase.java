package org.testah.runner.runnertests;

import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.testPlan.HttpTestPlan;

public class TestRunnerBase extends HttpTestPlan {

    @Test
    @TestCase
    public void testcasePass() {
        TS.asserts().isTrue("before", true);
        TS.util().pause(20L);
        TS.asserts().isTrue("after", true);
    }

    @Test
    @TestCase
    public void testcaseFail() {
        TS.asserts().isTrue("before", true);
        TS.util().pause(20L);
        TS.asserts().isTrue("after", true);
    }

    @Test
    @TestCase
    public void testcaseIgnore() {
        TS.util().pause(20L);
    }
}
