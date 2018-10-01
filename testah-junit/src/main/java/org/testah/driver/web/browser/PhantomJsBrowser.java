package org.testah.driver.web.browser;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;
import org.testah.framework.cli.Params;

import java.io.File;
import java.io.IOException;

/**
 * The Class PhantomJsBrowser.
 */
public class PhantomJsBrowser extends AbstractBrowser<PhantomJsBrowser> {

    /**
     * The service.
     */
    private PhantomJSDriverService service = null;

    /*
     * (non-Javadoc)
     *
     * @see org.testah.driver.web.browser.AbstractBrowser#getDriverBinary()
     */
    public PhantomJsBrowser getDriverBinary() {
        PhantomJsDriverManager.getInstance(DriverManagerType.PHANTOMJS).setup();
        return this;
    }

    /**
     * start Service.
     *
     * @return PhantomJsBrowser
     * @throws IOException thrown is issue starting service
     */
    public PhantomJsBrowser startService() throws IOException {
        service = new PhantomJSDriverService.Builder().usingPhantomJSExecutable(new File(getPhantomJsBinPath()))
                .usingAnyFreePort().usingCommandLineArguments(new String[]{"--ignore-ssl-errors=true"}).build();
        service.start();
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.testah.driver.web.browser.AbstractBrowser#getWebDriver(org.openqa.
     * selenium.remote.DesiredCapabilities)
     */
    public WebDriver getWebDriver(final DesiredCapabilities capabilities) {
        return new PhantomJSDriver(service, capabilities);
    }

    /**
     * create Capabilities.
     *
     * @return DesiredCapabilities
     */
    public DesiredCapabilities createCapabilities() {
        final DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();

        if (null != getUserAgentValue()) {
            capabilities.setCapability("phantomjs.page.settings.userAgent", getUserAgentValue());
        }
        capabilities.setCapability("takesScreenshot", true);
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, getPhantomJsBinPath());

        return capabilities;
    }

    /**
     * stop Service.
     *
     * @return PhantomJsBrowser
     * @throws IOException thrown if issue stopping service
     */
    public PhantomJsBrowser stopService() throws IOException {
        if (null != service) {
            service.stop();
        }
        return null;
    }

    @Override
    public AbstractBrowser<PhantomJsBrowser> logBrowserInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the phantom js bin path.
     *
     * @return the phantom js bin path
     */
    private String getPhantomJsBinPath() {

        String binPath = TS.params().getWebDriver_phantomJsDriverBinary();
        try {
            if (null == binPath || binPath.length() == 0 || !(new File(binPath)).exists()) {
                String urlSource = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2";
                if (Params.isWindows()) {
                    urlSource = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-windows.zip";
                } else if (Params.isMac()) {
                    urlSource = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-macosx.zip";
                }
                final File zip = TS.util().downloadFile(urlSource, "drivers");
                final File dest = TS.util().unZip(zip, new File(zip.getParentFile(), "phantomjs"));
                binPath = dest.getAbsolutePath();
            }
        } catch (final Exception e) {
            TS.log().warn(e);
        }
        return binPath;

    }

}
