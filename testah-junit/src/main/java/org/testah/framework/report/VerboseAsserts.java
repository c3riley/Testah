package org.testah.framework.report;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testah.TS;
import org.testah.framework.cli.Cli;
import org.testah.framework.dto.StepAction;
import org.testah.framework.report.asserts.AssertCollections;
import org.testah.framework.report.asserts.AssertStrings;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The Class VerboseAsserts.
 */
@SuppressWarnings("deprecation")
public class VerboseAsserts {

    /**
     * The record steps.
     */
    private boolean recordSteps = true;

    /**
     * The throw exception on fail.
     */
    private boolean throwExceptionOnFail = true;

    /**
     * The is verify only.
     */
    private boolean isVerifyOnly = false;

    private AssertStrings assertStrings;

    /**
     * Instantiates a new verbose asserts.
     */
    public VerboseAsserts() {
        setRecordSteps(TS.params().isRecordSteps());
        throwExceptionOnFail = TS.params().isThrowExceptionOnFail();
        this.assertStrings = new AssertStrings(this);
    }

    /**
     * Instantiates a new verbose asserts.
     *
     * @param throwExceptionOnFail the throw exception on fail
     */
    public VerboseAsserts(final boolean throwExceptionOnFail) {
        this.throwExceptionOnFail = throwExceptionOnFail;
    }

    /**
     * Custom assert boolean.
     *
     * @param runnableAssertBlock the runnable assert block
     * @return the boolean
     */
    public boolean customAssert(final Runnable runnableAssertBlock) {
        return customAssert("Custom Assert Block", runnableAssertBlock);
    }

    /**
     * Custom assert boolean.
     *
     * @param message             the message
     * @param runnableAssertBlock the runnable assert block
     * @return the boolean
     */
    public boolean customAssert(final String message, final Runnable runnableAssertBlock) {
        try {
            runnableAssertBlock.run();
            return addAssertHistory(message, true, "customAssert", true, true);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "customAssert", true, false, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Starts with.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedPrefix the expected prefix
     * @return true, if successful
     */
    public boolean startsWith(final String message, final String stringToCheck, final String expectedPrefix) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to startWith " + expectedPrefix,
                StringUtils.startsWith(stringToCheck, expectedPrefix));
    }

