package org.testah.framework.report.asserts;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.dto.StepAction;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Assert strings.
 */
public class AssertStrings extends AbstractAssertBase<AssertStrings, String> {

    /**
     * Instantiates a new Assert strings.
     */
    public AssertStrings(final String actual) {
        this(actual, TS.asserts());
    }

    public AssertStrings(final String actual, final VerboseAsserts verboseAsserts) {
        super(actual,verboseAsserts);
    }

    public AssertStrings equalsTo(final String expected) {
        return equalsTo(expected, false);
    }

    public AssertStrings equalsToIgnoreCase(final String expected) {
        return equalsTo(expected, true);
    }

    protected AssertStrings equalsTo(final String expected, boolean ignoreCase) {
        return equalsTo("", expected, getActual(), ignoreCase);
    }

    protected AssertStrings equalsTo(final String extraMessage, final String expected, final String actual, boolean ignoreCase) {
        if (actual == null || expected == null || !expected.contains(File.separator)) {
            runAssert("Traditional equals" + extraMessage,
                    "equalsTo",
                    () -> {
                        if (ignoreCase) {
                            Assert.assertTrue(StringUtils.equalsIgnoreCase(expected, actual));
                        } else {
                            Assert.assertTrue(StringUtils.equals(expected, actual));
                        }
                    }, expected, actual, true, () -> {
                        addStepForStringDifferences(expected, actual, ignoreCase);
                    });
            return this;
        }
        TS.log().debug("Since newline found in expected, splitting on newline and comparing as array of strings");
        equalsTo(StringUtils.split(getActual(), File.separator), StringUtils.split(expected, File.separator), ignoreCase);
        return this;
    }


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
        }
        return this;
    }

    /**
     * Add step for string differences assert strings.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return the assert strings
     */
    public AssertStrings addStepForStringDifferences(final String expected, final String actual, boolean ignoreCase) {
        final String diff = getEasyToDebugStringForStringDifferences(expected, actual, ignoreCase);
        TS.addStepAction(StepAction.createInfo("Differnce in Lines this will break it out to help debug.  "
                        + "Will show like << char >> char[Ascii char value].",
                "To help see issues, right click and inspect the textarea.",
                diff, true));
        TS.log().debug(diff);
        return this;
    }


    /**
     * Gets easy to debug string for string differences. This will generate a message with a line per
     * char compare and char ascii number to help see the difference, even for hidden control characters.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return the easy to debug string for string differences
     */
    public String getEasyToDebugStringForStringDifferences(final String expected, final String actual, boolean ignoreCase) {
        if (null != expected && null != actual) {
            final char[] expectedArray = expected.toCharArray();
            final char[] actualArray = actual.toCharArray();
            final StringBuilder strBuilder = new StringBuilder();
            int maxIndex = expectedArray.length > actualArray.length ? expectedArray.length : actualArray.length;
            String expectedChar;
            String actualChar;
            for (int ctr = 0; ctr < maxIndex; ctr++) {
                expectedChar = getCharWithAsciiForDebug(ctr, expectedArray);
                actualChar = getCharWithAsciiForDebug(ctr, actualArray);
                if (StringUtils.equals(expectedChar, actualChar) ||
                        (ignoreCase && StringUtils.equalsIgnoreCase(expectedChar, actualChar))) {
                    strBuilder.append(expectedChar + " == " + actualChar + "\n");
                } else {
                    strBuilder.append(expectedChar + " != " + actualChar + "  <error found>\n");
                }
            }
            return strBuilder.toString();
        }
        return null;
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
            return "[ " + CharUtils.toString(charArray[index]) + " ] char[" + (int) charArray[index] + "]";
        }
        return null;
    }

    public String getCharWithAsciiForDebug(final int index, final String stringValue) {
        return getCharWithAsciiForDebug(index, (stringValue != null ? stringValue.toCharArray() : null));
    }




    public AssertStrings startsWith(final String expectedPrefix) {
        return runAssert(" - expected String[" + getActual() + "] to startWith " + expectedPrefix,
                "startsWith",
                () -> {
                    Assert.assertTrue(StringUtils.startsWith( getActual(), expectedPrefix));
                }, expectedPrefix, getActual());
    }

    public AssertStrings startsWithIgnoreCase(final String expectedPrefix) {
        return runAssert(" - expected String[" + getActual() + "] to "
                        + "startsWithIgnoreCase " + expectedPrefix,
                "startsWithIgnoreCase",
                () -> {
                    Assert.assertTrue(StringUtils.startsWithIgnoreCase(getActual(), expectedPrefix));
                }, expectedPrefix, getActual());
    }

    public AssertStrings endsWith(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWith " + expectedSuffix,
                "endsWith",
                () -> {
                    Assert.assertTrue(StringUtils.endsWith(getActual(), expectedSuffix));
                }, expectedSuffix, getActual());
    }

    public AssertStrings endsWithIgnoreCase(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWithIgnoreCase " + expectedSuffix,
                "endsWithIgnoreCase",
                () -> {
                    Assert.assertTrue(StringUtils.endsWithIgnoreCase(getActual(), expectedSuffix));
                }, expectedSuffix, getActual());
    }

    public AssertStrings contains(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to contain " + expectedValueToContain,
                "contains",
                () -> {
                    Assert.assertTrue(StringUtils.contains(getActual(), expectedValueToContain));
                }, expectedValueToContain, getActual());
    }

    public AssertStrings containsIgnoreCase(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to containsIgnoreCase " + expectedValueToContain,
                "containsIgnoreCase",
                () -> {
                    Assert.assertTrue(StringUtils.containsIgnoreCase(getActual(), expectedValueToContain));
                }, expectedValueToContain, getActual());
    }

    public AssertStrings notEndsWith(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to notEndsWith " + expectedSuffix,
                "notEndsWith",
                () -> {
                    Assert.assertFalse(StringUtils.endsWith(getActual(), expectedSuffix));
                }, expectedSuffix, getActual());
    }

    public AssertStrings notEndsWithIgnoreCase(final String expectedSuffix) {
        return runAssert(" - expected String[" + getActual() + "] to endsWithIgnoreCase " + expectedSuffix,
                "endsWithIgnoreCase",
                () -> {
                    Assert.assertFalse(StringUtils.endsWithIgnoreCase(getActual(), expectedSuffix));
                }, expectedSuffix, getActual());
    }

    public AssertStrings notContains(final String expectedValueToContain) {
        return runAssert(" - expected String[" + getActual() + "] to notContains " + expectedValueToContain,
                "notContains",
                () -> {
                    Assert.assertFalse(StringUtils.contains(getActual(), expectedValueToContain));
                }, expectedValueToContain, getActual());
    }


    public AssertStrings notContainsIgnoreCase(final String expectedValueToContain) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Assert.assertFalse(StringUtils.containsIgnoreCase(actual, expected));
            return true;
        };
        return runAssert(" - expected String[" + getActual() + "] to notContainsIgnoreCase " + expectedValueToContain,
                "notContainsIgnoreCase",assertStatement, expectedValueToContain, getActual());
    }

    public AssertNumber<Integer> size() {
        if(canAssertRun("stringSize",true)) {
            return new AssertNumber<Integer>(getActual().length(),getAsserts())
                    .withMessage("String length of " + getActual());
        }
        return new AssertNumber<Integer>(null,getAsserts());
    }

    public AssertStrings isEmpty() {
        return isEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("");
            return StringUtils.isEmpty(actual.toString());
        });
    }

    public AssertStrings isNotEmpty() {
        return isNotEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("String to not be empty");
            return StringUtils.isNotEmpty(actual.toString());
        });
    }

    public AssertStrings isNumeric() {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            history.setMessage(String.format("expected String[%s] to be numeric",actual));
            history.setExpectedForHistory("Expect String to be Numeric");
            Assert.assertTrue(StringUtils.isNumeric(actual));
            return true;
        };
        return runAssert("isNumeric",assertStatement, null, getActual(), false, null);
    }

    public AssertStrings match(final String expectedPattern) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Pattern pattern = Pattern.compile(expected);
            Matcher matcher = pattern.matcher(actual);
            history.setMessage(String.format("Expected String[%s] to match pattern[%s]",actual, expectedPattern));
            Assert.assertTrue(matcher.find());
            return true;
        };
        return runAssert("match",assertStatement, expectedPattern, getActual(), false, null);
    }

    public AssertStrings notMatch(final String expectedPattern) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            Pattern pattern = Pattern.compile(expected);
            Matcher matcher = pattern.matcher(actual);
            history.setMessage(String.format("Expected String[%s] to not match pattern[%s]",actual, expectedPattern));
            Assert.assertFalse(matcher.find());
            return true;
        };
        return runAssert("notMatch",assertStatement, expectedPattern, getActual(), false, null);
    }

}
