package org.testah;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestPlanJUnit5(name = "test plan for junit5 example", components = {"c2"}, platforms = {"p2"},
    devices = {"d2"}, testType = TestType.AUTOMATED, runTypes = "r2", description = "desc",
    relatedIds = "i2", relatedLinks = "http://www.testah.com", tags = "JUNIT_5")
public class Junit5TestPlan {

    static boolean didDoOnPassOccur = false;

    static Stream<Arguments> myMethodDataProvider() {
        return Stream.of(
                arguments("RED", "POPPIES", LocalDate.now(), 10),
                arguments("BLUE", "OCEAN", LocalDate.now(), 9),
                arguments("GREEN", "LANTERN", LocalDate.now(), 12)
        );
    }
    /**
     * Setup Method.
     */
    @BeforeEach
    public void setup() {
        TS.testSystem().setDoOnPassClosure((description) -> {
            didDoOnPassOccur = true;
        });
    }

    @AfterAll
    public static void tearDown() {
        TS.asserts().isTrue(didDoOnPassOccur);
    }

    @TestCaseJUnit5(name = "test for junit5 example", components = {"c1"}, platforms = {"p1"},
        devices = {"d1"}, testType = TestType.AUTOMATED, runTypes = "r1", description = "desc",
        relatedIds = "i1", relatedLinks = "http://www.testah.com")
    @Test
    public void test() {
        TS.asserts().isTrue(true);
    }

    @TestCaseJUnit5(name = "test for junit5 example")
    @Test
    public void test3() {
        TS.asserts().isTrue(true);
    }

    @TestCaseJUnit5(name = "parametrized test for junit5 example")
    @ParameterizedTest(name = "{index} => input = {0}")
    @ValueSource(strings = {"aa", "ab", "ac", "ad"})
    public void testParams1(String testString) {
        TS.asserts().startsWith("String starts with 'a'.", testString, "a");
        TS.asserts().equalsTo("String is 2 chars long.", 2, testString.length());
    }

    @TestCaseJUnit5(name = "parametrized test for junit5 example with null and empty values")
    @ParameterizedTest()
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "\n", "\t", "   "})
    public void testParamsNullOrEmpty(String testString) {
        TS.asserts().isTrue(String.format("Checking testString = '%s'", testString), StringUtils.isEmpty(testString) || StringUtils.isBlank(testString));
    }

    @TestCaseJUnit5(name = "parametrized test for junit5 example with null and empty values")
    @ParameterizedTest()
    @EnumSource(DayOfWeek.class)
    public void testParamsEnum(DayOfWeek dayOfWeek) {
        TS.asserts().isTrue("Integer value of day of week.", dayOfWeek.getValue() <= 7 && dayOfWeek.getValue() >= 1);
    }

    @TestCaseJUnit5(name = "parametrized test for junit5 example with null and empty values")
    // the annotation parameters will create output like
    // data[<test_number>] = <prefix>:<suffix>:<date>:<charCount>
    // data[3] = GREEN:LANTERN:2021-02-14:12
    @ParameterizedTest(name = "data[{index}] = {0}:{1}:{2}:{3}")
    @MethodSource("myMethodDataProvider")
    public void testMethodDataProvider(String prefix, String suffix, LocalDate date, int charCount) {
        TS.asserts().equalsTo("Length of phrase.", charCount, (prefix + suffix).length());
        TS.asserts().equalsTo("Check date.", date, LocalDate.now());
    }

    @Test
    @TestCaseJUnit5
    public void test2() {
        TS.asserts().isTrue(true);
    }

}
