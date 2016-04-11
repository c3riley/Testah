package org.testah.util.database.dto;

import java.util.Date;

import org.testah.TS;

public class SqlExecutionDto {

	final String sql;
	Date startTime;
	Date endTime;
	Long duration;
	String durationPretty;
	Integer resultCount = null;
	Integer updateCount = null;

	public SqlExecutionDto(final String sql) {
		this.sql = sql;
	}

	public SqlExecutionDto start() {
		this.startTime = new Date();
		return this;
	}

	public SqlExecutionDto end() {
		this.endTime = new Date();
		this.duration = (this.endTime.getTime() - this.startTime.getTime());
		this.durationPretty = TS.util().getDurationPretty(this.duration);
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public SqlExecutionDto setStartTime(final Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public SqlExecutionDto setEndTime(final Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public SqlExecutionDto setDuration(final Long duration) {
		this.duration = duration;
		return this;
	}

	public String getDurationPretty() {
		return durationPretty;
	}

	public void setDurationPretty(final String durationPretty) {
		this.durationPretty = durationPretty;
	}

	public String getSql() {
		return sql;
	}

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(final Integer resultCount) {
		this.resultCount = resultCount;
	}

	public Integer getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(final Integer updateCount) {
		this.updateCount = updateCount;
	}

}
