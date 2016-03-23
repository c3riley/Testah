package org.testah;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.web.browser.AbstractBrowser;
import org.testah.framework.cli.Cli;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.StepActionDto;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.util.Log;
import org.testah.util.TestahUtil;

/**
 * The Class TS.
 */
public class TS {

    /** The _stateful data. */
    private static ThreadLocal<HashMap<String, Object>> _statefulData;

    /** The _browser. */
    private static ThreadLocal<AbstractBrowser>         _browser;

    /** The _http. */
    private static ThreadLocal<AbstractHttpWrapper>     _http;

    /** The _asserts. */
    private static VerboseAsserts                       _asserts;

    /** The _verify. */
    private static VerboseAsserts                       _verify;

    /** The _testah util. */
    private static TestahUtil                           _testahUtil;

    /** The _params. */
    private static Params                               _params;

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
            _verify = new VerboseAsserts();
            _verify.onlyVerfiy();
        }
        return _verify;
    }

    /**
     * Stateful data.
     *
     * @return the hash map
     */
    public static HashMap<String, Object> statefulData() {
        if (null == _statefulData) {
            _statefulData = new ThreadLocal<HashMap<String, Object>>();
            _statefulData.set(new HashMap<String, Object>());
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
     * @return the params
     */
    public static Params params() {
        if (null == _params) {
            _params = new Cli().getArgumentParser(null).getOpt();
        }
        return _params;
    }

    /**
     * Sets the params.
     *
     * @param params
     *            the params
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
    public static AbstractBrowser browser() {
        if (null == _browser) {
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
        return (null != _browser);
    }

    /**
     * Sets the browser.
     *
     * @param browser
     *            the browser
     * @return the abstract browser
     */
    public static AbstractBrowser setBrowser(final AbstractBrowser browser) {
        TS._browser = new ThreadLocal<AbstractBrowser>();
        TS._browser.set(browser);
        _browser.get().start();
        return _browser.get();
    }

    /**
     * Http.
     *
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper http() {
        if (null == _http) {
            TS.setHttp(new HttpWrapperV1());
        }
        return _http.get();
    }

    /**
     * Sets the http.
     *
     * @param http
     *            the http
     * @return the abstract http wrapper
     */
    public static AbstractHttpWrapper setHttp(final AbstractHttpWrapper http) {
        TS._http = new ThreadLocal<AbstractHttpWrapper>();
        TS._http.set(http);
        return TS._http.get();
    }

    /**
     * Adds the step action.
     *
     * @param stepAction
     *            the step action
     */
    public static void addStepAction(final StepActionDto stepAction) {
        AbstractTestPlan.addStepAction(stepAction);
        return;
    }

}
