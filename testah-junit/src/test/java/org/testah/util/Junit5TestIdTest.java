package org.testah.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestPlanJUnit5
class Junit5TestIdTest
{
    static final boolean INCL_ROW_NUM = true;
    static final boolean EXCL_ROW_NUM = false;

    @TestCaseWithParamsJUnit5
    @MethodSource("shortenStringProvider")
    void shortenString(int maxLength, String input, String expected)
    {
        assertEquals(expected, Junit5TestId.shortenString(input, maxLength));
    }

    static Stream<Arguments> shortenStringProvider() {
        return Stream.of(
            arguments(10, "EeEAaeBbeaiouA", "EEABbaiouA"),
            arguments(20, "EeEAaeBbeaiouA", "EeEAaeBbeaiouA"),
            arguments(5, "EeEAaeBbeaiouA", "EEABb"),
            arguments(5, "eeeeeeeeeeeeeee", "eeeee"),
            arguments(5, "ABCEFGHIJ", "ABCEF")
        );
    }

    @TestCaseWithParamsJUnit5
    @MethodSource("testCaseProvider")
    void getDeterministicPart(int maxLength, int randomLength, String expectedWithIndex, String expectedNoIndex, int index, TestInfo testInfo)
    {
        Junit5TestId junit5TestId = new Junit5TestId(testInfo, maxLength, randomLength);
        assertEquals(expectedWithIndex, junit5TestId.getDeterministicPart(INCL_ROW_NUM));
        assertEquals(expectedNoIndex, junit5TestId.getDeterministicPart(EXCL_ROW_NUM));
        assertEquals(index, junit5TestId.getDataSetIndex());
        assertTrue(junit5TestId.getTestCaseId().length() <= maxLength);
    }

    static Stream<Arguments> testCaseProvider() {
        return Stream.of(
            arguments(55, 0, "Junit5TestIdTestgetDeterministicPart1", "Junit5TestIdTestgetDeterministicPart", 1),
            arguments(25, 0, "Jnt5TstIdTstgtDtrmnstcPr2", "Jnt5TstIdTstgtDtrmnstcPr", 2),
            arguments(55, 5, "Junit5TestIdTestgetDeterministicPart3", "Junit5TestIdTestgetDeterministicPart", 3),
            arguments(35, 10, "Jnt5TstIdTstgtDtrmnstcPr4", "Jnt5TstIdTstgtDtrmnstcPr", 4),
            arguments(55, 0, "Junit5TestIdTestgetDeterministicPart5", "Junit5TestIdTestgetDeterministicPart", 5),
            arguments(10, 10, "", "", 6),
            arguments(55, 60, "", "", 7),
            arguments(11, 10, "8", "", 8),
            arguments(55, 0, "Junit5TestIdTestgetDeterministicPart9", "Junit5TestIdTestgetDeterministicPart", 9),
            arguments(55, 0, "Junit5TestIdTestgetDeterministicPart10", "Junit5TestIdTestgetDeterministicPart", 10),
            arguments(12, 10, "11", "", 11),
            arguments(25, 0, "Jnt5TstIdTstgtDtrmnstcP12", "Jnt5TstIdTstgtDtrmnstcP", 12)
        );
    }

    @TestCaseWithParamsJUnit5
    @MethodSource("testInfoProvider")
    void testTestInfo(String displayName, Class clazz, Method method,
                      int expIndex, String expIndexString, String expectedId)
    {
        TestInfo testInfo = new TestahTestInfo(displayName, clazz, method);
        Junit5TestId junit5TestId = new Junit5TestId(testInfo, 50, 0);
        assertEquals(expIndex, junit5TestId.getDataSetIndex());
        assertEquals(expIndexString, junit5TestId.getDataSetId());
        assertEquals(expectedId, junit5TestId.getDeterministicPart(INCL_ROW_NUM));
    }

    static Stream<Arguments> testInfoProvider() throws NoSuchMethodException
    {
        Method method = String.class.getMethod("indexOf", String.class);
        Class clazz = String.class;
        return Stream.of(
            arguments("displayName => 3 - arg1, arg2", clazz, method, 3, "3", "StringindexOf3"),
            arguments("displayName => 2 - arg1, arg2", clazz, null,  2, "2", "StringMETHOD_UNDEFINED2"),
            arguments("displayName => 4 - arg1, arg2", null, method,  4, "4", "CLASS_UNDEFINEDindexOf4"),
            arguments("displayName => 1 - arg1, arg2", null, null,  1, "1", "CLASS_UNDEFINEDMETHOD_UNDEFINED1"),
            arguments(" => 123 - ", clazz, method, 123, "123", "StringindexOf123"),
            arguments("displayName => - arg1, arg2", clazz, method,  0, "", "StringindexOf"),
            arguments("displayName => -5 - arg1, arg2", clazz, method,  0, "", "StringindexOf"),
            arguments("displayName => 912345678901234456788888 - arg1, arg2", clazz, method,  0, "", "StringindexOf")
        );
    }

    @TestCaseWithParamsJUnit5
    @MethodSource("testMatchProvider")
    void testMatch(int maxLength, int randomLength) throws NoSuchMethodException
    {
        Method method = String.class.getMethod("indexOf", String.class);
        Class clazz = String.class;
        String baseDeterministicPart = "StringindexOf"; //14 chars
        String deterministicPart = "StringindexOf12345"; //14 chars
        String displayName = "name => 12345 - args";
        int expectedLength = Math.min(deterministicPart.length() + randomLength, maxLength);
        TestInfo testInfo = new TestahTestInfo(displayName, clazz, method);
        Junit5TestId junit5TestId = new Junit5TestId(testInfo, maxLength, randomLength);
        assertEquals(baseDeterministicPart + ".*", junit5TestId.getRegexMatcher());
        assertEquals(baseDeterministicPart + "12345.*", junit5TestId.getRegexMatcher(INCL_ROW_NUM));
        assertEquals(expectedLength, junit5TestId.getTestCaseId().length());
        assertTrue((baseDeterministicPart + "avcd").matches(junit5TestId.getRegexMatcher()));
        assertTrue((baseDeterministicPart + "12345avcd").matches(junit5TestId.getRegexMatcher(INCL_ROW_NUM)));
    }

    static Stream<Arguments> testMatchProvider() throws NoSuchMethodException
    {
        return Stream.of(
            arguments(25, 5),
            arguments(50, 15)
        );
    }
}