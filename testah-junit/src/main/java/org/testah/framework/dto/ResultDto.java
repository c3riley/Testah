package org.testah.framework.dto;

import org.junit.runner.Result;
import org.testah.framework.testPlan.AbstractTestPlan;

public class ResultDto {

	private Result junitResult = null;
	private TestPlanDto testPlan = null;

	public ResultDto() {

	}

	public ResultDto(final Result junitResult, final TestPlanDto testPlan) {
		this.junitResult = junitResult;
		this.testPlan = testPlan;
	}

	public ResultDto(final Result junitResult) {
		this.junitResult = junitResult;
		this.testPlan = AbstractTestPlan.getTestPlan();
		AbstractTestPlan.stopTestPlan();
		// this.testPlan = new
		// Cloner().deepClone(AbstractTestPlan.getTestPlan()); // (TestPlanDto)
		// SerializationUtils.clone(AbstractTestPlan.getTestPlan());
	}

	public Result getJunitResult() {
		return junitResult;
	}

	public void setJunitResult(final Result junitResult) {
		this.junitResult = junitResult;
	}

	public TestPlanDto getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(final TestPlanDto testPlan) {
		this.testPlan = testPlan;
	}

}
