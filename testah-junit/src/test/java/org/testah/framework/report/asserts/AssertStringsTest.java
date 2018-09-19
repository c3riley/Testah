package org.testah.framework.report.asserts;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.framework.report.VerboseAsserts;

public class AssertStringsTest {

    private static final String testLineA = "This is a A test";
    private static final String testLineB = "This is a B test";

    public AssertStrings assertStrings;

    @Before
    public void setup() {

    }

    @Test
    public void testBothAreEmptyString() {
        assertStrings = new AssertStrings("", new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo("");
    }

    @Test
    public void testExpectedIsEmptyString() {
        assertStrings = new AssertStrings(testLineA, new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo("");
    }

    @Test
    public void testActualIsEmptyString() {
        assertStrings = new AssertStrings("", new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(testLineA);
    }

    @Test
    public void testBothHaveNullValues() {
        assertStrings = new AssertStrings(null, new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(null);
    }

    @Test
    public void testExpectedHasMoreLinesThanActual() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineA, 1), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 2));
    }

    private String getStringUsedWithNumberOfLines(final String lineValue, final int numberOfLines) {
        StringBuilder strBuilder = new StringBuilder(lineValue);
        for (int ctr = 1; ctr < numberOfLines; ctr++) {
            strBuilder.append("\n" + lineValue);
        }
        return strBuilder.toString();
    }

    @Test
    public void testActualHasMoreLinesThanExpected() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineA, 14), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 10));
    }

    @Test
    public void testOneLineStringWithDifferentValues() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineB, 1), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 1));
    }

    @Test
    public void testMultiLineStringWithDifferentValues() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineB, 10), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 10));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesSameNumberOfLines() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineB, 10), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 5)
                + getStringUsedWithNumberOfLines(testLineB, 2)
                + getStringUsedWithNumberOfLines(testLineA, 3));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesActualHasMoreLines() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineB, 12), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 5)
                + getStringUsedWithNumberOfLines(testLineB, 2)
                + getStringUsedWithNumberOfLines(testLineA, 3));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesExpectedHasMoreLines() {
        assertStrings = new AssertStrings(getStringUsedWithNumberOfLines(testLineB, 9), new VerboseAsserts().onlyVerify());
        assertStrings.equalsTo(getStringUsedWithNumberOfLines(testLineA, 5)
                + getStringUsedWithNumberOfLines(testLineB, 2)
                + getStringUsedWithNumberOfLines(testLineA, 3));
    }

    @Test
    public void testDiffBetweenStrings() {
        final String expected = "CLASS,,,,,,,,,,,,,,,,,";
        final String actual = "\tCLASS,,,,,,,,,,,,,,,,,";
        AssertStrings assertStrings = new AssertStrings(actual, new VerboseAsserts().onlyVerify());
        String diffString = assertStrings.getEasyToDebugStringForStringDifferences(expected, actual, false);
        System.out.println(diffString);
        Assert.assertEquals("Check 1st line with expected difference", "1#[ C ](67) != [ \\t ](9)  <error>",
                diffString.split(System.lineSeparator())[1].trim());
    }

    @After
    public void tearDown() {

    }

}
