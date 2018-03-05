package org.testah.client.dto;

import java.util.Date;


/**
 * The Class RunTimeDto.
 */
public class RunTimeDto {

    /** The start time. */
    private Long startTime = null;

    /** The start date. */
    private Date startDate = null;
    
    /** The end time. */
    private Long endTime   = null;

    /** The end date. */
    private Date endDate = null;
    
    /** The duration. */
    private long duration  = 0L;

    /**
     * Instantiates a new run time dto.
     */
    public RunTimeDto() {

    }

    /**
     * Start.
     *
     * @return the run time dto
     */
    public RunTimeDto start() {
        Date startDate = new Date();
        this.startDate = startDate;
        return start(startDate.getTime());

    }

    /**
     * Start.
     *
     * @param startTime the start time
     * @return the run time dto
     */
    public RunTimeDto start(final Long startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Stop.
     *
     * @return the run time dto
     */
    public RunTimeDto stop() {
        Date endDate = new Date();
        this.endDate = endDate;
        return stop(endDate.getTime());
    }

    /**
     * Stop.
     *
     * @param endTime the end time
     * @return the run time dto
     */
    public RunTimeDto stop(final Long endTime) {
        this.endTime = endTime;
        calculateDuration();
        return this;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Calculate duration.
     */
    public void calculateDuration() {
        this.duration = (this.endTime - this.startTime);
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() { return startDate; }

    /**
     * Gets the end date.
     * @return the end date
     */
    public Date getEndDate() { return endDate; }

}
