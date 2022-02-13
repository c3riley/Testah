package org.testah;


import org.testah.framework.annotations.TestCase;

public class TestCaseClassJUnit4
{
    @TestCase(name = "test for junit4 example")
    public void testCaseWithoutTestPlan() {
        TS.asserts().isTrue(true);
    }
}
