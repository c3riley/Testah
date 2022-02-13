package org.testah;

import org.testah.framework.annotations.TestCaseJUnit5;

public class TestCaseClassJUnit5
{
    @TestCaseJUnit5(name = "test for junit5 example")
    public void testCaseWithoutTestPlan() {
        TS.asserts().isTrue(true);
    }
}
