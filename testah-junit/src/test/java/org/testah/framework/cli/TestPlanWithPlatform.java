package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(platforms = {"TEST_Platform"})
class TestPlanWithPlatform {
    @TestCase()
    public void test1() {
    }

    @TestCase(platforms = {"TEST_Platform"})
    public void test2() {
    }
}