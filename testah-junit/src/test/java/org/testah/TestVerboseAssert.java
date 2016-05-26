
package org.testah;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestPlan()
public class TestVerboseAssert extends HttpTestPlan {

	@Before
	public void setup() {

	}

	@TestCase()
	@Test
	public void testDontStoponFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
		TS.verify().same("", "cool", "cool");
		TS.verify().same("", "cool", "cll");
		getTestPlan();
	}

	@TestCase()
	@Test(expected = AssertionError.class)
	public void testStopOnZFirstFail() throws JsonGenerationException, JsonMappingException, IOException {
		TS.asserts().setThrowExceptionOnFail(true);
		TS.asserts().same("", "cool", "cool");
		TS.asserts().same("", "cool", "cll");

	}

}
