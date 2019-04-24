package org.testah.framework.cli.requirerelatedids;

import org.junit.Test;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan(relatedIds = {"test-123"})
public class TestRequiredRelatedIds extends HttpTestPlan {

    @TestCase
    @Test
    public void testFilterTestCaseTestType() {

    }

    @TestCase(testType = TestType.PENDING)
    @Test
    public void testShouldGetFilteredOut() {

    }

    @TestCase(relatedIds = {"test-123a"})
    @Test
    public void testFilterTestPlanTestType() {

    }

}