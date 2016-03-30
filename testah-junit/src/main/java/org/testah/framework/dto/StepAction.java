package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.framework.testPlan.AbstractTestPlan;

public class StepAction extends StepActionDto {

    public static StepAction create() {
        return new StepAction();
    }

    public StepActionDto add() {
        if (TS.params().isRecordSteps()) {
            AbstractTestPlan.addStepAction(this);
        }
        return this;
    }

    public static StepAction createAssertResult(final String message, final Boolean status, final String assertMethod,
            final Object expected, final Object actual, final Throwable exception) {
        final StepAction step = new StepAction();
        step.setActionName(assertMethod);
        step.setMessage1(message);
        step.setStatus(status);
        step.setMessage2(String.valueOf(expected));
        step.setMessage3(String.valueOf(actual));
        step.setException(exception);
        step.setTestStepActionType(TestStepActionType.ASSERT);
        if (TS.isBrowser() && !status) {
            step.setSnapShotPath(TS.browser().takeScreenShot());
        }
        TS.log().debug(TestStepActionType.ASSERT + "[" + assertMethod + "] - " + status + " - " + message
                + " - expected[" + expected + "] actual[" + actual + "]");
        TS.log().trace("Exception Related to above Assert\n" + step.getExceptionString());
        return step;
    }

    public static StepAction createVerifyResult(final String message, final Boolean status, final String assertMethod,
            final Object expected, final Object actual, final Throwable exception) {
        final StepAction step = new StepAction();
        step.setActionName(assertMethod);
        step.setMessage1(message + " - " + status);
        step.setStatus(null);
        step.setMessage2(String.valueOf(expected));
        step.setMessage3(String.valueOf(actual));
        step.setException(null);
        step.setTestStepActionType(TestStepActionType.VERIFY);

        TS.log().debug(TestStepActionType.VERIFY + "[" + assertMethod + "] - " + status + " - " + message
                + " - expected[" + expected + "] actual[" + actual + "]");
        return step;
    }

    public static StepAction createInfo(final String message1) {
        return createInfo(message1, "", "", false);
    }

    public static StepAction createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", false);
    }

    public static StepAction createInfo(final String message1, final String message2, final String message3,
            final boolean autoLog) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setMessage3(message3);
        step.setTestStepActionType(TestStepActionType.INFO);
        if (autoLog) {
            TS.log().debug(TestStepActionType.INFO + " - " + step.getMessage1() + " - " + step.getMessage2());
        }
        return step;
    }

    public static StepAction createBrowserAction(final String message1, final Object by) {
        return createBrowserAction(message1, by.toString());
    }

    public static StepAction createBrowserAction(final String message1, final String message2) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        TS.log().debug(TestStepActionType.BROWSER_ACTION + "[" + step.getMessage1() + "] - " + step.getMessage2());
        return step;
    }
}
