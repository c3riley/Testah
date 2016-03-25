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
    private int           numConcurrentThreads   = 1;

    @Comment(info = "Folder to write output to, if empty will be {user dir}/testahOutput")
    @Arg(dest = "output")
    private String        output                 = "";

    @Comment(info = "Browser value allowed: ")
    @Arg(dest = "browser")
    private BrowserType   browser                = BrowserType.FIREFOX;

    @Comment(info = "envir values allowed: ")
    @Arg(dest = "envir")
    private String        envir                  = "dev";

    @Comment(info = "Log level for log4j: TRACE, DEBUG, INFO, WARN, ERROR")
    @Arg(dest = "level")
    private Level         level                  = Level.DEBUG;

    @Comment(
            info = "Default Wait time, determines the length of the loop, is not in seconds, works with the default pause time")
    @Arg(dest = "defaultWaitTime")
    private int           defaultWaitTime        = 10;

    @Comment(info = "Default Time to Pause when Called")
    @Arg(dest = "defaultPauseTime")
    private Long          defaultPauseTime       = 500L;

    @Comment(info = "Override Browser UserAgent property, allows tests to act as mobile device, etc")
    @Arg(dest = "userAgentValue")
    private String        userAgentValue         = null;

    @Comment(info = "Use A Remote Dirver to run the Browser with, allows for Grid use")
    @Arg(dest = "useRemoteDriver")
    private boolean       useRemoteDriver        = false;

    @Comment(
            info = "If Use Remote Driver is True, value will be used to connect to a Webdriver Grid, can be local or remote")
    @Arg(dest = "defaultRemoteUri")
    private String        defaultRemoteUri       = "http://localhost:4444/wd/hub";

    @Comment(info = "Add path to the Firefox Binary (only needed if in a non default location")
    @Arg(dest = "FirefoxDriverBinary")
    private String        FirefoxDriverBinary    = "";

    @Comment(
            info = "Add a path to the Phantomjs Driver Binary, required if using phatomjs, system will try to pull it locally if not found")
    @Arg(dest = "PhantomJsDriverBinary")
    private String        PhantomJsDriverBinary  = "";

    @Comment(
            info = "Add a path to the Chrome Driver Binary, required if using phatomjs, system will try to pull it locally if not found")
    @Arg(dest = "ChromeDriverBinary")
    private String        ChromeDriverBinary     = "";

    private static String osName                 = null;

    private static String userDir                = null;

    @Comment(info = "TBD")
    @Arg(dest = "filterByTagFound")
    private String        filterByTagFound       = "";

    @Comment(info = "TBD")
    @Arg(dest = "filterByTagNotFound")
    private String        filterByTagNotFound    = "";

    @Comment(info = "TBD")
    @Arg(dest = "filterByNameStartsWith")
    private String        filterByNameStartsWith = "";

    @Comment(info = "TBD")
    @Arg(dest = "filterByTestType")
    private TestType      filterByTestType       = TestType.AUTOMATED;

    @Comment(
            info = "Provide a value for the base level where tests can be found in the project, if empty will not run tests in jar")
    @Arg(dest = "lookAtInternalTests")
    private String        lookAtInternalTests    = "org.testah";

    @Comment(info = "Provide a path where to look for external uncompiled Test Classes, "
            + "can be stored as .java or .groovy, can supply a directory or specific file")
    @Arg(dest = "lookAtExternalTests")
    private String        lookAtExternalTests    = "";

    @Comment(info = "SHould System keep track of all actions occuring during test run")
    @Arg(dest = "recordSteps")
    private boolean       recordSteps            = true;

    @Comment(
            info = "Should the System work as normal JUnit and stop a test case method on first fail, if false will continue to end then check for any fails")
    @Arg(dest = "throwExceptionOnFail")
    private boolean       throwExceptionOnFail   = true;

    @Comment(info = "Should the Runner create a xml result file")
    @Arg(dest = "useXunitFormatter")
    private boolean       useXunitFormatter      = true;

    @Comment(info = "Should the Runner create a html result file")
    @Arg(dest = "useHtmlFormatter")
    private boolean       useHtmlFormatter       = true;

    @Comment(info = "Should the Runner automatically open the Html Report in a browser")
    @Arg(dest = "autoOpenHtmlReport")
    private boolean       autoOpenHtmlReport     = true;

    @Comment(
            info = "Version Id is a value that can be used to tell what version the test is testing against. Can be passed runtime via -Dtestah.versionId=login-0.0.2")
    @Arg(dest = "versionId")
    private String        versionId              = "";

    @Comment(
            info = "Build Number is a value that can be used to tell what build the test is testing against. Can be passed runtime via -Dtestah.buildNumber=login-0.0.2")
    @Arg(dest = "buildNumber")
    private String        buildNumber            = "";

    @Comment(
            info = "Run Id is a value that can be used to group many differnt results together. Can be passed runtime via -Dtestah.runId=run23")
    @Arg(dest = "runId")
    private String        runId                  = "";

    @Comment(
            info = "Run Location is a way to tell where the test ran, can be local machine name, or Jenkins Job Url. Can be passed runtime via -Dtestah.runLocation=local")
    @Arg(dest = "runLocation")
    private String        runLocation            = "localhost";

    @Comment(
            info = "Run Type is a way to tell what type of run the test execution was involed in, smoke, regression, other. Can be passed runtime via -Dtestah.runType=smoke")
    @Arg(dest = "runType")
    private String        runType                = "";

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

    public String getPhantomJsDriverBinary() {
        return PhantomJsDriverBinary;
    }

    public void setPhantomJsDriverBinary(final String phantomJsDriverBinary) {
        PhantomJsDriverBinary = phantomJsDriverBinary;
    }

    public boolean isAutoOpenHtmlReport() {
        return autoOpenHtmlReport;
    }

    public void setAutoOpenHtmlReport(boolean autoOpenHtmlReport) {
        this.autoOpenHtmlReport = autoOpenHtmlReport;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getRunLocation() {
        return runLocation;
    }

    public void setRunLocation(String runLocation) {
        this.runLocation = runLocation;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }
}
