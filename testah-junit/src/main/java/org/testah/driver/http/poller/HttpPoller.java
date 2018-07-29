package org.testah.driver.http.poller;

import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.dto.StepAction;
import org.testah.framework.testPlan.AbstractTestPlan;

import java.util.Date;

/**
 * The Class HttpPoller.
 */
public class HttpPoller {

    /**
     * The max poll iteration.
     */
    private int maxPollIteration = 5000;

    /**
     * The poll iteration pause.
     */
    private Long pollIterationPause = 2000L;

    /**
     * The status assert.
     */
    private boolean statusAssert = true;

    /**
     * The write to log.
     */
    private boolean writeToLog = true;

    /**
     * Poll request.
     *
     * @param request     the request
     * @param pollerCheck the poller check
     * @return the response dto
     */
    public ResponseDto pollRequest(final AbstractRequestDto<?> request, final HttpPollerCheck pollerCheck) {
        return pollRequest(request, pollerCheck, "polling");
    }

    /**
     * Poll request.
     *
     * @param request        the request
     * @param pollerCheck    the poller check
     * @param pollingMessage the polling message
     * @return the response dto
     */
    public ResponseDto pollRequest(final AbstractRequestDto<?> request, final HttpPollerCheck pollerCheck, final String pollingMessage) {
        ResponseDto response = null;
        if (null != TS.params()) {
            StepAction.createInfo("Running Poller for Http Request ").add();
            AbstractTestPlan.addStepAction(request.createRequestInfoStep());
        }
        Long start = new Date().getTime();
        int pollCtr = 0;
        boolean pollCheckPassed = false;
        boolean exceptionOccurred = false;
        try {
            for (pollCtr = 1; pollCtr < getMaxPollIteration(); pollCtr++) {
                try {
                    // doRequest does not return null
                    response = TS.http().doRequest(request, false);
                    if (isStatusAssert()) {
                        if (request.getExpectedStatus() != response.getStatusCode()) {
                            response.assertStatus(request.getExpectedStatus());
                        }
                    }
                    if (pollerCheck.isDone(response)) {
                        pollCheckPassed = true;
                        break;
                    }
                } catch (Exception e) {
                    TS.log().warn("Issue found during polling - " + e.getMessage());
                    exceptionOccurred = true;
                    throw e;
                } finally {
                    if (pollCtr == 1) {
                        if (null != TS.params() && response != null) {
                            AbstractTestPlan.addStepAction(response.createResponseInfoStep(false, true, 500), false);
                        }
                    } else if (isWriteToLog() && response != null) {
                        response.createResponseInfoStep(false, true, 500);
                    }
                }
                TS.util().pause(getPollIterationPause(), pollingMessage, pollCtr);
            }
        } catch (Exception e) {
            TS.log().warn("Issue found during polling - " + e.getMessage());
            exceptionOccurred = true;
            throw e;
        } finally {
            if (null != response && null != TS.params()) {
                AbstractTestPlan.addStepAction(response.createResponseInfoStep(false, true, 500), false);
            }
            StepAction.createInfo(
                    "Polled for " + pollCtr + " iterations for duration of " + TS.util().getDurationPretty((new Date().getTime()) - start));
            if (!exceptionOccurred && !pollCheckPassed) {
                TS.asserts().fail("Poller went over max iteration allowed[" + pollCtr + "] and the poller check was not true!");
            }
        }
        return response;
    }

    /**
     * Gets the max poll iteration.
     *
     * @return the max poll iteration
     */
    public int getMaxPollIteration() {
        return maxPollIteration;
    }

    /**
     * Sets the max poll iteration.
     *
     * @param maxPollIteration the max poll iteration
     * @return the http poller
     */
    public HttpPoller setMaxPollIteration(final int maxPollIteration) {
        this.maxPollIteration = maxPollIteration;
        return this;
    }

    /**
     * Gets the poll iteration pause.
     *
     * @return the poll iteration pause
     */
    public Long getPollIterationPause() {
        return pollIterationPause;
    }

    /**
     * Sets the poll iteration pause.
     *
     * @param pollIterationPause the poll iteration pause
     * @return the http poller
     */
    public HttpPoller setPollIterationPause(final Long pollIterationPause) {
        this.pollIterationPause = pollIterationPause;
        return this;
    }

    /**
     * Checks if is status assert.
     *
     * @return true, if is status assert
     */
    public boolean isStatusAssert() {
        return statusAssert;
    }

    /**
     * Sets the status assert.
     *
     * @param statusAssert the status assert
     * @return the http poller
     */
    public HttpPoller setStatusAssert(final boolean statusAssert) {
        this.statusAssert = statusAssert;
        return this;
    }

    /**
     * Checks if is write to log.
     *
     * @return true, if is write to log
     */
    public boolean isWriteToLog() {
        return writeToLog;
    }

    /**
     * Sets the write to log.
     *
     * @param writeToLog the write to log
     * @return the http poller
     */
    public HttpPoller setWriteToLog(final boolean writeToLog) {
        this.writeToLog = writeToLog;
        return this;
    }

}
