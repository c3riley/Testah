package org.testah;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.provider.MethodSource;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;

import static org.testah.framework.cli.CliTest.OVERRIDE_JUNIT5_NO_SUCH_METHOD;

@EnabledIfSystemProperty(named = OVERRIDE_JUNIT5_NO_SUCH_METHOD, matches = "true")
@TestPlanJUnit5(name = "test plan for junit5 test with data provider", tags = OVERRIDE_JUNIT5_NO_SUCH_METHOD, testType = TestType.AUTOMATED)
public class Junit5MethodDataProviderNoSuchMethod
{
    @TestCaseWithParamsJUnit5
    @MethodSource("noSuchSourceMethod")
    public void testMethodDataProvider(String name) {
        TS.asserts().equalsTo("", "testname", name);
    }
}
