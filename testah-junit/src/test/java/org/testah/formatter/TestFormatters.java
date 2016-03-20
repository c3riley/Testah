package org.testah.formatter;

import org.junit.Test;
import org.testah.TS;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.testPlan.HttpTestPlan;

@TestMeta
public class TestFormatters extends HttpTestPlan {

	@Test
	@TestMeta
	public void test1() {
		TS.asserts().isNull(null);
	}

}
