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

public class TS {

	private static ThreadLocal<HashMap<String, Object>> _statefulData;
	private static ThreadLocal<AbstractBrowser> _browser;
	private static ThreadLocal<AbstractHttpWrapper> _http;

	private static VerboseAsserts _asserts;
	private static VerboseAsserts _verify;
	private static TestahUtil _testahUtil;
	private static Params _params;

	public static VerboseAsserts asserts() {
		if (null == _asserts) {
			_asserts = new VerboseAsserts();
		}
		return _asserts;
	}

	public static VerboseAsserts verify() {
		if (null == _verify) {
			_verify = new VerboseAsserts();
			_verify.onlyVerfiy();
		}
		return _verify;
	}

	public static HashMap<String, Object> statefulData() {
		if (null == _statefulData) {
			_statefulData = new ThreadLocal<HashMap<String, Object>>();
			_statefulData.set(new HashMap<String, Object>());
		}
		return _statefulData.get();
	}

	public static HashMap<String, Object> resetStatefulData() {
		_statefulData = null;
		return statefulData();
	}

	public static TestahUtil util() {
		if (null == _testahUtil) {
			_testahUtil = new TestahUtil();
		}
		return _testahUtil;
	}

	public static Logger log() {
		return Log.getLog();
	}

	public static Params params() {
		if (null == _params) {
			_params = new Cli().getArgumentParser(null).getOpt();
		}
		return _params;
	}

	public static Params setParams(final Params params) {
		TS._params = params;
		return TS._params;
	}

	public static AbstractBrowser browser() {
		if (null == _browser) {
			setBrowser(AbstractBrowser.getDefaultBrowser());
		}
		return _browser.get();
	}

	public static boolean isBrowser() {
		return (null != _browser);
	}

	public static AbstractBrowser setBrowser(final AbstractBrowser browser) {
		TS._browser = new ThreadLocal<AbstractBrowser>();
		TS._browser.set(browser);
		_browser.get().start();
		return _browser.get();
	}

	public static AbstractHttpWrapper http() {
		if (null == _http) {
			TS.setHttp(new HttpWrapperV1());
		}
		return _http.get();
	}

	public static AbstractHttpWrapper setHttp(final AbstractHttpWrapper http) {
		TS._http = new ThreadLocal<AbstractHttpWrapper>();
		TS._http.set(http);
		return TS._http.get();
	}

	public static void addStepAction(final StepActionDto stepAction) {
		AbstractTestPlan.addStepAction(stepAction);
		return;
	}

}
