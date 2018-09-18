package org.testah.driver.http.poller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
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

    @JsonIgnore
    private AbstractHttpWrapper http;

    public HttpPoller() {
        this.http = TS.http();
    }

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
    public ResponseDto pollRequest(final AbstractRequestDto<?> request, final HttpPollerCheck pollerCheck,
                                   final String pollingMessage) {
        ResponseDto response = null;
        TS.step().action().createInfo("Running Poller for Http Request");
        TS.step().action().add(request.createRequestInfoStep());

        StepActionDto pollerStepActionToReuse = TS.step().action().create();
        final long start = new Date().getTime();
        int pollCtr = 0;
        boolean pollCheckPassed = false;
        Throwable exceptionOccurred = null;
        try {
            for (pollCtr = 1; pollCtr <= getMaxPollIteration(); pollCtr++) {
                try {
                    // doRequest will not return null
                    response = http.doRequest(request, false);
                    if (isStatusAssert()) {
                        response.assertStatus();
                    }
                    if (pollerCheck.isDone(response)) {
                        pollCheckPassed = true;
                        break;
                    }
                } catch (Throwable e) {
                    TS.log().warn("Issue found during polling - " + e.getMessage());
                    exceptionOccurred = e;
                    throw e;
                } finally {
                    if (pollCtr == 1 || pollCheckPassed) {
                        if (response != null) {
                            response.createResponseInfoStep(false, true, 500);
                        }
                    } else if (isWriteToLog() && response != null) {
                        pollerStepActionToReuse = response.createResponseInfoStep(false, true, 500, pollerStepActionToReuse);
                    }
                }
                TS.util().pause(getPollIterationPause(), pollingMessage, pollCtr);
            }
        } catch (Throwable e) {
            TS.log().warn("Issue found during polling - " + e.getMessage());
            exceptionOccurred = e;
            throw e;
        } finally {
            TS.step().action().createInfo(
                    "Polled for " + pollCtr + " iterations for duration of " +
                            TS.util().getDurationPretty((new Date().getTime()) - start));
            if (pollCheckPassed) {
                TS.asserts().pass("Poller completed successfully");
            } else if (exceptionOccurred != null) {
                TS.asserts().fail("Poller failed with an exception thrown - " +
                        exceptionOccurred.getMessage(), exceptionOccurred);
            } else if (pollCtr > getMaxPollIteration() && !pollCheckPassed) {
                TS.asserts().fail("Poller went over max iteration allowed[" + pollCtr +
                        "] and the poller check was not true!");
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
     * Deprecated: Should set on request using setAutoAssert instead
     *
     * @param statusAssert the status assert
     * @return the http poller
     */
    @Deprecated()
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

    @JsonIgnore
    public AbstractHttpWrapper getHttp() {
        return http;
    }

    @JsonIgnore
    public HttpPoller setHttp(AbstractHttpWrapper http) {
        this.http = http;
        return this;
    }
}