    /**
     * Starts with ignore case boolean.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedPrefix the expected prefix
     * @return the boolean
     */
    public boolean startsWithIgnoreCase(final String message, final String stringToCheck, final String expectedPrefix) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to "
                        + "startsWithIgnoreCase " + expectedPrefix,
                StringUtils.startsWithIgnoreCase(stringToCheck, expectedPrefix));
    }

    /**
     * Ends with.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedSuffix the expected suffix
     * @return true, if successful
     */
    public boolean endsWith(final String message, final String stringToCheck, final String expectedSuffix) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to endsWith " + expectedSuffix,
                StringUtils.endsWith(stringToCheck, expectedSuffix));
    }

    /**
     * Ends with ignore case boolean.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedSuffix the expected suffix
     * @return the boolean
     */
    public boolean endsWithIgnoreCase(final String message, final String stringToCheck, final String expectedSuffix) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to endsWithIgnoreCase " + expectedSuffix,
                StringUtils.endsWithIgnoreCase(stringToCheck, expectedSuffix));
    }

    /**
     * Contains.
     *
     * @param message                the message
     * @param stringToCheck          the string to check
     * @param expectedValueToContain the expected value to contain
     * @return true, if successful
     */
    public boolean contains(final String message, final String stringToCheck, final String expectedValueToContain) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to contain " + expectedValueToContain,
                StringUtils.contains(stringToCheck, expectedValueToContain));
    }

    /**
     * Contains ignore case boolean.
     *
     * @param message                the message
     * @param stringToCheck          the string to check
     * @param expectedValueToContain the expected value to contain
     * @return the boolean
     */
    public boolean containsIgnoreCase(final String message, final String stringToCheck, final String expectedValueToContain) {
        return this.isTrue(message + " - expected String[" + stringToCheck + "] to "
                        + "containsIgnoreCase " + expectedValueToContain,
                StringUtils.containsIgnoreCase(stringToCheck, expectedValueToContain));
    }

    /**
     * Size equals.
     *
     * @param message                     the message
     * @param objectToCheckSizeOrLengthOf the object to check size or length of
     * @param expectedSize                the expected size
     * @return true, if successful
     */
    public boolean sizeEquals(final String message, final Object objectToCheckSizeOrLengthOf, final int expectedSize) {
        return this.equalsTo(message + " - expected Object[" + objectToCheckSizeOrLengthOf
                + "] to have a size/length of " + expectedSize, getSize(objectToCheckSizeOrLengthOf), expectedSize);
    }

    /**
     * Not ends with.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedSuffix the expected suffix
     * @return true, if successful
     */
    public boolean notEndsWith(final String message, final String stringToCheck, final String expectedSuffix) {
        return this.isFalse(message + " - expected String[" + stringToCheck + "] to endsWith " + expectedSuffix,
                StringUtils.endsWith(stringToCheck, expectedSuffix));
    }

    /**
     * Not ends with ignore case boolean.
     *
     * @param message        the message
     * @param stringToCheck  the string to check
     * @param expectedSuffix the expected suffix
     * @return the boolean
     */
    public boolean notEndsWithIgnoreCase(final String message, final String stringToCheck, final String expectedSuffix) {
        return this.isFalse(message + " - expected String[" + stringToCheck + "] to "
                        + "notEndsWithIgnoreCase " + expectedSuffix,
                StringUtils.endsWithIgnoreCase(stringToCheck, expectedSuffix));
    }

    /**
     * Not contains.
     *
     * @param message                the message
     * @param stringToCheck          the string to check
     * @param expectedValueToContain the expected value to contain
     * @return true, if successful
     */
    public boolean notContains(final String message, final String stringToCheck, final String expectedValueToContain) {
        return this.isFalse(message + " - expected String[" + stringToCheck + "] to contain " + expectedValueToContain,
                StringUtils.contains(stringToCheck, expectedValueToContain));
    }

    /**
     * Not contains ignore case boolean.
     *
     * @param message                the message
     * @param stringToCheck          the string to check
     * @param expectedValueToContain the expected value to contain
     * @return the boolean
     */
    public boolean notContainsIgnoreCase(final String message, final String stringToCheck, final String expectedValueToContain) {
        return this.isFalse(message + " - expected String[" + stringToCheck + "] to "
                        + "notContainsIgnoreCase " + expectedValueToContain,
                StringUtils.containsIgnoreCase(stringToCheck, expectedValueToContain));
    }

    /**
     * Not size equals.
     *
     * @param message                     the message
     * @param objectToCheckSizeOrLengthOf the object to check size or length of
     * @param expectedSize                the expected size
     * @return true, if successful
     */
    public boolean notSizeEquals(final String message, final Object objectToCheckSizeOrLengthOf,
                                 final int expectedSize) {
        return this.notEquals(message + " - expected Object[" + objectToCheckSizeOrLengthOf
                + "] to have a size/length of " + expectedSize, getSize(objectToCheckSizeOrLengthOf), expectedSize);
    }

    /**
     * Gets the size.
     *
     * @param objectToCheck the object to check
     * @return the size
     */
    private int getSize(final Object objectToCheck) {
        if (!notNull("Check the getSize objectToCheck is not null", objectToCheck)) {
            return -1;
        }
        Integer actual = null;
        if (objectToCheck instanceof String) {
            actual = ((String) objectToCheck).length();
        } else if (objectToCheck instanceof Object[]) {
            actual = ((Object[]) objectToCheck).length;
        } else if (objectToCheck instanceof ArrayList) {
            actual = ((ArrayList<?>) objectToCheck).size();
        } else if (objectToCheck instanceof List) {
            actual = ((List<?>) objectToCheck).size();
        } else if (objectToCheck instanceof Set) {
            actual = ((Set<?>) objectToCheck).size();
        } else if (objectToCheck instanceof HashMap) {
            actual = ((HashMap<?, ?>) objectToCheck).size();
        }
        if (null != actual) {
            return actual;
        } else {
            throw new RuntimeException("Issue with object to Check for notEmpty Assert, object must be String, "
                    + "ArrayList, Set, List or HashMap");
        }
    }

    // final JsonNode jsonNode=new ObjectMapper().valueToTree(tp);

    /**
     * Same json uses JSONAssert.
     *
     * @param expected the expected object will be converted to JSONObject
     * @param actual   the actual object will be converted to JSONObject
     * @return the boolean if true
     */
    public boolean sameJson(final Object expected, final Object actual) {
        return sameJson("", expected, actual, true);
    }

    /**
     * Same json uses JSONAssert.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean sameJson(final String message, final Object expected, final Object actual) {
        return sameJson(message, expected, actual, true);

    }

    /**
     * Same json boolean.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param strict   the strict
     * @return the boolean
     */
    public boolean sameJson(final Object expected, final Object actual, final boolean strict) {
        return sameJson("", expected, actual, strict);
    }

    /**
     * Same json.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param strict   the strict
     * @return true, if successful
     */
    public boolean sameJson(final String message, final Object expected, final Object actual, final boolean strict) {
        final JSONObject expectedJsonNode = new JSONObject(expected);
        final JSONObject actualJsonNode = new JSONObject(actual);
        return sameJson(message, expectedJsonNode, actualJsonNode, strict);
    }

    /**
     * Same json boolean.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param strict   the strict
     * @return the boolean
     */
    public boolean sameJson(final JSONObject expected, final JSONObject actual, final boolean strict) {
        return sameJson("", expected, actual, strict);
    }

    /**
     * Same json.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param strict   the strict
     * @return true, if successful
     */
    public boolean sameJson(final String message, final JSONObject expected, final JSONObject actual, final boolean strict) {
        try {
            JSONAssert.assertEquals(expected, actual, strict);
            return addAssertHistory(message, true, "assertSameJson", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertSame", expected.toString(), actual.toString(), e);
            if (getThrowExceptionOnFail()) {
                throw new AssertionError(e);
            }
            return rtn;
        }
    }

    /**
     * Same.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean same(final Object expected, final Object actual) {
        return same("", expected, actual);
    }

    /**
     * Same.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean same(final String message, final Object expected, final Object actual) {
        try {
            Assert.assertSame(message, expected, actual);
            return addAssertHistory(message, true, "assertSame", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertSame", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Fail.
     *
     * @return true, if successful
     */
    public boolean fail() {
        return fail("");
    }

    /**
     * Fail.
     *
     * @param message the message
     * @return true, if successful
     */
    public boolean fail(final String message) {
        try {
            Assert.fail(message);
            return addAssertHistory(message, false, "fail", "", "");
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "fail", "", "", e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Critical failure.
     *
     * @param message message string
     */
    public void critical(final String message) {
        TS.log().fatal(Cli.BAR_LONG);
        TS.log().fatal(String.format("%s Critical Issue Occurred and test should be stopped! Message[%s]", Cli.BAR_WALL, message));
        TS.log().fatal(Cli.BAR_LONG);
        addAssertHistory(message, false, "critical", null, null, null);
    }

    /**
     * Not null.
     *
     * @param message the message
     * @param actual  the actual
     * @return true, if successful
     */
    public boolean notNull(final String message, final Object actual) {
        try {
            Assert.assertNotNull(message, actual);
            return addAssertHistory(message, true, "assertNotNull", true, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotNull", true, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Not null.
     *
     * @param actual the actual
     * @return true, if successful
     */
    public boolean notNull(final Object actual) {
        return notNull("", actual);
    }

    /**
     * Checks if is null.
     *
     * @param message the message
     * @param actual  the actual
     * @return true, if is null
     */
    public boolean isNull(final String message, final Object actual) {
        try {
            Assert.assertNull(message, actual);
            return addAssertHistory(message, true, "assertNull", null, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNull", null, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Checks if is null.
     *
     * @param actual the actual
     * @return true, if is null
     */
    public boolean isNull(final Object actual) {
        return isNull("", actual);
    }

    /**
     * Not same.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notSame(final String message, final Object expected, final Object actual) {
        try {
            Assert.assertNotSame(message, expected, actual);
            return addAssertHistory(message, true, "assertNotSame", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotSame", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Not same.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notSame(final Object expected, final Object actual) {
        try {
            Assert.assertNotSame(expected, actual);
            return addAssertHistory("", true, "assertNotSame", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertNotSame", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final double expected, final double actual) {
        return equalsTo(message, expected, actual);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final long expected, final long actual) {
        return equalsTo(expected, actual);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final Object[] expected, final Object[] actual) {
        return equalsTo(message, expected, actual);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final Object expected, final Object actual) {
        return equalsTo(expected, actual);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final Object[] expected, final Object[] actual) {
        return equalsTo(expected, actual);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param arg3     the arg3
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final double expected, final double actual, final double arg3) {
        return equalsTo(message, expected, actual, arg3);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final Object expected, final Object actual) {
        return equalsTo(message, expected, actual);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final float expected, final float actual, final float delta) {
        return equalsTo(expected, actual, delta);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final double expected, final double actual, final double delta) {
        return equalsTo(expected, actual, delta);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param arg3     the arg3
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final float expected, final float actual, final float arg3) {
        return equalsTo(message, expected, actual, arg3);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final String message, final long expected, final long actual) {
        return equalsTo(message, expected, actual);
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    @Deprecated
    public boolean equals(final double expected, final double actual) {
        return equalsTo(expected, actual);
    }

    /**
     * Equals to ignore case.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsToIgnoreCase(final String expected, final String actual) {
        return equalsToIgnoreCase("", expected, actual);
    }

    /**
     * Equals to ignore case.
     * Converts to lowercase both using StringUtils, then does assertEquals.
     * Will write the exact values into the desc to help.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsToIgnoreCase(final String message, final String expected, final String actual) {
        try {
            Assert.assertEquals(message + " - expected[" + expected + "] == actual[" + actual + "]",
                    StringUtils.lowerCase(expected), StringUtils.lowerCase(actual));
            return addAssertHistory(message, true, "equalsToIgnoreCase", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "equalsToIgnoreCase", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }


    /**
     * Equals to multiline string boolean.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return the boolean
     */
    public boolean equalsToMultilineString(final String message, final String expected, final String actual) {
        if (actual == null) {
            return isTrue("The Actual is null, checking to see if Expected is null, "
                    + "else will fail and not go further for the assert.", (expected == null));
        }
        return new AssertCollections(StringUtils.split(actual, System.lineSeparator()), this)
                .equals(StringUtils.split(expected, System.lineSeparator())).isPassed();
    }

    /**
     * Assert file exists.
     *
     * @param actual the actual
     * @return true, if successful
     */
    public boolean assertFileExists(final File actual) {
        return assertFileExists("", actual);
    }

    /**
     * Assert file exists.
     *
     * @param message the message
     * @param actual  the actual
     * @return true, if successful
     */
    public boolean assertFileExists(final String message, final File actual) {
        try {
            Assert.assertNotNull(message + " - File cannot be null", actual);
            Assert.assertTrue("Checking if file exits[" + actual.getAbsolutePath() + "] - " + message, actual.exists());
            return addAssertHistory(message, true, "equalsToIgnoreCase", true, actual.exists());
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "equalsToIgnoreCase", true, false, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals to boolean.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return the boolean
     */
    public boolean equalsTo(final String message, final String expected, final String actual) {
        try {
            Assert.assertEquals(message + " - expected[" + expected + "] == actual[" + actual + "]",
                    StringUtils.lowerCase(expected), StringUtils.lowerCase(actual));
            return addAssertHistory(message, true, "equalsTo", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "equalsTo", expected, actual, e);
            assertStrings.addStepForStringDifferences(expected, actual);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final double expected, final double actual) {
        try {
            Assert.assertEquals(message, expected, actual, expected / 10000.0);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final long expected, final long actual) {
        return equalsTo("", expected, actual);
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final Object[] expected, final Object[] actual) {
        try {
            Assert.assertEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final Object expected, final Object actual) {
        try {
            Assert.assertEquals(expected, actual);
            return addAssertHistory("", true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param arg3     the arg3
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final double expected, final double actual, final double arg3) {
        try {
            Assert.assertEquals(message, expected, actual, arg3);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final Object expected, final Object actual) {
        try {
            Assert.assertEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final Object[] expected, final Object[] actual) {
        try {
            Assert.assertEquals(expected, actual);
            return addAssertHistory("", true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    public boolean equalsTo(final float expected, final float actual, final float delta) {
        try {
            Assert.assertEquals(expected, actual, delta);
            return addAssertHistory("", true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    public boolean equalsTo(final double expected, final double actual, final double delta) {
        try {
            Assert.assertEquals(expected, actual, delta);
            return addAssertHistory("", true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param arg3     the arg3
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final float expected, final float actual, final float arg3) {
        try {
            Assert.assertEquals(message, expected, actual, arg3);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final String message, final long expected, final long actual) {
        try {
            Assert.assertEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean equalsTo(final double expected, final double actual) {
        try {
            Assert.assertEquals(expected, actual);
            return addAssertHistory("", true, "assertEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Unexpected exception.
     *
     * @param msg   the msg
     * @param error the error
     */
    public void unExpectedException(final String msg, final Throwable error) {
        addAssertHistory(msg, false, "unExpectedException", "", msg, error);
    }

    /**
     * Use Hamcrest matcher for assert.
     *
     * @param <T>      generic type
     * @param message  assert message
     * @param expected expected object
     * @param matcher  Hamcrest matcher
     */
    public <T> void assertThat(final String message, final T expected, final Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(message, expected, matcher);
        } catch (AssertionError e) {
            addAssertHistory(message, false, "assertThat", expected, matcher, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
        }
    }

    /**
     * Use Hamcrest matcher for assert.
     *
     * @param <T>      generic type
     * @param expected expected object
     * @param matcher  Hamcrest matcher
     */
    public <T> void assertThat(final T expected, final Matcher<? super T> matcher) {
        assertThat("", expected, matcher);
    }

    /**
     * Checks if is false.
     *
     * @param message the message
     * @param actual  the actual
     * @return true, if is false
     */
    public boolean isFalse(final String message, final boolean actual) {
        try {
            Assert.assertFalse(message, actual);
            return addAssertHistory(message, true, "assertFalse", false, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertFalse", false, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Checks if is false.
     *
     * @param actual the actual
     * @return true, if is false
     */
    public boolean isFalse(final boolean actual) {
        return isFalse("", actual);
    }

    /**
     * Checks if is true.
     *
     * @param message the message
     * @param actual  the actual
     * @return true, if is true
     */
    public boolean isTrue(final String message, final boolean actual) {
        try {
            Assert.assertTrue(message, actual);
            return addAssertHistory(message, true, "assertTrue", true, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertTrue", true, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Checks if is true.
     *
     * @param actual the actual
     * @return true, if is true
     */
    public boolean isTrue(final boolean actual) {
        return isTrue("", actual);
    }

    /**
     * Not equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notEquals(final Object expected, final Object actual) {
        return notEquals("", expected, actual);
    }

    /**
     * Not equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notEquals(final String message, final Object expected, final Object actual) {
        try {
            Assert.assertNotEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertNotEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Not equals.
     *
     * @param unexpected the unexpected
     * @param actual     the actual
     * @param delta      the delta
     * @return true, if successful
     */
    public boolean notEquals(final float unexpected, final float actual, final float delta) {
        return notEquals("", unexpected, actual, delta);
    }

    /**
     * Not equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    public boolean notEquals(final String message, final float expected, final float actual, final float delta) {
        try {
            Assert.assertNotEquals(message, expected, actual, delta);
            return addAssertHistory(message, true, "assertNotEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Not equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param delta    the delta
     * @return true, if successful
     */
    public boolean notEquals(final String message, final double expected, final double actual, final double delta) {
        try {
            Assert.assertNotEquals(message, expected, actual, delta);
            return addAssertHistory(message, true, "assertNotEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Not equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notEquals(final long expected, final long actual) {
        return notEquals("", expected, actual);
    }

    /**
     * Not equals.
     *
     * @param unexpected the unexpected
     * @param actual     the actual
     * @param delta      the delta
     * @return true, if successful
     */
    public boolean notEquals(final double unexpected, final double actual, final double delta) {
        return notEquals("", unexpected, actual, delta);
    }

    /**
     * Not equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean notEquals(final String message, final long expected, final long actual) {
        try {
            Assert.assertNotEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertNotEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * That.
     *
     * @param <T>     the generic type
     * @param actual  the actual
     * @param matcher the matcher
     * @return true, if successful
     */
    public <T> boolean that(final T actual, final Matcher<? super T> matcher) {
        return that("", actual, matcher);
    }

    /**
     * That.
     *
     * @param <T>     the generic type
     * @param message the message
     * @param actual  the actual
     * @param matcher the matcher
     * @return true, if successful
     */
    public <T> boolean that(final String message, final T actual, final Matcher<? super T> matcher) {
        try {
            Assert.assertThat(message, actual, matcher);
            return addAssertHistory(message, true, "assertThat", actual, matcher);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertThat", actual, matcher, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final byte[] expected, final byte[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final byte[] expected, final byte[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final char[] expected, final char[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @param arg3     the arg3
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final double[] expected, final double[] actual,
                               final double arg3) {
        try {
            Assert.assertArrayEquals(message, expected, actual, arg3);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final Object[] expected, final Object[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final Object[] expected, final Object[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final boolean[] expected, final boolean[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final boolean[] expected, final boolean[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final long[] expected, final long[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param expectedAry the expectedAry
     * @param actualAry   the actualAry
     * @param delta       the delta
     * @return true, if successful
     */
    public boolean arrayEquals(final double[] expectedAry, final double[] actualAry, final double delta) {
        try {
            Assert.assertArrayEquals(expectedAry, actualAry, delta);
            return addAssertHistory("", true, "assertArrayEquals", expectedAry, actualAry);
        } catch (final AssertionError e) {
            final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expectedAry, actualAry, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param message   the message
     * @param expecteds the expecteds
     * @param actuals   the actuals
     * @param delta     the delta
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final float[] expecteds, final float[] actuals,
                               final float delta) {
        try {
            Assert.assertArrayEquals(message, expecteds, actuals, delta);
            return addAssertHistory(message, true, "assertArrayEquals", expecteds, actuals);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expecteds, actuals, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expecteds the expecteds
     * @param actuals   the actuals
     * @param delta     the delta
     * @return true, if successful
     */
    public boolean arrayEquals(final float[] expecteds, final float[] actuals, final float delta) {
        return arrayEquals("", expecteds, actuals, delta);
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final char[] expected, final char[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final short[] expected, final short[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final short[] expected, final short[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final int[] expected, final int[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Array equals.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final int[] expected, final int[] actual) {
        return arrayEquals("", expected, actual);
    }

    /**
     * Array equals.
     *
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return true, if successful
     */
    public boolean arrayEquals(final String message, final long[] expected, final long[] actual) {
        try {
            Assert.assertArrayEquals(message, expected, actual);
            return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Checks if is empty.
     *
     * @param message       the message
     * @param objectToCheck the object to check
     * @return true, if is empty
     */
    public boolean isEmpty(final String message, final Object objectToCheck) {
        if (notNull(message, objectToCheck)) {
            final Boolean actual = isEmpty(objectToCheck);
            return isTrue(message + " - Is Empty[" + objectToCheck + "]", actual);
        }
        return false;
    }

    /**
     * Checks if is empty.
     *
     * @param objectToCheck the object to check
     * @return true, if is empty
     */
    private boolean isEmpty(final Object objectToCheck) {
        Boolean actual = null;
        if (objectToCheck instanceof String) {
            actual = ((String) objectToCheck).length() == 0;
        } else if (objectToCheck instanceof Object[]) {
            actual = ((Object[]) objectToCheck).length == 0;
        } else if (objectToCheck instanceof ArrayList) {
            actual = ((ArrayList<?>) objectToCheck).isEmpty();
        } else if (objectToCheck instanceof List) {
            actual = ((List<?>) objectToCheck).isEmpty();
        } else if (objectToCheck instanceof Set) {
            actual = ((Set<?>) objectToCheck).isEmpty();
        } else if (objectToCheck instanceof HashMap) {
            actual = ((HashMap<?, ?>) objectToCheck).isEmpty();
        }
        if (null != actual) {
            return actual;
        } else {
            throw new RuntimeException("Issue with object to Check for notEmpty Assert, object must be String, "
                    + "ArrayList, Set, List or HashMap");
        }
    }

    /**
     * Checks if is not empty.
     *
     * @param message       the message
     * @param objectToCheck the object to check
     * @return true, if is not empty
     */
    public boolean isNotEmpty(final String message, final Object objectToCheck) {
        if (notNull(message, objectToCheck)) {
            final Boolean actual = isEmpty(objectToCheck);
            return isFalse(message + " - Is Empty", actual);
        }
        return false;
    }


    /**
     * Is greater than or equal to boolean.
     *
     * @param message              the message
     * @param valueToBeGreaterThan the value to be greater than
     * @param actual               the actual
     * @return the boolean
     */
    public boolean isGreaterThanOrEqualTo(String message, final Number valueToBeGreaterThan, final Number actual) {
        return isGreaterThan(message, valueToBeGreaterThan, actual, true);
    }


    /**
     * Is greater than or equal to boolean.
     *
     * @param message              the message
     * @param valueToBeGreaterThan the value to be greater than
     * @param actual               the actual
     * @return the boolean
     */
    public boolean isGreaterThanOrEqualTo(String message, final BigDecimal valueToBeGreaterThan, final BigDecimal actual) {
        return isGreaterThan("", valueToBeGreaterThan, actual, true);

    }

    /**
     * Is greater than boolean.
     *
     * @param message              the message
     * @param valueToBeGreaterThan the value to be greater than
     * @param actual               the actual
     * @param allowEqualTo         the allow equal to
     * @return the boolean
     */
    public boolean isGreaterThan(String message, final Number valueToBeGreaterThan, final Number actual, final boolean allowEqualTo) {
        return isGreaterThan(message, new BigDecimal(valueToBeGreaterThan.toString()), new BigDecimal(actual.toString()), allowEqualTo);
    }

    /**
     * Is greater than boolean.
     *
     * @param message              the message
     * @param valueToBeGreaterThan the value to be greater than
     * @param actual               the actual
     * @return the boolean
     */
    public boolean isGreaterThan(String message, final Number valueToBeGreaterThan, final Number actual) {
        return isGreaterThan(message, valueToBeGreaterThan, actual, false);
    }

    /**
     * Is greater than boolean.
     *
     * @param message              the message
     * @param valueToBeGreaterThan the value to be greater than
     * @param actual               the actual
     * @return the boolean
     */
    public boolean isGreaterThan(String message, final BigDecimal valueToBeGreaterThan, final BigDecimal actual) {
        return isGreaterThan("", valueToBeGreaterThan, actual, false);
    }

    private boolean isGreaterThan(String message, final BigDecimal valueToBeGreaterThan, final BigDecimal actual,
                                  final boolean allowEqualTo) {
        String assertMethod = (allowEqualTo ? "isGreaterThanOrEqualTo" : "isGreaterThan");
        try {

            notNull("Check valueToBeGreaterThan is not null", valueToBeGreaterThan);
            notNull("Check actual is not null", actual);

            message = message + " - actual[" + actual.toPlainString() + "] " + assertMethod + " " + valueToBeGreaterThan.toPlainString();
            if (allowEqualTo) {
                Assert.assertTrue(message, actual.compareTo(valueToBeGreaterThan) >= 0);
            } else {
                Assert.assertTrue(message, actual.compareTo(valueToBeGreaterThan) > 0);
            }
            return addAssertHistory(message, true, assertMethod, valueToBeGreaterThan, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, assertMethod, valueToBeGreaterThan, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * Is less than or equal to boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @return the boolean
     */
    public boolean isLessThanOrEqualTo(String message, final Number valueToBeLessThan, final Number actual) {
        return isLessThan(message, valueToBeLessThan, actual, true);
    }


    /**
     * Is less than or equal to boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @return the boolean
     */
    public boolean isLessThanOrEqualTo(String message, final BigDecimal valueToBeLessThan, final BigDecimal actual) {
        return isLessThan(message, valueToBeLessThan, actual, true);
    }

    /**
     * Is less than boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @return the boolean
     */
    public boolean isLessThan(String message, final Number valueToBeLessThan, final Number actual) {
        return isLessThan(message, valueToBeLessThan, actual, false);
    }


    /**
     * Is less than boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @param allowEqualTo      the allow equal to
     * @return the boolean
     */
    public boolean isLessThan(String message, final Number valueToBeLessThan, final Number actual, final boolean allowEqualTo) {
        return isLessThan(message, new BigDecimal(valueToBeLessThan.toString()), new BigDecimal(actual.toString()), allowEqualTo);
    }

    /**
     * Is less than boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @return the boolean
     */
    public boolean isLessThan(String message, final BigDecimal valueToBeLessThan, final BigDecimal actual) {
        return isLessThan(message, valueToBeLessThan, actual, false);
    }


    /**
     * Is less than boolean.
     *
     * @param message           the message
     * @param valueToBeLessThan the value to be less than
     * @param actual            the actual
     * @param allowEqualTo      the allow equal to
     * @return the boolean
     */
    public boolean isLessThan(String message, final BigDecimal valueToBeLessThan, final BigDecimal actual, final boolean allowEqualTo) {
        String assertMethod = (allowEqualTo ? "isLessThanOrEqualTo" : "isLessThan");

        notNull("Check valueToBeLessThan is not null", valueToBeLessThan);
        notNull("Check actual is not null", actual);

        try {
            message = message + " - actual[" + actual.toPlainString() + "] " + assertMethod + " " + valueToBeLessThan.toPlainString();
            if (allowEqualTo) {
                Assert.assertTrue(message, actual.compareTo(valueToBeLessThan) <= 0);
            } else {
                Assert.assertTrue(message, actual.compareTo(valueToBeLessThan) < 0);
            }
            return addAssertHistory(message, true, assertMethod, valueToBeLessThan, actual);
        } catch (final Throwable e) {
            final boolean rtn = addAssertHistory(message, false, assertMethod, valueToBeLessThan, actual, e);
            if (getThrowExceptionOnFail()) {
                throw e;
            }
            return rtn;
        }
    }

    /**
     * ************************************************************************* ***********.
     *
     * @return true, if successful
     */
    public boolean pass() {
        return pass("");
    }

    /**
     * Pass.
     *
     * @param message the message
     * @return true, if successful
     */
    public boolean pass(final String message) {
        return addAssertHistory(message, true, "pass", true, true, null);
    }

    /**
     * Adds the assert history.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @return true, if successful
     */
    public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
                                    final Object expected, final Object actual) {
        return addAssertHistory(message, status, assertMethod, expected, actual, null);
    }

    /**
     * Adds the assert history.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @return true, if successful
     */
    public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
                                    final Object expected, final Object actual, final Throwable exception) {
        if (isVerifyOnly()) {
            StepAction.createVerifyResult(message, status, assertMethod, expected, actual, exception).add(false);
        } else {
            StepAction.createAssertResult(message, status, assertMethod, expected, actual, exception).add(false);
        }

        return status;
    }

    /**
     * Gets the throw exception on fail.
     *
     * @return the throw exception on fail
     */
    public boolean getThrowExceptionOnFail() {
        return throwExceptionOnFail;
    }

    /**
     * Checks if is throw exception on fail.
     *
     * @return true, if is throw exception on fail
     */
    public boolean isThrowExceptionOnFail() {
        return getThrowExceptionOnFail();
    }

    /**
     * Only verify.
     *
     * @return the verbose asserts
     */
    public VerboseAsserts onlyVerify() {
        this.recordSteps = false;
        this.throwExceptionOnFail = false;
        this.setVerifyOnly(true);
        return this;
    }

    /**
     * Checks if is record steps.
     *
     * @return true, if is record steps
     */
    public boolean isRecordSteps() {
        return recordSteps;
    }

    /**
     * Sets the throw exception on fail.
     *
     * @param throwExceptionOnFail the new throw exception on fail
     */
    public VerboseAsserts setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
        this.throwExceptionOnFail = throwExceptionOnFail;
        return this;
    }

    /**
     * Sets the record steps.
     *
     * @param recordSteps the new record steps
     */
    public VerboseAsserts setRecordSteps(final boolean recordSteps) {
        this.recordSteps = recordSteps;
        return this;
    }

    /**
     * Checks if is verify only.
     *
     * @return true, if is verify only
     */
    public boolean isVerifyOnly() {
        return isVerifyOnly;
    }

    /**
     * Sets the verify only.
     *
     * @param isVerifyOnly the new verify only
     */
    public VerboseAsserts setVerifyOnly(final boolean isVerifyOnly) {
        this.isVerifyOnly = isVerifyOnly;
        setThrowExceptionOnFail(false);
        return this;
    }

}
