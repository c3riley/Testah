package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(tags = {"TEST_Tag"})
class TestPlanWithTag {
    @TestCase()
    public void test1() {
    }

    @TestCase(tags = {"TEST_Tag"})
    public void test2() {
    }
}