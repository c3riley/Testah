package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(runTypes = {""})
class TestPlanWithRunTypeEmptyString {

    @TestCase()
    public void test1() {
    }
}