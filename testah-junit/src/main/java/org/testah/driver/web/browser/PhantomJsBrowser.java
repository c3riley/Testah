package org.testah.driver.web.browser;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.PhantomJsDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testah.TS;

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
    public AbstractBrowser<PhantomJsBrowser> getDriverBinary() {
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
    public WebDriver getWebDriver(final MutableCapabilities capabilities) {
        return new PhantomJSDriver(service, capabilities);
    }

    /**
     * create Capabilities.
     *
     * @return DesiredCapabilities
     */
    public MutableCapabilities createCapabilities() {
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
        TS.log().warn("logBrowserInfo - Not supported yet by phantomjs code");
        return null;
    }

    /**
     * Gets the phantom js bin path.
     *
     * @return the phantom js bin path
     */
    private String getPhantomJsBinPath() {
        return PhantomJsDriverManager.phantomjs().getDownloadedDriverPath();
    }

}
