package org.testah.driver.web.browser;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;

public class FirefoxBrowser extends AbstractBrowser {
    
    
    public AbstractBrowser startInternal() {
        return this;
    }

    
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        return new FirefoxDriver(capabilities);
    }

    
    public AbstractBrowser getDriverBinay() {
        return this;
    }

    
    public AbstractBrowser startService() {
        return this;
    }

    
    public AbstractBrowser setCapabilities() {
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
        if (null != TS.params().getFirefoxDriverBinary()) {
            capabilities.setCapability(FirefoxDriver.BINARY,
                    new FirefoxBinary(new File(TS.params().getFirefoxDriverBinary())));
        }
        setCapabilities(capabilities);
        return this;
    }
    
}
