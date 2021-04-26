package org.testah.util;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testah.TS;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestPlanJUnit5
class JUnit5TestUtilTest
{
    static final boolean INCL_ROW_NUM = true;

    @TestCaseJUnit5
    void testSingle(TestInfo testInfo)
    {
        int maxLength = 50;
        int randomLength = 5;

        String testCaseId = JUnit5TestUtil.getTestCaseId(testInfo, maxLength, randomLength);
        Junit5TestId junit5TestId =  JUnit5TestUtil.getJunit5TestId(testInfo, maxLength, randomLength);
        assertTrue(testCaseId.matches(junit5TestId.getRegexMatcher()));
        assertTrue(testCaseId.matches(junit5TestId.getRegexMatcher(INCL_ROW_NUM)));
        assertTrue(junit5TestId.getTestCaseId().matches(junit5TestId.getRegexMatcher(INCL_ROW_NUM)));
        assertNotEquals(junit5TestId.getTestCaseId(), testCaseId);
    }

    @TestCaseWithParamsJUnit5
    @MethodSource("testMatchProvider")
    void testMultiple(int maxLength, int randomLength, TestInfo testInfo)
    {
        String testCaseId = JUnit5TestUtil.getTestCaseId(testInfo, maxLength, randomLength);
        Junit5TestId junit5TestId =  JUnit5TestUtil.getJunit5TestId(testInfo, maxLength, randomLength);
        assertTrue(testCaseId.matches(junit5TestId.getRegexMatcher()));
        assertTrue(testCaseId.matches(junit5TestId.getRegexMatcher(INCL_ROW_NUM)));
        assertTrue(junit5TestId.getTestCaseId().matches(junit5TestId.getRegexMatcher(INCL_ROW_NUM)));
        assertNotEquals(junit5TestId.getTestCaseId(), testCaseId);
    }

    static Stream<Arguments> testMatchProvider()
    {
        return Stream.of(
            arguments(50, 5),
            arguments(20, 20),
            arguments(20, 30)
        );
    }
}
