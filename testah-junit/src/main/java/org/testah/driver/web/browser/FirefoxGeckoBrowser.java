package org.testah.driver.web.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testah.TS;

import java.io.File;
import java.io.IOException;

/**
 * The Class FirefoxBrowser.
 */
public class FirefoxGeckoBrowser extends AbstractBrowser<FirefoxGeckoBrowser> {

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinary()
     */
    public FirefoxGeckoBrowser getDriverBinary() {
        WebDriverManager.firefoxdriver().setup();
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

    /**
     * getWebDriver will return a webdriver object for firefox.
     * @param capabilities the capabilities
     * @return
     */
    public WebDriver getWebDriver(final MutableCapabilities capabilities) {
        if (capabilities instanceof FirefoxOptions) {
            return new FirefoxDriver((FirefoxOptions) capabilities);
        } else {
            return new FirefoxDriver(capabilities);
        }
    }

    /**
     * create Capabilities.
     *
     * @return DesiredCapabilities
     */
    public MutableCapabilities createCapabilities() {


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
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);

        firefoxOptions.setCapability("elementScrollBehavior", 1);

        if (null != TS.params().getWebDriver_firefoxDriverBinary() &&
            TS.params().getWebDriver_firefoxDriverBinary().length() > 0) {
            String binPath = TS.params().getWebDriver_firefoxDriverBinary();
            firefoxOptions.setCapability(FirefoxDriver.BINARY,
                new FirefoxBinary(new File(binPath)));
        }


        return firefoxOptions;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
     */
    public FirefoxGeckoBrowser stopService() throws IOException {
        return this;
    }

    /**
     * log Browser Info, including cookies and session to help with debugging issues.
     *
     * @return FirefoxGeckoBrowser for use.
     */
    public AbstractBrowser<FirefoxGeckoBrowser> logBrowserInfo() {
        try {
            TS.log().trace("Browser SessionId: " + ((FirefoxDriver) TS.browser().getDriver()).getSessionId().toString());
            TS.util().toJsonPrint(TS.browser().getDriver().manage().getCookies());
        } catch (Exception e) {
            TS.log().trace("Issue getting browser info", e);
        }
        return getSelf();
    }

}
