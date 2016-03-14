package org.testah.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.testah.TS;

public class TestahUtil {

	private final ObjectMapper map;

	public TestahUtil() {
		map = new ObjectMapper();
		map.enable(Feature.INDENT_OUTPUT);
		// map.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		map.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	public String toJsonPrint(final Object object) {
		final String s = toJson(object);
		TS.log().debug("JSON Output for " + object.getClass() + "\n" + s);
		return s;
	}

	public String toJson(final Object object) {

		if (null == object) {
			return null;
		}

		try {
			return map.writeValueAsString(object);
		} catch (final Exception ingoreFailOnFirstAttempt) {
			// Very odd bug fails first time then passes
		}

		try {
			return map.writeValueAsString(object);
		} catch (final Exception e) {
			TS.log().debug(e);
		}
		return null;
	}

	public ObjectMapper getMap() {
		return map;
	}

	public void pause(final Long milliseconds) {
		pause(milliseconds, null, null);
	}

	public void pause() {
		pause(TS.params().getDefaultPauseTime(), null, null);
	}

	public void pause(final String reasonForPause) {
		pause(TS.params().getDefaultPauseTime(), reasonForPause, null);
	}

	public void pause(final String reasonForPause, final Integer iteration) {
		pause(TS.params().getDefaultPauseTime(), reasonForPause, iteration);
	}

	public void pause(final Long milliseconds, final String reasonForPause, final Integer iteration) {
		try {
			if (null == iteration) {
				TS.log().debug(reasonForPause + " - " + milliseconds + "ms");
			} else {
				TS.log().debug(iteration + "] " + reasonForPause + " - " + milliseconds + "ms");
			}

			Thread.sleep(milliseconds);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public String now() {
		return now("MM/dd/yyyy HH:mm:ss.S");
	}

	public String nowUnique() {
		return now("MMddyyyyHH:mm:ss.S");
	}

	public String now(final String dateTimeFormat) {
		return toDateString(System.currentTimeMillis(), dateTimeFormat);
	}

	public String toDateString(final Long time) {
		return toDateString(time, "MM/dd/yyyy HH:mm:ss.S");
	}

	public String toDateString(final Long time, final String dateTimeFormat) {
		final SimpleDateFormat f = new SimpleDateFormat(dateTimeFormat);
		return f.format(new Date(time));
	}

}
