package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * The type Assert big decimal.
 */
public class AssertBigDecimal extends AbstractAssertBase<AssertBigDecimal, BigDecimal> {

    private static final MathContext DEFAULT_MATH_CONTEXT = MathContext.DECIMAL32;

    public AssertBigDecimal(final Object actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert big decimal.
     *
     * @param actual         the actual
     * @param verboseAsserts the verbose asserts
     */
    public AssertBigDecimal(final Object actual, final VerboseAsserts verboseAsserts) {
        this(getBigDecimal(actual, DEFAULT_MATH_CONTEXT), verboseAsserts);
    }

    public AssertBigDecimal(final BigDecimal actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert big decimal.
     *
     * @param actual         the actual
     * @param verboseAsserts the verbose asserts
     */
    public AssertBigDecimal(final BigDecimal actual, final VerboseAsserts verboseAsserts) {
        super(actual, verboseAsserts);
    }

    public AssertBigDecimal(final Number actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert big decimal.
     *
     * @param actual         the actual
     * @param verboseAsserts the verbose asserts
     */
    public AssertBigDecimal(final Number actual, final VerboseAsserts verboseAsserts) {
        this(getBigDecimal(actual), verboseAsserts);
    }

    /**
     * Gets big decimal.
     *
     * @param number      the number
     * @param mathContext the math context
     * @return the big decimal
     */
    public static BigDecimal getBigDecimal(final Object number, MathContext mathContext) {
        try {
            if (number instanceof Number) {
                return getBigDecimal((Number) number, mathContext);
            } else if (number instanceof BigDecimal) {
                return (BigDecimal) number;
            } else if (number instanceof String) {
                return new BigDecimal(number.toString(), mathContext);
            }
        } catch (Throwable parseIssue) {
            TS.log().info("Unable to parse[" + number + "] into a BigDecimal, returning null!", parseIssue);
        }
        return null;
    }

    /**
     * Gets big decimal.
     *
     * @param number      the number
     * @param mathContext the math context
     * @return the big decimal
     */
    public static BigDecimal getBigDecimal(final Number number, MathContext mathContext) {
        if (number == null) {
            return null;
        }
        return new BigDecimal(number.toString(), mathContext);
    }

    /**
     * Gets big decimal.
     *
     * @param number the number
     * @return the big decimal
     */
    public static BigDecimal getBigDecimal(final Number number) {
        return getBigDecimal(number, DEFAULT_MATH_CONTEXT);
    }

    /**
     * Gets big decimal.
     *
     * @param number the number
     * @return the big decimal
     */
    public static BigDecimal getBigDecimal(Object number) {
        return getBigDecimal(number, DEFAULT_MATH_CONTEXT);
    }

    /**
     * Should not have decimal value assert big decimal.
     *
     * @return the assert big decimal
     */
    public AssertBigDecimal shouldNotHaveDecimalValue() {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory("Should not have a decimal value");
            Assert.assertTrue((getActual().scale() <= 0)
                    || (getActual().setScale(0, RoundingMode.HALF_UP).compareTo(getActual()) == 0));
            return true;
        };
        return runAssert("Actual[" + getActual() + "] should not have a decimal", "shouldNotHaveDecimalValue",
                assertRun, null, getActual());
    }

    /**
     * Should have decimal value assert big decimal.
     *
     * @return the assert big decimal
     */
    public AssertBigDecimal shouldHaveDecimalValue() {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory("Should have a decimal");
            Assert.assertTrue((getActual().scale() > 0)
                    || (getActual().setScale(0, RoundingMode.HALF_UP).compareTo(getActual()) != 0));
            return true;
        };
        return runAssert("Actual[" + getActual() + "] should have a decimal", "shouldHaveDecimalValue",
                assertRun, null, getActual());
    }

    /**
     * Is within range assert big decimal.
     *
     * @param minRangeValue the min range value
     * @param maxRangeValue the max range value
     * @return the assert big decimal
     */
    public AssertBigDecimal isWithinRange(final Number minRangeValue, final Number maxRangeValue) {
        return isWithinRange(minRangeValue, maxRangeValue, true);
    }

    /**
     * Is within range assert big decimal.
     *
     * @param minRangeValue the min range value
     * @param maxRangeValue the max range value
     * @param allowEqualTo  the allow equal to
     * @return the assert big decimal
     */
    public AssertBigDecimal isWithinRange(final Number minRangeValue, final Number maxRangeValue, final boolean allowEqualTo) {
        return isWithinRange(getBigDecimal(minRangeValue), getBigDecimal(maxRangeValue), allowEqualTo);
    }

    /**
     * Is within range assert big decimal.
     *
     * @param minRangeValue the min range value
     * @param maxRangeValue the max range value
     * @param allowEqualTo  the allow equal to
     * @return the assert big decimal
     */
    public AssertBigDecimal isWithinRange(final BigDecimal minRangeValue, final BigDecimal maxRangeValue, final boolean allowEqualTo) {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory(minRangeValue + " < actual < " + maxRangeValue);
            history.setActualForHistory(minRangeValue + " < " + getActual() + " < " + maxRangeValue);
            isGreaterThan(minRangeValue, allowEqualTo);
            isLessThan(maxRangeValue, allowEqualTo);
            return true;
        };
        return runAssert("Check if actual is in the ranger [ "
                        + minRangeValue + " < " + getActual() + " < " + maxRangeValue, "isWithinRange",
                assertRun, null, getActual());
    }

