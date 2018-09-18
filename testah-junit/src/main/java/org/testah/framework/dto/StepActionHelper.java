package org.testah.framework.dto;

import com.google.common.collect.Iterables;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;
import org.testah.framework.testPlan.AbstractTestPlan;

public class StepActionHelper {

    public StepActionDto create() {
        StepActionDto stepActionDto = new StepActionDto();
        add(stepActionDto);
        return stepActionDto;
    }

    public StepActionDto current() {
        try {
            return Iterables.getLast(AbstractTestPlan.getTestStep().getStepActions());
        } catch (Exception exception) {
            TS.log().error("Unable to get current stepAction", exception);
            throw new RuntimeException(exception);
        }
    }

    public StepActionDto add(final StepActionDto stepAction) {
        if (null == AbstractTestPlan.getTestStep()) {
            throw new RuntimeException("Unable to add stepAction as current Test Step is null");
        }
        if (null == stepAction) {
            throw new RuntimeException("Unable to add stepAction as it is null");
        }
        AbstractTestPlan.getTestStep().addStepAction(stepAction);
        return stepAction;
    }

    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception) {
        return createAssertResult(message, status, assertMethod, expected, actual, exception, true);
    }

    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception, final boolean autoLog) {
        return assertResult(message, status, assertMethod, expected, actual, exception, create(), autoLog);
    }

    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception,
                                            final StepActionDto step) {
        return assertResult(message, status, assertMethod, expected, actual, exception, step, true);
    }

    public StepActionDto assertResult(final String message, final Boolean status, final String assertMethod,
                                      final Object expected, final Object actual, final Throwable exception,
                                      final StepActionDto step, final boolean autoLog) {
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
        if (autoLog) {
            TS.log().debug(TestStepActionType.ASSERT + "[" + assertMethod + "] - " + status + " - " + message + " - expected[" + expected
                    + "] actual[" + actual + "]");
            if (null != step.getExceptionString()) {
                TS.log().trace("Exception Related to above Assert\n" + step.getExceptionString());
            }
        }
        return step;
    }

    public StepActionDto createVerifyResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception) {
        return createVerifyResult(message, status, assertMethod, expected, actual, exception, true);
    }

    public StepActionDto createVerifyResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception,
                                            final boolean autoLog) {
        return verifyResult(message, status, assertMethod, expected, actual, exception, create(), autoLog);
    }

    public StepActionDto verifyResult(final String message, final Boolean status, final String assertMethod,
                                      final Object expected, final Object actual, final Throwable exception,
                                      final StepActionDto step) {
        return verifyResult(message, status, assertMethod, expected, actual, exception, step, true);
    }

    public StepActionDto verifyResult(final String message, final Boolean status, final String assertMethod,
                                      final Object expected, final Object actual, final Throwable exception,
                                      final StepActionDto step, boolean autoLog) {
        step.setActionName(assertMethod);
        step.setMessage1(message + " - " + status);
        step.setStatus(null);
        step.setMessage2(String.valueOf(expected));
        step.setMessage3(String.valueOf(actual));
        step.setException(null);
        step.setTestStepActionType(TestStepActionType.VERIFY);
        if (autoLog) {
            TS.log().debug(TestStepActionType.VERIFY + "[" + assertMethod + "] - " +
                    status + " - " + message + " - expected[" + expected + "] actual[" + actual + "]");
        }
        return step;
    }

    public StepActionDto createInfo(final String message1) {
        return createInfo(message1, "", "", true);
    }

    public StepActionDto createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", true);
    }

    public StepActionDto info(final String message1, final String message2, final String message3,
                              final boolean autoLog, final StepActionDto step) {
        return info(message1, message2, message3, autoLog, false, step);
    }

    public StepActionDto createInfo(final String message1, final String message2, final String message3,
                                    final boolean autoLog) {
        return createInfo(message1, message2, message3, autoLog, false);
    }


    public StepActionDto createInfo(final String message1, final String message2, final String message3,
                                    final boolean autoLog, final boolean takeSnapShot) {
        return info(message1, message2, message3, autoLog, takeSnapShot, create());
    }

    public StepActionDto info(final String message1, final String message2, final String message3,
                              final boolean autoLog, final boolean takeSnapShot, final StepActionDto step) {
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

    public StepActionDto createBrowserAction(final String message1, final Object by) {
        return createBrowserAction(message1, by.toString());
    }

    public StepActionDto createBrowserAction(final String message1, final Object by, final StepActionDto step) {
        return browserAction(message1, by.toString(), step);
    }

    public StepActionDto createBrowserAction(final String message1, final String message2) {
        return browserAction(message1, message2, create());
    }

    public StepActionDto browserAction(final String message1, final String message2, final StepActionDto step) {
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        step.log();
        return step;
    }

    public StepActionDto screenshot(final String message) {
        return info(message, "", "", true, true, create());
    }

    public StepActionDto screenshot(final String message, final StepActionDto step) {
        return info(message, "", "", true, true, step);
    }

    public StepActionDto screenshot() {
        return screenshot("");
    }

    public StepActionDto addSnapshot(final StepActionDto stepActionDto) {
        stepActionDto.setSnapShotPath(TS.browser().takeScreenShot());
        stepActionDto.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        return stepActionDto;
    }

}
