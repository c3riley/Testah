package org.testah.framework.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.enums.TestStatus;

public class TestPlanDto extends TestMetaDto {
	private RunTimeDto runTime = new RunTimeDto();
	private List<TestCaseDto> testCases = new ArrayList<TestCaseDto>();
	private Boolean status = null;
	private final RunInfoDto runInfo = new RunInfoDto();

	public TestPlanDto() {
		this(null);
	}

	public TestPlanDto(final TestPlan meta) {
		this.fillFromTestPlan(meta);
	}

	public TestPlanDto(final Description desc, final TestPlan meta) {
		this.setName(desc.getClassName());
		this.setSource(desc.getTestClass().getCanonicalName());
		if (null != meta) {
			this.fillFromTestPlan(meta);
		}
	}

	public TestPlanDto addTestCase(final TestCaseDto testCase) {
		if (null != testCase) {
			getTestCases().add(testCase);
		}
		return this;
	}

	public TestPlanDto start() {
		setStatus(null);
		getRunTime().start();
		return this;
	}

	public TestPlanDto stop() {
		setStatus();
		getRunTime().stop();
		runInfo.recalc(this);
		return this;
	}

	public RunInfoDto getRunInfo() {
		return runInfo;
	}

	public TestPlanDto setStatus() {
		for (final TestCaseDto e : testCases) {
			if (null == e.getStatus()) {

			} else if (e.getStatus() == false) {
				status = false;
				return this;
			} else if (e.getStatus() == true) {
				status = true;
			}
		}
		return this;
	}

	public RunTimeDto getRunTime() {
		return runTime;
	}

	public void setRunTime(final RunTimeDto runTime) {
		this.runTime = runTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(final Boolean status) {
		this.status = status;
	}

	public List<TestCaseDto> getTestCases() {
		return testCases;
	}

	public void setTestCases(final List<TestCaseDto> testCases) {
		this.testCases = testCases;
	}

	public TestStatus getStatusEnum() {
		return TestStatus.getStatus(status);
	}

}
