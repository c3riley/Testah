package org.testah.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.testah.TS;
import org.testah.framework.cli.Params;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * The Class TestahUtil.
 */
public class TestahUtil {

    /**
     * The map.
     */
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
     * @param object the object
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
     * @param object the object
     * @return the string
     */
    public String toJson(final Object object) {

        if (null == object) {
            return null;
        }

        try {
            return map.writeValueAsString(object);
        } catch (final Exception ignoreFailOnFirstAttempt) {
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
     * @param milliseconds the milliseconds
     * @return the testah util
     */
    public TestahUtil pause(final Long milliseconds) {
        pause(milliseconds, null, null);
        return this;
    }

    /**
     * Pause.
     *
     * @param milliseconds   the milliseconds
     * @param reasonForPause the reason for pause
     * @param iteration      the iteration
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
     * @param reasonForPause the reason for pause
     * @return the testah util
     */
    public TestahUtil pause(final String reasonForPause) {
        pause(TS.params().getDefaultPauseTime(), reasonForPause, null);
        return this;
    }

    /**
     * Pause.
     *
     * @param milliseconds   the milliseconds
     * @param reasonForPause the reason for pause
     * @return the testah util
     */
    public TestahUtil pause(final Long milliseconds, final String reasonForPause) {
        pause(milliseconds, reasonForPause, null);
        return this;
    }

    /**
     * Pause.
     *
     * @param reasonForPause the reason for pause
     * @param iteration      the iteration
     * @return the testah util
     */
    public TestahUtil pause(final String reasonForPause, final Integer iteration) {
        pause(TS.params().getDefaultPauseTime(), reasonForPause, iteration);
        return this;
    }

    /**
     * Pause.
     *
     * @param reasonForPause the reason for pause
     * @param milliseconds   the milliseconds
     * @return the testah util
     */
    public TestahUtil pause(final String reasonForPause, final Long milliseconds) {
        pause(milliseconds, reasonForPause, null);
        return this;
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
     * @param dateTimeFormat the date time format
     * @return the string
     */
    public String now(final String dateTimeFormat) {
        return toDateString(System.currentTimeMillis(), dateTimeFormat);
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
     * To date string.
     *
     * @param time           the time
     * @param dateTimeFormat the date time format
     * @return the string
     */
    public String toDateString(final Long time, final String dateTimeFormat) {
        return toDateString(time, dateTimeFormat, TimeZone.getDefault().getID());
    }

    /**
     * To date string string.
     *
     * @param time           the time
     * @param dateTimeFormat the date time format
     * @param timeZone       the time zone
     * @return the string
     */
    public String toDateString(final Long time, final String dateTimeFormat, final String timeZone) {
        if (time != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            return sdf.format(new Date(time));
        }
        return "";
    }


    /**
     * To date string.
     *
     * @param time the time
     * @return the string
     */
    public String toDateString(final Long time) {
        return toDateString(time, "MM/dd/yyyy HH:mm:ss.S");
    }

    /**
     * Gets the duration pretty.
     *
     * @param duration the duration
     * @return the duration pretty
     */
    public String getDurationPretty(final Long duration) {
        if (duration != null) {
            final Period period = new Duration(duration).toPeriod().normalizedStandard(PeriodType.time());
            return PeriodFormat.getDefault().print(period);
        }
        return "";
    }

    /**
     * Create File reference to parent directory of a file download.
     *
     * @param relativePath to download parent directory.
     * @return return file for download
     */
    public File getDownloadDestinationDirectory(String relativePath) {
        return new File(Params.addUserDir(relativePath));
    }

    /**
     * Download file.
     *
     * @param urlToUse       the url to use
     * @param destinationDir the target directory
     * @return the file
     */
    public File downloadFile(final String urlToUse, final File destinationDir) {
        try {
            TS.log().trace("downloadFileDirectory mkdirs: " + destinationDir.mkdirs());
            final File fileDownLoaded = File.createTempFile("download", "", destinationDir);
            final byte[] fileBytes = TS.http().doGet(urlToUse).getResponseBytes();
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileDownLoaded)) {
                fileOutputStream.write(fileBytes);
            }
            return fileDownLoaded;
        } catch (final Exception e) {
            TS.log().warn(e);
        }
        return null;
    }

    /**
     * Download file.
     *
     * @param urlToUse the url to use
     * @return the file
     */
    public File downloadFile(final String urlToUse) {
        return downloadFile(urlToUse, "");
    }

    /**
     * Download file.
     *
     * @param urlToUse    the url to use
     * @param destination the destination
     * @return the file
     */
    public File downloadFile(final String urlToUse, final String destination) {
        final File downloadFileDirectory = getDownloadDestinationDirectory(destination);
        return downloadFile(urlToUse, downloadFileDirectory);
    }

    /**
     * Create File reference to parent directory of a file download.
     *
     * @param relativePath to download parent directory.
     * @return Unzip directory File
     */
    public File getUnZipDestinationDirectory(String relativePath) {
        return new File(Params.addUserDir(relativePath));
    }

    /**
     * Un zip.
     *
     * @param zip               the zip
     * @param destinationFolder the destination folder
     * @return the file
     */
    public File unZip(final File zip, final File destinationFolder) {
        TS.log().trace("destination mkdirs: " + destinationFolder.mkdirs());
        try (ZipFile zipFile = new ZipFile(zip)) {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final File entryDestination = new File(destinationFolder, entry.getName());
                if (entry.isDirectory()) {
                    TS.log().trace("entryDestination mkdirs: " + entryDestination.mkdirs());
                } else {
                    TS.log().trace("getParentFile mkdirs: " + entryDestination.getParentFile().mkdirs());
                    final InputStream in = zipFile.getInputStream(entry);
                    final OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    out.close();
                }
            }
        } catch (final Exception e) {
            TS.log().warn(e);
        }
        return destinationFolder;
    }

    /**
     * Split camel case.
     *
     * @param stringToUse the string To Use
     * @return the string
     */
    public String splitCamelCase(final String stringToUse) {
        return stringToUse.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }

    /**
     * Url encode.
     *
     * @param value the value
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
     * @param value the value
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
     * @param min the min
     * @param max the max
     * @return the random int
     */
    public int getRandomInt(final int min, final int max) {
        final Random rn = new Random();
        final int range = max - min + 1;
        return rn.nextInt(range) + min;
    }

    /**
     * Gets the resource as string.
     *
     * @param path the path
     * @return the resource as string
     */
    public String getResourceAsString(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return IOUtils.toString(is, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            TS.log().error(e);
        }
        TS.log().debug("Resource[" + path + "] not found via getClass().getResourceAsStream");
        return null;
    }

    /**
     * Gets resource folder file.
     *
     * @param folder   the folder
     * @param fileName the file name
     * @return the resource folder file
     */
    public File getResourceFolderFile(final String folder, final String fileName) {
        final List<File> files = getResourceFolderFiles(folder);
        Optional<File> fileFound = files.stream().filter(file -> StringUtils.equalsIgnoreCase(file.getName(), fileName)).findFirst();
        if (fileFound.isPresent()) {
            return fileFound.get();
        }
        return null;
    }

    /**
     * Gets resource folder files.
     *
     * @param folder the folder
     * @return the resource folder files
     */
    public List<File> getResourceFolderFiles(final String folder) {
        return getResourceFolderFiles(folder, true);
    }

    /**
     * Gets resource folder files.
     *
     * @param folder             the folder
     * @param excludeDirectories the exclude directories
     * @return the resource folder files
     */
    public List<File> getResourceFolderFiles(final String folder, final boolean excludeDirectories) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //guard against path starting with a slash
        final URL url = loader.getResource(StringUtils.replacePattern(folder, "^/+", ""));
        if (url != null) {
            final String path = url.getPath();
            if (path != null) {
                final File[] arrayOfFiles = new File(path).listFiles();
                if (arrayOfFiles != null) {
                    return Arrays.asList(arrayOfFiles).stream().filter(file -> !excludeDirectories ||
                        !file.isDirectory()).collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * get time format in HH:MM:SS:MMM.
     *
     * @param duration - duration in milliseconds
     * @return duration in HH:MM:SS:MMM format
     */
    public String getDurationShort(final Long duration) {
        if (duration != null) {
            return DurationFormatUtils.formatDuration(duration, "HH:mm:ss:S");
        }
        return "";
    }
}
