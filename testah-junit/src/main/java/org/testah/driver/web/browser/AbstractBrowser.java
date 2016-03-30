package org.testah.driver.web.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testah.TS;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.driver.web.element.WebElementWrapperV1;

public abstract class AbstractBrowser {

    private WebDriver           driver;
    private int                 elementWaitTime     = 10;
    private final String        remoteUri           = TS.params().getWebDriver_defaultRemoteUri();
    private final boolean       remote              = TS.params().isWebDriver_useRemoteDriver();
    private final String        userAgentValue      = TS.params().getWebDriver_userAgentValue();
    private DesiredCapabilities desiredCapabilities = null;

    public static AbstractBrowser getDefaultBrowser() {
        switch (TS.params().getBrowser()) {
        case PHANTOMJS:
            return new PhantomJsBrowser();
        case FIREFOX:
            return new FirefoxBrowser();
        default:
            return new FirefoxBrowser();
        }
    }

    public AbstractBrowser start() {
        return start(remote);
    }

    public AbstractBrowser start(final boolean remote) {
        try {
            final DesiredCapabilities capabilities = getCapabilities();

            if (remote) {
                driver = getRemoteDriver(capabilities);
            } else {
                getDriverBinay();
                startService();
                driver = getWebDriver(capabilities);
            }
        } catch (final Exception e) {
            throw new RuntimeException("Issue Stating browser", e);
        }
        return this;
    }

    public static AbstractBrowser getFirefoxBrowser() {
        return new FirefoxBrowser().start();
    }

