package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AssertBigDecimal extends AbstractAssertBase<AssertBigDecimal, BigDecimal> {

    private static final MathContext DEFAULT_MATH_CONTEXT = MathContext.DECIMAL32;

    public AssertBigDecimal(final Object actual, final VerboseAsserts verboseAsserts) {
        this(getBigDecimal(actual,DEFAULT_MATH_CONTEXT), verboseAsserts);
    }

    public AssertBigDecimal(final Number actual, final VerboseAsserts verboseAsserts) {
        this(getBigDecimal(actual), verboseAsserts);
    }

    public AssertBigDecimal(final BigDecimal actual, final VerboseAsserts verboseAsserts) {
        super(actual, verboseAsserts);
    }

    public AssertBigDecimal equalsTo(final Number expected) {
        return equalsTo(getBigDecimal(expected));
    }

    public AssertBigDecimal shouldNotHaveDecimal() {
        return runAssert("Actual[" + getActual() + "] should not have a decimal", "shouldNotHaveDecimal",
                () -> {
                    Assert.assertTrue((getActual().scale() <= 0) ||
                            (getActual().setScale(0, RoundingMode.HALF_UP).compareTo(getActual()) == 0));
                }, getActual(), "Should Not have Decimal");
    }

    public AssertBigDecimal shouldHaveDecimal() {
        return runAssert("Actual[" + getActual() + "] should have a decimal", "shouldHaveDecimal",
                () -> {
                    Assert.assertTrue((getActual().scale() > 0) ||
                            (getActual().setScale(0, RoundingMode.HALF_UP).compareTo(getActual()) != 0));
                }, getActual(), "Should have Decimal");
    }

    public AssertBigDecimal isWithinRange(final Number minRangeValue, final Number maxRangeValue) {
        return isWithinRange(minRangeValue, maxRangeValue, true);
    }

    public AssertBigDecimal isWithinRange(final Number minRangeValue, final Number maxRangeValue, final boolean allowEqualTo) {
        return isWithinRange(getBigDecimal(minRangeValue), getBigDecimal(maxRangeValue), allowEqualTo);
    }

    public AssertBigDecimal isWithinRange(final BigDecimal minRangeValue, final BigDecimal maxRangeValue, final boolean allowEqualTo) {
        return runAssert("Check if actual is in the ranger [ " +
                        minRangeValue + " < " + getActual() + " < " + maxRangeValue, "isWithinRange",
                () -> {
                    isGreaterThan(minRangeValue, allowEqualTo);
                    isLessThan(maxRangeValue, allowEqualTo);
                }, getActual(), minRangeValue + " < " + getActual() + " < " + maxRangeValue);
    }

    public AssertBigDecimal isGreaterThan(final Number valueToBeGreaterThan, final boolean allowEqualTo) {
        return isGreaterThan(new BigDecimal(valueToBeGreaterThan.toString()), allowEqualTo);
    }

    public AssertBigDecimal isGreaterThanOrEqualTo(final Number valueToBeGreaterThan) {
        return isGreaterThan(valueToBeGreaterThan, true);
    }

    public AssertBigDecimal isLessThanOrEqualTo(final Number valueToBeLessThan) {
        return isLessThan(valueToBeLessThan, true);
    }

    public AssertBigDecimal isLessThan(final Number valueToBeLessThan) {
        return isLessThan(valueToBeLessThan, false);
    }

    public AssertBigDecimal isLessThan(final Number valueToBeLessThan, final boolean allowEqualTo) {
        return isLessThan(new BigDecimal(valueToBeLessThan.toString()), allowEqualTo);
    }

    public AssertBigDecimal isGreaterThan(final BigDecimal valueToBeGreaterThan, final boolean allowEqualTo) {
        String assertMethod = (allowEqualTo ? "isGreaterThanOrEqualTo" : "isGreaterThan");
        final String message = getMessage() + " - actual[" + getActual().toPlainString() + "] " +
                assertMethod + " " + valueToBeGreaterThan.toPlainString();
        runAssert(message, assertMethod, () -> {
            if (allowEqualTo) {
                Assert.assertTrue(message, getActual().compareTo(valueToBeGreaterThan) >= 0);
            } else {
                Assert.assertTrue(message, getActual().compareTo(valueToBeGreaterThan) > 0);
            }
        }, getActual(), getActual() + ">" + (allowEqualTo ? "=" : "") + valueToBeGreaterThan);
        return this;
    }

    public AssertBigDecimal isLessThan(final BigDecimal valueToBeLessThan, final boolean allowEqualTo) {
        String assertMethod = (allowEqualTo ? "isLessThanOrEqualTo" : "isLessThan");
        final String message = getMessage() + " - actual[" + getActual().toPlainString() + "] " + assertMethod + " " +
                valueToBeLessThan.toPlainString();
        runAssert(message, assertMethod, () -> {
            if (allowEqualTo) {
                Assert.assertTrue(message, getActual().compareTo(valueToBeLessThan) <= 0);
            } else {
                Assert.assertTrue(message, getActual().compareTo(valueToBeLessThan) < 0);
            }
        },getActual(),getActual() + "<" + (allowEqualTo ? "=" : "") + valueToBeLessThan);
        return this;
    }

    @Override
    public AssertBigDecimal equalsTo(final BigDecimal expected) {
        return equalsTo(expected,0);
    }

    public AssertBigDecimal equalsTo(final BigDecimal expected, final int delta) {
        runAssert("", "equalsTo", () -> {
            if(delta==0) {
                Assert.assertTrue(Math.abs(getActual().compareTo(expected))== delta);
            } else {
                Assert.assertTrue(Math.abs(getActual().compareTo(expected)) <= delta);
            }

        }, expected, getActual());
        return this;
    }

    public static BigDecimal getBigDecimal(Object number) {
        return getBigDecimal(number, DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal getBigDecimal(final Object number, MathContext mathContext) {
        if(number instanceof Number) {
            return getBigDecimal((Number)number, mathContext );
        }else if(number instanceof BigDecimal) {
            return (BigDecimal)number;
        }
        TS.log().info("Unable to convert to BigDecimal: " +number);
        return null;
    }

    public static BigDecimal getBigDecimal(final Number number) {
        return getBigDecimal(number, DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal getBigDecimal(final Number number, MathContext mathContext) {
        if (number == null) {
            return null;
        }
        return new BigDecimal(number.toString(),mathContext);
    }
}