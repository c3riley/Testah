package org.testah.framework.cli;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.testah.framework.annotations.Comment;
import org.testah.framework.enums.BrowserType;
import org.testah.framework.enums.TestType;

import net.sourceforge.argparse4j.annotation.Arg;

public class Params {

	private static String osName = null;

	private static String userDir = null;

	public static String addUserDir(final String path) {
		return (getUserDir() + File.separator + path).replace(File.separator + File.separator, File.separator);
	}

	public static String getOsName() {
		if (null == osName) {
			osName = System.getProperty("os.name").toLowerCase();
		}
		return osName;
	}

	public static String getUserDir() {
		if (null == userDir) {
			userDir = System.getProperty("user.dir");
		}
		return userDir;
	}

	public static boolean isMac() {
		if (getOsName().contains("darwin")) {
			return true;
		}
		return false;
	}

	public static boolean isUnix() {
		if (getOsName().contains("win")) {
			return false;
		}
		return true;
	}

	public static boolean isWindows() {
		if (getOsName().contains("win")) {
			return true;
		}
		return false;
	}

	public static void setOsName(final String osName) {
		Params.osName = osName;
	}

	public static void setUserDir(final String userDir) {
		Params.userDir = userDir;
	}

	@Comment(info = "Number of Concurrent Threads")
	@Arg(dest = "numConcurrentThreads")
	private int numConcurrentThreads = 1;

	@Comment(info = "Browser value allowed: ")
	@Arg(dest = "browser")
	private BrowserType browser = BrowserType.FIREFOX;

	@Comment(info = "envir values allowed: ")
	@Arg(dest = "envir")
	private String envir = "dev";

	@Comment(info = "Log level for log4j: TRACE, DEBUG, INFO, WARN, ERROR")
	@Arg(dest = "level")
	private Level level = Level.DEBUG;

	@Comment(info = "Default Wait time, determines the length of the loop, is not in seconds, works with the default pause time")
	@Arg(dest = "defaultWaitTime")
	private int defaultWaitTime = 10;

	@Comment(info = "Default Time to Pause when Called, value is in milliseconds.")
	@Arg(dest = "defaultPauseTime")
	private Long defaultPauseTime = 500L;

	@Comment(info = "Provide a value for the base level where tests can be found in the project, if empty will not run tests in jar")
	@Arg(dest = "lookAtInternalTests")
	private String lookAtInternalTests = "org.testah";

	@Comment(info = "Provide a path where to look for external uncompiled Test Classes, "
			+ "can be stored as .java or .groovy, can supply a directory or specific file")
	@Arg(dest = "lookAtExternalTests")
	private String lookAtExternalTests = "";

	@Comment(info = "SHould System keep track of all actions occuring during test run")
	@Arg(dest = "recordSteps")
	private boolean recordSteps = true;

	@Comment(info = "Should the System work as normal JUnit and stop a test case method on first fail, if false will continue to end then check for any fails")
	@Arg(dest = "throwExceptionOnFail")
	private boolean throwExceptionOnFail = true;

	@Comment(info = "[BAR1]Webdriver properties[BAR2]Override Browser UserAgent property, allows tests to act as mobile device, etc")
	@Arg(dest = "webDriver_userAgentValue")
	private String webDriver_userAgentValue = "";

	@Comment(info = "Use A Remote Dirver to run the Browser with, allows for Grid use")
	@Arg(dest = "webDriver_useRemoteDriver")
	private boolean webDriver_useRemoteDriver = false;

	@Comment(info = "If Use Remote Driver is True, value will be used to connect to a Webdriver Grid, can be local or remote")
	@Arg(dest = "webDriver_defaultRemoteUri")
	private String webDriver_defaultRemoteUri = "http://localhost:4444/wd/hub";

	@Comment(info = "Add path to the Firefox Binary (only needed if in a non default location")
	@Arg(dest = "webDriver_FirefoxDriverBinary")
	private String webDriver_firefoxDriverBinary = "";

