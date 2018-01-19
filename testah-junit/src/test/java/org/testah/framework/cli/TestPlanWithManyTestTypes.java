package org.testah.framework.cli;

import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(testType = TestType.AUTOMATED)
class TestPlanWithManyTestTypes {
    @TestCase()
    public void test1() {
    }

    @TestCase(testType = TestType.AUTOMATED)
    public void test2() {
    }

    @TestCase(testType = TestType.PENDING)
    public void test3() {
    }

    @TestCase(testType = TestType.MANUAL)
    public void test4() {
    }

    @TestCase(testType = TestType.RETIRE)
    public void test5() {
    }

    @TestCase(testType = TestType.DEFAULT)
    public void test6() {
    }
}