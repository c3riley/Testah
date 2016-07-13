package org.testah.driver.http.poller;

import org.testah.driver.http.response.ResponseDto;

/**
 * The Interface HttpPollerCheck.
 */
public interface HttpPollerCheck {

    /**
     * Checks if is done.
     *
     * @param response
     *            the response
     * @return true, if is done
     */
    public boolean isDone(final ResponseDto response);

}
