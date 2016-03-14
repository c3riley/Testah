package org.testah.framework.dto;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.enums.TestStepActionType;

public class StepActionDto {

	private TestStepActionType testStepActionType = TestStepActionType.INFO;
	private String message1 = null;
	private Object message2 = null;
	private Object message3 = null;
	private Boolean status = null;
	private String actionName = null;
	private AssertionError exception = null;
	private String snapShotPath = null;

	public StepActionDto() {

	}

	public StepActionDto addAssertResult(final String message, final Boolean status, final String assertMethod,
			final Object actual, final Object expected, final AssertionError exception) {
		this.actionName = assertMethod;
		this.message1 = message;
		this.status = status;
		this.message2 = actual;
		this.message3 = expected;
		this.exception = exception;
		this.setTestStepActionType(TestStepActionType.ASSERT);
		if (TS.isBrowser() && !status && message1.toLowerCase().contains("web")) {
			this.snapShotPath = TS.browser().takeScreenShot();
		}
		return this;
	}

	public StepActionDto addInfo(final String message1, final String message2) {
		this.message1 = message1;
		this.message2 = message2;
		this.setTestStepActionType(TestStepActionType.INFO);
		return this;
	}

	public StepActionDto addHttpAction(final ResponseDto response) {
		this.message1 = response.getUrl();
		this.message2 = response.getStatusCode();
		this.message3 = response.getResponseBody();
		this.setTestStepActionType(TestStepActionType.HTTP_REQUEST);
		return this;
	}

	public StepActionDto addBrowserAction(final String message1, final String message2) {
		this.message1 = message1;
		this.message2 = message2;
		this.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
		return this;
	}

	public String getMessage() {
		return message1;
	}

	public Boolean getStatus() {
		return status;
	}

	public String getAssertMethod() {
		return actionName;
	}

	public Object getActual() {
		return message2;
	}

	public Object getExpected() {
		return message3;
	}

	public AssertionError getException() {
		return exception;
	}

	public String getExceptionString() {
		if (null == exception)
			return null;
		final StringWriter sWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(sWriter));
		return sWriter.toString();
	}

	public TestStepActionType getTestStepActionType() {
		return testStepActionType;
	}

	public StepActionDto setTestStepActionType(final TestStepActionType testStepActionType) {
		this.testStepActionType = testStepActionType;
		return this;
	}

	public StepActionDto setMessage(final String message) {
		this.message1 = message;
		return this;
	}

	public StepActionDto setActual(final Object actual) {
		this.message2 = actual;
		return this;
	}

	public StepActionDto setExpected(final Object expected) {
		this.message3 = expected;
		return this;
	}

	public StepActionDto setStatus(final Boolean status) {
		this.status = status;
		return this;
	}

	public StepActionDto setAssertMethod(final String assertMethod) {
		this.actionName = assertMethod;
		return this;
	}

	public StepActionDto setException(final AssertionError exception) {
		this.exception = exception;
		return this;
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(final String message1) {
		this.message1 = message1;
	}

	public Object getMessage2() {
		return message2;
	}

	public void setMessage2(final Object message2) {
		this.message2 = message2;
	}

	public Object getMessage3() {
		return message3;
	}

	public void setMessage3(final Object message3) {
		this.message3 = message3;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public String getSnapShotPath() {
		return snapShotPath;
	}

	public void setSnapShotPath(final String snapShotPath) {
		this.snapShotPath = snapShotPath;
	}

}