	@Comment(info = "Add a path to the Phantomjs Driver Binary, required if using phatomjs, system will try to pull it locally if not found")
	@Arg(dest = "webDriver_PhantomJsDriverBinary")
	private String webDriver_phantomJsDriverBinary = "";

	@Comment(info = "Add a path to the Chrome Driver Binary, required if using phatomjs, system will try to pull it locally if not found")
	@Arg(dest = "webDriver_ChromeDriverBinary")
	private String webDriver_chromeDriverBinary = "";

	@Comment(info = "[BAR1]Reporting Properties[BAR2]Folder to write output to, if empty will be {user dir}/testahOutput")
	@Arg(dest = "output")
	private String output = "";

	@Comment(info = "Should the Runner create a xml result file")
	@Arg(dest = "useXunitFormatter")
	private boolean useXunitFormatter = true;

	@Comment(info = "Should the Runner create a html result file")
	@Arg(dest = "useHtmlFormatter")
	private boolean useHtmlFormatter = true;

	@Comment(info = "Should the Runner automatically open the Html Report in a browser")
	@Arg(dest = "autoOpenHtmlReport")
	private boolean autoOpenHtmlReport = true;

	@Comment(info = "[BAR1]Run Info Properties[BAR2]Version Id is a value that can be used to tell what version the test is testing against. Can be passed runtime via -Dtestah.versionId=login-0.0.2")
	@Arg(dest = "runInfo_versionId")
	private String runInfo_versionId = "";

	@Comment(info = "Build Number is a value that can be used to tell what build the test is testing against. Can be passed runtime via -Dtestah.buildNumber=login-0.0.2")
	@Arg(dest = "runInfo_buildNumber")
	private String runInfo_buildNumber = "";

	@Comment(info = "Run Id is a value that can be used to group many differnt results together. Can be passed runtime via -Dtestah.runId=run23")
	@Arg(dest = "runInfo_runId")
	private String runInfo_runId = "";

	@Comment(info = "Run Location is a way to tell where the test ran, can be local machine name, or Jenkins Job Url. Can be passed runtime via -Dtestah.runLocation=local")
	@Arg(dest = "runInfo_runLocation")
	private String runLocation = "localhost";

	@Comment(info = "Run Type is a way to tell what type of run the test execution was involed in, smoke, regression, other. Can be passed runtime via -Dtestah.runType=smoke")
	@Arg(dest = "runInfo_runType")
	private String runType = "";

	@Comment(info = "[BAR1]Filter Properties[BAR2]Filter Schema To Use. Leave empty to not apply fitlers, to use default filter, set value to DEFAULT.  You can also create your own, like REG, or SMOKE")
	@Arg(dest = "usefilterSchema")
	private String usefilterSchema = "DEFAULT";

	@Comment(info = "Filter using the Platform metadata field. Can be a comma seperated list of values that a test must match. Value can use the (~) for [Must Not Have], any match will be excluded.")
	@Arg(dest = "filterByPlatform")
	private String filterByPlatform = "";

	@Comment(info = "Filter using the Device metadata field. Can be a comma seperated list of values that a test must match. Value can use the (~) for [Must Not Have], any match will be excluded.")
	@Arg(dest = "filterByDevice")
	private String filterByDevice = "";

	@Comment(info = "Filter using the Component metadata field. Can be a comma seperated list of values that a test must match. Value can use the (~) for [Must Not Have], any match will be excluded.")
	@Arg(dest = "filterByComponent")
	private String filterByComponent = "";

	@Comment(info = "Filter using the RunType metadata field. Can be a comma seperated list of values that a test must match. Value can use the (~) for [Must Not Have], any match will be excluded.")
	@Arg(dest = "filterByRunType")
	private String filterByRunType = "";

