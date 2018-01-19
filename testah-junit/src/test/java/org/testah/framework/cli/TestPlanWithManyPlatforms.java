package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(platforms = {"TEST_Platform", "TEST_Platform1", "TEST_Platform2", "TEST_Platform3"})
class TestPlanWithManyPlatforms {
    @TestCase(platforms = {})
    public void test1() {
    }

    @TestCase(platforms = {""})
    public void test2() {
    }

    @TestCase(platforms = {"TEST_Platform"})
    public void test3() {
    }

    @TestCase(platforms = {"TEST_Platform", "TEST_Platform1", "TEST_Platform2", "TEST_Platform3"})
    public void test4() {
    }

    @TestCase()
    public void test5() {
    }
}