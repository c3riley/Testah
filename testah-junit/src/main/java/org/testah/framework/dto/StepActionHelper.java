package org.testah.framework.dto;

import com.google.common.collect.Iterables;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;

/**
 * The type Step action helper.
 */
public class StepActionHelper {

    /**
     * Current step action dto.
     *
     * @return the step action dto
     */
    public StepActionDto current() {
        try {
            return Iterables.getLast(TS.testSystem().getTestStep().getStepActions());
        } catch (Exception exception) {
            TS.log().error("Unable to get current stepAction", exception);
            throw new RuntimeException(exception);
        }
    }

    /**
     * Create assert result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @return the step action dto
     */
    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception) {
        return createAssertResult(message, status, assertMethod, expected, actual, exception, true);
    }

    /**
     * Create assert result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param autoLog      the auto log
     * @return the step action dto
     */
    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception, final boolean autoLog) {
        return assertResult(message, status, assertMethod, expected, actual, exception, create(), autoLog);
    }

    /**
     * Create assert result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param step         the step
     * @return the step action dto
     */
    public StepActionDto createAssertResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception,
                                            final StepActionDto step) {
        return assertResult(message, status, assertMethod, expected, actual, exception, step, true);
    }

    /**
     * Assert result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param step         the step
     * @param autoLog      the auto log
     * @return the step action dto
     */
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
            TS.log().debug(String.format("%s [%s] - %s - %s%nexpected%n[%s]%nactual%n[%s]",
                TestStepActionType.ASSERT, assertMethod, status, message, expected, actual));
            if (null != step.getExceptionString()) {
                TS.log().trace(String.format("Exception Related to above Assert%n%s", step.getExceptionString()));
            }
        }
        return step;
    }

    /**
     * Create step action dto.
     *
     * @return the step action dto
     */
    public StepActionDto create() {
        StepActionDto stepActionDto = new StepActionDto();
        add(stepActionDto);
        return stepActionDto;
    }

    /**
     * Add step action dto.
     *
     * @param stepAction the step action
     * @return the step action dto
     */
    public StepActionDto add(final StepActionDto stepAction) {
        if (null == TS.testSystem().getTestStep()) {
            throw new RuntimeException("Unable to add stepAction as current Test Step is null");
        }
        if (null == stepAction) {
            throw new RuntimeException("Unable to add stepAction as it is null");
        }
        TS.testSystem().getTestStep().addStepAction(stepAction);
        return stepAction;
    }

    /**
     * Create verify result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @return the step action dto
     */
    public StepActionDto createVerifyResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception) {
        return createVerifyResult(message, status, assertMethod, expected, actual, exception, true);
    }

    /**
     * Create verify result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param autoLog      the auto log
     * @return the step action dto
     */
    public StepActionDto createVerifyResult(final String message, final Boolean status, final String assertMethod,
                                            final Object expected, final Object actual, final Throwable exception,
                                            final boolean autoLog) {
        return verifyResult(message, status, assertMethod, expected, actual, exception, create(), autoLog);
    }

    /**
     * Verify result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param step         the step
     * @param autoLog      the auto log
     * @return the step action dto
     */
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
            TS.log().debug(String.format("%s [%s] - %s - %s%nexpected%n[%s]%nactual%n[%s]",
                    TestStepActionType.VERIFY, assertMethod, status, message, expected, actual));
        }
        return step;
    }

    /**
     * Verify result step action dto.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @param step         the step
     * @return the step action dto
     */
    public StepActionDto verifyResult(final String message, final Boolean status, final String assertMethod,
                                      final Object expected, final Object actual, final Throwable exception,
                                      final StepActionDto step) {
        return verifyResult(message, status, assertMethod, expected, actual, exception, step, true);
    }

    /**
     * Create info step action dto.
     *
     * @param message1 the message 1
     * @return the step action dto
     */
    public StepActionDto createInfo(final String message1) {
        return createInfo(message1, "", "", true);
    }

    /**
     * Create info step action dto.
     *
     * @param message1 the message 1
     * @param message2 the message 2
     * @param message3 the message 3
     * @param autoLog  the auto log
     * @return the step action dto
     */
    public StepActionDto createInfo(final String message1, final String message2, final String message3,
                                    final boolean autoLog) {
        return createInfo(message1, message2, message3, autoLog, false);
    }

    /**
     * Create info step action dto.
     *
     * @param message1     the message 1
     * @param message2     the message 2
     * @param message3     the message 3
     * @param autoLog      the auto log
     * @param takeSnapShot the take snap shot
     * @return the step action dto
     */
    public StepActionDto createInfo(final String message1, final String message2, final String message3,
                                    final boolean autoLog, final boolean takeSnapShot) {
        return info(message1, message2, message3, autoLog, takeSnapShot, create());
    }

    /**
     * Create info step action dto.
     *
     * @param message1 the message 1
     * @param message2 the message 2
     * @return the step action dto
     */
    public StepActionDto createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", true);
    }

    /**
     * Info step action dto.
     *
     * @param message1     the message 1
     * @param message2     the message 2
     * @param message3     the message 3
     * @param autoLog      the auto log
     * @param takeSnapShot the take snap shot
     * @param step         the step
     * @return the step action dto
     */
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

    /**
     * Info step action dto.
     *
     * @param message1 the message 1
     * @param message2 the message 2
     * @param message3 the message 3
     * @param autoLog  the auto log
     * @param step     the step
     * @return the step action dto
     */
    public StepActionDto info(final String message1, final String message2, final String message3,
                              final boolean autoLog, final StepActionDto step) {
        return info(message1, message2, message3, autoLog, false, step);
    }

    /**
     * Create browser action step action dto.
     *
     * @param message1 the message 1
     * @param by       the by
     * @return the step action dto
     */
    public StepActionDto createBrowserAction(final String message1, final Object by) {
        return createBrowserAction(message1, by.toString());
    }

    /**
     * Create browser action step action dto.
     *
     * @param message1 the message 1
     * @param message2 the message 2
     * @return the step action dto
     */
    public StepActionDto createBrowserAction(final String message1, final String message2) {
        return browserAction(message1, message2, create());
    }

    /**
     * Create browser action step action dto.
     *
     * @param message1 the message 1
     * @param by       the by
     * @param step     the step
     * @return the step action dto
     */
    public StepActionDto createBrowserAction(final String message1, final Object by, final StepActionDto step) {
        return browserAction(message1, by.toString(), step);
    }

    /**
     * Browser action step action dto.
     *
     * @param message1 the message 1
     * @param message2 the message 2
     * @param step     the step
     * @return the step action dto
     */
    public StepActionDto browserAction(final String message1, final String message2, final StepActionDto step) {
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        step.log();
        return step;
    }

    /**
     * Screenshot step action dto.
     *
     * @param message the message
     * @param step    the step
     * @return the step action dto
     */
    public StepActionDto screenshot(final String message, final StepActionDto step) {
        return info(message, "", "", true, true, step);
    }

    /**
     * Screenshot step action dto.
     *
     * @return the step action dto
     */
    public StepActionDto screenshot() {
        return screenshot("");
    }

    /**
     * Screenshot step action dto.
     *
     * @param message the message
     * @return the step action dto
     */
    public StepActionDto screenshot(final String message) {
        return info(message, "", "", true, true, create());
    }

    /**
     * Add snapshot step action dto.
     *
     * @param stepActionDto the step action dto
     * @return the step action dto
     */
    public StepActionDto addSnapshot(final StepActionDto stepActionDto) {
        stepActionDto.setSnapShotPath(TS.browser().takeScreenShot());
        stepActionDto.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        return stepActionDto;
    }

}
