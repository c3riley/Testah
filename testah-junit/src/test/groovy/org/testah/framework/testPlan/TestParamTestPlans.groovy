package org.testah.framework.testPlan

import org.junit.jupiter.params.provider.ValueSource
import org.testah.TS
import org.testah.framework.annotations.TestCaseWithParamsJUnit5
import org.testah.framework.annotations.TestPlanJUnit5

@TestPlanJUnit5
class TestParamTestPlans {

    @ValueSource(strings = ["racecar", "radar", "able was I ere I saw elba"])
    @TestCaseWithParamsJUnit5
    public void test(final String param) {
        TS.asserts().notNull(param)
    }

}
