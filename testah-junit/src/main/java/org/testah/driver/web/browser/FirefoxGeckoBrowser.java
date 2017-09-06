package org.testah.driver.web.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;

import java.io.File;
import java.io.IOException;

/**
 * The Class FirefoxBrowser.
 */
public class FirefoxGeckoBrowser extends AbstractBrowser<FirefoxGeckoBrowser> {

    private final static String DRIVER_PATH_VAR = "webdriver.gecko.driver";

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.selenium.remote.DesiredCapabilities)
     */
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        return new FirefoxDriver(capabilities);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinay()
     */
    public FirefoxGeckoBrowser getDriverBinay() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#startService()
     */
    public FirefoxGeckoBrowser startService() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#createCapabilities()
     */
    public DesiredCapabilities createCapabilities() {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        final String driverPath = TS.params().getValue(DRIVER_PATH_VAR);
        if (driverPath != null) {
            capabilities.setCapability("marionette", true);
        }
        final FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        if (null != getUserAgentValue()) {
            profile.setPreference("general.useragent.override", getUserAgentValue());
        }
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "text/csv;application/pdf;application/vnd.ms-excelapplication/txt;application/txt;");
        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("pdfjs.firstRun", false);
        profile.setPreference("dom.max_script_run_time", 0);
        capabilities.setCapability("nativeEvents", false);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        capabilities.setCapability("elementScrollBehavior", 1);
        if (null != TS.params().getWebDriver_firefoxDriverBinary()
                && TS.params().getWebDriver_firefoxDriverBinary().length() > 0) {
            capabilities.setCapability(FirefoxDriver.BINARY,
                    new FirefoxBinary(new File(TS.params().getWebDriver_firefoxDriverBinary())));
        }
        return capabilities;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
     */
    public FirefoxGeckoBrowser stopService() throws IOException {
        return this;
    }

    public AbstractBrowser<FirefoxGeckoBrowser> logBrowerInfo() {
        try {
            TS.log().trace("Browser SessionId: " + ((FirefoxDriver) TS.browser().getDriver()).getSessionId().toString());
            TS.util().toJsonPrint(TS.browser().getDriver().manage().getCookies());
        } catch (Exception e) {
            TS.log().trace("Issue geting browser info", e);
        }
        return getSelf();
    }

}
