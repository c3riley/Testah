package org.testah;

import org.junit.jupiter.params.provider.CsvFileSource;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

@TestPlanJUnit5(name = "test plan for junit5 test with data provider", tags = "JUNIT_5_NEG")
public class Junit5TestPlanNegative
{
    @TestCaseWithParamsJUnit5
    @CsvFileSource(resources = "/org/testah/runner/invalid/path/data.csv", numLinesToSkip = 1)
    public void testCsvDataProvider(String name) {
        TS.asserts().equalsTo("", "testname", name);
    }
}
