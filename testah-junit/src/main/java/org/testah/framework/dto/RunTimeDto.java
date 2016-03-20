package org.testah.framework.dto;

import java.util.Date;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.testah.TS;

public class RunTimeDto {

	private Long startTime = null;
	private Long endTime = null;
	private long duration = 0L;
	private String durationPretty = null;

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

	public String getStartTimePretty() {
		return TS.util().toDateString(startTime);
	}

	public String getEndTimePretty() {
		return TS.util().toDateString(endTime);
	}

	public String getDurationPretty() {
		if (null == durationPretty) {
			final Period period = new Duration(duration).toPeriod().normalizedStandard(PeriodType.time());
			durationPretty = PeriodFormat.getDefault().print(period);
		}
		return durationPretty;
	}

}
