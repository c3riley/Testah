package org.testah.framework.cli;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(tags = {""})
class TestPlanWithTagEmptyString {

    @TestCase()
    public void test1() {
    }
}