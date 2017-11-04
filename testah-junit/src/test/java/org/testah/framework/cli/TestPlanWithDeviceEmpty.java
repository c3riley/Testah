package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(devices = {})
class TestPlanWithDeviceEmpty {
    @TestCase()
    public void test1() {
    }
}