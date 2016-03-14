package org.testah.framework.dto;

import java.util.Date;

public class RunTimeDto {

    private Long startTime = null;
    private Long endTime   = null;
    private long duration  = 0L;
    
    public RunTimeDto() {

    }

    public RunTimeDto start() {
        return start(new Date().getTime());
        
    }
    
    public RunTimeDto start(final Long startTime) {
        this.startTime = startTime;
        return this;
    }
    
    public RunTimeDto stop() {
        return stop(new Date().getTime());
    }
    
    public RunTimeDto stop(final Long endTime) {
        this.endTime = endTime;
        calculateDuration();
        return this;
    }
    
    public long getDuration() {
        return duration;
    }

    public void calculateDuration() {
        this.duration = (this.endTime - this.startTime);
    }
    
    public Long getStartTime() {
        return startTime;
    }
    
    public Long getEndTime() {
        return endTime;
    }
    
}
