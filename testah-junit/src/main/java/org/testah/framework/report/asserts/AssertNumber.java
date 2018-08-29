package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.Collection;

public class AssertNumber<T extends Number & Comparable<T>> extends AbstractAssertBase<AssertNumber, T> {

    public AssertNumber(T actual) {
        super(actual);
    }

    public AssertNumber(T actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    public AssertNumber<T> equalsTo(T expectedValue, int delta) {
        AssertFunctionReturnBooleanActual<T> assertRun = (expected, actual, history) -> {
            Assert.assertTrue(Math.abs(getActual().compareTo(expected)) <= delta);
            return true;
        };
        return runAssert("equalsTo expected[" + expectedValue + "] and actuals[" + getActual() +
                "] with delta allowed: " + delta, "equalsTo", assertRun, expectedValue, getActual());
    }

    @Override
    public AssertNumber<T> equalsTo(T expected) {
        return equalsTo(expected, 0);
    }

}
