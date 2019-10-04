package org.testah;

import org.apache.logging.log4j.Logger;
import org.testah.client.dto.StepActionDto;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.framework.cli.Cli;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.StepActionHelper;
import org.testah.framework.dto.StepHelper;
import org.testah.framework.report.TestPlanReporter;
import org.testah.framework.report.VerboseAsserts;
import org.testah.util.Log;
import org.testah.util.TestahUtil;
import org.testah.util.StringMasking;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class TS - Test State Class.
 * This is the main entry point to the core testah functionality, it is designed to make it easy to write tests with
 * a limited amount of code.  The majority of objects this class holds are thread locals, so each thread will get its
 * own instance to avoid concurrent tests stepping on each other.
 *
 * <p>The class has opted for not using the traditional get prefix, so calls like TS.browser().blah(), instead
 * of TS.getBrowser().blah()
 *
 * <p>The objects do offer a setter, but this is primarily there to make it easy to test the test code and framework and
 * should never be used in general for an e2e.
 */
public class TS {

    private static final StepHelper stepHelper = new StepHelper();
    private static final StepActionHelper stepActionHelper = new StepActionHelper();

    /**
     * The _stateful data.
     * Allows a test to store data that helpers and other classes can use instead of always having to pass it.
     */
    private static ThreadLocal<HashMap<String, Object>> _statefulData = new ThreadLocal<HashMap<String, Object>>();
    /**
     * The _browser.
     * The wrapper used around selenium/webdriver, the entry point for all web/browser functionality.
     */
    private static ThreadLocal<AbstractBrowser<?>> _browser = new ThreadLocal<AbstractBrowser<?>>();
    /**
     * The _http.
     * The wrapper used around httpclient, the entry point for all rest/service functionality.
     */
    private static ThreadLocal<AbstractHttpWrapper> _http = new ThreadLocal<AbstractHttpWrapper>();
    private static TestPlanReporter _testPlanReporter;
    /**
     * The _asserts.
     * The main assert class, it auto keeps track of state and logs not only fails but also passes.
     */
    private static ThreadLocal<VerboseAsserts> _asserts = new ThreadLocal<VerboseAsserts>();
    /**
     * The _verify.
     * The main assert class but will not fail a test so you can use in an if to determine flow or logic, its the
     * same code as the asserts.
     */
    private static ThreadLocal<VerboseAsserts> _verify = new ThreadLocal<VerboseAsserts>();
    /**
     * The _testah util.
     * Common util with things like the mapper, time methods, etc.
     */
    private static TestahUtil _testahUtil;
    /**
     * The _params.
     * The runtime params from the testah.properties file.
     */
    private static Params _params;

    /**
     * Asserts.
     * The main assert class, it auto keeps track of state and logs not only fails but also passes.
     *
     * @return the verbose asserts
     */
    public static VerboseAsserts asserts() {
        if (null == _asserts.get()) {
            setAsserts(new VerboseAsserts());
        }
        return _asserts.get();
    }

    /**
     * Sets asserts.
     * Should only be used for unit testing the framework or test code itself
     *
     * @param verboseAsserts the verbose asserts
     * @return the asserts
     */
    public static VerboseAsserts setAsserts(final VerboseAsserts verboseAsserts) {
        _asserts.set(verboseAsserts);
        return _asserts.get();
    }

    /**
     * Verify.
     * The main assert class but will not fail a test so you can use in an if to determine flow or logic, its the
     * same code as the asserts.
     *
     * @return the verbose asserts
     */
    public static VerboseAsserts verify() {
        if (null == _verify.get()) {
            setVerify(new VerboseAsserts().onlyVerify());
        }
        return _verify.get();
    }

    /**
     * Sets verify.
     * Should only be used to test the test code
     *
     * @param verboseAsserts the verbose asserts
     * @return the verify
     */
    public static VerboseAsserts setVerify(final VerboseAsserts verboseAsserts) {
        _verify.set(verboseAsserts);
        return _verify.get();
    }

    /**
     * Reset stateful data.
     *
     * @return the hash map
     */
    public static HashMap<String, Object> resetStatefulData() {
        statefulData().clear();
        return statefulData();
    }

    /**
     * Stateful data.
     * Allows a test to store data that helpers and other classes can use instead of always having to pass it.
     *
     * @return the hash map
     */
    public static HashMap<String, Object> statefulData() {
        if (null == _statefulData.get()) {
            _statefulData.set(new HashMap<String, Object>());
        }
        return _statefulData.get();
    }

    /**
     * Util.
     * Common util with things like the mapper, time methods, etc.
     *
     * @return the testah util
     */
    public static TestahUtil util() {
        if (null == _testahUtil) {
            _testahUtil = new TestahUtil();
        }
        return _testahUtil;
    }

