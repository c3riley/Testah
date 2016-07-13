package org.testah.driver.web.browser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;
import org.testah.framework.cli.Params;

/**
 * The Class GoogleChromeBrowser.
 */
public class GoogleChromeBrowser extends AbstractBrowser {

    /** The service. */
    private ChromeDriverService service = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.selenium.remote.DesiredCapabilities)
     */
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        if (null == service) {
            return new ChromeDriver(capabilities);
        } else {
            return new ChromeDriver(service, capabilities);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinay()
     */
    public AbstractBrowser getDriverBinay() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testah.driver.web.browser.AbstractBrowser#startService()
     */
    public AbstractBrowser startService() throws IOException {
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(getChromePath())).usingAnyFreePort()
                .withLogFile(File.createTempFile("googleChromeLog", ".log")).build();
        service.start();
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testah.driver.web.browser.AbstractBrowser#createCapabilities()
     */
    public DesiredCapabilities createCapabilities() {
        final DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        if (null != getUserAgentValue()) {
            capabilities.setCapability("user-agent", getUserAgentValue());
        }

        final Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", TS.params().getOutput());
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        chromeOptions.setExperimentalOption("prefs", prefs);

        capabilities.setCapability("chrome.switches",
                Arrays.asList("--disable-default-apps", "--allow-running-insecure-content", "--start-maximized"));

        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return capabilities;
    }

    /**
     * Gets the chrome path.
     *
     * @return the chrome path
     */
    private String getChromePath() {

        String binPath = TS.params().getWebDriver_chromeDriverBinary();
        try {
            if (null == binPath || binPath.length() == 0 || !(new File(binPath)).exists()) {
                String urlSource = "http://chromedriver.storage.googleapis.com/2.21/chromedriver_linux64.zip";
                if (Params.isWindows()) {
                    urlSource = "http://chromedriver.storage.googleapis.com/2.21/chromedriver_win32.zip";
                } else if (Params.isMac()) {
                    urlSource = "http://chromedriver.storage.googleapis.com/2.21/chromedriver_mac32.zip";
                }
                final File zip = TS.util().downloadFile(urlSource, "drivers");
                final File dest = TS.util().unZip(zip, new File(zip.getParentFile(), "chrome"));
                binPath = dest.getAbsolutePath();
            }
        } catch (final Exception e) {
            TS.log().warn(e);
        }
        return binPath;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
     */
    public AbstractBrowser stopService() throws IOException {
        if (null != service) {
            service.stop();
        }
        return null;
    }

    protected AbstractBrowser getSelf() {
        return this;
    }

}
