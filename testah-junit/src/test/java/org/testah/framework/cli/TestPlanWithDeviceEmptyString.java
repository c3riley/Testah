package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(devices = {""})
class TestPlanWithDeviceEmptyString {

    @TestCase()
    public void test1() {
    }
}