    /**
     * Params params - Deprecated.
     *
     * @param params the params
     * @return the params
     */
    @Deprecated
    public static Params params(final Params params) {
        _params = params;
        return _params;
    }

    /**
     * Params.
     * The runtime params from the testah.properties file.
     *
     * @return the params
     */
    public static Params params() {
        if (null == _params) {
            _params = new Cli().getArgumentParser(null).getOpt();
            Log.setLevel(_params.getLevel());
        }
        return _params;
    }

    /**
     * Sets the params.
     * Should only be used to test the test code.
     *
     * @param params the params
     * @return the params
     */
    public static Params setParams(final Params params) {
        _params = params;
        return TS._params;
    }

    /**
     * Browser.
     * The wrapper used around selenium/webdriver, the entry point for all web/browser functionality.
     *
     * @return the abstract browser
     */
    public static AbstractBrowser<?> browser() {
        if (null == _browser.get()) {
            setBrowser(AbstractBrowser.getDefaultBrowser());
        }
        return _browser.get();
    }

    /**
     * Sets the browser.
     * Should only be used to test the test code.
     *
     * @param browser the browser
     * @return the abstract browser
     */
    public static AbstractBrowser<?> setBrowser(final AbstractBrowser<?> browser) {
        final ThreadLocal<AbstractBrowser<?>> browserTemp = new ThreadLocal<>();
        browserTemp.set(browser);
        _browser = browserTemp;
        return _browser.get();
    }

    /**
     * Checks if is browser is currently initialized.
     *
     * @return true, if is browser
     */
    public static boolean isBrowser() {
        return (null != _browser && null != _browser.get());
    }

    /**
     * Http.
     * The wrapper used around httpclient, the entry point for all rest/service functionality.
     *
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper http() {
        if (null == _http.get()) {
            setHttp(new HttpWrapperV2());
        }
        return _http.get();
    }

    /**
     * Sets the http.
     * Should only be used to test the test code.
     *
     * @param http the http
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper setHttp(final AbstractHttpWrapper http) {
        ThreadLocal<AbstractHttpWrapper> tempHttp = new ThreadLocal<>();
        tempHttp.set(http);
        TS._http = tempHttp;
        return TS._http.get();
    }

    /**
     * Adds the step action.
     * Adds a step action into the running testcase on the active testplan for the thread
     *
     * @param stepActionDto the step action
     * @deprecated Use TS.step().action().add(stepActionDto) instead
     */
    @Deprecated
    public static void addStepAction(final StepActionDto stepActionDto) {
        step().action().add(stepActionDto);
        return;
    }

    public static StepHelper step() {
        return stepHelper;
    }

    /**
     * Gets mask values.
     * Used to help mask password and other sensitive strings from the logs/reports.
     *
     * @return the mask values
     */
    public static Map<String, String> getMaskValues() {
        return StringMasking.getInstance().getMap();
    }


    /**
     * Gets masked string.
     * Used to help mask password and other sensitive strings from the logs/reports.
     *
     * @return the masked string
     */
    public static String getMaskedValue(String plainString) {
        return StringMasking.getInstance().getValue(plainString);
    }

    /**
     * Add mask.
     * Used to help mask password and other sensitive strings from the logs/reports.
     *
     * @param valueToMask the value to mask
     */
    public static void addMask(final String valueToMask)
    {
        StringMasking.getInstance().add(valueToMask);
    }

    /**
     * Gets test plan reporter.
     *
     * @return the test plan reporter
     */
    public static TestPlanReporter getTestPlanReporter() {
        if (null == _testPlanReporter) {
            _testPlanReporter = new TestPlanReporter();
        }
        return _testPlanReporter;
    }

    /**
     * Sets test plan reporter.
     * Sets the test plan reporter for use.
     *
     * @param testPlanReporter the test plan reporter
     */
    public static void setTestPlanReporter(final TestPlanReporter testPlanReporter) {
        _testPlanReporter = testPlanReporter;
    }


    /**
     * Tear down.
     */
    public static void tearDown() {
        cleanUpThreadLocal(_browser);
        cleanUpThreadLocal(_statefulData);
        cleanUpThreadLocal(_http);
        cleanUpThreadLocal(_asserts);
        cleanUpThreadLocal(_verify);
    }

    /**
     * Cleans up thread locals that are no longer used.
     *
     * @param threadLocal the thread local
     */
    public static void cleanUpThreadLocal(final ThreadLocal<?> threadLocal) {
        try {
            if (null != threadLocal) {
                threadLocal.remove();
            }
        } catch (final Exception e) {
            log().warn("Trying to remove thread local", e);
        }
    }

    /**
     * Log.
     * A log4j instantiated object, to make it easier to log the write way.
     * Should never use System.out.println in an e2e test.
     *
     * @return the logger
     */
    public static Logger log() {
        return Log.getLog();
    }

}
