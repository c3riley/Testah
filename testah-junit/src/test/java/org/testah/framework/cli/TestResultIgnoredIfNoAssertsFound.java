package org.testah.framework.cli;

import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

import java.io.IOException;

@TestPlan
public class TestResultIgnoredIfNoAssertsFound extends HttpTestPlan {

    private boolean expectPass = false;

    @TestCase
    @Test
    public void testResultIgnoredIfNoAssertsFoundTrue() throws IOException {
        TS.params().setResultIgnoredIfNoAssertsFound(true);
    }

    @TestCase
    @Test
    public void testResultIgnoredIfNoAssertsFoundFalse() throws IOException {
        TS.params().setResultIgnoredIfNoAssertsFound(false);
        expectPass = true;
    }

    @Override
    public void doOnPass() {
        if (!expectPass) {
            throw new AssertionError("Didn't expect test to show as passed!");
        }
    }

}
