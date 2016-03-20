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
	private int numConcurrentThreads = 1;

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
	private Level level = Level.DEBUG;

	private int defaultWaitTime = 10;
	private Long defaultPauseTime = 500L;
	private String defaultRemoteUri = "http://localhost:4444/wd/hub";

	private String userAgentValue = null;

	private boolean useRemoteDriver = false;

	@Comment(info = "")
	@Arg(dest = "FirefoxDriverBinary")
	private String FirefoxDriverBinary = "";

	public String getPhantomJsDriverBinary() {
		return PhantomJsDriverBinary;
	}

	public void setPhantomJsDriverBinary(final String phantomJsDriverBinary) {
		PhantomJsDriverBinary = phantomJsDriverBinary;
	}

	@Comment(info = "")
	@Arg(dest = "PhantomJsDriverBinary")
	private String PhantomJsDriverBinary = "";

	@Comment(info = "")
	@Arg(dest = "ChromeDriverBinary")
	private String ChromeDriverBinary = "";

	private static String osName = null;
	private static String userDir = null;

	@Comment(info = "")
	@Arg(dest = "filterByTagFound")
	private String filterByTagFound = "";

	@Comment(info = "")
	@Arg(dest = "filterByTagNotFound")
	private String filterByTagNotFound = "";

	@Comment(info = " ")
	@Arg(dest = "filterByNameStartsWith")
	private String filterByNameStartsWith = "";

	@Comment(info = "")
	@Arg(dest = "filterByTestType")
	private TestType filterByTestType = TestType.AUTOMATED;

	@Comment(info = "")
	@Arg(dest = "lookAtInternalTests")
	private String lookAtInternalTests = "org.testah";

	public void setNumConcurrentThreads(final int numConcurrentThreads) {
		this.numConcurrentThreads = numConcurrentThreads;
	}

	public void setLevel(final Level level) {
		this.level = level;
	}

	public void setFilterByTagFound(final String filterByTagFound) {
		this.filterByTagFound = filterByTagFound;
	}

	public void setFilterByTagNotFound(final String filterByTagNotFound) {
		this.filterByTagNotFound = filterByTagNotFound;
	}

	public void setFilterByNameStartsWith(final String filterByNameStartsWith) {
		this.filterByNameStartsWith = filterByNameStartsWith;
	}

	public void setFilterByTestType(final TestType filterByTestType) {
		this.filterByTestType = filterByTestType;
	}

	public void setLookAtInternalTests(final String lookAtInternalTests) {
		this.lookAtInternalTests = lookAtInternalTests;
	}

	public void setLookAtExternalTests(final String lookAtExternalTests) {
		this.lookAtExternalTests = lookAtExternalTests;
	}

	@Comment(info = "")
	@Arg(dest = "lookAtExternalTests")
	private String lookAtExternalTests = "";

	@Comment(info = "")
	@Arg(dest = "recordSteps")
	private boolean recordSteps = true;

	@Comment(info = "")
	@Arg(dest = "throwExceptionOnFail")
	private boolean throwExceptionOnFail = true;

	@Comment(info = "")
	@Arg(dest = "useXunitFormatter")
	private boolean useXunitFormatter = true;

	public boolean isUseXunitFormatter() {
		return useXunitFormatter;
	}

	public void setUseXunitFormatter(final boolean useXunitFormatter) {
		this.useXunitFormatter = useXunitFormatter;
	}

	public boolean isUseHtmlFormatter() {
		return useHtmlFormatter;
	}

	public void setUseHtmlFormatter(final boolean useHtmlFormatter) {
		this.useHtmlFormatter = useHtmlFormatter;
	}

	@Comment(info = "")
	@Arg(dest = "useHtmlFormatter")
	private boolean useHtmlFormatter = true;

	public boolean isThrowExceptionOnFail() {
		return throwExceptionOnFail;
	}

	public void setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	public void setRecordSteps(final boolean recordSteps) {
		this.recordSteps = recordSteps;
	}

	public boolean isRecordSteps() {
		return recordSteps;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public static void setUserDir(final String userDir) {
		Params.userDir = userDir;
	}

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

	public static boolean isMac() {
		if (getOsName().contains("darwin")) {
			return true;
		}
		return true;
	}

	public static boolean isWIndows() {
		if (getOsName().contains("win")) {
			return true;
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

	public String getChromeDriverBinary() {
		return ChromeDriverBinary;
	}

	public void setChromeDriverBinary(final String chromeDriverBinary) {
		ChromeDriverBinary = chromeDriverBinary;
	}
}
