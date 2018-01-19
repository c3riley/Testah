package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(devices = {"TEST_Device", "TEST_Device1", "TEST_Device2", "TEST_Device3"})
class TestPlanWithManyDevices {
    @TestCase(devices = {})
    public void test1() {
    }

    @TestCase(devices = {""})
    public void test2() {
    }

    @TestCase(devices = {"TEST_Device"})
    public void test3() {
    }

    @TestCase(devices = {"TEST_Device", "TEST_Device1", "TEST_Device2", "TEST_Device3"})
    public void test4() {
    }

    @TestCase()
    public void test5() {
    }
}