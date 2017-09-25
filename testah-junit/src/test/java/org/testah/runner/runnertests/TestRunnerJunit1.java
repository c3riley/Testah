package org.testah.runner.runnertests;

import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan
public class TestRunnerJunit1 {

    @TestCase
    @Test
    public void testcasePass() {
        TS.asserts().isTrue("before", true);
        TS.util().pause(20L);
        TS.asserts().isTrue("after", true);
    }

    @Test
    public void testcaseFail() {
        TS.asserts().isTrue("before", true);
        TS.util().pause(20L);
        TS.asserts().isTrue("after", true);
    }

    @Test
    public void testcaseIgnore() {
        TS.util().pause(20L);
    }
}
