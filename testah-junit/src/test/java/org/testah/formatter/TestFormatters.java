package org.testah.formatter;

import org.junit.Ignore;
import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan(platforms = "SimpleTestPlatforms", components = "SimpleTestComponents", devices = "SimpleTestDevicess",
        relatedIds = "TEST_ID", relatedLinks = "HTTP_TEST_LINK", tags = "TEST_TAG", runTypes = "TEST_SVR",
        name = "SERVICE_TEST", description = "THIS IS A JUST TEST")
public class TestFormatters extends HttpTestPlan {

    @Test
    @TestCase(description = "THIS IS A JUST TEST")
    public void test1() {

    }

    @Ignore
    @Test
    @TestCase(description = "THIS IS A JUST TEST")
    public void test2() {

    }

}
