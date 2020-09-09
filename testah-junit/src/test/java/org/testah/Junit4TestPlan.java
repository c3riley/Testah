package org.testah;

import org.junit.Test;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan(name = "test plan for junit4 example", components = {"c2"}, platforms = {"p2"},
        devices = {"d2"}, testType = TestType.AUTOMATED, runTypes = "r2", description = "desc",
        relatedIds = "i2", relatedLinks = "http://www.testah.com", tags = "JUNIT_4")
public class Junit4TestPlan extends HttpTestPlan {

    @Test
    @TestCase(name = "test for junit4 example", components = {"c1"}, platforms = {"p1"},
            devices = {"d1"}, testType = TestType.AUTOMATED, runTypes = "r1", description = "desc",
            relatedIds = "i1", relatedLinks = "http://www.testah.com")
    public void test() {
        TS.asserts().isTrue(true);
    }

    @Test
    @TestCase(name = "test for junit4 example")
    public void test3() {
        TS.asserts().isTrue(true);
    }

    @Test
    public void test2() {
        TS.asserts().isTrue(true);
    }
}
