package org.testah.driver.http.poller;

import org.testah.driver.http.requests.AbstractRequestDto;

import java.util.Date;

public class PollerDto {

    final AbstractRequestDto<?> request;
    final Long start;
    int pollCtr = 0;
    boolean pollCheckPassed = false;
    boolean exceptionOccurred = false;

    public PollerDto(final AbstractRequestDto<?> request) {
        this.request = request;
        start = new Date().getTime();
    }



}
