package org.testah.framework.report.asserts;

import org.junit.jupiter.api.Test;
import org.testah.TS;

public class AssertNumberTest {

    @Test
    public void testDouble() {
        TS.verify().given(0.0).equalsTo(0.0);
    }

}
