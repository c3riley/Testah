package org.testah.framework.cli;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.testah.framework.annotations.Comment;
import org.testah.framework.enums.BrowserType;
import org.testah.framework.enums.TestType;

import net.sourceforge.argparse4j.annotation.Arg;

public class Params {

	@Comment(info = "Number of Concurrent Threads")
	@Arg(dest = "numConcurrentThreads")
	private final int numConcurrentThreads = 1;

	@Comment(info = "Folder to write output to, if empty will be {user dir}/testahOutput")
	@Arg(dest = "output")
	private String output = "";

	@Comment(info = "Browser value allowed: ")
	@Arg(dest = "browser")
	private BrowserType browser = BrowserType.FIREFOX;

	@Comment(info = "envir values allowed: ")
	@Arg(dest = "envir")
	private String envir = "dev";

	@Comment(info = "Log level for log4j: TRACE, DEBUG, INFO, WARN, ERROR")
	@Arg(dest = "level")
	private final Level level = Level.DEBUG;

	private int defaultWaitTime = 10;
	private Long defaultPauseTime = 500L;
	private String defaultRemoteUri = "http://localhost:4444/wd/hub";

	private String userAgentValue = null;

	private boolean useRemoteDriver = false;
	private String FirefoxDriverBinary = "";

	private static String osName = null;
	private static String userDir = null;

	@Comment(info = "")
	@Arg(dest = "filterByTagFound")
	private final String filterByTagFound = "";

	@Comment(info = "")
	@Arg(dest = "filterByTagNotFound")
	private final String filterByTagNotFound = "";

	@Comment(info = " ")
	@Arg(dest = "filterByNameStartsWith")
	private final String filterByNameStartsWith = "";

	@Comment(info = "")
	@Arg(dest = "filterByTestType")
	private final TestType filterByTestType = TestType.AUTOMATED;

	@Comment(info = "")
	@Arg(dest = "lookAtInternalTests")
	private final String lookAtInternalTests = "org.testah";

	@Comment(info = "")
	@Arg(dest = "lookAtExternalTests")
	private final String lookAtExternalTests = "";

	public static void setOsName(final String osName) {
		Params.osName = osName;
	}

	public Level getLevel() {
		return level;
	}

	public String getFilterByTagFound() {
		return filterByTagFound;
	}

	public String getFilterByTagNotFound() {
		return filterByTagNotFound;
	}

	public String getFilterByNameStartsWith() {
		return filterByNameStartsWith;
	}

	public TestType getFilterByTestType() {
		return filterByTestType;
	}

	public String getLookAtInternalTests() {
		return lookAtInternalTests;
	}

	public String getLookAtExternalTests() {
		return lookAtExternalTests;
	}

	public BrowserType getBrowser() {
		return browser;
	}

	public String getEnvir() {
		return envir;
	}

	public int getDefaultWaitTime() {
		return defaultWaitTime;
	}

	public void setDefaultWaitTime(final int defaultWaitTime) {
		this.defaultWaitTime = defaultWaitTime;
	}

	public String getDefaultRemoteUri() {
		return defaultRemoteUri;
	}

	public void setDefaultRemoteUri(final String defaultRemoteUri) {
		this.defaultRemoteUri = defaultRemoteUri;
	}

	public boolean isUseRemoteDriver() {
		return useRemoteDriver;
	}

	public void setUseRemoteDriver(final boolean useRemoteDriver) {
		this.useRemoteDriver = useRemoteDriver;
	}

	public void setBrowser(final BrowserType browser) {
		this.browser = browser;
	}

	public void setEnvir(final String envir) {
		this.envir = envir;
	}

	public String getUserAgentValue() {
		return userAgentValue;
	}

	public void setUserAgentValue(final String userAgentValue) {
		this.userAgentValue = userAgentValue;
	}

	public Long getDefaultPauseTime() {
		return defaultPauseTime;
	}

	public void setDefaultPauseTime(final Long defaultPauseTime) {
		this.defaultPauseTime = defaultPauseTime;
	}

	public String getFirefoxDriverBinary() {
		return FirefoxDriverBinary;
	}

	public void setFirefoxDriverBinary(final String firefoxDriverBinary) {
		FirefoxDriverBinary = firefoxDriverBinary;
	}

	public static String getUserDir() {
		if (null == userDir) {
			userDir = System.getProperty("user.dir");
		}
		return userDir;
	}

	public static String addUserDir(final String path) {
		return (getUserDir() + File.separator + path).replace(File.separator + File.separator, File.separator);
	}

	public static String getOsName() {
		if (null == osName) {
			osName = System.getProperty("os.name").toLowerCase();
		}
		return osName;
	}

	public static boolean isUnix() {
		if (getOsName().contains("win")) {
			return false;
		}
		return true;
	}

	public int getNumConcurrentThreads() {
		return numConcurrentThreads;
	}

	public String getOutput() {
		if (null == output || output.length() == 0) {
			output = addUserDir("testahOutput");
			new File(output).mkdirs();
		}
		return output;
	}
}
