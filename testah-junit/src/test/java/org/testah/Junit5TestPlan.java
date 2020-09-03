package org.testah;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

@TestPlanJUnit5(name = "test plan for junit5 example", components = {"c2"}, platforms = {"p2"},
    devices = {"d2"}, testType = TestType.AUTOMATED, runTypes = "r2", description = "desc",
    relatedIds = "i2", relatedLinks = "http://www.testah.com", tags = "JUNIT_5")
public class Junit5TestPlan {

    static boolean didDoOnPassOccur = false;

    /**
     * Setup Method.
     */
    @BeforeEach
    public void setup() {
        TS.testSystem().setDoOnPassClosure((description) -> {
            didDoOnPassOccur = true;
        });
    }

    @AfterAll
    public static void tearDown() {
        TS.asserts().isTrue(didDoOnPassOccur);
    }

    @TestCaseJUnit5(name = "test for junit5 example", components = {"c1"}, platforms = {"p1"},
        devices = {"d1"}, testType = TestType.AUTOMATED, runTypes = "r1", description = "desc",
        relatedIds = "i1", relatedLinks = "http://www.testah.com")
    public void test() {
        TS.asserts().isTrue(true);
    }

    @Test
    public void test2() {
        TS.asserts().isTrue(true);
    }

}
