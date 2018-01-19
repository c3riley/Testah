package org.testah.framework.cli;

import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(testType = TestType.RETIRE)
class TestPlanWithTestTypeRetire {
    @TestCase()
    public void test1() {
    }
}