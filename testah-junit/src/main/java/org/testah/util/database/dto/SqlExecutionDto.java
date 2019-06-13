package org.testah.util.database.dto;

import org.testah.TS;

import java.util.Date;

/**
 * The type Sql execution dto.
 */
public class SqlExecutionDto {

    /**
     * The Sql.
     */
    final String sql;
    /**
     * The Start time.
     */
    Long startTime;
    /**
     * The End time.
     */
    Long endTime;
    /**
     * The Duration.
     */
    Long duration;
    /**
     * The Duration pretty.
     */
    String durationPretty;
    /**
     * The Result count.
     */
    Integer resultCount = null;
    /**
     * The Update count.
     */
    Integer updateCount = null;

    /**
     * Constructor.
     *
     * @param sql SQL string
     */
    public SqlExecutionDto(final String sql) {
        this.sql = sql;
    }

    /**
     * Mark the start of the SQL execution.
     *
     * @return this object
     */
    public SqlExecutionDto start() {
        return start(new Date());
    }

    /**
     * Start sql execution dto.
     *
     * @param date the date
     * @return the sql execution dto
     */
    public SqlExecutionDto start(final Date date) {
        this.startTime = date.getTime();
        return this;
    }

    public SqlExecutionDto end() {
        return end(new Date());
    }

    /**
     * Mark the end to the SQL execution.
     *
     * @param date the date
     * @return this object
     */
    public SqlExecutionDto end(final Date date) {
        this.endTime = date.getTime();
        this.duration = (this.endTime - this.startTime);
        this.durationPretty = TS.util().getDurationPretty(this.duration);
        return this;
    }

    /**
     * Get the start date/time.
     *
     * @return the start date/time
     */
    public Date getStartTime() {
        return (Date) new Date(startTime);
    }

    /**
     * Set the start date/time.
     *
     * @param startTime the start date/time
     * @return this object
     */
    public SqlExecutionDto setStartTime(final Date startTime) {
        this.startTime = startTime.getTime();
        return this;
    }

    /**
     * Get the end date/time.
     *
     * @return the end date.
     */
    public Date getEndTime() {
        return new Date(endTime);
    }

    /**
     * Set the end time.
     *
     * @param endTime the end date
     * @return this object
     */
    public SqlExecutionDto setEndTime(final Date endTime) {
        this.endTime = endTime.getTime();
        return this;
    }

    /**
     * Get the duration.
     *
     * @return the duration
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Set the duration.
     *
     * @param duration the duration
     * @return this object
     */
    public SqlExecutionDto setDuration(final Long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Get the duration in readable format.
     *
     * @return the duration
     */
    public String getDurationPretty() {
        return durationPretty;
    }

    /**
     * Set the duration.
     *
     * @param durationPretty the duration
     * @return this object
     */
    public SqlExecutionDto setDurationPretty(final String durationPretty) {
        this.durationPretty = durationPretty;
        return this;
    }

    /**
     * Get the SQL string.
     *
     * @return the SQL string
     */
    public String getSql() {
        return sql;
    }

    /**
     * Get the result count.
     *
     * @return the result count
     */
    public Integer getResultCount() {
        return resultCount;
    }

    /**
     * Set the result count.
     *
     * @param resultCount the result count
     * @return this object
     */
    public SqlExecutionDto setResultCount(final Integer resultCount) {
        this.resultCount = resultCount;
        return this;
    }

    /**
     * Get the update count.
     *
     * @return update count
     */
    public Integer getUpdateCount() {
        return updateCount;
    }

    /**
     * Set the update count.
     *
     * @param updateCount the count
     * @return this object
     */
    public SqlExecutionDto setUpdateCount(final Integer updateCount) {
        this.updateCount = updateCount;
        return this;
    }
}