	@Comment(info = "Filter Using The @KnownProblem Annotation.  True means testplan/testcase must not be a known problem. False means must be a known problem. Empty means do not use filter.")
	@Arg(dest = "filterKnownProblem")
	private String filterKnownProblem = "true";

	@Comment(info = "Filter using the Tag metadata field. Can be a comma seperated list of values that a test must match. Value can use the (~) for [Must Not Have], any match will be excluded.")
	@Arg(dest = "filterByTag")
	private String filterByTag = "";

	@Comment(info = "Filter by TestPlan Name, TestPlan must start with prefix. Can supply a comma seperated list.")
	@Arg(dest = "filterByTestPlanNameStartsWith")
	private String filterByTestPlanNameStartsWith = "";

	@Comment(info = "Filter by TestPlan Id, TestPlan must match supplied Uuid. Can supply a comma seperated list.")
	@Arg(dest = "filterById")
	private String filterById = "";

	@Comment(info = "Filter by TestType.  Can supply a comma seperated List.")
	@Arg(dest = "filterByTestType")
	private TestType filterByTestType = TestType.AUTOMATED;

	public String getOutput() {
		if (null == output || output.length() == 0) {
			output = addUserDir("testahOutput");
			new File(output).mkdirs();
		}
		return output;
	}

	public int getNumConcurrentThreads() {
		return numConcurrentThreads;
	}

	public void setNumConcurrentThreads(final int numConcurrentThreads) {
		this.numConcurrentThreads = numConcurrentThreads;
	}

	public BrowserType getBrowser() {
		return browser;
	}

	public void setBrowser(final BrowserType browser) {
		this.browser = browser;
	}

	public String getEnvir() {
		return envir;
	}

