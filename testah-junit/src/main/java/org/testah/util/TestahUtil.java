package org.testah.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.testah.TS;
import org.testah.framework.cli.Params;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * The Class TestahUtil.
 */
public class TestahUtil {

	/** The map. */
	private final ObjectMapper map;

	/**
	 * Instantiates a new testah util.
	 */
	public TestahUtil() {
		map = new ObjectMapper();
		map.enable(SerializationFeature.INDENT_OUTPUT);
		// map.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		map.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	/**
	 * To json print.
	 *
	 * @param object
	 *            the object
	 * @return the string
	 */
	public String toJsonPrint(final Object object) {
		final String s = toJson(object);
		TS.log().debug("JSON Output for " + object.getClass() + "\n" + s);
		return s;
	}

	/**
	 * To json.
	 *
	 * @param object
	 *            the object
	 * @return the string
	 */
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

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public ObjectMapper getMap() {
		return map;
	}

	/**
	 * Pause.
	 *
	 * @param milliseconds            the milliseconds
	 * @return the testah util
	 */
	public TestahUtil pause(final Long milliseconds) {
		pause(milliseconds, null, null);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @return the testah util
	 */
	public TestahUtil pause() {
		pause(TS.params().getDefaultPauseTime(), null, null);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @param reasonForPause            the reason for pause
	 * @return the testah util
	 */
	public TestahUtil pause(final String reasonForPause) {
		pause(TS.params().getDefaultPauseTime(), reasonForPause, null);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @param milliseconds            the milliseconds
	 * @param reasonForPause            the reason for pause
	 * @return the testah util
	 */
	public TestahUtil pause(final Long milliseconds, final String reasonForPause) {
		pause(milliseconds, reasonForPause, null);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @param reasonForPause            the reason for pause
	 * @param iteration            the iteration
	 * @return the testah util
	 */
	public TestahUtil pause(final String reasonForPause, final Integer iteration) {
		pause(TS.params().getDefaultPauseTime(), reasonForPause, iteration);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @param reasonForPause            the reason for pause
	 * @param milliseconds            the milliseconds
	 * @return the testah util
	 */
	public TestahUtil pause(final String reasonForPause, final Long milliseconds) {
		pause(milliseconds, reasonForPause, null);
		return this;
	}

	/**
	 * Pause.
	 *
	 * @param milliseconds            the milliseconds
	 * @param reasonForPause            the reason for pause
	 * @param iteration            the iteration
	 * @return the testah util
	 */
	public TestahUtil pause(final Long milliseconds, final String reasonForPause, final Integer iteration) {
		try {
			if (null == iteration) {
				TS.log().debug("pause - " + reasonForPause + " - " + milliseconds + "ms");
			} else {
				TS.log().debug("pause - " + iteration + "] " + reasonForPause + " - " + milliseconds + "ms");
			}

			Thread.sleep(milliseconds);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return this;
	}

	/**
	 * Now.
	 *
	 * @return the string
	 */
	public String now() {
		return now("MM/dd/yyyy HH:mm:ss.S");
	}

	/**
	 * Now unique.
	 *
	 * @return the string
	 */
	public String nowUnique() {
		return now("MMddyyyyHHmmssS");
	}

	/**
	 * Now.
	 *
	 * @param dateTimeFormat
	 *            the date time format
	 * @return the string
	 */
	public String now(final String dateTimeFormat) {
		return toDateString(System.currentTimeMillis(), dateTimeFormat);
	}

	/**
	 * To date string.
	 *
	 * @param time
	 *            the time
	 * @return the string
	 */
	public String toDateString(final Long time) {
		return toDateString(time, "MM/dd/yyyy HH:mm:ss.S");
	}

	/**
	 * To date string.
	 *
	 * @param time
	 *            the time
	 * @param dateTimeFormat
	 *            the date time format
	 * @return the string
	 */
	public String toDateString(final Long time, final String dateTimeFormat) {
		final SimpleDateFormat f = new SimpleDateFormat(dateTimeFormat);
		return f.format(new Date(time));
	}

	/**
	 * Gets the duration pretty.
	 *
	 * @param duration
	 *            the duration
	 * @return the duration pretty
	 */
	public String getDurationPretty(final Long duration) {
		final Period period = new Duration(duration).toPeriod().normalizedStandard(PeriodType.time());
		return PeriodFormat.getDefault().print(period);
	}

	/**
	 * Download file.
	 *
	 * @param urlToUse
	 *            the url to use
	 * @return the file
	 */
	public File downloadFile(final String urlToUse) {
		return downloadFile(urlToUse, "");
	}

	/**
	 * Download file.
	 *
	 * @param urlToUse
	 *            the url to use
	 * @param destination
	 *            the destination
	 * @return the file
	 */
	public File downloadFile(final String urlToUse, final String destination) {
		try {
			final File downloadFileDirectory = new File(Params.addUserDir(destination));
			downloadFileDirectory.mkdirs();
			final File fileDownLoaded = File.createTempFile("download", "", downloadFileDirectory);
			final byte[] fileBytes = TS.http().doGet(urlToUse).getResponseBytes();
			try (FileOutputStream fileOuputStream = new FileOutputStream(
					File.createTempFile("download", "", downloadFileDirectory))) {
				fileOuputStream.write(fileBytes);
			}
			return fileDownLoaded;
		} catch (final Exception e) {
			TS.log().warn(e);
		}
		return null;
	}

	/**
	 * Un zip.
	 *
	 * @param zip
	 *            the zip
	 * @param destination
	 *            the destination
	 * @return the file
	 */
	public File unZip(final File zip, final File destination) {
		destination.mkdirs();
		try (ZipFile zipFile = new ZipFile(zip)) {
			final Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				final ZipEntry entry = entries.nextElement();
				final File entryDestination = new File(destination, entry.getName());
				if (entry.isDirectory()) {
					entryDestination.mkdirs();
				} else {
					entryDestination.getParentFile().mkdirs();
					final InputStream in = zipFile.getInputStream(entry);
					final OutputStream out = new FileOutputStream(entryDestination);
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
					out.close();
				}
			}
		} catch (final Exception e) {
			TS.log().warn(e);
		} finally {

		}
		return destination;
	}

	/**
	 * Split camel case.
	 *
	 * @param s
	 *            the s
	 * @return the string
	 */
	public String splitCamelCase(final String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	/**
	 * Url encode.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public String urlEncode(final String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (final Exception e) {
			TS.log().error("Issue with urlEncode for " + value, e);
		}
		return "";
	}

	/**
	 * Html encode.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	public String htmlEncode(final String value) {
		try {
			return StringEscapeUtils.escapeHtml(value);
		} catch (final Exception e) {
			TS.log().error("Issue with htmlEncode for " + value, e);
		}
		return "";
	}

	/**
	 * Gets the random int.
	 *
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the random int
	 */
	public int getRandomInt(final int min, final int max) {
		final Random rn = new Random();
		final int range = max - min + 1;
		return rn.nextInt(range) + min;
	}

}
