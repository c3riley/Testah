package org.testah.framework.cli;

import net.sourceforge.argparse4j.annotation.Arg;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.testah.TS;
import org.testah.client.enums.BrowserType;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.Comment;
import org.testah.framework.report.AbstractFormatter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * The Class Params.
 */
public class Params {

    /**
     * The os name.
     */
    private static String osName = null;

    /**
     * The user dir.
     */
    private static String userDir = null;
    @Comment(info = "Base Url For Jira ex: http://company.jira.com")
    @Arg(dest = "jiraUrl")
    private String jiraUrl = "";
    @Comment(info = "Inject Jira Remote Link for Associated Items")
    @Arg(dest = "useJiraRemoteLink")
    private boolean useJiraRemoteLink = false;
    @Comment(info = "UserName for access to jira, will be added to mask")
    @Arg(dest = "jiraUserName")
    private String jiraUserName = "";
    @Comment(info = "Pwd for access to jira, will be added to mask, should be supplied via job or system")
    @Arg(dest = "jiraPassword")
    private String jiraPassword = "";
    @Comment(info = "UserName for access to email, will be added to mask")
    @Arg(dest = "emailUserName")
    private String emailUserName = "";
    @Comment(info = "Pwd for access to email, will be added to mask, should be supplied via job or system")
    @Arg(dest = "emailPassword")
    private String emailPassword = "";
    @Comment(info = "Domain for access to email, will be added to mask, should be supplied via job or system")
    @Arg(dest = "emailDomain")
    private String emailDomain = "";
    @Comment(info = "If No TS.asserts were found, so status is null, show tests as ignored")
    @Arg(dest = "resultIgnoredIfNoAssertsFound")
    private boolean resultIgnoredIfNoAssertsFound = true;
    @Comment(info = "Url base to where the class can be read, can point to source control, or testcase mgmt system")
    @Arg(dest = "sourceUrl")
    private String sourceUrl = "";
    /**
     * The num concurrent threads.
     */
    @Comment(info = "Number of Concurrent Threads")
    @Arg(dest = "numConcurrentThreads")
    private int numConcurrentThreads = 1;
    @Comment(info = "Default to truncate request response in report and logging, to turn off set to 0")
    @Arg(dest = "defaultResponseTruncate")
    private int defaultResponseTruncate = 500;
    /**
     * The browser.
     */
    @Comment(info = "Browser value allowed: ")
    @Arg(dest = "browser")
    private BrowserType browser = BrowserType.CHROME;
    /**
     * The envir.
     */
    @Comment(info = "envir values allowed: ")
    @Arg(dest = "envir")
    private String envir = "dev";
    /**
     * The level.
     */
    @Comment(info = "Log level for log4j: TRACE, DEBUG, INFO, WARN, ERROR")
    @Arg(dest = "level")
    private Level level = Level.DEBUG;
    /**
     * The default wait time.
     */
    @Comment(
            info = "Default Wait time, determines the length of the loop, is not in seconds, works with the default pause time")
    @Arg(dest = "defaultWaitTime")
    private int defaultWaitTime = 10;
    /**
     * The default pause time.
     */
    @Comment(info = "Default Time to Pause when Called, value is in milliseconds.")
    @Arg(dest = "defaultPauseTime")
    private Long defaultPauseTime = 500L;
    /**
     * The look at internal tests.
     */
    @Comment(
            info = "Provide a value for the base level where tests can be found in the project, if empty will not run tests in jar")
    @Arg(dest = "lookAtInternalTests")
    private String lookAtInternalTests = "org.testah";
    /**
     * The look at external tests.
     */
    @Comment(info = "Provide a path where to look for external uncompiled Test Classes, " +
            "can be stored as .java or .groovy, can supply a directory or specific file")
    @Arg(dest = "lookAtExternalTests")
    private String lookAtExternalTests = "";
    /**
     * The record steps.
     */
    @Comment(info = "Should System keep track of all actions occurring during test run")
    @Arg(dest = "recordSteps")
    private boolean recordSteps = true;
    /**
     * The throw exception on fail.
     */
    @Comment(
            info = "Should the System work as normal JUnit and stop a test case method on first fail, " +
                    "if false will continue to end " +
                    "then check for any fails")
    @Arg(dest = "throwExceptionOnFail")
    private boolean throwExceptionOnFail = true;
    @Comment(
            info = "timezone to use for runtime data, if empty will use system default. " +
                    "If supplied should be string representation of timezone like America/New_York")
    @Arg(dest = "timezone")
    private String timezone = TimeZone.getDefault().getID();
    @Comment(
            info = "timeFormat to use for runtime data, if empty will use default. " +
                    "If supplied should be string representation of timezone like yyyy-MM-dd'T'HH:mm:ss.SSZ")
    @Arg(dest = "timeFormat")
    private String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSZ";
    /**
     * The web driver_user agent value.
     */
    @Comment(
            info = "[BAR1]Webdriver properties[BAR2]Override Browser UserAgent property, " +
                    "allows tests to act as mobile device, etc")
    @Arg(dest = "webDriver_userAgentValue")
    private String webDriverUserAgentValue = "";
    /**
     * The web driver_use remote driver.
     */
    @Comment(info = "Use A Remote Driver to run the Browser with, allows for Grid use")
    @Arg(dest = "webDriver_useRemoteDriver")
    private boolean webDriverUseRemoteDriver = false;
    /**
     * The web driver_default remote uri.
     */
    @Comment(
            info = "If Use Remote Driver is True, value will be used to connect to a Webdriver Grid, can be local or remote")
    @Arg(dest = "webDriver_defaultRemoteUri")
    private String webDriverDefaultRemoteUri = "http://localhost:4444/wd/hub";
    /**
     * The web driver_firefox driver binary.
     */
    @Comment(info = "Add path to the Firefox Binary (only needed if in a non default location")
    @Arg(dest = "webDriver_FirefoxDriverBinary")
    private String webDriverFirefoxDriverBinary = "";
    /**
     * The web driver_phantom js driver binary.
     */
    @Comment(
            info = "Add a path to the Phantomjs Driver Binary, required if using phatomjs, " +
                    "system will try to pull it locally if not found")
    @Arg(dest = "webDriver_PhantomJsDriverBinary")
    private String webDriverPhantomJsDriverBinary = "";
    /**
     * The web driver_chrome driver binary.
     */
    @Comment(
            info = "Add a path to the Chrome Driver Binary, required if using phatomjs, " +
                    "system will try to pull it locally if not found")
    @Arg(dest = "webDriver_ChromeDriverBinary")
    private String webDriverChromeDriverBinary = "";
    @Comment(info = "Default window Width to open browser window to. If empty then browser will be maximized")
    @Arg(dest = "windowWidth")
    private int windowWidth = 1366;
    @Comment(info = "Default window Height to open browser window to. If empty then browser will be maximized")
    @Arg(dest = "windowHeight")
    private int windowHeight = 1000;
    /**
     * The output.
     */
    @Comment(
            info = "Should rest response get written to file and linked into the html report " +
                    "which normally truncates the responses.")
    @Arg(dest = "writeResponseToFile")
    private boolean writeResponseToFile = false;
    /**
     * The output.
     */
    @Comment(
            info = "[BAR1]Reporting Properties[BAR2]Folder to write output to, if empty will be {user dir}/testahOutput")
    @Arg(dest = "output")
    private String output = "";

