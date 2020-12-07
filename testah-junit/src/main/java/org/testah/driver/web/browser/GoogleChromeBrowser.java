package org.testah.driver.web.browser;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testah.TS;
import org.testah.framework.cli.Params;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class GoogleChromeBrowser.
 */
public class GoogleChromeBrowser extends AbstractBrowser<GoogleChromeBrowser> {

    /**
     * The service.
     */
    private ChromeDriverService service = null;

    /*
     * (non-Javadoc).
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinary().
     */
    public GoogleChromeBrowser getDriverBinary() {
        ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
        return this;
    }

    /**
     * start Service for webdriver.
     *
     * @return GoogleChromeBrowser
     * @throws IOException thrown if issues starting service
     */
    public GoogleChromeBrowser startService() throws IOException {
        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(getChromePath())).usingAnyFreePort()
            .withLogFile(File.createTempFile("googleChromeLog", ".log")).build();
        service.start();
        return this;
    }

    /**
     * get Web Driver object.
     *
     * @param capabilities the capabilities
     * @return WebDriver
     */
    public WebDriver getWebDriver(final MutableCapabilities capabilities) {
        if (capabilities instanceof ChromeOptions) {
            ChromeOptions chromeOptions = (ChromeOptions) capabilities;
            if (null == service) {
                return new ChromeDriver(chromeOptions);
            } else {
                return new ChromeDriver(service, chromeOptions);
            }
        } else {
            if (null == service) {
                return new ChromeDriver(capabilities);
            } else {
                return new ChromeDriver(service, capabilities);
            }
        }
    }

    /**
     * create Capabilities.
     *
     * @return DesiredCapabilities
     */
    public MutableCapabilities createCapabilities() {
        final ChromeOptions chromeOptions = new ChromeOptions();

        if (null != getUserAgentValue()) {
            chromeOptions.setCapability("user-agent", getUserAgentValue());
        }

        final Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", TS.params().getOutput());
        chromeOptions.addArguments("start-maximized");
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setCapability("chrome.switches",
            Arrays.asList("--disable-default-apps", "--allow-running-insecure-content", "--start-maximized"));

        return chromeOptions;
    }

    /**
     * stop Service for chrome driver.
     *
     * @return GoogleChromeBrowser
     * @throws IOException thrown is issue stopping service
     */
    public GoogleChromeBrowser stopService() throws IOException {
        if (null != service) {
            service.stop();
        }
        return null;
    }

    /**
     * logBrowserInfo will log info about the browser session.
     *
     * @return returns class instance
     */
    @Override
    public AbstractBrowser<GoogleChromeBrowser> logBrowserInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the chrome path.
     *
     * @return the chrome path.
     */
    private String getChromePath() {

        String binPath = TS.params().getWebDriver_chromeDriverBinary();
        try {
            if (null == binPath || binPath.length() == 0 || !(new File(binPath)).exists()) {
                final File downloadDestinationDir = TS.util().getDownloadDestinationDirectory("drivers");
                final File unZipDestination = new File(downloadDestinationDir, "chrome");
                File webDriverExecutable = findWebdriverExecutable(unZipDestination);
                if (null == webDriverExecutable) {
                    String urlSource = "https://chromedriver.storage.googleapis.com/2.32/chromedriver_linux64.zip";
                    if (Params.isWindows()) {
                        urlSource = "https://chromedriver.storage.googleapis.com/2.32/chromedriver_win32.zip";
                    } else if (Params.isMac()) {
                        urlSource = "https://chromedriver.storage.googleapis.com/2.32/chromedriver_mac64.zip";
                    }
                    final File zip = TS.util().downloadFile(urlSource, downloadDestinationDir);
                    TS.util().unZip(zip, unZipDestination);
                    cleanupDownloads(downloadDestinationDir);
                    webDriverExecutable = findWebdriverExecutable(unZipDestination);
                } else {
                    TS.log().info("WebDriver executable already downloaded : " + webDriverExecutable.getAbsolutePath());
                }
                if (webDriverExecutable.exists()) {
                    webDriverExecutable.setExecutable(true);
                    binPath = webDriverExecutable.getAbsolutePath();
                    TS.params().setWebDriver_chromeDriverBinary(binPath);
                } else {
                    TS.log().error("WebDriver not found : " + webDriverExecutable.getAbsolutePath());
                }
            }
        } catch (final Exception e) {
            TS.log().warn(e);
        }
        // return binPath;
        return ChromeDriverManager.getInstance(DriverManagerType.CHROME).getDownloadedDriverPath();
    }

    private File findWebdriverExecutable(final File driverParentDirectory) {
        File webdriverExecutable = null;
        if (driverParentDirectory.exists()) {
            webdriverExecutable = Arrays.stream(driverParentDirectory.listFiles((d, s) -> {
                return d.exists() && d.isDirectory() && s.toLowerCase().contains("driver");
            })).findAny().orElse((File) null);
        }
        return webdriverExecutable;
    }

    private void cleanupDownloads(final File downloadDestinationDir) {
        Arrays.stream(downloadDestinationDir.listFiles((d, s) -> {
            return d.exists() && d.isDirectory() && s.toLowerCase().contains("download");
        })).filter(f -> {
            return f.isFile();
        }).forEach(File::delete);
    }

}