    /**
     * Is greater than assert big decimal.
     *
     * @param valueToBeGreaterThan the value to be greater than
     * @param allowEqualTo         the allow equal to
     * @return the assert big decimal
     */
    public AssertBigDecimal isGreaterThan(final BigDecimal valueToBeGreaterThan, final boolean allowEqualTo) {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            if (allowEqualTo) {
                history.setExpectedForHistory(actual + ">=" + valueToBeGreaterThan);
                Assert.assertTrue(getActual().compareTo(valueToBeGreaterThan) >= 0);
            } else {
                history.setExpectedForHistory(actual + ">" + valueToBeGreaterThan);
                Assert.assertTrue(getActual().compareTo(valueToBeGreaterThan) > 0);
            }
            return true;
        };
        return runAssert(getActual() + "<" + (allowEqualTo ? "=" : "") + valueToBeGreaterThan,
                (allowEqualTo ? "isGreaterThanOrEqualsTo" : "isGreaterThan"),
                assertRun, null, getActual());
    }

    /**
     * Is greater than assert big decimal.
     *
     * @param valueToBeGreaterThan the value to be greater than
     * @param allowEqualTo         the allow equal to
     * @return the assert big decimal
     */
    public AssertBigDecimal isGreaterThan(final Number valueToBeGreaterThan, final boolean allowEqualTo) {
        return isGreaterThan(new BigDecimal(valueToBeGreaterThan.toString()), allowEqualTo);
    }

    /**
     * Is less than assert big decimal.
     *
     * @param valueToBeLessThan the value to be less than
     * @param allowEqualTo      the allow equal to
     * @return the assert big decimal
     */
    public AssertBigDecimal isLessThan(final Number valueToBeLessThan, final boolean allowEqualTo) {
        return isLessThan(new BigDecimal(valueToBeLessThan.toString()), allowEqualTo);
    }

    /**
     * Is less than assert big decimal.
     *
     * @param valueToBeLessThan the value to be less than
     * @return the assert big decimal
     */
    public AssertBigDecimal isLessThan(final Number valueToBeLessThan) {
        return isLessThan(valueToBeLessThan, false);
    }
    /**
     * Is less than assert big decimal.
     *
     * @param valueToBeLessThan the value to be less than
     * @param allowEqualTo      the allow equal to
     * @return the assert big decimal
     */

    public AssertBigDecimal isLessThan(final BigDecimal valueToBeLessThan, final boolean allowEqualTo) {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            if (allowEqualTo) {
                history.setExpectedForHistory(actual + "<=" + valueToBeLessThan);
                Assert.assertTrue(getActual().compareTo(valueToBeLessThan) <= 0);
            } else {
                history.setExpectedForHistory(actual + "<" + valueToBeLessThan);
                Assert.assertTrue(getActual().compareTo(valueToBeLessThan) < 0);
            }
            return true;
        };
        return runAssert(getActual() + "<" + (allowEqualTo ? "=" : "") + valueToBeLessThan,
                (allowEqualTo ? "isLessThanOrEqualsTo" : "isLessThan"),
                assertRun, null, getActual());
    }

    /**
     * Is greater than or equal to assert big decimal.
     *
     * @param valueToBeGreaterThan the value to be greater than
     * @return the assert big decimal
     */
    public AssertBigDecimal isGreaterThanOrEqualTo(final Number valueToBeGreaterThan) {
        return isGreaterThan(valueToBeGreaterThan, true);
    }

    /**
     * Is less than or equal to assert big decimal.
     *
     * @param valueToBeLessThan the value to be less than
     * @return the assert big decimal
     */
    public AssertBigDecimal isLessThanOrEqualTo(final Number valueToBeLessThan) {
        return isLessThan(valueToBeLessThan, true);
    }

    /**
     * Equals to assert big decimal.
     *
     * @param expected the expected
     * @return the assert big decimal
     */
    public AssertBigDecimal equalsTo(final Number expected) {
        return equalsTo(getBigDecimal(expected));
    }

    @Override
    public AssertBigDecimal equalsTo(final BigDecimal expected) {
        return equalsTo(expected, 0);
    }

    /**
     * Equals to assert big decimal.
     *
     * @param expectedValue the expected value
     * @param delta         the delta
     * @return the assert big decimal
     */
    public AssertBigDecimal equalsTo(final BigDecimal expectedValue, final int delta) {
        AssertFunctionReturnBooleanActual<BigDecimal> assertRun = (expected, actual, history) -> {
            if (delta == 0) {
                Assert.assertTrue(Math.abs(actual.compareTo(expected)) == delta);
            } else {
                Assert.assertTrue(Math.abs(actual.compareTo(expected)) <= delta);
            }
            return true;
        };
        return runAssert("Actual[" + getActual() + "] to equal Expected[" + expectedValue + "] "
                        + "with delta[" + delta + "]", "equalsTo",
                assertRun, expectedValue, getActual());
    }
}