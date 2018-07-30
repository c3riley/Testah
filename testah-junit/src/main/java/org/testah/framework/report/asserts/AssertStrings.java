package org.testah.framework.report.asserts;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.testah.TS;
import org.testah.framework.dto.StepAction;
import org.testah.framework.report.VerboseAsserts;

/**
 * The type Assert strings.
 */
public class AssertStrings {

    private final VerboseAsserts verboseAsserts;

    /**
     * Instantiates a new Assert strings.
     */
    public AssertStrings() {
        this(TS.asserts());
    }

    /**
     * Instantiates a new Assert strings.
     *
     * @param verboseAsserts the verbose asserts
     */
    public AssertStrings(final VerboseAsserts verboseAsserts) {
        this.verboseAsserts = verboseAsserts;
    }

    /**
     * Deep assert, will go through each line of a String File Content and assert each for easier troubleshooting.
     *
     * @param expectedLines the expected lines
     * @param actualLines   the actual lines
     * @return the assert strings
     */
    public AssertStrings deepAssert(final String expectedLines, final String actualLines) {
        final boolean throwOnFail = verboseAsserts.getThrowExceptionOnFail();
        try {
            //Turn off so each line can be checked

            verboseAsserts.setThrowExceptionOnFail(false);
            int ctr = 0;

            if (!verboseAsserts.notNull("Check that expectedLines is not null", expectedLines)) {
                return this;
            }

            if (!verboseAsserts.notNull("Check that actualLines is not null", actualLines)) {
                return this;
            }

            final String[] expectedLineArray = expectedLines.split("\n");
            final String[] actualLineArray = actualLines.split("\n");

            verboseAsserts.equalsTo("Check that the 2 files contain the same number of lines",
                    expectedLineArray.length, actualLineArray.length);

            for (final String expectedLine : expectedLineArray) {
                if (ctr < actualLineArray.length) {
                    if (!verboseAsserts.equalsTo(String.format("Checking File Lines[%d] are equal", (ctr + 1))
                            + " - To help debug issues, right click and inspect the textarea "
                            + "some characters will only be seen this way.", expectedLine, actualLineArray[ctr])) {
                        addStepForStringDifferences(expectedLine, actualLineArray[ctr]);
                    }
                } else {
                    verboseAsserts.equalsTo(String.format("Checking File Lines[%d] are equal", (ctr + 1)), expectedLine,
                            "NO_LINE[" + ctr + "]_IN_ACTUAL_FILE");
                }
                ctr++;
            }
            for (int actualLinesMoreThanExpected = ctr; actualLinesMoreThanExpected < actualLineArray.length;
                 actualLinesMoreThanExpected++) {
                verboseAsserts.equalsTo(String.format("Checking File Lines[%d] are equal", (actualLinesMoreThanExpected + 1)),
                        "NO_LINE[" + actualLinesMoreThanExpected + "]_IN_EXPECTED_FILE",
                        actualLineArray[ctr++]);
            }

        } finally {
            //Turn on, so any follow on assert fails will throw exception as expected.
            verboseAsserts.setThrowExceptionOnFail(throwOnFail);
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
    public AssertStrings addStepForStringDifferences(final String expected, final String actual) {
        final String diff = getEasyToDebugStringForStringDifferences(expected, actual);
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
    public String getEasyToDebugStringForStringDifferences(final String expected, final String actual) {
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

                if (StringUtils.equals(expectedChar, actualChar)) {
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
        if (index < charArray.length) {
            return "[ " + CharUtils.toString(charArray[index]) + " ] char[" + (int) charArray[index] + "]";
        }
        return null;
    }

}
