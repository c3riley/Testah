package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(components = {"TEST_COMPONENT"})
class TestPlanWithComponent {
    @TestCase()
    public void test1() {
    }

    @TestCase(components = {"TEST_COMPONENT"})
    public void test2() {
    }
}