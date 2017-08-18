package org.testah.driver.web.browser;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;

import io.github.bonigarcia.wdm.FirefoxDriverManager;

/**
 * The Class FirefoxBrowser.
 */
public class FirefoxBrowser extends AbstractBrowser<FirefoxBrowser> {
    
    /*
     * (non-Javadoc)
     *
     * @see
     * org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.
     * selenium.remote. DesiredCapabilities)
     */
    @Override
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        return new FirefoxDriver(capabilities);
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinay()
     */
    @Override
    public FirefoxBrowser getDriverBinay() {
        FirefoxDriverManager.getInstance().setup();
        System.setProperty("webdriver.gecko.driver", FirefoxDriverManager.getInstance().getBinaryPath());
        TS.params().setWebDriver_firefoxDriverBinary(FirefoxDriverManager.getInstance().getBinaryPath());
        return this;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#startService()
     */
    @Override
    public FirefoxBrowser startService() {
        return this;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#createCapabilities()
     */
    @Override
    public DesiredCapabilities createCapabilities() {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
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
    @Override
    public FirefoxBrowser stopService() throws IOException {
        return this;
    }
    
    @Override
    public AbstractBrowser<FirefoxBrowser> logBrowerInfo() {
        try {
            TS.log().trace(
                    "Browser SessionId: " + ((FirefoxDriver) TS.browser().getDriver()).getSessionId().toString());
            TS.util().toJsonPrint(TS.browser().getDriver().manage().getCookies());
        }
        catch (final Exception e) {
            TS.log().trace("Issue geting browser info", e);
        }
        return getSelf();
    }
    
}