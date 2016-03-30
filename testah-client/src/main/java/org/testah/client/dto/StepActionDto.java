package org.testah.client.dto;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testah.client.enums.TestStatus;
import org.testah.client.enums.TestStepActionType;

public class StepActionDto {

    private TestStepActionType testStepActionType = TestStepActionType.INFO;
    private String             message1           = null;
    private String             message2           = null;
    private String             message3           = null;
    private Boolean            status             = null;
    private String             actionName         = null;
    private Throwable          exception          = new Throwable();
    private String             snapShotPath       = null;

    public StepActionDto() {

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
        this.message3 = String.valueOf(actual);
        return this;
    }

    public StepActionDto setExpected(final Object expected) {
        this.message2 = String.valueOf(expected);
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

    public StepActionDto setException(final Throwable exception) {
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
        this.message2 = String.valueOf(message2);
        return this;
    }

    public Object getMessage3() {
        return message3;
    }

    public StepActionDto setMessage3(final Object message3) {
        this.message3 = String.valueOf(message3);
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
