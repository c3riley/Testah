package org.testah.framework.dto;

import java.util.ArrayList;
import java.util.List;

public class TestStepDto {

	protected List<StepActionDto> stepActions = new ArrayList<StepActionDto>();
	private TestMetaDto meta = new TestMetaDto();
	private RunTimeDto runTime = new RunTimeDto();
	private Boolean status = null;

	public TestStepDto() {

	}

	public TestStepDto(final String name, final String description) {
		meta.setName(name);
		meta.setDescription(description);
	}

	public TestStepDto addAssertHistory(final StepActionDto assertHistoryItem) {
		if (null != assertHistoryItem) {
			getStepActions().add(assertHistoryItem);
		}
		return this;
	}

	public TestStepDto start() {
		setStatus(null);
		getRunTime().start();
		return this;
	}

	public TestStepDto stop() {
		setStatus();
		getRunTime().stop();
		return this;
	}

	public TestStepDto setStatus() {
		for (final StepActionDto e : stepActions) {
			if (null == e.getStatus()) {

			}
			if (e.getStatus() == false) {
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

	public TestStepDto setMeta(final TestMetaDto meta) {
		this.meta = meta;
		return this;
	}

	public RunTimeDto getRunTime() {
		return runTime;
	}

	public TestStepDto setRunTime(final RunTimeDto runTime) {
		this.runTime = runTime;
		return this;
	}

	public Boolean getStatus() {
		return status;
	}

	public TestStepDto setStatus(final Boolean status) {
		this.status = status;
		return this;
	}

	public String getExceptionMessages() {
		try {
			final StringBuffer sb = new StringBuffer("Step: " + this.getMeta().getName() + "\n ");
			for (final StepActionDto a : getStepActions()) {
				if (null != a.getException()) {
					sb.append(a.getException().getMessage() + "\n ");
					sb.append(a.getException() + "\n ");
				}
			}
			return sb.toString();
		} catch (final Exception e) {
			return null;
		}
	}

	public List<StepActionDto> getStepActions() {
		return stepActions;
	}

	public TestStepDto addStepActions(final StepActionDto stepAction) {
		stepActions.add(stepAction);
		return this;
	}

	public TestStepDto setStepActions(final List<StepActionDto> stepActions) {
		this.stepActions = stepActions;
		return this;
	}

}
