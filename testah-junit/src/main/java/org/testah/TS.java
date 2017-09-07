package org.testah;

import org.apache.logging.log4j.Logger;
import org.testah.client.dto.StepActionDto;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.framework.cli.Cli;
import org.testah.framework.cli.Params;
import org.testah.framework.report.TestPlanReporter;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.util.Log;
import org.testah.util.TestahUtil;

import java.util.HashMap;

/**
 * The Class TS.
 */
public class TS {

    /**
     * The _stateful data.
     */
    private static ThreadLocal<HashMap<String, Object>> _statefulData;

    /**
     * The _browser.
     */
    private static ThreadLocal<AbstractBrowser<?>> _browser;

    /**
     * The _http.
     */
    private static ThreadLocal<AbstractHttpWrapper> _http;

    private static TestPlanReporter _testPlanReporter;

    /**
     * The _asserts.
     */
    private static VerboseAsserts _asserts;

    /**
     * The _verify.
     */
    private static VerboseAsserts _verify;

    /**
     * The _testah util.
     */
    private static TestahUtil _testahUtil;

    /**
     * The _params.
     */
    private static Params _params;

    private static final HashMap<String, String> maskValues = new HashMap<>();

    /**
     * Asserts.
     *
     * @return the verbose asserts
     */
    public static VerboseAsserts asserts() {
        if (null == _asserts) {
            _asserts = new VerboseAsserts();
        }
        return _asserts;
    }

    /**
     * Verify.
     *
     * @return the verbose asserts
     */
    public static VerboseAsserts verify() {
        if (null == _verify) {
            _verify = new VerboseAsserts().onlyVerfiy();
        }
        return _verify;
    }

    /**
     * Stateful data.
     *
     * @return the hash map
     */
    public static HashMap<String, Object> statefulData() {
        if (null == _statefulData || null == _statefulData.get()) {
            final ThreadLocal<HashMap<String, Object>> _statefulDataTmp = new ThreadLocal<>();
            _statefulDataTmp.set(new HashMap<String, Object>());
            _statefulData = _statefulDataTmp;
        }
        return _statefulData.get();
    }

    /**
     * Reset stateful data.
     *
     * @return the hash map
     */
    public static HashMap<String, Object> resetStatefulData() {
        _statefulData = null;
        return statefulData();
    }

    /**
     * Util.
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
     * Log.
     *
     * @return the logger
     */
    public static Logger log() {
        return Log.getLog();
    }

    /**
     * Params.
     *
     * @param params the params
     * @return the params
     */
    public static Params params(final Params params) {
        if (null == _params) {
            _params = params;
        }
        return _params;
    }

    /**
     * Params.
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
     *
     * @param params the params
     * @return the params
     */
    public static Params setParams(final Params params) {
        TS._params = params;
        return TS._params;
    }

    /**
     * Browser.
     *
     * @return the abstract browser
     */
    public static AbstractBrowser<?> browser() {
        if (null == _browser || null == _browser.get()) {
            setBrowser(AbstractBrowser.getDefaultBrowser());
        }
        return _browser.get();
    }

    /**
     * Checks if is browser.
     *
     * @return true, if is browser
     */
    public static boolean isBrowser() {
        return (null != _browser && null != _browser.get());
    }

    /**
     * Sets the browser.
     *
     * @param browser the browser
     * @return the abstract browser
     */
    public static AbstractBrowser<?> setBrowser(final AbstractBrowser<?> browser) {
        TS._browser = new ThreadLocal<>();
        TS._browser.set(browser);
        // _browser.get().start();
        return _browser.get();
    }

    /**
     * Http.
     *
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper http() {
        if (null == _http || null == _http.get()) {
            TS.setHttp(new HttpWrapperV1());
        }
        return _http.get();
    }

    /**
     * Sets the http.
     *
     * @param http the http
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper setHttp(final AbstractHttpWrapper http) {
        TS._http = new ThreadLocal<>();
        TS._http.set(http);
        return TS._http.get();
    }

    /**
     * Adds the step action.
     *
     * @param stepAction the step action
     */
    public static void addStepAction(final StepActionDto stepAction) {
        AbstractTestPlan.addStepAction(stepAction);
        return;
    }

    public static HashMap<String, String> getMaskValues() {
        return maskValues;
    }

    public static void addMask(final String valueToMask) {
        maskValues.put(valueToMask, "*masked*");
    }

    public static TestPlanReporter getTestPlanReporter() {
        if (null == _testPlanReporter) {
            _testPlanReporter = new TestPlanReporter();
        }
        return _testPlanReporter;
    }

    public static void setTestPlanReporter(final TestPlanReporter testPlanReporter) {
        _testPlanReporter = testPlanReporter;
    }

    public static void tearDown() {
        cleanUpThreadLocal(_browser);
        cleanUpThreadLocal(_statefulData);
        cleanUpThreadLocal(_http);
    }

    private static void cleanUpThreadLocal(final ThreadLocal<?> threadLocal) {
        try {
            if (null != threadLocal) {
                threadLocal.remove();
            }
        } catch (final Exception e) {
            TS.log().warn("Trying to remove thread local", e);
        }
    }

}