	public void setEnvir(final String envir) {
		this.envir = envir;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(final Level level) {
		this.level = level;
	}

	public int getDefaultWaitTime() {
		return defaultWaitTime;
	}

	public void setDefaultWaitTime(final int defaultWaitTime) {
		this.defaultWaitTime = defaultWaitTime;
	}

	public Long getDefaultPauseTime() {
		return defaultPauseTime;
	}

	public void setDefaultPauseTime(final Long defaultPauseTime) {
		this.defaultPauseTime = defaultPauseTime;
	}

	public String getLookAtInternalTests() {
		return lookAtInternalTests;
	}

	public void setLookAtInternalTests(final String lookAtInternalTests) {
		this.lookAtInternalTests = lookAtInternalTests;
	}

	public String getLookAtExternalTests() {
		return lookAtExternalTests;
	}

	public void setLookAtExternalTests(final String lookAtExternalTests) {
		this.lookAtExternalTests = lookAtExternalTests;
	}

	public boolean isRecordSteps() {
		return recordSteps;
	}

	public void setRecordSteps(final boolean recordSteps) {
		this.recordSteps = recordSteps;
	}

	public boolean isThrowExceptionOnFail() {
		return throwExceptionOnFail;
	}

	public void setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

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

	public boolean isAutoOpenHtmlReport() {
		return autoOpenHtmlReport;
	}

	public void setAutoOpenHtmlReport(final boolean autoOpenHtmlReport) {
		this.autoOpenHtmlReport = autoOpenHtmlReport;
	}

	public String getRunLocation() {
		return runLocation;
	}

	public void setRunLocation(final String runLocation) {
		this.runLocation = runLocation;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(final String runType) {
		this.runType = runType;
	}

	public String getUsefilterSchema() {
		return usefilterSchema;
	}

	public void setUsefilterSchema(final String usefilterSchema) {
		this.usefilterSchema = usefilterSchema;
	}

	public String getFilterByPlatform() {
		return filterByPlatform;
	}

	public void setFilterByPlatform(final String filterByPlatform) {
		this.filterByPlatform = filterByPlatform;
	}

	public String getFilterByDevice() {
		return filterByDevice;
	}

	public void setFilterByDevice(final String filterByDevice) {
		this.filterByDevice = filterByDevice;
	}

	public String getFilterByComponent() {
		return filterByComponent;
	}

	public void setFilterByComponent(final String filterByComponent) {
		this.filterByComponent = filterByComponent;
	}

	public String getFilterByRunType() {
		return filterByRunType;
	}

	public void setFilterByRunType(final String filterByRunType) {
		this.filterByRunType = filterByRunType;
	}

	public String getFilterKnownProblem() {
		return filterKnownProblem;
	}

	public void setFilterKnownProblem(final String filterKnownProblem) {
		this.filterKnownProblem = filterKnownProblem;
	}

	public String getFilterByTag() {
		return filterByTag;
	}

	public void setFilterByTag(final String filterByTag) {
		this.filterByTag = filterByTag;
	}

	public String getFilterByTestPlanNameStartsWith() {
		return filterByTestPlanNameStartsWith;
	}

	public void setFilterByTestPlanNameStartsWith(final String filterByTestPlanNameStartsWith) {
		this.filterByTestPlanNameStartsWith = filterByTestPlanNameStartsWith;
	}

	public String getFilterById() {
		return filterById;
	}

	public void setFilterById(final String filterById) {
		this.filterById = filterById;
	}

	public TestType getFilterByTestType() {
		return filterByTestType;
	}

	public void setFilterByTestType(final TestType filterByTestType) {
		this.filterByTestType = filterByTestType;
	}

	public boolean isWebDriver_useRemoteDriver() {
		return webDriver_useRemoteDriver;
	}

	public String getWebDriver_defaultRemoteUri() {
		return webDriver_defaultRemoteUri;
	}

	public String getWebDriver_firefoxDriverBinary() {
		return webDriver_firefoxDriverBinary;
	}

	public String getWebDriver_phantomJsDriverBinary() {
		return webDriver_phantomJsDriverBinary;
	}

	public String getWebDriver_chromeDriverBinary() {
		return webDriver_chromeDriverBinary;
	}

	public String getRunInfo_versionId() {
		return runInfo_versionId;
	}

	public String getRunInfo_buildNumber() {
		return runInfo_buildNumber;
	}

	public String getRunInfo_runId() {
		return runInfo_runId;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public String getWebDriver_userAgentValue() {
		return webDriver_userAgentValue;
	}

	public void setWebDriver_userAgentValue(final String webDriver_userAgentValue) {
		this.webDriver_userAgentValue = webDriver_userAgentValue;
	}

	public void setWebDriver_useRemoteDriver(final boolean webDriver_useRemoteDriver) {
		this.webDriver_useRemoteDriver = webDriver_useRemoteDriver;
	}

	public void setWebDriver_defaultRemoteUri(final String webDriver_defaultRemoteUri) {
		this.webDriver_defaultRemoteUri = webDriver_defaultRemoteUri;
	}

	public void setWebDriver_firefoxDriverBinary(final String webDriver_firefoxDriverBinary) {
		this.webDriver_firefoxDriverBinary = webDriver_firefoxDriverBinary;
	}

	public void setWebDriver_phantomJsDriverBinary(final String webDriver_phantomJsDriverBinary) {
		this.webDriver_phantomJsDriverBinary = webDriver_phantomJsDriverBinary;
	}

	public void setWebDriver_chromeDriverBinary(final String webDriver_chromeDriverBinary) {
		this.webDriver_chromeDriverBinary = webDriver_chromeDriverBinary;
	}

	public void setRunInfo_versionId(final String runInfo_versionId) {
		this.runInfo_versionId = runInfo_versionId;
	}

	public void setRunInfo_buildNumber(final String runInfo_buildNumber) {
		this.runInfo_buildNumber = runInfo_buildNumber;
	}

	public void setRunInfo_runId(final String runInfo_runId) {
		this.runInfo_runId = runInfo_runId;
	}

}
