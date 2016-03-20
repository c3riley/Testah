package org.testah.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.runner.Description;
import org.testah.framework.annotations.TestMeta;
import org.testah.framework.enums.TestStatus;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TestCaseDto {

	private TestMetaDto meta = new TestMetaDto();
	private RunTimeDto runTime = new RunTimeDto();
	private List<TestStepDto> testSteps = new ArrayList<TestStepDto>();
	private Boolean status = null;

	public TestCaseDto() {
		this(null, null, null);
	}

	public TestCaseDto(final Description desc, final TestMeta meta, final TestMeta testPlan) {

		this.meta.setName(desc.getDisplayName());
		this.meta.setSource(desc.getTestClass().getCanonicalName() + ":" + desc.getMethodName());

		if (null != meta) {
			this.meta.fillFromTestMeta(meta);
		}
	}

	public TestCaseDto addTestStep(final TestStepDto testStep) {
		if (null != testStep) {
			getTestSteps().add(testStep.setId(getTestSteps().size() + 1));
		}
		return this;
	}

	public TestCaseDto start() {
		setStatus(null);
		getRunTime().start();
		return this;
	}

	public TestCaseDto stop() {
		return stop(null);
	}

	public TestCaseDto stop(final Boolean status) {
		if (null != status) {
			setStatus(status);
		} else {
			setStatus();
		}
		getRunTime().stop();
		return this;
	}

	public TestCaseDto setStatus() {
		for (final TestStepDto e : testSteps) {
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

	public TestMetaDto getMeta() {
		return meta;
	}

	public void setMeta(final TestMetaDto meta) {
		this.meta = meta;
	}

	public RunTimeDto getRunTime() {
		return runTime;
	}

	public void setRunTime(final RunTimeDto runTime) {
		this.runTime = runTime;
	}

	public List<TestStepDto> getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(final List<TestStepDto> testSteps) {
		this.testSteps = testSteps;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(final Boolean status) {
		this.status = status;
	}

	public String getExceptions() {
		final StringBuffer sb = new StringBuffer();
		for (final TestStepDto e : testSteps) {
			if (null != e.getStatus() && e.getStatus() == false) {
				sb.append(e.getExceptionMessages());
			}
		}
		return sb.toString();
	}

	public void getAssertionError() {
		if (null != status && !status) {
			throw new AssertionError("TestCase: " + getMeta().getName() + " Failed!\n " + getExceptions());
		}
	}

	public TestStatus getStatusEnum() {
		return TestStatus.getStatus(status);
	}

}
