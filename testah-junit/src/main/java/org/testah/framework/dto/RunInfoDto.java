package org.testah.framework.dto;

public class RunInfoDto {

	private String runId = "";
	private String runLocation = null;
	private String buildNumber = null;
	private int pass = 0;
	private int fail = 0;
	private int ignore = 0;
	private int total = 0;

	public RunInfoDto() {

	}

	public RunInfoDto recalc(final TestPlanDto testPlans) {
		total = testPlans.getTestCases().size();
		pass = 0;
		fail = 0;
		ignore = 0;

		for (final TestCaseDto testCase : testPlans.getTestCases()) {
			if (null == testCase.getStatus()) {
				ignore++;
			} else if (testCase.getStatus()) {
				pass++;
			} else {
				fail++;
			}
		}
		return this;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(final int pass) {
		this.pass = pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(final int fail) {
		this.fail = fail;
	}

	public int getIgnore() {
		return ignore;
	}

	public void setIgnore(final int ignore) {
		this.ignore = ignore;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(final String runId) {
		this.runId = runId;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	public String getRunLocation() {
		if (null == runLocation) {
			runLocation = "localhost";
		}
		return runLocation;
	}

	public void setRunLocation(final String runLocation) {
		this.runLocation = runLocation;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(final String buildNumber) {
		this.buildNumber = buildNumber;
	}

}
