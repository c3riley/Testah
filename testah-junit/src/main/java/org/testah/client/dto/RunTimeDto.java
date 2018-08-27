package org.testah.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.testah.TS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The Class RunTimeDto.
 */
public class RunTimeDto {

    /**
     * Date format.
     */
    @JsonIgnore
    private SimpleDateFormat dateFormat;

    /**
     * The start time.
     */
    private Long startTime = null;

    /**
     * The start date.
     */
    private Date startDate = null;

    /**
     * The end time.
     */
    private Long endTime = null;

    /**
     * The end date.
     */
    private Date endDate = null;

    /**
     * The duration.
     */
    private long duration = 0L;

    /**
     * Instantiates a new run time dto.
     */
    public RunTimeDto() {
        this.dateFormat = new SimpleDateFormat(TS.params().getTimeFormat());
        if (StringUtils.isEmpty(TS.params().getTimezone())) {
            this.dateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            this.dateFormat.setTimeZone(TimeZone.getTimeZone(TS.params().getTimezone()));
        }
    }

    /**
     * Start.
     *
     * @return the run time dto
     */
    @JsonIgnore
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
    @JsonIgnore
    public RunTimeDto start(final Long startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Stop.
     *
     * @return the run time dto
     */
    @JsonIgnore
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
    @JsonIgnore
    public RunTimeDto stop(final Long endTime) {
        this.endTime = endTime;
        this.endDate = new Date(endTime);
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
     * Returns the start date in the preset dateformat.
     *
     * @return start date
     */
    public String getStartDate() {
        return formatDate(this.startDate);
    }

    /**
     * Returns the end date in the preset dateformat.
     *
     * @return end date
     */
    public String getEndDate() {
        return formatDate(this.endDate);
    }

    /**
     * Format date string. Used to format a date like the getter for getStart and getEnd dates do.
     *
     * @param date the date
     * @return the string of the date formated
     */
    @JsonIgnore
    public String formatDate(final Date date) {
        return this.dateFormat.format(date);
    }

}
