package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;

/**
 * The type Assert number.
 *
 * @param <T> the type parameter
 */
public class AssertNumber<T extends Number & Comparable<T>> extends AbstractAssertBase<AssertNumber, T> {

    /**
     * Instantiates a new Assert number.
     *
     * @param actual the actual
     */
    public AssertNumber(T actual) {
        super(actual);
    }

    /**
     * Instantiates a new Assert number.
     *
     * @param actual  the actual
     * @param asserts the asserts
     */
    public AssertNumber(T actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    @Override
    public AssertNumber<T> equalsTo(T expected) {
        return equalsTo(expected, 0);
    }

    /**
     * Equals to assert number.
     *
     * @param expectedValue the expected value
     * @param delta         the delta
     * @return the assert number
     */
    public AssertNumber<T> equalsTo(T expectedValue, int delta) {
        AssertFunctionReturnBooleanActual<T> assertRun = (expected, actual, history) -> {
            Assert.assertTrue(Math.abs(getActual().compareTo(expected)) <= delta);
            return true;
        };
        return runAssert("equalsTo expected[" + expectedValue + "] and actuals[" + getActual() +
                "] with delta allowed: " + delta, "equalsTo", assertRun, expectedValue, getActual());
    }


}