    /**
     * Mask output using TS.sanitizeString(...).
     */
    @Comment(
            info = "If set to true output from asserts will be sanitized using the masking map."
    )
    @Arg(dest = "isMaskOutput")
    private boolean isMaskOutput = true;
    /**
     * The use xunit formatter.
     */
    @Comment(info = "Should the Runner create a xml result file")
    @Arg(dest = "useXunitFormatter")
    private boolean useXunitFormatter = true;
    /**
     * The use json formatter.
     */
    @Comment(info = "Should the Runner create a json result file")
    @Arg(dest = "useXunitFormatter")
    private boolean useJsonFormatter = true;
    /**
     * The use html formatter.
     */
    @Comment(info = "Should the Runner create a html result file")
    @Arg(dest = "useHtmlFormatter")
    private boolean useHtmlFormatter = true;

    @Comment(info = "Html Template the Runner should use")
    @Arg(dest = "reportHtmlTemplate")
    private String reportHtmlTemplate = AbstractFormatter.DEFAULT_PACKAGE + "htmlReportV2.vm";

    /**
     * The use meta formatter.
     */
    @Comment(info = "Should the Runner create a meta text file")
    @Arg(dest = "useMetaFormatter")
    private boolean useMetaFormatter = true;
    /**
     * The auto open html report.
     */
    @Comment(info = "Should the Runner automatically open the Html Report in a browser")
    @Arg(dest = "autoOpenHtmlReport")
    private boolean autoOpenHtmlReport = true;
    /**
     * The unique Report Name.
     */
    @Comment(info = "Should the reports use unique names.  If the test report runner is used, it is on by default, " +
            "else is false and will cause reports to get overridden each run.")
    @Arg(dest = "uniqueReportName")
    private boolean uniqueReportName = false;
    /**
     * The send json test data to service.
     */
    @Comment(
            info = "Should the Runner Post info to a service in Json Format. If Uri is supplied it will attempt to send " +
                    "the json info as a List of 1 to many TestPlans")
    @Arg(dest = "sendJsonTestDataToService")
    private String sendJsonTestDataToService = "";
    /**
     * The add results.
     */
    @Comment(info = "True will send a flag saying to ignore the results.")
    @Arg(dest = "addResults")
    private boolean addResults = true;
    /**
     * The update metadata.
     */
    @Comment(info = "True will send a flag saying to update the repository with metadata.")
    @Arg(dest = "updateMetadata")
    private boolean updateMetadata = true;
    /**
     * The run info_version id.
     */
    @Comment(
            info = "[BAR1]Run Info Properties[BAR2]Version Id is a value that can be used to tell what version the test is " +
                    "testing against. Can be passed runtime via -Dtestah.versionId=login-0.0.2")
    @Arg(dest = "runInfo_versionId")
    private String runInfoVersionId = "";
    /**
     * The run info_build number.
     */
    @Comment(
            info = "Build Number is a value that can be used to tell what build the test is testing against. " +
                    "Can be passed runtime via -Dtestah.buildNumber=login-0.0.2")
    @Arg(dest = "runInfo_buildNumber")
    private String runInfoBuildNumber = "";
    /**
     * The run info_run id.
     */
    @Comment(
            info = "Run Id is a value that can be used to group many different results together. " +
                    "Can be passed runtime via -Dtestah.runId=run23")
    @Arg(dest = "runInfo_runId")
    private String runInfoRunId = "";
    /**
     * The run location.
     */
    @Comment(
            info = "Run Location is a way to tell where the test ran, can be local machine name, or Jenkins Job Url. " +
                    "Can be passed runtime via -Dtestah.runLocation=local")
    @Arg(dest = "runInfo_runLocation")
    private String runLocation = "localhost";
    /**
     * The run type.
     */
    @Comment(
            info = "Run Type is a way to tell what type of run the test execution was involved in, smoke, regression, other. " +
                    "Can be passed runtime via -Dtestah.runType=smoke")
    @Arg(dest = "runInfo_runType")
    private String runType = "";
    /**
     * The usefilter schema.
     */
    @Comment(
            info = "[BAR1]Filter Properties[BAR2]Filter Schema To Use. Leave empty to not apply filters, to use default filter, " +
                    "set value to DEFAULT.  You can also create your own, like REG, or SMOKE")
    @Arg(dest = "usefilterSchema")
    private String usefilterSchema = "DEFAULT";
    /**
     * The filter by platform.
     */
    @Comment(
            info = "Filter using the Platform metadata field. Can be a comma separated list of values that a test must match. " +
                    "Value can use the (~) for [Must Not Have], any match will be excluded.")
    @Arg(dest = "filterByPlatform")
    private String filterByPlatform = "";
    /**
     * The filter by device.
     */
    @Comment(
            info = "Filter using the Device metadata field. Can be a comma separated list of values that a test must match. " +
                    "Value can use the (~) for [Must Not Have], any match will be excluded.")
    @Arg(dest = "filterByDevice")
    private String filterByDevice = "";
    /**
     * The filter by component.
     */
    @Comment(
            info = "Filter using the Component metadata field. Can be a comma separated list of values that a test must match. " +
                    "Value can use the (~) for [Must Not Have], any match will be excluded.")
    @Arg(dest = "filterByComponent")
    private String filterByComponent = "";
    /**
     * The filter by run type.
     */
    @Comment(
            info = "Filter using the RunType metadata field. Can be a comma separated list of values that a test must match. " +
                    "Value can use the (~) for [Must Not Have], any match will be excluded.")
    @Arg(dest = "filterByRunType")
    private String filterByRunType = "";
    /**
     * The filter ignore known problem.
     */
    @Comment(
            info = "Filter Using The @KnownProblem Annotation.  True means testplan/testcase must not be a known problem. " +
                    "False means must be a known problem. Empty means do not use filter.")
    @Arg(dest = "filterIgnoreKnownProblem")
    private String filterIgnoreKnownProblem = "";
    /**
     * The filter by tag.
     */
    @Comment(
            info = "Filter using the Tag metadata field. Can be a comma separated list of values that a test must match. " +
                    "Value can use the (~) for [Must Not Have], any match will be excluded.")
    @Arg(dest = "filterByTag")
    private String filterByTag = "";
    /**
     * The filter by test plan name starts with.
     */
    @Comment(info = "Filter by TestPlan Name, TestPlan must start with prefix. Can supply a comma separated list.")
    @Arg(dest = "filterByTestPlanNameStartsWith")
    private String filterByTestPlanNameStartsWith = "";
    /**
     * The filter by id.
     */
    @Comment(info = "Filter by TestPlan Id, TestPlan must match supplied Uuid. Can supply a comma separated list.")
    @Arg(dest = "filterById")
    private String filterById = "";
    /**
     * The filter by test type.
     */
    @Comment(info = "Filter by TestType.  Can supply a comma separated List.")
    @Arg(dest = "filterByTestType")
    private TestType filterByTestType = TestType.AUTOMATED;
    private HashMap<String, String> other = new HashMap<>();

