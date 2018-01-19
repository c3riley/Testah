package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(runTypes = {"TEST_RunType"})
class TestPlanWithRunType {
    @TestCase()
    public void test1() {
    }

    @TestCase(runTypes = {"TEST_RunType"})
    public void test2() {
    }
}