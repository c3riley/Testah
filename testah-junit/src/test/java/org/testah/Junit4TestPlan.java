package org.testah;

import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan(tags = "JUNIT_4")
public class Junit4TestPlan extends HttpTestPlan {

    @Test
    @TestCase()
    public void test() {
        TS.asserts().isTrue(true);
    }

    @Test
    public void test2() {
        TS.asserts().isTrue(true);
    }


}
