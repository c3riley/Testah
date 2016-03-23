package org.testah.framework.dto;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.framework.enums.TestStatus;
import org.testah.framework.enums.TestStepActionType;
import org.testah.framework.testPlan.AbstractTestPlan;

public class StepActionDto {

    private TestStepActionType testStepActionType = TestStepActionType.INFO;
    private String             message1           = null;
    private Object             message2           = null;
    private Object             message3           = null;
    private Boolean            status             = null;
    private String             actionName         = null;
    private Throwable          exception          = new Throwable();
    private String             snapShotPath       = null;

    public StepActionDto() {

    }

    public StepActionDto add() {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.addStepAction(this);
        }
        return this;
    }

    public static StepActionDto createAssertResult(final String message, final Boolean status,
            final String assertMethod, final Object expected, final Object actual, final AssertionError exception) {
        final StepActionDto step = new StepActionDto();
        step.actionName = assertMethod;
        step.message1 = message;
        step.status = status;
        step.message2 = expected;
        step.message3 = actual;
        step.exception = exception;
        step.setTestStepActionType(TestStepActionType.ASSERT);
        if (TS.isBrowser() && !status) {
            step.snapShotPath = TS.browser().takeScreenShot();
        }
        TS.log().debug(TestStepActionType.ASSERT + "[" + assertMethod + "] - " + status + " - " + message
                + " - expected[" + expected + "] actual[" + actual + "]");
        TS.log().trace("Exception Related to above Assert\n" + step.getExceptionString());
        return step;
    }

    public static StepActionDto createVerifyResult(final String message, final Boolean status,
            final String assertMethod, final Object expected, final Object actual, final AssertionError exception) {
        final StepActionDto step = new StepActionDto();
        step.actionName = assertMethod;
        step.message1 = message + " - " + status;
        step.status = null;
        step.message2 = expected;
        step.message3 = actual;
        step.exception = null;
        step.setTestStepActionType(TestStepActionType.VERIFY);

        TS.log().debug(TestStepActionType.VERIFY + "[" + assertMethod + "] - " + status + " - " + message
                + " - expected[" + expected + "] actual[" + actual + "]");
        return step;
    }

    public static StepActionDto createInfo(final String message1) {
        return createInfo(message1, "", "", false);
    }

    public static StepActionDto createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", false);
    }

    public static StepActionDto createInfo(final String message1, final String message2, final String message3,
            final boolean autoLog) {
        final StepActionDto step = new StepActionDto();
        step.message1 = message1;
        step.message2 = message2;
        step.message3 = message3;
        step.setTestStepActionType(TestStepActionType.INFO);
        if (autoLog) {
            TS.log().debug(TestStepActionType.INFO + " - " + step.message1 + " - " + step.message2);
        }
        return step;
    }

    public static StepActionDto createBrowserAction(final String message1, final By by) {
        return createBrowserAction(message1, by.toString());
    }

    public static StepActionDto createBrowserAction(final String message1, final String message2) {
        final StepActionDto step = new StepActionDto();
        step.message1 = message1;
        step.message2 = message2;
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        TS.log().debug(TestStepActionType.BROWSER_ACTION + "[" + step.message1 + "] - " + step.message2);
        return step;
    }

    public String getDescription() {
        return getActionName() + "[" + message1 + "]";
    }

    public Boolean getStatus() {
        return status;
    }

    public String getAssertMethod() {
        return actionName;
    }

    public Object getActual() {
        return message3;
    }

    public Object getExpected() {
        return message2;
    }

    @JsonIgnore
    public Throwable getException() {
        return exception;
    }

    public String getExceptionString() {
        if (null == exception)
            return null;
        final StringWriter sWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(sWriter));
        return sWriter.toString().replace("\t", "");
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
        this.message3 = actual;
        return this;
    }

    public StepActionDto setExpected(final Object expected) {
        this.message2 = expected;
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

    public StepActionDto setMessage1(final String message1) {
        this.message1 = message1;
        return this;
    }

    public Object getMessage2() {
        return message2;
    }

    public StepActionDto setMessage2(final Object message2) {
        this.message2 = message2;
        return this;
    }

    public Object getMessage3() {
        return message3;
    }

    public StepActionDto setMessage3(final Object message3) {
        this.message3 = message3;
        return this;
    }

    public String getActionName() {
        if (null == actionName) {
            actionName = getTestStepActionType().name();
        }
        return actionName;
    }

    public StepActionDto setActionName(final String actionName) {
        this.actionName = actionName;
        return this;
    }

    public String getSnapShotPath() {
        return snapShotPath;
    }

    public StepActionDto setSnapShotPath(final String snapShotPath) {
        this.snapShotPath = snapShotPath;
        return this;
    }

    public TestStatus getStatusEnum() {
        return TestStatus.getStatus(status);
    }

}
