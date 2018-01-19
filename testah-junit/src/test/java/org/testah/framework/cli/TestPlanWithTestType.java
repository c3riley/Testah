package org.testah.framework.cli;

import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(testType = TestType.AUTOMATED)
class TestPlanWithTestType {
    @TestCase()
    public void test1() {
    }

    @TestCase(testType = TestType.AUTOMATED)
    public void test2() {
    }
}