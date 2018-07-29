package org.testah.framework.report.asserts;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.framework.report.VerboseAsserts;

public class AssertStringsTest {

    private static final String testLineA = "This is a A test";
    private static final String testLineB = "This is a B test";

    public AssertStrings assertStrings = new AssertStrings(new VerboseAsserts().onlyVerify());

    @Before
    public void setup()
    {

    }

    @Test
    public void testBothAreEmptyString()
    {
        assertStrings.deepAssert("", "");
    }

    @Test
    public void testExpectedIsEmptyString()
    {
        assertStrings.deepAssert("", testLineA);
    }

    @Test
    public void testActualIsEmptyString()
    {
        assertStrings.deepAssert(testLineA, "");
    }

    @Test
    public void testBothHaveNullValues()
    {
        assertStrings.deepAssert(null, null);
    }

    @Test
    public void testExpectedHasMoreLinesThanActual()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 2), getStringUsedWithNumberOfLines(testLineA, 1));
    }

    @Test
    public void testActualHasMoreLinesThanExpected()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 10), getStringUsedWithNumberOfLines(testLineA, 14));
    }

    @Test
    public void testOneLineStringWithDifferentValues()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 1), getStringUsedWithNumberOfLines(testLineB, 1));
    }

    @Test
    public void testMultiLineStringWithDifferentValues()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 10), getStringUsedWithNumberOfLines(testLineB, 10));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesSameNumberOfLines()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 5) +
                getStringUsedWithNumberOfLines(testLineB, 2)
                + getStringUsedWithNumberOfLines(testLineA, 3), getStringUsedWithNumberOfLines(testLineB, 10));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesActualHasMoreLines()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 5)
                + getStringUsedWithNumberOfLines(testLineB, 2)
                + getStringUsedWithNumberOfLines(testLineA, 3), getStringUsedWithNumberOfLines(testLineB, 12));
    }

    @Test
    public void testWithSomeSameLinesAndSomeDifferentLinesExpectedHasMoreLines()
    {
        assertStrings.deepAssert(getStringUsedWithNumberOfLines(testLineA, 5)
               + getStringUsedWithNumberOfLines(testLineB, 2)
               + getStringUsedWithNumberOfLines(testLineA, 3), getStringUsedWithNumberOfLines(testLineB, 9));
    }

    @Test
    public void testDiffBetweenStrings()
    {
        final String expected = "POOLED FUNDS RETURNS BY ASSET CLASS,,,,,,,,,,,,,,,,,";
        final String actual = "\tPOOLED FUNDS RETURNS BY ASSET CLASS,,,,,,,,,,,,,,,,,";

        String diffString = assertStrings.getEasyToDebugStringForStringDifferences(expected, actual);
        System.out.println(diffString);
        Assert.assertEquals("Check 1st line with expected difference",
                diffString.split("\n")[0], "[ P ] char[80] != [ \t ] char[9]  <error found>");
    }

    private String getStringUsedWithNumberOfLines(final String lineValue, final int numberOfLines)
    {
        StringBuilder strBuilder = new StringBuilder(lineValue);
        for (int ctr = 1; ctr < numberOfLines; ctr++)
        {
            strBuilder.append("\n" + lineValue);
        }
        return strBuilder.toString();
    }

    @After
    public void tearDown()
    {

    }

}
