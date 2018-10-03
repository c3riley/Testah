package org.testah.framework.report.asserts;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The type Assert strings.
 */
@SuppressWarnings("checkstyle:lineWrappingIndentation")
public class AssertStrings extends AbstractAssertBase<AssertStrings, String> {
    //CHECKSTYLE.OFF: checkstyle:lineWrappingIndentation

    /**
     * Instantiates a new Assert strings.
     *
     * @param actual the actual
     */
    public AssertStrings(final String actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert strings.
     *
     * @param actual         the actual
     * @param verboseAsserts the verbose asserts
     */
    public AssertStrings(final String actual, final VerboseAsserts verboseAsserts) {
        super(actual, verboseAsserts);
    }

    /**
     * Equals to ignore case assert strings.
     *
     * @param expected the expected
     * @return the assert strings
     */
    public AssertStrings equalsToIgnoreCase(final String expected) {
        return equalsTo(expected, true);
    }

    public AssertStrings equalsTo(final String expected) {
        return equalsTo(expected, false);
    }

    /**
     * Equals to assert strings.
     *
     * @param expected   the expected
     * @param ignoreCase the ignore case
     * @return the assert strings
     */
    protected AssertStrings equalsTo(final String expected, boolean ignoreCase) {
        return equalsTo("", expected, getActual(), ignoreCase);
    }

    /**
     * Equals to assert strings.
     *
     * @param extraMessage the extra message
     * @param expected     the expected
     * @param actual       the actual
     * @param ignoreCase   the ignore case
     * @return the assert strings
     */
    protected AssertStrings equalsTo(final String extraMessage, final String expected, final String actual, boolean ignoreCase) {
        if (actual == null || expected == null || !expected.contains(System.lineSeparator())) {
            runAssert("Traditional equals" + extraMessage,
                "equalsTo",
                getAssertBlock(() -> {
                    if (ignoreCase) {
                        Assert.assertTrue(StringUtils.equalsIgnoreCase(expected, actual));
                    } else {
                        Assert.assertTrue(StringUtils.equals(expected, actual));
                    }
                }), expected, actual, true, () -> {
                    addStepForStringDifferences(expected, actual, ignoreCase);
                });
            return this;
        }
        //StringUtils.split does not render correct lines
        TS.log().debug("Since newline found in expected, splitting on newline and comparing as array of strings");
        equalsTo(getActual().split(System.lineSeparator()),
            expected.split(System.lineSeparator()), ignoreCase);
        return this;
    }


    /**
     * Equals to assert strings.
     *
     * @param expectedLineArray the expected line array
     * @param actualLineArray   the actual line array
     * @param ignoreCase        the ignore case
     * @return the assert strings
     */
    protected AssertStrings equalsTo(final String[] expectedLineArray, final String[] actualLineArray, boolean ignoreCase) {
        final boolean throwOnFail = getAsserts().getThrowExceptionOnFail();
        try {
            //Turn off so each line can be checked
            getAsserts().setThrowExceptionOnFail(false);
            int ctr = 0;

            getAsserts().equalsTo("Check that the 2 files contain the same number of lines",
                expectedLineArray.length, actualLineArray.length);

            for (final String expectedLine : expectedLineArray) {
                if (ctr < actualLineArray.length) {
                    equalsTo(" - Line " + (ctr + 1), expectedLine, actualLineArray[ctr], ignoreCase);
                } else {
                    equalsTo(" - Line " + (ctr + 1), expectedLine, "NO_LINE[" + ctr + "]_IN_ACTUAL_FILE", ignoreCase);
                }
                ctr++;
            }
            for (int actualLinesMoreThanExpected = ctr; actualLinesMoreThanExpected < actualLineArray.length;
                 actualLinesMoreThanExpected++) {
                equalsTo(" - Line " + (actualLinesMoreThanExpected + 1),
                    "NO_LINE[" + actualLinesMoreThanExpected + "]_IN_EXPECTED_FILE",
                    actualLineArray[ctr++], ignoreCase);

            }
        } finally {
            //Turn on, so any follow on assert fails will throw exception as expected.
            getAsserts().setThrowExceptionOnFail(throwOnFail);
            if (this.isFailed()) {
                getAsserts().fail("Failed During equalsTo - see above for the asserts that caused the failure.");
            }
        }
        return this;
    }

    /**
     * Add step for string differences assert strings.
     *
     * @param expected   the expected
     * @param actual     the actual
     * @param ignoreCase the ignore case
     * @return the assert strings
     */
    public AssertStrings addStepForStringDifferences(final String expected, final String actual, boolean ignoreCase) {
        final String diff = getEasyToDebugStringForStringDifferences(expected, actual, ignoreCase);
        TS.step().action().createInfo("Differnce in Lines this will break it out to help debug.  " +
                "Will show like << char >> char[Ascii char value].",
            "To help see issues, right click and inspect the textarea.",
            diff, true);
        TS.log().debug(diff);
        return this;
    }


    /**
     * Gets easy to debug string for string differences. This will generate a message with a line per
     * char compare and char ascii number to help see the difference, even for hidden control characters.
     *
     * @param expected   the expected
     * @param actual     the actual
     * @param ignoreCase the ignore case
     * @return the easy to debug string for string differences
     */
    public String getEasyToDebugStringForStringDifferences(final String expected, final String actual, boolean ignoreCase) {
        if (null != expected && null != actual) {
            final char[] expectedArray = expected.toCharArray();
            final char[] actualArray = actual.toCharArray();
            final StrBuilder strBuilder = new StrBuilder();
            int maxIndex = expectedArray.length > actualArray.length ? expectedArray.length : actualArray.length;
            String expectedChar;
            String actualChar;
            strBuilder.appendln("#[Char](Ascii)eq[Char](Ascii)");
            for (int ctr = 0; ctr < maxIndex; ctr++) {
                expectedChar = getCharWithAsciiForDebug(ctr, expectedArray);
                actualChar = getCharWithAsciiForDebug(ctr, actualArray);
                strBuilder.append((ctr + 1) + "#");
                if (StringUtils.equals(expectedChar, actualChar) ||
                    (ignoreCase && StringUtils.equalsIgnoreCase(expectedChar, actualChar))) {
                    strBuilder.appendln(StringEscapeUtils.escapeJava(expectedChar) + " == " +
                        StringEscapeUtils.escapeJava(actualChar));
                } else {
                    strBuilder.appendln(StringEscapeUtils.escapeJava(expectedChar) + " != " +
                        StringEscapeUtils.escapeJava(actualChar) + "  <error>");
                }
            }
            return strBuilder.toString();
        }
        return null;
    }

    /**
     * Gets char with ascii for debug.
     *
     * @param index       the index
     * @param stringValue the string value
     * @return the char with ascii for debug
     */
    public String getCharWithAsciiForDebug(final int index, final String stringValue) {
        return getCharWithAsciiForDebug(index, (stringValue != null ? stringValue.toCharArray() : null));
    }

    /**
     * Gets char to make the code cleaner, if not in index will return null, if is will return a string with the char and char Ascii value.
     *
     * @param index     the index
     * @param charArray the char array
     * @return the char
     */
    public String getCharWithAsciiForDebug(final int index, final char[] charArray) {
        if (charArray != null && index < charArray.length) {
            return "[ " + CharUtils.toString(charArray[index]) + " ](" + (int) charArray[index] + ")";
        }
        return null;
    }

    /**
     * Starts with assert strings.
     *
     * @param expectedPrefix the expected prefix
     * @return the assert strings
     */
    public AssertStrings startsWith(final String expectedPrefix) {
        return runAssert(" - expected String[" + getActual() + "] to startWith " + expectedPrefix,
            "startsWith",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.startsWith(getActual(), expectedPrefix));
                return true;
            }, expectedPrefix, getActual());
    }

    /**
     * Starts with ignore case assert strings.
     *
     * @param expectedPrefix the expected prefix
     * @return the assert strings
     */
    public AssertStrings startsWithIgnoreCase(final String expectedPrefix) {
        return runAssert(" - expected String[" + getActual() + "] to " +
                "startsWithIgnoreCase " + expectedPrefix,
            "startsWithIgnoreCase",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.startsWithIgnoreCase(getActual(), expectedPrefix));
                return true;
            }, expectedPrefix, getActual());
    }

    /**
     * Ends with assert strings.
     *
     * @param expectedSuffix the expected suffix
     * @return the assert strings
     */
    public AssertStrings endsWith(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWith " + expectedSuffix,
            "endsWith",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.endsWith(getActual(), expectedSuffix));
                return true;
            }, expectedSuffix, getActual());
    }

    /**
     * Ends with ignore case assert strings.
     *
     * @param expectedSuffix the expected suffix
     * @return the assert strings
     */
    public AssertStrings endsWithIgnoreCase(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWithIgnoreCase " + expectedSuffix,
            "endsWithIgnoreCase",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(getActual(), expectedSuffix));
                return true;
            }, expectedSuffix, getActual());
    }

    /**
     * Contains assert strings.
     *
     * @param expectedValueToContain the expected value to contain
     * @return the assert strings
     */
    public AssertStrings contains(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to contain " + expectedValueToContain,
            "contains",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.contains(getActual(), expectedValueToContain));
                return true;
            }, expectedValueToContain, getActual());
    }

    /**
     * Contains ignore case assert strings.
     *
     * @param expectedValueToContain the expected value to contain
     * @return the assert strings
     */
    public AssertStrings containsIgnoreCase(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to containsIgnoreCase " + expectedValueToContain,
            "containsIgnoreCase",
            (expected, actual, history) -> {
                Assert.assertTrue(StringUtils.containsIgnoreCase(getActual(), expectedValueToContain));
                return true;
            }, expectedValueToContain, getActual());
    }

    /**
     * Not ends with assert strings.
     *
     * @param expectedSuffix the expected suffix
     * @return the assert strings
     */
    public AssertStrings notEndsWith(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to notEndsWith " + expectedSuffix,
            "notEndsWith",
            (expected, actual, history) -> {
                Assert.assertFalse(StringUtils.endsWith(getActual(), expectedSuffix));
                return true;
            }, expectedSuffix, getActual());
    }

    /**
     * Not ends with ignore case assert strings.
     *
     * @param expectedSuffix the expected suffix
     * @return the assert strings
     */
    public AssertStrings notEndsWithIgnoreCase(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWithIgnoreCase " + expectedSuffix,
            "endsWithIgnoreCase",
            (expected, actual, history) -> {
                Assert.assertFalse(StringUtils.endsWithIgnoreCase(getActual(), expectedSuffix));
                return true;
            }, expectedSuffix, getActual());
    }

    /**
     * Not contains assert strings.
     *
     * @param expectedValueToContain the expected value to contain
     * @return the assert strings
     */
    public AssertStrings notContains(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to notContains " + expectedValueToContain,
            "notContains",
            (expected, actual, history) -> {
                Assert.assertFalse(StringUtils.contains(getActual(), expectedValueToContain));
                return true;
            }, expectedValueToContain, getActual());
    }


    /**
     * Not contains ignore case assert strings.
     *
     * @param expectedValueToContain the expected value to contain
     * @return the assert strings
     */
    public AssertStrings notContainsIgnoreCase(final String expectedValueToContain) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Assert.assertFalse(StringUtils.containsIgnoreCase(actual, expected));
            return true;
        };
        return runAssert(" - expected String[" + getActual() + "] to notContainsIgnoreCase " + expectedValueToContain,
            "notContainsIgnoreCase", assertStatement, expectedValueToContain, getActual());
    }

    /**
     * Size assert number.
     *
     * @return the assert number
     */
    public AssertNumber<Integer> size() {
        if (canAssertRun("stringSize", true)) {
            return new AssertNumber<Integer>(getActual().length(), getAsserts())
                .withMessage("String length of " + getActual());
        }
        return new AssertNumber<Integer>(null, getAsserts());
    }

    /**
     * Is empty assert strings.
     *
     * @return the assert strings
     */
    public AssertStrings isEmpty() {
        return isEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("");
            return StringUtils.isEmpty(actual.toString());
        });
    }

    /**
     * Is not empty assert strings.
     *
     * @return the assert strings
     */
    public AssertStrings isNotEmpty() {
        return isNotEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("String to not be empty");
            return StringUtils.isNotEmpty(actual.toString());
        });
    }

    /**
     * Is numeric assert strings.
     *
     * @return the assert strings
     */
    public AssertStrings isNumeric() {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            history.setMessage(String.format("expected String[%s] to be numeric", actual));
            history.setExpectedForHistory("Expect String to be Numeric");
            Assert.assertTrue(StringUtils.isNumeric(actual));
            return true;
        };
        return runAssert("isNumeric", assertStatement, null, getActual(), false, null);
    }

    /**
     * Match assert strings.
     *
     * @param expectedPattern the expected pattern
     * @return the assert strings
     */
    public AssertStrings match(final String expectedPattern) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Pattern pattern = Pattern.compile(expected);
            Matcher matcher = pattern.matcher(actual);
            history.setMessage(String.format("Expected String[%s] to match pattern[%s]", actual, expectedPattern));
            Assert.assertTrue(matcher.find());
            return true;
        };
        return runAssert("match", assertStatement, expectedPattern, getActual(), false, null);
    }

    /**
     * Not match assert strings.
     *
     * @param expectedPattern the expected pattern
     * @return the assert strings
     */
    public AssertStrings notMatch(final String expectedPattern) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Pattern pattern = Pattern.compile(expected);
            Matcher matcher = pattern.matcher(actual);
            history.setMessage(String.format("Expected String[%s] to not match pattern[%s]", actual, expectedPattern));
            Assert.assertFalse(matcher.find());
            return true;
        };
        return runAssert("notMatch", assertStatement, expectedPattern, getActual(), false, null);
    }
    //CHECKSTYLE.ON: checkstyle:lineWrappingIndentation
}
