package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;

public class AssertNumber<T extends Number & Comparable<T>> extends AbstractAssertBase<AssertNumber, T> {

    public AssertNumber(T actual) {
        super(actual);
    }

    public AssertNumber(T actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    public AssertNumber<T> equalsTo(T expected, int delta) {
        return runAssert("equalsTo expected[" + expected + "] and actuals[" + getActual() +
                "] with delta allowed: " + delta, "equalsTo", ()-> {
            Assert.assertTrue(Math.abs(getActual().compareTo(expected))<=delta);
        },expected,getActual());
    }

    @Override
    public AssertNumber<T> equalsTo(T expected) {
        return equalsTo(expected, 0);
    }

}
