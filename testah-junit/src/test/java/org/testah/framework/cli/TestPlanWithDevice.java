package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(devices = {"TEST_Device"})
class TestPlanWithDevice {
    @TestCase()
    public void test1() {
    }

    @TestCase(devices = {"TEST_Device"})
    public void test2() {
    }
}