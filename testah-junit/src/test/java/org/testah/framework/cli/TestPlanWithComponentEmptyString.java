package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(components = {""})
class TestPlanWithComponentEmptyString {

    @TestCase()
    public void test1() {
    }
}