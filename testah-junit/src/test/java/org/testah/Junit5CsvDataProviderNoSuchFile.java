package org.testah;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import static org.testah.framework.cli.CliTest.OVERRIDE_JUNIT5_CSV_NO_SUCH_FILE;

@EnabledIfSystemProperty(named = OVERRIDE_JUNIT5_CSV_NO_SUCH_FILE, matches = "true")
@TestPlanJUnit5(name = "test plan for junit5 test with data provider",
    tags = OVERRIDE_JUNIT5_CSV_NO_SUCH_FILE, testType = TestType.AUTOMATED)
public class Junit5CsvDataProviderNoSuchFile
{
    @TestCaseWithParamsJUnit5
    @CsvFileSource(resources = "/org/testah/runner/invalid/path/data.csv", numLinesToSkip = 1)
    public void testCsvDataProviderInvalidFile(String name) {
        TS.asserts().equalsTo("", "testname", name);
    }
}