    public WebDriver getRemoteDriver(final DesiredCapabilities capabilities) {
        try {
            return new RemoteWebDriver(new URL(remoteUri), capabilities);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public abstract WebDriver getWebDriver(final DesiredCapabilities capabilities);

    public abstract AbstractBrowser getDriverBinay();

    public abstract AbstractBrowser startService() throws IOException;

    public abstract AbstractBrowser stopService() throws IOException;

    public DesiredCapabilities getCapabilities() {
        if (null == desiredCapabilities) {
            desiredCapabilities = createCapabilities();
        }
        return desiredCapabilities;
    }

    public AbstractBrowser setCapabilities(final DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        return this;
    }

    public abstract DesiredCapabilities createCapabilities();

    public AbstractWebElementWrapper getWebelement(final By by) {
        return new WebElementWrapperV1(by, getWebElementNative(by, false));
    }

    public AbstractWebElementWrapper getWebelement(final AbstractWebElementWrapper webElement) {
        return new WebElementWrapperV1(webElement.getBy(), getWebElementNative(webElement.getBy(), false));
    }

    public List<AbstractWebElementWrapper> getWebElements(final By by) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<AbstractWebElementWrapper>();
        for (final WebElement e : getWebElementsNative(by, false)) {
            lst.add(new WebElementWrapperV1(by, e));
        }
        return lst;
    }

    private List<WebElement> getWebElementsNative(final By by, final boolean noWait) {
        String error = "";
        List<WebElement> lst = new ArrayList<WebElement>();
        for (int i = 1; i <= elementWaitTime; i++) {
            error = "";
            try {
                lst = driver.findElements(by);
                break;
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getWebElementsNative", i);
        }
        TS.asserts().isTrue("Expected to find WebElements with By[" + by + "] found: " + lst.size() + " " + error,
                lst.size() > 0);
        return lst;
    }

    private WebElement getWebElementNative(final By by, final boolean noWait) {
        String error = "";
        WebElement element = null;
        for (int i = 1; i <= elementWaitTime; i++) {
            try {
                element = driver.findElement(by);
                break;
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getWebElementNative", i);
        }
        TS.asserts().notNull("Expected to find WebElement with By[" + by + "]: " + error, element);
        return element;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(final WebDriver driver) {
        this.driver = driver;
    }

    public int getElementWaitTime() {
        return elementWaitTime;
    }

    public void setElementWaitTime(final int elementWaitTime) {
        this.elementWaitTime = elementWaitTime;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public AbstractBrowser assertTitle(final String expectedTitle) {
        TS.asserts().equals("Assert Web Browser Pagetitle", expectedTitle, getTitle());
        return this;
    }

    public boolean verifyTitle(final String expectedTitle) {
        return TS.verify().equals("Verify Browser Pagetitle", expectedTitle, getTitle());
    }

    public AbstractBrowser waitForTitleToChange(final String pageTitleToChange, final int timeout) {
        String title;
        for (int i = 1; i <= timeout; i++) {
            title = getTitle();
            if (TS.verify().notEquals(pageTitleToChange, title)) {
                break;
            }
            TS.util().pause("waitForTitleToChange from [" + pageTitleToChange + "] - current [" + title + "]", i);
        }
        return this;
    }

    public AbstractBrowser waitForTitle(final String pageTitle, final int timeout) {
        String title;
        for (int i = 1; i <= timeout; i++) {
            title = getTitle();
            if (TS.verify().equals(pageTitle, title)) {
                break;
            }
            TS.util().pause("waitForTitle from [" + pageTitle + "] - current [" + title + "]", i);
        }
        return this;
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public AbstractBrowser assertUrl(final String expectedUrl) {
        TS.asserts().equals("Assert Web Browser Pagetitle", expectedUrl, getUrl());
        return this;
    }

    public boolean verifyUrl(final String expectedUrl) {
        return TS.verify().equals("Verify Browser Pagetitle", expectedUrl, getUrl());
    }

    public AbstractBrowser waitForUrlToChange(final String pageUrlToChange, final int timeout) {
        String url;
        for (int i = 1; i <= timeout; i++) {
            url = getUrl();
            if (TS.verify().notEquals(pageUrlToChange, url)) {
                break;
            }
            TS.util().pause("waitForUrlToChange from [" + pageUrlToChange + "] - current [" + url + "]", i);
        }

        return this;
    }

    public AbstractBrowser goTo(final String uri) {
        driver.get(uri);
        return this;
    }

    public AbstractBrowser goToAndWaitForTitleToChange(final String uri) {
        final String title = "WaitingForChange - " + TS.util().now();
        runJavaScript("document.title='" + title + "';");
        goTo(uri);
        waitForTitleToChange(title, 10);
        return this;
    }

    public AbstractBrowser goToAndWaitForTitle(final String uri, final String title) {
        goTo(uri);
        waitForTitle(title, 10);
        return this;
    }

    public AbstractBrowser runJavaScript(final String javaSript) {
        getJavaScriptValue(javaSript);
        return this;
    }

    public String getJavaScriptValue(final String javaScript) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        return String.valueOf(js.executeScript(javaScript));
    }

    public String getUserAgentValue() {
        return userAgentValue;
    }

    public String getRemoteUri() {
        return remoteUri;
    }

    public boolean isRemote() {
        return remote;
    }

    public String takeScreenShot() {

        return takeScreenShot(TS.params().getOutput());
    }

    public String takeScreenShot(final String path) {
        try {
            final String version = driver.toString().toLowerCase();
            File f = new File(path);
            if (f.isDirectory()) {
                f = File.createTempFile("screenshot_", ".png", f);
            }
            if (version.contains("remotewebdriver")) {
                final WebDriver augmentedDriver = new Augmenter().augment(driver);
                final File s = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(s, f);
                TS.log().info("Screenshot file: " + f.getAbsolutePath());
            } else {
                final File sf = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(sf, f);
                TS.log().info("Screenshot file: " + f.getAbsolutePath());
            }
            return f.getAbsolutePath();
        } catch (final Exception e) {
            TS.log().error(e);
        }

        return null;
    }

    public void close() {
        if (null != driver) {
            try {
                driver.close();
                driver = null;
                stopService();
            } catch (final Exception e) {
                TS.log().warn("issue closing browser", e);
            }
        }

    }

    public boolean verifyElementIsPresent(final By by) {
        try {
            TS.browser().getDriver().findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
