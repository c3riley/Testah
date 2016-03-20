package org.testah.framework.enums;

public enum TestStatus {

	PASSED(true), FAILED(false), NA(null);
	private final Boolean status;

	TestStatus(final Boolean status) {
		this.status = status;
	}

	public static TestStatus getStatus(final Boolean statusValue) {
		if (null == statusValue) {
			return NA;
		} else if (statusValue) {
			return PASSED;
		} else {
			return FAILED;
		}
	}

	public Boolean getStatus() {
		return status;
	}

}
