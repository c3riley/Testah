package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(tags = {"TEST_Tag", "TEST_Tag1", "TEST_Tag2", "TEST_Tag3"})
class TestPlanWithManyTags {
    @TestCase(tags = {})
    public void test1() {
    }

    @TestCase(tags = {""})
    public void test2() {
    }

    @TestCase(tags = {"TEST_Tag"})
    public void test3() {
    }

    @TestCase(tags = {"TEST_Tag", "TEST_Tag1", "TEST_Tag2", "TEST_Tag3"})
    public void test4() {
    }

    @TestCase()
    public void test5() {
    }
}