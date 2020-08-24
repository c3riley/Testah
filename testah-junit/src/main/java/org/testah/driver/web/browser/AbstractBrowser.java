package org.testah.driver.web.browser;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testah.TS;
import org.testah.client.enums.BrowserType;
import org.testah.driver.web.element.AbstractWebElementWrapper;
import org.testah.driver.web.element.WebElementWrapperV1;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AbstractBrowser wraps Webdriver Api implementation with many
 * methods to reduce code in tests.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractBrowser<T> {

    /**
     * The remote uri.
     */
    private final String remoteUri = TS.params().getWebDriver_defaultRemoteUri();
    /**
     * The remote.
     */
    private final boolean remote = TS.params().isWebDriver_useRemoteDriver();
    /**
     * The driver.
     */
    private WebDriver driver;
    /**
     * The element wait time.
     */
    private int elementWaitTime = 10;
    /**
     * The user agent value.
     */
    private String userAgentValue = TS.params().getWebDriver_userAgentValue();

    /**
     * The desired capabilities.
     */
    private MutableCapabilities desiredCapabilities = null;

    /**
     * Gets the default browser.
     *
     * @return the default browser
     */
    public static AbstractBrowser<?> getDefaultBrowser() {
        TS.log().trace("Setting default browser: " + TS.params().getBrowser());
        if (TS.params().getBrowser() == BrowserType.PHANTOMJS) {
            return new PhantomJsBrowser().start();
        } else if (TS.params().getBrowser() == BrowserType.CHROME) {
            return new GoogleChromeBrowser().start();
        } else if (TS.params().getBrowser() == BrowserType.FIREFOX) {
            return new FirefoxBrowser().start();
        } else {
            TS.log().debug("No Browser Match Found defaulting to Chrome");
            return new GoogleChromeBrowser().start();
        }
    }

    ;

    /**
     * Gets the firefox browser.
     *
     * @return the firefox browser
     */
    public static AbstractBrowser<?> getFirefoxBrowser() {
        return new FirefoxBrowser().start();
    }

    /**
     * Start.
     *
     * @return the abstract browser
     */
    public AbstractBrowser<T> start() {
        return start(remote);
    }

    /**
     * Start.
     *
     * @param remote the remote
     * @return the abstract browser
     */
    public AbstractBrowser<T> start(final boolean remote) {
        try {
            final MutableCapabilities capabilities = getCapabilities();

            if (remote) {
                driver = getRemoteDriver(capabilities);
            } else {
                getDriverBinary();
                startService();
                driver = getWebDriver(capabilities);
            }
            this.setWindowSize();
        } catch (final Exception e) {
            throw new RuntimeException("Issue Starting browser", e);
        }
        return getSelf();
    }

    /**
     * Gets the capabilities.
     *
     * @return the capabilities
     */
    public MutableCapabilities getCapabilities() {
        if (null == desiredCapabilities) {
            desiredCapabilities = createCapabilities();
        }
        return desiredCapabilities;
    }

    /**
     * Sets the capabilities.
     *
     * @param desiredCapabilities the desired capabilities
     * @return the abstract browser
     */
    public AbstractBrowser<T> setCapabilities(final DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        return getSelf();
    }

    /**
     * Gets the remote driver.
     *
     * @param capabilities the capabilities
     * @return the remote driver
     */
    public WebDriver getRemoteDriver(final MutableCapabilities capabilities) {
        try {
            return new RemoteWebDriver(new URL(remoteUri), capabilities);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Gets the driver binary.
     *
     * @return the driver binary
     */
    public abstract AbstractBrowser<T> getDriverBinary();

    /**
     * Start service.
     *
     * @return the abstract browser
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract T startService() throws IOException;

    /**
     * Gets the web driver.
     *
     * @param capabilities the capabilities
     * @return the web driver
     */
    public abstract WebDriver getWebDriver(final MutableCapabilities capabilities);

    /**
     * Sets window size. Use the default values from TS.params(), if not found maximize window.
     *
     * @return the window size
     */
    public AbstractBrowser<T> setWindowSize() {
        return setWindowSize(TS.params().getWindowWidth(), TS.params().getWindowHeight());
    }

    /**
     * Sets window size, if width or height is 0 or less just maximize window.
     *
     * @param width  the width
     * @param height the height
     * @return the window size
     */
    public AbstractBrowser<T> setWindowSize(final Integer width, final Integer height) {
        try {
            if (width > 0 && height > 0) {
                driver.manage().window().setSize(new Dimension(width, height));
            } else {
                TS.log().debug("Unable to set size as value is null, will maximize window instead");
                this.maximize();
            }
        } catch (final Exception e) {
            TS.log().warn(String.format("Issue with setting window size - width: %d height: %d", width, height), e);
        }
        return getSelf();
    }

    /**
     * Creates the capabilities.
     *
     * @return the desired capabilities
     */
    public abstract MutableCapabilities createCapabilities();

    /**
     * Maximize.
     *
     * @return the abstract browser
     */
    public AbstractBrowser<T> maximize() {
        try {
            driver.manage().window().maximize();
        } catch (final Exception e) {
            TS.log().warn("Issue with maximize", e);
        }
        return getSelf();
    }

    /**
     * Assert title.
     *
     * @param expectedTitle the expected title
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertTitle(final String expectedTitle) {
        TS.asserts().equalsTo("Assert Web Browser PageTitle", expectedTitle, getTitle());
        return getSelf();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the self.
     *
     * @return the self
     */
    protected AbstractBrowser<T> getSelf() {
        return this;
    }

    /**
     * Assert url.
     *
     * @param expectedUrl the expected url
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertUrl(final String expectedUrl) {
        TS.asserts().equalsTo("Assert Web Browser PageTitle", expectedUrl, getUrl());
        return getSelf();
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Close.
     *
     * @return the abstract browser
     */
    public AbstractBrowser<T> close() {
        if (null != driver) {
            try {
                driver.close();
                driver = null;
                stopService();
            } catch (final Exception e) {
                TS.log().warn("issue closing browser", e);
            }
        }
        return getSelf();
    }

    /**
     * Stop service.
     *
     * @return the abstract browser
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract T stopService() throws IOException;

    /**
     * Gets the element wait time.
     *
     * @return the element wait time
     */
    public int getElementWaitTime() {
        return elementWaitTime;
    }

    /**
     * Sets the element wait time.
     *
     * @param elementWaitTime the new element wait time
     * @return the abstract browser
     */
    public AbstractBrowser<T> setElementWaitTime(final int elementWaitTime) {
        this.elementWaitTime = elementWaitTime;
        return getSelf();
    }

    /**
     * Gets the remote uri.
     *
     * @return the remote uri
     */
    public String getRemoteUri() {
        return remoteUri;
    }

    /**
     * Gets the user agent value.
     *
     * @return the user agent value
     */
    public String getUserAgentValue() {
        return userAgentValue;
    }

    /**
     * Sets the user agent value.
     *
     * @param userAgentValue the user agent value
     * @return the abstract browser
     */
    public AbstractBrowser<T> setUserAgentValue(final String userAgentValue) {
        this.userAgentValue = userAgentValue;
        return this;
    }

    /**
     * Gets the webelement.
     *
     * @param webElement the web element
     * @return the webelement
     */
    public AbstractWebElementWrapper getWebElement(final AbstractWebElementWrapper webElement) {
        return new WebElementWrapperV1(webElement.getBy(), getWebElementNative(webElement.getBy(), false), this);
    }

    /**
     * Gets the web element.
     *
     * @param by                 the by
     * @param waitIterationCount the wait iteration count
     * @return the web element
     */
    public AbstractWebElementWrapper getWebElement(final By by, final int waitIterationCount) {
        return new WebElementWrapperV1(by, getWebElementNative(by, false, waitIterationCount), this);
    }

    /**
     * Gets the webelement.
     *
     * @param by the by
     * @return the webelement
     */
    public AbstractWebElementWrapper getWebElement(final By by) {
        return new WebElementWrapperV1(by, getWebElementNative(by, false), this);
    }

    /**
     * Gets the web element native.
     *
     * @param by     the by
     * @param noWait the no wait
     * @return the web element native
     */
    private WebElement getWebElementNative(final By by, final boolean noWait) {
        return getWebElementNative(by, noWait, elementWaitTime);
    }

    /**
     * Gets the web element native.
     *
     * @param by                 the by
     * @param noWait             the no wait
     * @param waitIterationCount the wait iteration count
     * @return the web element native
     */
    private WebElement getWebElementNative(final By by, final boolean noWait, final int waitIterationCount) {
        String error = "";
        WebElement element = null;
        for (int count = 1; count <= waitIterationCount; count++) {
            try {
                element = driver.findElement(by);
                break;
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getWebElementNative", count);
        }
        TS.asserts().notNull("Expected to find WebElement with By[" + by + "]: " + error, element);
        return element;
    }

    /**
     * Gets the web elements.
     *
     * @param by the by
     * @return the web elements
     */
    public List<AbstractWebElementWrapper> getWebElements(final By by) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<>();
        for (final WebElement e : getWebElementsNative(by, false)) {
            lst.add(new WebElementWrapperV1(by, e, this));
        }
        return lst;
    }

    /**
     * Gets the web elements.
     *
     * @param by                 the by
     * @param waitIterationCount the wait iteration count
     * @return the web elements
     */
    public List<AbstractWebElementWrapper> getWebElements(final By by, final int waitIterationCount) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<>();
        for (final WebElement e : getWebElementsNative(by, false, waitIterationCount)) {
            lst.add(new WebElementWrapperV1(by, e, this));
        }
        return lst;
    }

    /**
     * Gets the web elements native.
     *
     * @param by     the by
     * @param noWait the no wait
     * @return the web elements native
     */
    private List<WebElement> getWebElementsNative(final By by, final boolean noWait) {
        return getWebElementsNative(by, noWait, elementWaitTime);
    }

    /**
     * Gets the web elements native.
     *
     * @param by                 the by
     * @param noWait             the no wait
     * @param waitIterationCount the wait iteration count
     * @return the web elements native
     */
    private List<WebElement> getWebElementsNative(final By by, final boolean noWait, final int waitIterationCount) {
        String error = "";
        List<WebElement> lst = new ArrayList<>();
        for (int count = 1; count <= waitIterationCount; count++) {
            error = "";
            try {
                lst = driver.findElements(by);
                if (!lst.isEmpty()) {
                    TS.log().trace("List of Elements is empty");
                    break;
                }
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getWebElementsNative", count);
        }
        TS.asserts().isTrue("Expected to find WebElements with By[" + by + "] found: " + lst.size() + " " + error,
            lst.size() > 0);
        return lst;
    }

    /**
     * Gets the webelement no wait.
     *
     * @param by the by
     * @return the webelement no wait
     */
    public AbstractWebElementWrapper getWebElementNoWait(final By by) {
        return new WebElementWrapperV1(by, getWebElementNative(by, true), this);
    }

    /**
     * Gets the webelement no wait.
     *
     * @param webElement the web element
     * @return the webelement no wait
     */
    public AbstractWebElementWrapper getWebElementNoWait(final AbstractWebElementWrapper webElement) {
        return new WebElementWrapperV1(webElement.getBy(), getWebElementNative(webElement.getBy(), true), this);
    }

    /**
     * Gets the web elements no wait.
     *
     * @param by the by
     * @return the web elements no wait
     */
    public List<AbstractWebElementWrapper> getWebElementsNoWait(final By by) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<>();
        for (final WebElement e : getWebElementsNative(by, true)) {
            lst.add(new WebElementWrapperV1(by, e, this));
        }
        return lst;
    }

    /**
     * Go to and wait for title.
     *
     * @param uri   the uri
     * @param title the title
     * @return the abstract browser
     */
    public AbstractBrowser<T> goToAndWaitForTitle(final String uri, final String title) {
        goTo(uri);
        waitForTitle(title, 10);
        return getSelf();
    }

    /**
     * Go to.
     *
     * @param uri the uri
     * @return the abstract browser
     */
    public AbstractBrowser<T> goTo(final String uri) {
        TS.step().action().createInfo("goTo", uri);
        if (null == driver) {
            start();
        }
        driver.get(uri);
        return getSelf();
    }

    /**
     * Wait for title.
     *
     * @param pageTitle the page title
     * @param timeout   the timeout
     * @return the abstract browser
     */
    public AbstractBrowser<T> waitForTitle(final String pageTitle, final int timeout) {
        String title;
        for (int count = 1; count <= timeout; count++) {
            title = getTitle();
            if (TS.verify().equalsTo(pageTitle, title)) {
                break;
            }
            TS.util().pause("waitForTitle from [" + pageTitle + "] - current [" + title + "]", count);
        }
        return getSelf();
    }

    /**
     * Go to and wait for title to change.
     *
     * @param uri the uri
     * @return the abstract browser
     */
    public AbstractBrowser<T> goToAndWaitForTitleToChange(final String uri) {
        final String title = "WaitingForChange - " + TS.util().now();
        runJavaScript("document.title='" + title + "';");
        goTo(uri);
        waitForTitleToChange(title, 10);
        return getSelf();
    }

    /**
     * Run java script.
     *
     * @param javaScript the java script
     * @return the abstract browser
     */
    public AbstractBrowser<T> runJavaScript(final String javaScript) {
        getJavaScriptValue(javaScript);
        return getSelf();
    }

    /**
     * Wait for title to change.
     *
     * @param pageTitleToChange the page title to change
     * @param timeout           the timeout
     * @return the abstract browser
     */
    public AbstractBrowser<T> waitForTitleToChange(final String pageTitleToChange, final int timeout) {
        String title;
        for (int count = 1; count <= timeout; count++) {
            title = getTitle();
            if (TS.verify().notEquals(pageTitleToChange, title)) {
                break;
            }
            TS.util().pause("waitForTitleToChange from [" + pageTitleToChange + "] - current [" + title + "]", count);
        }
        return getSelf();
    }

    /**
     * Gets the java script value.
     *
     * @param javaScript the java script
     * @return the java script value
     */
    public String getJavaScriptValue(final String javaScript) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        return String.valueOf(js.executeScript(javaScript));
    }

    /**
     * Checks if is remote.
     *
     * @return true, if is remote
     */
    public boolean isRemote() {
        return remote;
    }

    /**
     * Take html snapshot.
     *
     * @return the string
     */
    public String takeHtmlSnapshot() {
        return takeHtmlSnapshot(TS.params().getOutput());
    }

    /**
     * Take html snapshot.
     *
     * @param path the path
     * @return the string
     */
    public String takeHtmlSnapshot(final String path) {
        try {
            File file = getScreenshotDir(path);
            if (file.isDirectory()) {
                file = File.createTempFile("htmlsnapshot_", ".html", file);
            }
            FileUtils.writeStringToFile(file, this.getHtml(), "UTF-8");
            TS.log().info("Html Snapshot file: " + file.getAbsolutePath());
            return getScreenshotPathToUse(file);
        } catch (final Exception e) {
            TS.log().error(e);
        }

        return null;
    }

    /**
     * Create parent directory for screenshots.
     *
     * @param directoryPath parent directory
     * @return parent directory of screenshot directories
     */
    public File getScreenshotDir(final String directoryPath) {
        final File screenshotDir = new File(directoryPath, "screenshots");
        TS.log().trace("screenshotDir.mkdir " + screenshotDir.mkdirs());
        return screenshotDir;
    }

    /**
     * Gets the html.
     *
     * @return the html
     */
    public String getHtml() {
        return driver.findElement(By.tagName("html")).getAttribute("outerHTML");
    }

    /**
     * Get the intended path to the screenshot path.
     *
     * @param screenshot file
     * @return new path to screenshot file
     */
    public String getScreenshotPathToUse(final File screenshot) {
        String replaceAbsolutePath = "";
        try {
            replaceAbsolutePath = screenshot.getParentFile().getParentFile().getAbsolutePath() + File.separator;
        } catch (final Exception e) {
            TS.log().trace("issue getting screenshot replace path");
        }
        return screenshot.getAbsolutePath().replace(replaceAbsolutePath, "");
    }

    /**
     * Take screen shot.
     *
     * @return the string
     */
    public String takeScreenShot() {
        return takeScreenShot(TS.params().getOutput());
    }

    /**
     * Take screen shot.
     *
     * @param path the path
     * @return the string
     */
    public String takeScreenShot(final String path) {
        try {
            final String version = driver.toString().toLowerCase();
            File file = getScreenshotDir(path);
            TS.log().trace("screenshotDir.mkdir " + file.mkdirs());
            if (file.isDirectory()) {
                file = File.createTempFile("screenshot_", ".png", file);
            }
            if (version.contains("remotewebdriver")) {
                final WebDriver augmentedDriver = new Augmenter().augment(driver);
                final File s = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(s, file);
                TS.log().info("Screenshot file: " + file.getAbsolutePath());
            } else {
                final File sf = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(sf, file);
                TS.log().info("Screenshot file: " + file.getAbsolutePath());
            }
            return getScreenshotPathToUse(file);
        } catch (final Exception e) {
            TS.log().error(e);
        }

        return null;
    }

    /**
     * Wait till element is present.
     *
     * @param by the by
     * @return true, if successful
     */
    public boolean waitTillElementIsPresent(final By by) {
        return waitTillElementIsPresent(by, elementWaitTime);
    }

    /**
     * Wait till element is present.
     *
     * @param by            the by
     * @param secondsToWait the seconds to wait
     * @return true, if successful
     */
    public boolean waitTillElementIsPresent(final By by, final int secondsToWait) {
        for (int counter = 0; counter < secondsToWait; counter++) {
            if (verifyElementIsPresent(by)) {
                return true;
            }
            TS.util().pause(1000L);
        }
        return false;
    }

    /**
     * Verify element is present.
     *
     * @param by the by
     * @return true, if successful
     */
    public boolean verifyElementIsPresent(final By by) {
        return TS.verify().isTrue("Element[ " + by + "] is expected to be present", isElementPresent(by));
    }

    /**
     * Checks if is element present.
     *
     * @param by the by
     * @return true, if is element present
     */
    private boolean isElementPresent(final By by) {
        try {
            if (null != getDriver().findElement(by)) {
                return true;
            }
        } catch (final Exception e) {
            TS.log().trace("Element is Not present " + by);
        }
        return false;
    }

    /**
     * Gets the driver.
     *
     * @return the driver
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Sets the driver.
     *
     * @param driver the new driver
     * @return the abstract browser
     */
    public AbstractBrowser<T> setDriver(final WebDriver driver) {
        this.driver = driver;
        return getSelf();
    }

    /**
     * Assert element is present.
     *
     * @param by the by
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertElementIsPresent(final By by) {
        TS.asserts().isTrue("Element[ " + by + "] is expected to be present", isElementPresent(by));
        return getSelf();
    }

    /**
     * Verify element is not present.
     *
     * @param by the by
     * @return true, if successful
     */
    public boolean verifyElementIsNotPresent(final By by) {
        return TS.verify().isFalse("Element[ " + by + "] is expected to Not be present", isElementPresent(by));
    }

    /**
     * Assert element is not present.
     *
     * @param by the by
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertElementIsNotPresent(final By by) {
        TS.asserts().isFalse("Element[ " + by + "] is expected to Not be present", isElementPresent(by));
        return getSelf();
    }

    /**
     * Verify title.
     *
     * @param expectedTitle the expected title
     * @return true, if successful
     */
    public boolean verifyTitle(final String expectedTitle) {
        return TS.verify().equalsTo("Verify Browser PageTitle", expectedTitle, getTitle());
    }

    /**
     * Verify url.
     *
     * @param expectedUrl the expected url
     * @return true, if successful
     */
    public boolean verifyUrl(final String expectedUrl) {
        return TS.verify().equalsTo("Verify Browser PageTitle", expectedUrl, getUrl());
    }

    /**
     * Wait for url to change.
     *
     * @param pageUrlToChange the page url to change
     * @param timeout         the timeout
     * @return the abstract browser
     */
    public AbstractBrowser<T> waitForUrlToChange(final String pageUrlToChange, final int timeout) {
        String url;
        for (int count = 1; count <= timeout; count++) {
            url = getUrl();
            if (TS.verify().notEquals(pageUrlToChange, url)) {
                break;
            }
            TS.util().pause("waitForUrlToChange from [" + pageUrlToChange + "] - current [" + url + "]", count);
        }

        return getSelf();
    }

    /**
     * Back.
     *
     * @return the abstract browser
     */
    public AbstractBrowser<T> back() {
        driver.navigate().back();
        return getSelf();
    }

    /**
     * Forward.
     *
     * @return the abstract browser
     */
    public AbstractBrowser<T> forward() {
        driver.navigate().forward();
        return getSelf();
    }

    /**
     * Wait till text is present.
     *
     * @param textExpected the text expected
     * @return true, if successful
     */
    public boolean waitTillTextIsPresent(final String textExpected) {
        return waitTillTextIsPresent(textExpected, elementWaitTime);
    }

    /**
     * Wait till text is present.
     *
     * @param textExpected  the text expected
     * @param secondsToWait the seconds to wait
     * @return true, if successful
     */
    public boolean waitTillTextIsPresent(final String textExpected, final int secondsToWait) {
        for (int counter = 0; counter < secondsToWait; counter++) {
            if (verifyTextIsPresent(textExpected)) {
                return true;
            }
            TS.util().pause(1000L);
        }
        return false;
    }

    /**
     * Verify text is present.
     *
     * @param textExpected the text expected
     * @return true, if successful
     */
    public boolean verifyTextIsPresent(final String textExpected) {
        return TS.verify().isTrue("Looking for Text[" + textExpected + "] on Page", isTextPresent(textExpected));
    }

    /**
     * Checks if is text present.
     *
     * @param textExpected the text expected
     * @return true, if is text present
     */
    private boolean isTextPresent(final String textExpected) {
        final String src = getText();
        if (null != src && src.contains(textExpected)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return getWebElement(By.tagName("body")).getText();
    }

    /**
     * Assert text is present.
     *
     * @param textExpected the text expected
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertTextIsPresent(final String textExpected) {
        TS.asserts().isTrue("Looking for Text[" + textExpected + "] on Page", isTextPresent(textExpected));
        return getSelf();
    }

    /**
     * Verify text is not present.
     *
     * @param textExpected the text expected
     * @return true, if successful
     */
    public boolean verifyTextIsNotPresent(final String textExpected) {
        return TS.verify().isFalse("Looking for Text[" + textExpected + "] To Not be on Page",
            isTextPresent(textExpected));
    }

    /**
     * Assert text is not present.
     *
     * @param textExpected the text expected
     * @return the abstract browser
     */
    public AbstractBrowser<T> assertTextIsNotPresent(final String textExpected) {
        TS.asserts().isFalse("Looking for Text[" + textExpected + "] To Not be on Page", isTextPresent(textExpected));
        return getSelf();
    }

    /**
     * Switch to window.
     *
     * @param pageTitle the page title
     * @return the abstract browser
     */
    public AbstractBrowser<T> switchToWindow(final String pageTitle) {
        return switchToWindow(pageTitle, true);
    }

    /**
     * Switch to window.
     *
     * @param pageTitle  the page title
     * @param autoReport the auto report
     * @return the abstract browser
     */
    public AbstractBrowser<T> switchToWindow(final String pageTitle, final boolean autoReport) {
        boolean rtn = false;
        final int waitForWindow = 10;
        for (int count = 0; count < waitForWindow; count++) {
            for (final String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                if (pageTitle.equalsIgnoreCase(driver.getTitle())) {
                    rtn = true;
                    break;
                }
            }
            TS.util().pause();
        }
        if (autoReport) {
            TS.asserts().isTrue("switchToWindow for pageTitle: " + pageTitle, rtn);
        }
        return getSelf();

    }

    /**
     * Scroll down.
     *
     * @param scrollDownBy the scroll down by
     * @return the abstract browser
     */
    public AbstractBrowser<T> scrollDown(final int scrollDownBy) {
        return scroll(0, scrollDownBy);

    }

    /**
     * Scroll.
     *
     * @param x the x
     * @param y the y
     * @return the abstract browser
     */
    public AbstractBrowser<T> scroll(final int x, final int y) {
        final String scroll = "window.scrollBy(" + x + "," + y + ")";
        TS.step().action().createInfo("Scrolling Browser", scroll);
        TS.browser().runJavaScript(scroll);
        return getSelf();
    }

    /**
     * Log browser info abstract browser.
     *
     * @return the abstract browser
     */
    public abstract AbstractBrowser<T> logBrowserInfo();


}
