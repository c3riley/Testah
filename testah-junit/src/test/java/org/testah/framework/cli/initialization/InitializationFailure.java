package org.testah.framework.cli.initialization;

import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;

/**
 * The type Initialization failure.
 */
@TestPlan(components = {"TEST_COMPONENT"})
public class InitializationFailure {

    /**
     * The constant throwError.
     */
    public static final boolean throwError = true;

    private static final String failure = getFailure();

    /**
     * Gets failure.
     *
     * @return the failure
     */
    public static String getFailure() {
        if (throwError) {
            throw new RuntimeException("Test Not Loading");
        }
        return "Will not make it here";
    }

    /**
     * Test 1.
     */
    @TestCase()
    public void test1() {
    }

    /**
     * Test 2.
     */
    @TestCase(components = {"TEST_COMPONENT"})
    public void test2() {
    }
}