    /**
     * Checks if is mac.
     *
     * @return true, if is mac
     */
    public static boolean isMac() {
        final String os = getOsName();
        if (os.contains("darwin") || os.contains("mac")) {
            return true;
        }
        return false;
    }

    /**
     * Gets the os name.
     *
     * @return the os name
     */
    public static String getOsName() {
        if (null == osName) {
            osName = System.getProperty("os.name").toLowerCase();
        }
        return osName;
    }

    /**
     * Sets the os name.
     *
     * @param osName the new os name
     */
    public static void setOsName(final String osName) {
        Params.osName = osName;
    }

    /**
     * Checks if is unix.
     *
     * @return true, if is unix
     */
    public static boolean isUnix() {
        if (getOsName().contains("win")) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is windows.
     *
     * @return true, if is windows
     */
    public static boolean isWindows() {
        if (getOsName().contains("win")) {
            return true;
        }
        return false;
    }

    /**
     * Gets the user dir.
     *
     * @return the user dir
     */
    public static String getUserDir() {
        if (null == userDir) {
            userDir = System.getProperty("user.dir");
        }
        return userDir;
    }

    /**
     * Sets the user dir.
     *
     * @param userDir the new user dir
     */
    public static void setUserDir(final String userDir) {
        Params.userDir = userDir;
    }

    /**
     * Adds the user dir.
     *
     * @param path the path
     * @return the string
     */
    public static String addUserDir(final String path) {
        return (getUserDir() + File.separator + path).replace(File.separator + File.separator, File.separator);
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * Sets the output.
     *
     * @param output the new output
     * @return the params
     */
    public Params setOutput(final String output) {
        if (null != output) {
            File outputFile = new File(output.replace("{user dir}", Params.getUserDir()));
            if (!outputFile.exists()) {
                TS.log().trace("getOutput mkdirs: " + outputFile.mkdirs());
            }
            this.output = outputFile.getAbsolutePath();
        } else {
            this.output = null;
        }
        return this;
    }

    /**
     * Gets the num concurrent threads.
     *
     * @return the num concurrent threads
     */
    public int getNumConcurrentThreads() {
        return numConcurrentThreads;
    }

    /**
     * Sets the num concurrent threads.
     *
     * @param numConcurrentThreads the new num concurrent threads
     * @return the params
     */
    public Params setNumConcurrentThreads(final int numConcurrentThreads) {
        this.numConcurrentThreads = numConcurrentThreads;
        return this;
    }

    /**
     * Gets the browser.
     *
     * @return the browser
     */
    public BrowserType getBrowser() {
        return browser;
    }

    /**
     * Sets the browser.
     *
     * @param browser the new browser
     * @return the params
     */
    public Params setBrowser(final BrowserType browser) {
        this.browser = browser;
        return this;
    }

    /**
     * Gets the envir.
     *
     * @return the envir
     */
    public String getEnvir() {
        return envir;
    }

    /**
     * Sets the envir.
     *
     * @param envir the new envir
     * @return the params
     */
    public Params setEnvir(final String envir) {
        this.envir = envir;
        return this;
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the level.
     *
     * @param level the new level
     * @return the params
     */
    public Params setLevel(final Level level) {
        this.level = level;
        return this;
    }

    /**
     * Gets the default wait time.
     *
     * @return the default wait time
     */
    public int getDefaultWaitTime() {
        return defaultWaitTime;
    }

    /**
     * Sets the default wait time.
     *
     * @param defaultWaitTime the new default wait time
     * @return the params
     */
    public Params setDefaultWaitTime(final int defaultWaitTime) {
        this.defaultWaitTime = defaultWaitTime;
        return this;
    }

    /**
     * Gets the default pause time.
     *
     * @return the default pause time
     */
    public Long getDefaultPauseTime() {
        return defaultPauseTime;
    }

    /**
     * Sets the default pause time.
     *
     * @param defaultPauseTime the new default pause time
     * @return the params
     */
    public Params setDefaultPauseTime(final Long defaultPauseTime) {
        this.defaultPauseTime = defaultPauseTime;
        return this;
    }

    /**
     * Gets the look at internal tests.
     *
     * @return the look at internal tests
     */
    public String getLookAtInternalTests() {
        return lookAtInternalTests;
    }

    /**
     * Sets the look at internal tests.
     *
     * @param lookAtInternalTests the new look at internal tests
     * @return the params
     */
    public Params setLookAtInternalTests(final String lookAtInternalTests) {
        this.lookAtInternalTests = lookAtInternalTests;
        return this;
    }

    /**
     * Gets the look at external tests.
     *
     * @return the look at external tests
     */
    public String getLookAtExternalTests() {
        return lookAtExternalTests;
    }

    /**
     * Sets the look at external tests.
     *
     * @param lookAtExternalTests the new look at external tests
     * @return the params
     */
    public Params setLookAtExternalTests(final String lookAtExternalTests) {
        this.lookAtExternalTests = lookAtExternalTests;
        return this;
    }

    /**
     * Checks if is record steps.
     *
     * @return true, if is record steps
     */
    public boolean isRecordSteps() {
        return recordSteps;
    }

    /**
     * Sets the record steps.
     *
     * @param recordSteps the new record steps
     * @return the params
     */
    public Params setRecordSteps(final boolean recordSteps) {
        this.recordSteps = recordSteps;
        return this;
    }

    /**
     * Checks if is throw exception on fail.
     *
     * @return true, if is throw exception on fail
     */
    public boolean isThrowExceptionOnFail() {
        return throwExceptionOnFail;
    }

    /**
     * Sets the throw exception on fail.
     *
     * @param throwExceptionOnFail the new throw exception on fail
     * @return the params
     */
    public Params setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
        this.throwExceptionOnFail = throwExceptionOnFail;
        return this;
    }

    /**
     * Checks if is use xunit formatter.
     *
     * @return true, if is use xunit formatter
     */
    public boolean isUseXunitFormatter() {
        return useXunitFormatter;
    }

    /**
     * Sets the use xunit formatter.
     *
     * @param useXunitFormatter the new use xunit formatter
     * @return the params
     */
    public Params setUseXunitFormatter(final boolean useXunitFormatter) {
        this.useXunitFormatter = useXunitFormatter;
        return this;
    }

    /**
     * Checks if is use html formatter.
     *
     * @return true, if is use html formatter
     */
    public boolean isUseHtmlFormatter() {
        return useHtmlFormatter;
    }

    /**
     * Sets the use html formatter.
     *
     * @param useHtmlFormatter the new use html formatter
     * @return the params
     */
    public Params setUseHtmlFormatter(final boolean useHtmlFormatter) {
        this.useHtmlFormatter = useHtmlFormatter;
        return this;
    }

    /**
     * Checks if is auto open html report.
     *
     * @return true, if is auto open html report
     */
    public boolean isAutoOpenHtmlReport() {
        return autoOpenHtmlReport;
    }

    /**
     * Sets the auto open html report.
     *
     * @param autoOpenHtmlReport the new auto open html report
     * @return the params
     */
    public Params setAutoOpenHtmlReport(final boolean autoOpenHtmlReport) {
        this.autoOpenHtmlReport = autoOpenHtmlReport;
        return this;
    }

    /**
     * Gets the run location.
     *
     * @return the run location
     */
    public String getRunLocation() {
        return runLocation;
    }

    /**
     * Sets the run location.
     *
     * @param runLocation the new run location
     * @return the params
     */
    public Params setRunLocation(final String runLocation) {
        this.runLocation = runLocation;
        return this;
    }

    /**
     * Gets the run type.
     *
     * @return the run type
     */
    public String getRunType() {
        return runType;
    }

    /**
     * Sets the run type.
     *
     * @param runType the new run type
     * @return the params
     */
    public Params setRunType(final String runType) {
        this.runType = runType;
        return this;
    }

    /**
     * Gets the usefilter schema.
     *
     * @return the usefilter schema
     */
    public String getUsefilterSchema() {
        return usefilterSchema;
    }

    /**
     * Sets the usefilter schema.
     *
     * @param usefilterSchema the new usefilter schema
     * @return the params
     */
    public Params setUsefilterSchema(final String usefilterSchema) {
        this.usefilterSchema = usefilterSchema;
        return this;
    }

    /**
     * Gets the filter by platform.
     *
     * @return the filter by platform
     */
    public String getFilterByPlatform() {
        return filterByPlatform;
    }

    /**
     * Sets the filter by platform.
     *
     * @param filterByPlatform the new filter by platform
     * @return the params
     */
    public Params setFilterByPlatform(final String filterByPlatform) {
        this.filterByPlatform = filterByPlatform;
        return this;
    }

    /**
     * Gets the filter by device.
     *
     * @return the filter by device
     */
    public String getFilterByDevice() {
        return filterByDevice;
    }

    /**
     * Sets the filter by device.
     *
     * @param filterByDevice the new filter by device
     * @return the params
     */
    public Params setFilterByDevice(final String filterByDevice) {
        this.filterByDevice = filterByDevice;
        return this;
    }

    /**
     * Gets the filter by component.
     *
     * @return the filter by component
     */
    public String getFilterByComponent() {
        return filterByComponent;
    }

    /**
     * Sets the filter by component.
     *
     * @param filterByComponent the new filter by component
     * @return the params
     */
    public Params setFilterByComponent(final String filterByComponent) {
        this.filterByComponent = filterByComponent;
        return this;
    }

    /**
     * Gets the filter by run type.
     *
     * @return the filter by run type
     */
    public String getFilterByRunType() {
        return filterByRunType;
    }

    /**
     * Sets the filter by run type.
     *
     * @param filterByRunType the new filter by run type
     * @return the params
     */
    public Params setFilterByRunType(final String filterByRunType) {
        this.filterByRunType = filterByRunType;
        return this;
    }

    /**
     * Gets the filter by tag.
     *
     * @return the filter by tag
     */
    public String getFilterByTag() {
        return filterByTag;
    }

    /**
     * Sets the filter by tag.
     *
     * @param filterByTag the new filter by tag
     * @return the params
     */
    public Params setFilterByTag(final String filterByTag) {
        this.filterByTag = filterByTag;
        return this;
    }

    /**
     * Gets the filter by test plan name starts with.
     *
     * @return the filter by test plan name starts with
     */
    public String getFilterByTestPlanNameStartsWith() {
        return filterByTestPlanNameStartsWith;
    }

    /**
     * Sets the filter by test plan name starts with.
     *
     * @param filterByTestPlanNameStartsWith the new filter by test plan name starts with
     * @return the params
     */
    public Params setFilterByTestPlanNameStartsWith(final String filterByTestPlanNameStartsWith) {
        this.filterByTestPlanNameStartsWith = filterByTestPlanNameStartsWith;
        return this;
    }

    /**
     * Gets the filter by id.
     *
     * @return the filter by id
     */
    public String getFilterById() {
        return filterById;
    }

    /**
     * Sets the filter by id.
     *
     * @param filterById the new filter by id
     * @return the params
     */
    public Params setFilterById(final String filterById) {
        this.filterById = filterById;
        return this;
    }

    /**
     * Gets the filter by test type.
     *
     * @return the filter by test type
     */
    public TestType getFilterByTestType() {
        return filterByTestType;
    }

    /**
     * Sets the filter by test type.
     *
     * @param filterByTestType the new filter by test type
     * @return the params
     */
    public Params setFilterByTestType(final TestType filterByTestType) {
        this.filterByTestType = filterByTestType;
        return this;
    }

    /**
     * Checks if is web driver_use remote driver.
     *
     * @return true, if is web driver_use remote driver
     */
    public boolean isWebDriver_useRemoteDriver() {
        return webDriverUseRemoteDriver;
    }

    /**
     * Sets the web driver_use remote driver.
     *
     * @param webDriverUseRemoteDriver the new web driver_use remote driver
     * @return the params
     */
    public Params setWebDriver_useRemoteDriver(final boolean webDriverUseRemoteDriver) {
        this.webDriverUseRemoteDriver = webDriverUseRemoteDriver;
        return this;
    }

    /**
     * Gets the web driver_default remote uri.
     *
     * @return the web driver_default remote uri
     */
    public String getWebDriver_defaultRemoteUri() {
        return webDriverDefaultRemoteUri;
    }

    /**
     * Sets the web driver_default remote uri.
     *
     * @param webDriverDefaultRemoteUri the new web driver_default remote uri
     * @return the params
     */
    public Params setWebDriver_defaultRemoteUri(final String webDriverDefaultRemoteUri) {
        this.webDriverDefaultRemoteUri = webDriverDefaultRemoteUri;
        return this;
    }

    /**
     * Gets the web driver_firefox driver binary.
     *
     * @return the web driver_firefox driver binary
     */
    public String getWebDriver_firefoxDriverBinary() {
        return webDriverFirefoxDriverBinary;
    }

    /**
     * Sets the web driver_firefox driver binary.
     *
     * @param webDriverFirefoxDriverBinary the new web driver_firefox driver binary
     * @return the params
     */
    public Params setWebDriver_firefoxDriverBinary(final String webDriverFirefoxDriverBinary) {
        this.webDriverFirefoxDriverBinary = webDriverFirefoxDriverBinary;
        return this;
    }

    /**
     * Gets the web driver_phantom js driver binary.
     *
     * @return the web driver_phantom js driver binary
     */
    public String getWebDriver_phantomJsDriverBinary() {
        return webDriverPhantomJsDriverBinary;
    }

    /**
     * Sets the web driver_phantom js driver binary.
     *
     * @param webDriverPhantomJsDriverBinary the new web driver_phantom js driver binary
     * @return the params
     */
    public Params setWebDriver_phantomJsDriverBinary(final String webDriverPhantomJsDriverBinary) {
        this.webDriverPhantomJsDriverBinary = webDriverPhantomJsDriverBinary;
        return this;
    }

    /**
     * Gets the web driver_chrome driver binary.
     *
     * @return the web driver_chrome driver binary
     */
    public String getWebDriver_chromeDriverBinary() {
        return webDriverChromeDriverBinary;
    }

    /**
     * Sets the web driver_chrome driver binary.
     *
     * @param webDriverChromeDriverBinary the new web driver_chrome driver binary
     * @return the params
     */
    public Params setWebDriver_chromeDriverBinary(final String webDriverChromeDriverBinary) {
        this.webDriverChromeDriverBinary = webDriverChromeDriverBinary;
        return this;
    }

    /**
     * Gets the run info_version id.
     *
     * @return the run info_version id
     */
    public String getRunInfo_versionId() {
        return runInfoVersionId;
    }

    /**
     * Sets the run info_version id.
     *
     * @param runInfoVersionId the new run info_version id
     * @return the params
     */
    public Params setRunInfo_versionId(final String runInfoVersionId) {
        this.runInfoVersionId = runInfoVersionId;
        return this;
    }

    /**
     * Gets the run info_build number.
     *
     * @return the run info_build number
     */
    public String getRunInfo_buildNumber() {
        return runInfoBuildNumber;
    }

    /**
     * Sets the run info_build number.
     *
     * @param runInfoBuildNumber the new run info_build number
     * @return the params
     */
    public Params setRunInfo_buildNumber(final String runInfoBuildNumber) {
        this.runInfoBuildNumber = runInfoBuildNumber;
        return this;
    }

    /**
     * Gets the run info_run id.
     *
     * @return the run info_run id
     */
    public String getRunInfo_runId() {
        return runInfoRunId;
    }

    /**
     * Sets the run info_run id.
     *
     * @param runInfoRunId the new run info_run id
     * @return the params
     */
    public Params setRunInfo_runId(final String runInfoRunId) {
        this.runInfoRunId = runInfoRunId;
        return this;
    }

    /**
     * Create output directories.
     *
     * @return the params
     */
    public Params mkOutput() {

        if (null == output || output.length() == 0) {
            output = addUserDir("testahOutput");
            TS.log().trace("getOutput mkdirs: " + new File(output).mkdirs());
        } else {
            File outputFile = new File(output.replace("{user dir}", Params.getUserDir()));
            if (!outputFile.exists()) {
                TS.log().trace("getOutput mkdirs: " + outputFile.mkdirs());
            }
            this.output = outputFile.getAbsolutePath();
        }
        return this;
    }

    /**
     * Gets the web driver_user agent value.
     *
     * @return the web driver_user agent value
     */
    public String getWebDriver_userAgentValue() {
        return webDriverUserAgentValue;
    }

    /**
     * Sets the web driver_user agent value.
     *
     * @param webDriverUserAgentValue the new web driver_user agent value
     * @return the params
     */
    public Params setWebDriver_userAgentValue(final String webDriverUserAgentValue) {
        this.webDriverUserAgentValue = webDriverUserAgentValue;
        return this;
    }

    /**
     * Gets the filter ignore known problem.
     *
     * @return the filter ignore known problem
     */
    public String getFilterIgnoreKnownProblem() {
        return filterIgnoreKnownProblem;
    }

    /**
     * Sets the filter ignore known problem.
     *
     * @param filterIgnoreKnownProblem the new filter ignore known problem
     * @return the params
     */
    public Params setFilterIgnoreKnownProblem(final String filterIgnoreKnownProblem) {
        this.filterIgnoreKnownProblem = filterIgnoreKnownProblem;
        return this;
    }

    /**
     * Gets the send json test data to service.
     *
     * @return the send json test data to service
     */
    public String getSendJsonTestDataToService() {
        return sendJsonTestDataToService;
    }

    /**
     * Sets the send json test data to service.
     *
     * @param sendJsonTestDataToService the new send json test data to service
     * @return the params
     */
    public Params setSendJsonTestDataToService(final String sendJsonTestDataToService) {
        this.sendJsonTestDataToService = sendJsonTestDataToService;
        return this;
    }

    /**
     * Checks if is adds the results.
     *
     * @return true, if is adds the results
     */
    public boolean isAddResults() {
        return addResults;
    }

    /**
     * Sets the adds the results.
     *
     * @param addResults the new adds the results
     * @return the params
     */
    public Params setAddResults(final boolean addResults) {
        this.addResults = addResults;
        return this;
    }

    /**
     * Checks if is update metadata.
     *
     * @return true, if is update metadata
     */
    public boolean isUpdateMetadata() {
        return updateMetadata;
    }

    /**
     * Sets the update metadata.
     *
     * @param updateMetadata the new update metadata
     * @return the params
     */
    public Params setUpdateMetadata(final boolean updateMetadata) {
        this.updateMetadata = updateMetadata;
        return this;
    }

    /**
     * Checks if is use meta formatter.
     *
     * @return true, if is use meta formatter
     */
    public boolean isUseMetaFormatter() {
        return useMetaFormatter;
    }

    /**
     * Sets the use meta formatter.
     *
     * @param useMetaFormatter the use meta formatter
     * @return the params
     */
    public Params setUseMetaFormatter(final boolean useMetaFormatter) {
        this.useMetaFormatter = useMetaFormatter;
        return this;
    }

    /**
     * Gets the computer name.
     *
     * @return the computer name
     */
    public String getComputerName() {
        final Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        } else {
            return "NA";
        }
    }

    /**
     * Gets jira url.
     *
     * @return the jira url
     */
    public String getJiraUrl() {
        return jiraUrl;
    }

    /**
     * Sets jira url.
     *
     * @param jiraUrl the jira url
     * @return the jira url
     */
    public Params setJiraUrl(final String jiraUrl) {
        this.jiraUrl = jiraUrl;
        return this;
    }

    /**
     * Is use jira remote link boolean.
     *
     * @return the boolean
     */
    public boolean isUseJiraRemoteLink() {
        return useJiraRemoteLink;
    }

    /**
     * Sets use jira remote link.
     *
     * @param useJiraRemoteLink the use jira remote link
     * @return the use jira remote link
     */
    public Params setUseJiraRemoteLink(final boolean useJiraRemoteLink) {
        this.useJiraRemoteLink = useJiraRemoteLink;
        return this;
    }

    /**
     * Gets jira user name.
     *
     * @return the jira user name
     */
    public String getJiraUserName() {
        return jiraUserName;
    }

    /**
     * Sets jira user name.
     *
     * @param jiraUserName the jira user name
     * @return the jira user name
     */
    public Params setJiraUserName(final String jiraUserName) {
        TS.addMask(jiraUserName);
        this.jiraUserName = jiraUserName;
        return this;
    }

    /**
     * Gets jira password.
     *
     * @return the jira password
     */
    public String getJiraPassword() {
        return jiraPassword;
    }

    /**
     * Sets jira password.
     *
     * @param jiraPassword the jira password
     * @return the jira password
     */
    public Params setJiraPassword(final String jiraPassword) {
        TS.addMask(jiraPassword);
        this.jiraPassword = jiraPassword;
        return this;
    }

    /**
     * Gets email user name.
     *
     * @return the email user name
     */
    public String getEmailUserName() {
        return emailUserName;
    }

    /**
     * Sets email user name.
     *
     * @param emailUserName the email user name
     * @return the email user name
     */
    public Params setEmailUserName(final String emailUserName) {
        this.emailUserName = emailUserName;
        return this;
    }

    /**
     * Gets email password.
     *
     * @return the email password
     */
    public String getEmailPassword() {
        return emailPassword;
    }

    /**
     * Sets email password.
     *
     * @param emailPassword the email password
     * @return the email password
     */
    public Params setEmailPassword(final String emailPassword) {
        TS.addMask(emailPassword);
        this.emailPassword = emailPassword;
        return this;
    }

    /**
     * Gets email domain.
     *
     * @return the email domain
     */
    public String getEmailDomain() {
        return emailDomain;
    }

    /**
     * Sets email domain.
     *
     * @param emailDomain the email domain
     * @return the email domain
     */
    public Params setEmailDomain(final String emailDomain) {
        this.emailDomain = emailDomain;
        return this;
    }

    /**
     * Is result ignored if no asserts found boolean.
     *
     * @return the boolean
     */
    public boolean isResultIgnoredIfNoAssertsFound() {
        return resultIgnoredIfNoAssertsFound;
    }

    /**
     * Sets result ignored if no asserts found.
     *
     * @param resultIgnoredIfNoAssertsFound the result ignored if no asserts found
     * @return the result ignored if no asserts found
     */
    public Params setResultIgnoredIfNoAssertsFound(final boolean resultIgnoredIfNoAssertsFound) {
        this.resultIgnoredIfNoAssertsFound = resultIgnoredIfNoAssertsFound;
        return this;
    }

    /**
     * Gets source url.
     *
     * @return the source url
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * Sets source url.
     *
     * @param sourceUrl the source url
     * @return the source url
     */
    public Params setSourceUrl(final String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    /**
     * Gets value.
     *
     * @param key the key
     * @return the value
     */
    public String getValue(final String key) {
        return getValue(key, getOther().get(key));
    }

    /**
     * Get the value of the specified environment parameter. Return the default value if not found.
     *
     * @param key          parameter name
     * @param defaultValue default parameter name
     * @return parameter value
     */
    public String getValue(final String key, final String defaultValue) {
        String rtnValue = defaultValue;
        if (!StringUtils.isEmpty(key)) {
            rtnValue = System.getProperty(key, System.getenv(key));
            if (null == rtnValue) {
                rtnValue = defaultValue;
            }
        }
        return rtnValue;
    }

    /**
     * Gets other.
     *
     * @return the other
     */
    public HashMap<String, String> getOther() {
        return other;
    }

    /**
     * Sets other.
     *
     * @param other the other
     * @return the other
     */
    public Params setOther(final HashMap<String, String> other) {
        this.other = other;
        return this;
    }

    /**
     * Is use json formatter boolean.
     *
     * @return the boolean
     */
    public boolean isUseJsonFormatter() {
        return useJsonFormatter;
    }

    /**
     * Sets use json formatter.
     *
     * @param useJsonFormatter the use json formatter
     * @return the use json formatter
     */
    public Params setUseJsonFormatter(final boolean useJsonFormatter) {
        this.useJsonFormatter = useJsonFormatter;
        return this;
    }

    /**
     * Gets window width.
     *
     * @return the window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Sets window width.
     *
     * @param windowWidth the window width
     * @return the window width
     */
    public Params setWindowWidth(final int windowWidth) {
        this.windowWidth = windowWidth;
        return this;
    }

    /**
     * Gets window height.
     *
     * @return the window height
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Sets window height.
     *
     * @param windowHeight the window height
     * @return the window height
     */
    public Params setWindowHeight(final int windowHeight) {
        this.windowHeight = windowHeight;
        return this;
    }

    /**
     * Gets timezone.
     *
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets timezone.
     *
     * @param timezone the timezone
     * @return the timezone
     */
    public Params setTimezone(final String timezone) {
        this.timezone = timezone;
        return this;
    }

    /**
     * Gets time format.
     *
     * @return the time format
     */
    public String getTimeFormat() {
        return timeFormat;
    }

    /**
     * Sets time format.
     *
     * @param timeFormat the time format
     * @return the time format
     */
    public Params setTimeFormat(final String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    /**
     * Is unique report name boolean.
     *
     * @return the boolean
     */
    public boolean isUniqueReportName() {
        return uniqueReportName;
    }

    /**
     * Sets unique report name.
     *
     * @param uniqueReportName the unique report name
     * @return the unique report name
     */
    public Params setUniqueReportName(final boolean uniqueReportName) {
        this.uniqueReportName = uniqueReportName;
        return this;
    }

    /**
     * Gets default response truncate.
     *
     * @return the default response truncate
     */
    public int getDefaultResponseTruncate() {
        return defaultResponseTruncate;
    }

    /**
     * Sets default response truncate.
     *
     * @param defaultResponseTruncate the default response truncate
     * @return the default response truncate
     */
    public Params setDefaultResponseTruncate(int defaultResponseTruncate) {
        this.defaultResponseTruncate = defaultResponseTruncate;
        return this;
    }

    public boolean getWriteResponseToFile() {
        return writeResponseToFile;
    }

    public void setWriteResponseToFile(boolean writeResponseToFile) {
        this.writeResponseToFile = writeResponseToFile;
    }

    public boolean isMaskOutput()
    {
        return isMaskOutput;
    }

    public Params setMaskOutput(boolean maskOutput)
    {
        isMaskOutput = maskOutput;
        return this;
    }

    public String getReportHtmlTemplate() {
        return reportHtmlTemplate;
    }

    public Params setReportHtmlTemplate(final String reportHtmlTemplate) {
        this.reportHtmlTemplate = reportHtmlTemplate;
        return this;
    }
}
