package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(components = {"TEST_COMPONENT", "TEST_COMPONENT1", "TEST_COMPONENT2", "TEST_COMPONENT3"})
class TestPlanWithManyComponents {
    @TestCase(components = {})
    public void test1() {
    }

    @TestCase(components = {""})
    public void test2() {
    }

    @TestCase(components = {"TEST_COMPONENT"})
    public void test3() {
    }

    @TestCase(components = {"TEST_COMPONENT", "TEST_COMPONENT1", "TEST_COMPONENT2", "TEST_COMPONENT3"})
    public void test4() {
    }

    @TestCase()
    public void test5() {
    }
}