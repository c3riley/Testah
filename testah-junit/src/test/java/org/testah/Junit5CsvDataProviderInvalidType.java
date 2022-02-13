package org.testah;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import static org.testah.framework.cli.CliTest.OVERRIDE_JUNIT5_CSV_INVALID_TYPE;

@EnabledIfSystemProperty(named = OVERRIDE_JUNIT5_CSV_INVALID_TYPE, matches = "true")
@TestPlanJUnit5(name = "test plan for junit5 test with data provider",
    tags = OVERRIDE_JUNIT5_CSV_INVALID_TYPE, testType = TestType.AUTOMATED)
public class Junit5CsvDataProviderInvalidType
{

    @TestCaseWithParamsJUnit5
    @CsvFileSource(resources = "/org/testah/data.csv", numLinesToSkip = 1)
    public void testCsvDataProviderInvalidType(String name, Integer testId) {
        TS.asserts().equalsTo("", "testname", name);
        TS.asserts().equalsTo("", "testId", testId);
    }
}
