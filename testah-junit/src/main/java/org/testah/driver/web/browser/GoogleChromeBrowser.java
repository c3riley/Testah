package org.testah.driver.web.browser;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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
     * (non-Javadoc)
     *
     * @see
     * org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.
     * selenium.remote.DesiredCapabilities)
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
    public GoogleChromeBrowser getDriverBinay() {
        ChromeDriverManager.getInstance().setup();
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#startService()
     */
    public GoogleChromeBrowser startService() throws IOException {
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

        final Map<String, Object> prefs = new HashMap<>();
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
                final File downloadDestinationDir = TS.util().getDownloadDestinationDirectory("drivers");
                final File unZipDestination = new File(downloadDestinationDir, "chrome");
                File webDriverExecutable = findWebriverExecutable(unZipDestination);
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
                    webDriverExecutable = findWebriverExecutable(unZipDestination);
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
        return ChromeDriverManager.getInstance().getBinaryPath();
    }

    private File findWebriverExecutable(final File driverParentDirectory) {
        File webriverExecutable = null;
        if (driverParentDirectory.exists()) {
            webriverExecutable = Arrays.stream(driverParentDirectory.listFiles((d, s) -> {
                return d.exists() && d.isDirectory() && s.toLowerCase().contains("driver");
            })).findAny().orElse((File) null);
        }
        return webriverExecutable;
    }

    private void cleanupDownloads(final File downloadDestinationDir) {
        Arrays.stream(downloadDestinationDir.listFiles((d, s) -> {
            return d.exists() && d.isDirectory() && s.toLowerCase().contains("download");
        })).filter(f -> {
            return f.isFile();
        }).forEach(file -> file.delete());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#stopService()
     */
    public GoogleChromeBrowser stopService() throws IOException {
        if (null != service) {
            service.stop();
        }
        return null;
    }

    @Override
    public AbstractBrowser<GoogleChromeBrowser> logBrowerInfo() {
        // TODO Auto-generated method stub
        return null;
    }

}
