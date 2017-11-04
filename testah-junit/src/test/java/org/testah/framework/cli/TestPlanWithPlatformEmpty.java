package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(platforms = {})
class TestPlanWithPlatformEmpty {
    @TestCase()
    public void test1() {
    }
}