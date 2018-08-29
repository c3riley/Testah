package org.testah.framework.report.asserts;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AssertNotAllowedWithNullActual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssertStringsTestComplete {

    private VerboseAsserts posAssert;
    private VerboseAsserts negAssert;

    @BeforeEach
    void setUpTest() {
        posAssert = new VerboseAsserts();
        negAssert = new VerboseAsserts().onlyVerify();
    }

    @AfterEach
    void tearDownTest() {
    }

    private Map<String, String> getEqualsDataPass() {
        Map<String, String> data = new HashMap<>();
        data.put("", "");
        data.put("\t", "\t");
        data.put(null, null);
        data.put("test", "test");
        data.put("this is a test", "this is a test");
        data.put("1234", "1234");
        data.put("\"", "\"");
        data.put(" ", " ");
        return data;
    }

    private Map<String, String> getEqualsDataFail() {
        Map<String, String> data = new HashMap<>();
        data.put("", " ");
        data.put("\t", "");
        data.put(null, "");
        data.put("", null);
        data.put("test", "tes");
        data.put("this is a test", "this is a test ");
        data.put("12 34", "1234");
        data.put("'", "\"");
        data.put("", " ");
        return data;
    }

    @Test
    void equalsToTest() {
        getEqualsDataPass().forEach((expected, actual) -> {
            Assert.assertTrue(new AssertStrings(actual).equalsTo(expected).isPassed());
            Assert.assertTrue(new AssertStrings(actual).equalsToWithReflection(expected).isPassed());
        });
    }

    @Test
    void equalsToTestNeg() {
        getEqualsDataFail().forEach((expected, actual) -> {
            Assert.assertTrue(new AssertStrings(actual, negAssert).equalsTo(expected).isFailed());
            Assert.assertTrue(new AssertStrings(actual, negAssert).equalsToWithReflection(expected).isFailed());
        });
    }

    @Test
    void equalsToIgnoreCaseTest() {
        getEqualsDataPass().forEach((expected, actual) -> {
            Assert.assertTrue(new AssertStrings(actual).equalsToIgnoreCase(expected).isPassed());
            Assert.assertTrue(new AssertStrings(StringUtils.upperCase(actual)).equalsToIgnoreCase(expected).isPassed());
            Assert.assertTrue(new AssertStrings(actual).equalsToIgnoreCase(StringUtils.upperCase(expected)).isPassed());
        });
    }

    private String getMultilineString(final int numberOfLines) {
        StrBuilder str = new StrBuilder();
        for (int ctr = 0; ctr < numberOfLines; ctr++) {
            for (int wordCtr = RandomUtils.nextInt(0, 8); wordCtr > 0; wordCtr--) {
                str.append(RandomStringUtils.random(RandomUtils.nextInt(0, 23)));
            }
            str.append("~REPLACE_" + ctr + "~");
            str.append(System.lineSeparator());
        }
        return str.toString();
    }

    @Test
    void equalsToWithMultiLinesTest() {
        for (int lineCount : new int[]{0, 1, 2, 5, 20, 500}) {
            String multiLine = getMultilineString(lineCount);
            Assert.assertTrue(new AssertStrings(multiLine,negAssert).equalsTo(multiLine).isPassed());
            Assert.assertTrue(new AssertStrings(multiLine,negAssert).equalsTo(multiLine+getMultilineString(2)).isFailed());
            Assert.assertTrue(new AssertStrings(multiLine+getMultilineString(1),negAssert).equalsTo(multiLine).isFailed());
            if (lineCount > 1) {
                String actualWithChange = multiLine.replace("~REPLACE_1~",
                        "").replace("~REPLACE_4~",
                        "wie er llNot MAtch").replace("~REPLACE_17~",
                        "willew  Not e r~!#$*$!~|;\t\fwerMAtch").replace("~REPLACE_42~",
                        "wie er llNot MAtch").replace("~REPLACE_498~",
                        "wie er ch");
                Assert.assertTrue(new AssertStrings(actualWithChange,negAssert).equalsTo(multiLine).isFailed());
                Assert.assertTrue(new AssertStrings(actualWithChange,negAssert).equalsTo(multiLine+getMultilineString(1)).isFailed());
                Assert.assertTrue(new AssertStrings(actualWithChange+getMultilineString(4),negAssert).equalsTo(multiLine).isFailed());
            }
        }
    }

    @Test
    void addStepForStringDifferencesTest() {
    }

    @Test
    void getEasyToDebugStringForStringDifferencesTest() {
    }

    @Test
    void getCharWithAsciiForDebugTest() {
        final String data = null;
        Assert.assertNull(new AssertStrings("test", negAssert).getCharWithAsciiForDebug(1, data));
        Assert.assertEquals("[ e ](101)", new AssertStrings("test", negAssert).getCharWithAsciiForDebug(1, "test"));
    }

    @Test
    void getCharWithAsciiForDebug1Test() {
    }

    @Test
    void startsWithTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).startsWith("te").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).startsWith("TE").isFailed());
    }

    @Test
    void startsWithIgnoreCaseTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).startsWithIgnoreCase("TE").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).startsWithIgnoreCase(" T").isFailed());
    }

    @Test
    void endsWithTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).endsWith("st").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).endsWith("ST").isFailed());
    }

    @Test
    void endsWithIgnoreCaseTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).endsWithIgnoreCase("ST").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).endsWithIgnoreCase("t\t").isFailed());
    }

    @Test
    void containsTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).contains("es").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).contains("e ").isFailed());
        Assert.assertTrue(new AssertStrings("test", negAssert).contains("e").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).contains("").isPassed());
    }

    @Test
    void containsIgnoreCaseTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).containsIgnoreCase("Es").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).containsIgnoreCase("E ").isFailed());
    }

    @Test
    void notEndsWithTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).notEndsWith("ST").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).notEndsWith("st").isFailed());
    }

    @Test
    void notEndsWithIgnoreCaseTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).notEndsWithIgnoreCase("2").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).notEndsWithIgnoreCase("ST").isFailed());
    }

    @Test
    void notContainsTest() {
        Assert.assertTrue(new AssertStrings("test", negAssert).notContains("2").isPassed());
        Assert.assertTrue(new AssertStrings("test", negAssert).notContains("e s").isPassed());
        Assertions.assertThrows(AssertNotAllowedWithNullActual.class, () -> {
            Assert.assertTrue(new AssertStrings(null, negAssert).notContains("").isPassed());
        });
        Assert.assertTrue(new AssertStrings(" wer52 525rt 4rfewsre\n", negAssert).notContains("\n ").isPassed());
    }

    @Test
    void notContainsIgnoreCaseTest() {
        Assert.assertTrue(new AssertStrings("Test23", negAssert).notContainsIgnoreCase("Te2").isPassed());
        Assert.assertTrue(new AssertStrings("Test1234", negAssert).notContainsIgnoreCase("1 ").isPassed());
    }

    @Test
    void sizeTest() {
        Assert.assertTrue(new AssertStrings("", negAssert).size().equalsTo(0).isPassed());
        Assert.assertTrue(new AssertStrings("", negAssert).size().equalsTo(1).isFailed());

        Assertions.assertThrows(AssertNotAllowedWithNullActual.class, () -> {
            Assert.assertTrue(new AssertStrings(null, negAssert).size().equalsTo(0).isFailed());
        });

        Assert.assertTrue(new AssertStrings("   ", negAssert).size().equalsTo(3).isPassed());
        Assert.assertTrue(new AssertStrings("tttttttttttttt\t\t\t", negAssert).size().equalsTo(17).isPassed());
        Assert.assertTrue(new AssertStrings("", negAssert).size().equalsTo(0).isPassed());
    }

    @Test
    void isEmptyTest() {
        Assert.assertTrue(new AssertStrings("", negAssert).isEmpty().isPassed());
        Assert.assertTrue(new AssertStrings("\t", negAssert).isEmpty().isFailed());
        Assertions.assertThrows(AssertNotAllowedWithNullActual.class, () -> {
            Assert.assertTrue(new AssertStrings(null, negAssert).isEmpty().isFailed());
        });
        Assert.assertTrue(new AssertStrings(" ", negAssert).isEmpty().isFailed());
        Assert.assertTrue(new AssertStrings("o", negAssert).isEmpty().isFailed());
    }

    @Test
    void isNotEmptyTest() {
        Assert.assertTrue(new AssertStrings("", negAssert).isNotEmpty().isFailed());
        Assert.assertTrue(new AssertStrings("\t", negAssert).isNotEmpty().isPassed());
        Assertions.assertThrows(AssertNotAllowedWithNullActual.class, () -> {
            Assert.assertTrue(new AssertStrings(null, negAssert).isNotEmpty().isPassed());
        });
        Assert.assertTrue(new AssertStrings(" ", negAssert).isNotEmpty().isPassed());
        Assert.assertTrue(new AssertStrings("o", negAssert).isNotEmpty().isPassed());
    }

    @Test
    void isNumericTest() {
    }

    @Test
    void matchTest() {
    }

    @Test
    void notMatchTest() {
    }

    @Test
    void getAssertsTest() {
    }

    @Test
    void setMessageTest() {
    }

    @Test
    void withMessageTest() {
    }

    @Test
    void getMessageTest() {
    }

    @Test
    void getMessage1Test() {
    }

    @Test
    void getStatusTest() {
    }

    @Test
    void addStatusTest() {
    }

    @Test
    void resetStatusTest() {
    }

    @Test
    void isPassedTest() {
    }

    @Test
    void isFailedTest() {
    }

    @Test
    void canAssertRunTest() {
    }

    @Test
    void canAssertRun1Test() {
    }

    @Test
    void canAssertRun2Test() {
    }

    @Test
    void canAssertRun3Test() {
    }

    @Test
    void runAssertTest() {
    }

    @Test
    void runAssert1Test() {
    }

    @Test
    void runAssert2Test() {
    }

    @Test
    void runAssert3Test() {
    }

    @Test
    void runAssert4Test() {
    }

    @Test
    void runAssert5Test() {
    }

    @Test
    void runAssert6Test() {
    }

    @Test
    void equalsTest() {
    }

    @Test
    void equalsTo4Test() {
    }

    @Test
    void equalsToWithReflectionTest() {
    }

    @Test
    void getActualTest() {
    }

    @Test
    void hashCodeTest() {
    }

    @Test
    void isEmpty1Test() {
    }

    @Test
    void isNotEmpty1Test() {
    }

    @Test
    void runAssert7Test() {
    }
}