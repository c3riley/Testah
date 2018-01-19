package org.testah.framework;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

@TestPlan
public class TestFailingInBeforeMethod extends HttpTestPlan {

    @Before
    public void setUp() {
        throw new RuntimeException("Failed in Before");
    }

    @Ignore
    @TestCase
    @Test
    public void test() {
        TS.asserts().isTrue(true);
    }
}
