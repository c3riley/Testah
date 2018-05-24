package org.testah.framework.cli.initialization;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

@TestPlan(components = {"TEST_COMPONENT"})
public class InitializationFailure {

    public final static boolean throwError = true;
    private final static String failure = getFailure();

    @TestCase()
    public void test1() {
    }

    @TestCase(components = {"TEST_COMPONENT"})
    public void test2() {
    }

    public static String getFailure() {
        if(throwError) {
            throw new RuntimeException("Test Not Loading");
        }
        return "Will not make it here";
    }
}