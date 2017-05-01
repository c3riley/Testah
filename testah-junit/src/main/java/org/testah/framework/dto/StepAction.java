package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.framework.testPlan.AbstractTestPlan;

/**
 * The Class StepAction.
 */
public class StepAction extends StepActionDto {

    /**
     * Creates the.
     *
     * @return the step action
     */
    public static StepAction create() {
        return new StepAction();
    }

    public static StepActionDto add(final StepActionDto stepAction) {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            AbstractTestPlan.addStepAction(stepAction);
        }
        return stepAction;
    }

    /**
     * Adds the.
     *
     * @param stepAction
     *            the step action
     * @return the step action dto
     */
    public static StepActionDto add(final StepAction stepAction) {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            AbstractTestPlan.addStepAction(stepAction);
        }
        return stepAction;
    }

    /**
     * Adds the.
     *
     * @return the step action dto
     */
    public StepActionDto add() {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            AbstractTestPlan.addStepAction(this);
        }
        return this;
    }

    /**
     * Creates the assert result.
     *
     * @param message
     *            the message
     * @param status
     *            the status
     * @param assertMethod
     *            the assert method
     * @param expected
     *            the expected
     * @param actual
     *            the actual
     * @param exception
     *            the exception
     * @return the step action
     */
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
            step.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        }
        TS.log().debug(TestStepActionType.ASSERT + "[" + assertMethod + "] - " + status + " - " + message + " - expected[" + expected +
                "] actual[" + actual + "]");
        if (null != step.getExceptionString()) {
            TS.log().trace("Exception Related to above Assert\n" + step.getExceptionString());
        }
        return step;
    }

    /**
     * Creates the verify result.
     *
     * @param message
     *            the message
     * @param status
     *            the status
     * @param assertMethod
     *            the assert method
     * @param expected
     *            the expected
     * @param actual
     *            the actual
     * @param exception
     *            the exception
     * @return the step action
     */
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

        TS.log().debug(TestStepActionType.VERIFY + "[" + assertMethod + "] - " + status + " - " + message + " - expected[" + expected +
                "] actual[" + actual + "]");
        return step;
    }

    /**
     * Creates the info.
     *
     * @param message1
     *            the message1
     * @return the step action
     */
    public static StepAction createInfo(final String message1) {
        return createInfo(message1, "", "", true);
    }

    /**
     * Creates the info.
     *
     * @param message1
     *            the message1
     * @param message2
     *            the message2
     * @return the step action
     */
    public static StepAction createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", true);
    }

    /**
     * Creates the info.
     *
     * @param message1
     *            the message1
     * @param message2
     *            the message2
     * @param message3
     *            the message3
     * @param autoLog
     *            the auto log
     * @return the step action
     */
    public static StepAction createInfo(final String message1, final String message2, final String message3,
            final boolean autoLog) {
        return createInfo(message1, message2, message3, autoLog, false);
    }

    public static StepAction createInfo(final String message1, final String message2, final String message3,
            final boolean autoLog, final boolean takeSnapShot) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setMessage3(message3);
        step.setTestStepActionType(TestStepActionType.INFO);
        if (autoLog) {
            TS.log().debug(TestStepActionType.INFO + " - " + step.getMessage1() + " - " + step.getMessage2());
        }
        if (takeSnapShot) {
            if (TS.isBrowser()) {
                step.setSnapShotPath(TS.browser().takeScreenShot());
                step.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
            }
        }
        return step;
    }

    /**
     * Creates the browser action.
     *
     * @param message1
     *            the message1
     * @param by
     *            the by
     * @return the step action
     */
    public static StepAction createBrowserAction(final String message1, final Object by) {
        return createBrowserAction(message1, by.toString());
    }

    public static StepAction screenshot(final String message) {
        return createInfo(message, "", "", true, true);
    }

    public static StepAction screenshot() {
        return screenshot("");
    }

    /**
     * Creates the browser action.
     *
     * @param message1
     *            the message1
     * @param message2
     *            the message2
     * @return the step action
     */
    public static StepAction createBrowserAction(final String message1, final String message2) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        TS.log().debug(TestStepActionType.BROWSER_ACTION + "[" + step.getMessage1() + "] - " + step.getMessage2());
        return step;
    }

    public StepAction addSnapshot(final StepAction stepAction) {
        stepAction.setSnapShotPath(TS.browser().takeScreenShot());
        stepAction.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        return stepAction;
    }
}
