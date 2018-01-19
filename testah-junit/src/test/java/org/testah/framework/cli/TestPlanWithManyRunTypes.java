package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(runTypes = {"TEST_RunType", "TEST_RunType1", "TEST_RunType2", "TEST_RunType3"})
class TestPlanWithManyRunTypes {
    @TestCase(runTypes = {})
    public void test1() {
    }

    @TestCase(runTypes = {""})
    public void test2() {
    }

    @TestCase(runTypes = {"TEST_RunType"})
    public void test3() {
    }

    @TestCase(runTypes = {"TEST_RunType", "TEST_RunType1", "TEST_RunType2", "TEST_RunType3"})
    public void test4() {
    }

    @TestCase()
    public void test5() {
    }
}