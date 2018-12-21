package org.testah.driver.web.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testah.TS;
import org.testah.driver.web.browser.AbstractBrowser;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AbstractWebElementWrapper is designed to wrap Webdriver WebElements and provide for more methods and chaining for less
 * code in tests.
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class AbstractWebElementWrapper {

    /**
     * The by.
     */
    private final By by;

    /**
     * The web element.
     */
    private WebElement webElement;

    /**
     * The auto report.
     */
    private boolean autoReport = true;

    /**
     * The timeout.
     */
    private int timeout = TS.params().getDefaultWaitTime();

    /**
     * The driver.
     */
    private AbstractBrowser<?> driver = null;

    /**
     * Instantiates a new abstract web element wrapper.
     *
     * @param by         the by
     * @param webElement the web element
     * @param driver     the driver
     */
    public AbstractWebElementWrapper(final By by, final WebElement webElement, final AbstractBrowser<?> driver) {
        this.by = by;
        this.webElement = webElement;
        this.driver = driver;
    }

    /**
     * Assert attribute exists.
     *
     * @param attributeName the attribute name
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertAttributeExists(final String attributeName) {
        TS.asserts().notNull("assertAttributeExists", getAttribute(attributeName));
        return getSelf();
    }

    /**
     * Gets the attribute.
     *
     * @param attributeName the attribute name
     * @return the attribute
     */
    public String getAttribute(final String attributeName) {
        if (elementIsOk("getAttribute", isAutoReport())) {
            final String v = webElement.getAttribute(attributeName);
            TS.step().action().createBrowserAction("Element getAttribute", attributeName + " = " + v);
            return v;
        }
        return null;
    }

    /**
     * Gets the self.
     *
     * @return the self
     */
    protected abstract AbstractWebElementWrapper getSelf();

    /**
     * Element is ok.
     *
     * @param activity   the activity
     * @param autoReport the auto report
     * @return true, if successful
     */
    public boolean elementIsOk(final String activity, final boolean autoReport) {
        if (null == getDriverWebElement()) {
            final String msg = "Unable to preform activity[" + activity + "], webelement[" + by +
                    "] is null and not available.";
            if (autoReport) {
                TS.asserts().notNull(msg, webElement);
            } else {
                TS.log().warn(msg);
            }
            return false;
        }
        TS.step().action().createInfo("Doing Activity: " + activity, "Element[" + getBy() + "]");
        return (null != getDriverWebElement());
    }

    /**
     * Checks if is auto report.
     *
     * @return true, if is auto report
     */
    public boolean isAutoReport() {
        return autoReport;
    }

    /**
     * Sets the auto report.
     *
     * @param autoReport the auto report
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper setAutoReport(final boolean autoReport) {
        this.autoReport = autoReport;
        return getSelf();
    }

    /**
     * Gets the driver web element.
     *
     * @return the driver web element
     */
    public WebElement getDriverWebElement() {
        try {
            if (null != webElement) {
                webElement.isDisplayed(); // check is still working
            }
        } catch (final Throwable e) {
            webElement = null;
        }
        return webElement;
    }

    /**
     * Gets the by.
     *
     * @return the by
     */
    public By getBy() {
        return by;
    }

    /**
     * Assert is enabled.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertIsEnabled() {
        TS.asserts().isTrue("Expecting Element is Enabled", isEnabled());
        return getSelf();
    }

    /**
     * Checks if is enabled.
     *
     * @return the boolean
     */
    public Boolean isEnabled() {
        if (elementIsOk("isEnabled", isAutoReport())) {
            return webElement.isEnabled();
        }
        return false;
    }

    /**
     * Assert is not enabled.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertIsNotEnabled() {
        TS.asserts().isFalse("Expecting Element is Not Enabled", isEnabled());
        return getSelf();
    }

    /**
     * Assert is not selected.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertIsNotSelected() {
        TS.asserts().isFalse("Expecting Element is Not Selected", isSelected());
        return getSelf();
    }

    /**
     * Checks if is selected.
     *
     * @return the boolean
     */
    public Boolean isSelected() {
        if (elementIsOk("isSelected", isAutoReport())) {
            return webElement.isSelected();
        }
        return false;
    }

    /**
     * Assert is selected.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertIsSelected() {
        TS.asserts().isTrue("Expecting Element is Selected", isSelected());
        return getSelf();
    }

    /**
     * Assert is displayed.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertIsDisplayed() {
        TS.asserts().isTrue("Expecting Element is Displayed", isDisplayed());
        return getSelf();
    }

    /**
     * Checks if is displayed.
     *
     * @return the boolean
     */
    public boolean isDisplayed() {
        return isDisplayed(false);
    }

    /**
     * Checks if is displayed.
     *
     * @param autoReport the auto report
     * @return true, if is displayed
     */
    public boolean isDisplayed(final boolean autoReport) {
        if (null == webElement) {
            TS.step().action().createInfo("Element is not Displayed as it is not found, element is null");
            return false;
        } else {
            return webElement.isDisplayed();
        }

    }

    /**
     * Verify is displayed.
     *
     * @return true, if successful
     */
    public boolean verifyIsDisplayed() {
        return TS.verify().isTrue("Expecting Element is Displayed", isDisplayed());
    }

    /**
     * Assert not found.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertNotFound() {
        TS.asserts().isNull("assertNotFound WebElement[" + by + "]", getDriverWebElement());
        return getSelf();
    }

    /**
     * Assert type text.
     *
     * @param value the value
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertTypeText(final String value) {
        if (elementIsOk("typeText", isAutoReport())) {
            webElement.clear();
            webElement.sendKeys(value);
            assertAttributeValue("value", value);
        }
        return getSelf();
    }

    /**
     * Assert attribute value.
     *
     * @param attributeName          the attribute name
     * @param attributeExpectedValue the attribute expected value
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertAttributeValue(final String attributeName,
                                                          final String attributeExpectedValue) {
        TS.asserts().equalsTo("assertAttributeValue", attributeExpectedValue, getAttribute(attributeName));
        return getSelf();
    }

    /**
     * Clear text.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper clearText() {
        if (elementIsOk("clear", isAutoReport())) {
            webElement.clear();
            this.assertAttributeValue("value", "");
        }
        return getSelf();
    }

    /**
     * Click.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper click() {
        waitTillIsDisplayed();
        if (elementIsOk("click", isAutoReport())) {
            TS.step().action().createBrowserAction("Element Click", by);
            webElement.click();
        }
        return getSelf();
    }

    /**
     * Wait till is displayed.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillIsDisplayed() {
        return waitTillIsDisplayed(timeout);
    }

    /**
     * Wait till is displayed.
     *
     * @param timeout the timeout
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillIsDisplayed(final int timeout) {
        try {
            for (int count = 1; count <= timeout; count++) {
                if (null == getDriverWebElement()) {
                    resetElement();
                }
                if (null != getDriverWebElement() && isDisplayed(false)) {
                    break;
                }
                TS.util().pause("waitTillIsDisplayed", count);
            }
        } catch (final Exception e) {
            TS.log().debug(e);
        }
        TS.asserts().isTrue("Expected Element[" + by + "] to be displayed", isDisplayed(false));
        return getSelf();
    }

    /**
     * Reset element.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper resetElement() {
        try {
            this.webElement = driver.getWebElementNoWait(getBy()).getDriverWebElement();
        } catch (Throwable t) {
            TS.log().warn("Unable to find element[" + by + "] to reset");
        }
        return getSelf();
    }

    /**
     * Drag to and drop on.
     *
     * @param by the by
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper dragToAndDropOn(final By by) {
        return dragToAndDropOn(TS.browser().getWebElement(by));
    }

    /**
     * Drag to and drop on.
     *
     * @param elementToDropOn the element to drop on
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper dragToAndDropOn(final AbstractWebElementWrapper elementToDropOn) {
        try {
            getActionBuilder().dragAndDrop(this.getDriverWebElement(), elementToDropOn.getDriverWebElement()).build().perform();
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with getActionBuilder for: " + by, e);
        }
        return getSelf();
    }

    /**
     * Gets the action builder.
     *
     * @return the action builder
     */
    public Actions getActionBuilder() {
        try {
            return new Actions(driver.getDriver());
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with getActionBuilder for: " + by, e);
        }
        return null;
    }

    /**
     * Drag to and drop by.
     *
     * @param xoffset the x offset
     * @param yoffset the y offset
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper dragToAndDropBy(final int xoffset, final int yoffset) {
        try {
            getActionBuilder().dragAndDropBy(this.getDriverWebElement(), xoffset, yoffset);
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with getActionBuilder for: " + by, e);
        }
        return getSelf();
    }

    /**
     * Move to.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper moveTo() {
        return moveTo(this);
    }

    /**
     * Move to.
     *
     * @param elementToMoveTo the element to move to
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper moveTo(final AbstractWebElementWrapper elementToMoveTo) {
        if (elementIsOk("moveTo", isAutoReport())) {
            getActionBuilder().moveToElement(elementToMoveTo.getDriverWebElement()).build().perform();
            return getSelf();
        }
        return null;
    }

    /**
     * Move to.
     *
     * @param elementToMoveTo the element to move to
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper moveTo(final By elementToMoveTo) {
        return moveTo(driver.getWebElement(elementToMoveTo));
    }

    /**
     * Gets the as select list.
     *
     * @return the as select list
     */
    public Select getAsSelectList() {
        if (elementIsOk("getAsSelectList", isAutoReport())) {
            return new Select(webElement);
        }
        return null;
    }

    /**
     * Gets the elements with in.
     *
     * @param locator the locator
     * @return the elements with in
     */
    public List<AbstractWebElementWrapper> getElementsWithIn(final By locator) {
        return getElementsWithIn(locator, false, true);
    }

    /**
     * Gets the elements with in.
     *
     * @param locator    the locator
     * @param noWait     the no wait
     * @param autoAssert the auto assert
     * @return the elements with in
     */
    public List<AbstractWebElementWrapper> getElementsWithIn(final By locator, final boolean noWait,
                                                             final boolean autoAssert) {
        assertFound();
        String error = "";
        for (int count = 1; count <= timeout; count++) {
            error = "";
            try {
                return getListOfWebelementsWrapped(by, webElement.findElements(locator));
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getElementsWithIn", count);
        }
        if (autoAssert) {
            TS.asserts().equalsTo("Expected to find WebElements within Element[" + this.by +
                    "] using By[" + locator + "] - error: " + error, true, false);
        }
        return new ArrayList<>();
    }

    /**
     * Assert found.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper assertFound() {
        elementIsOk("assertFound", true);
        return getSelf();
    }

    /**
     * Gets the list of webelements wrapped.
     *
     * @param locator     the locator
     * @param webElements the web elements
     * @return the list of webelements wrapped
     */
    public List<AbstractWebElementWrapper> getListOfWebelementsWrapped(final By locator,
                                                                       final List<WebElement> webElements) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<>();
        if (null != webElements) {
            for (final WebElement e : webElements) {
                lst.add(new WebElementWrapperV1(locator, e, driver));
            }
        }
        return lst;
    }

    /**
     * Gets the elements with in no wait.
     *
     * @param locator the locator
     * @return the elements with in no wait
     */
    public List<AbstractWebElementWrapper> getElementsWithInNoWait(final By locator) {
        return getElementsWithIn(locator, true, true);
    }

    /**
     * Gets the element with in.
     *
     * @param locator the locator
     * @return the element with in
     */
    public AbstractWebElementWrapper getElementWithIn(final By locator) {
        return getElementWithIn(locator, false, true);
    }

    /**
     * Gets the element with in.
     *
     * @param locator    the locator
     * @param noWait     the no wait
     * @param autoAssert the auto assert
     * @return the element with in
     */
    public AbstractWebElementWrapper getElementWithIn(final By locator, final boolean noWait,
                                                      final boolean autoAssert) {
        assertFound();
        String error = "";
        for (int count = 1; count <= timeout; count++) {
            error = "";
            try {
                return new WebElementWrapperV1(locator, webElement.findElement(locator), driver);
            } catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getElementWithIn", count);
        }
        if (autoAssert) {
            TS.asserts().equalsTo("Expected to find WebElements within Element[" + this.by +
                    "] using By[" + locator + "] - error: " + error, true, false);
        }
        return null;
    }

    /**
     * Gets the element with in no wait.
     *
     * @param locator the locator
     * @return the element with in no wait
     */
    public AbstractWebElementWrapper getElementWithInNoWait(final By locator) {
        return getElementWithIn(locator, true, true);
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public List<AbstractWebElementWrapper> getOptions() {
        return getElementsWithIn(new By.ByTagName("option"), false, true);
    }

    /**
     * Gets the html.
     *
     * @return the html
     */
    public String getHtml() {
        return getOuterHtml();
    }

    /**
     * Gets the outer html.
     *
     * @return the outer html
     */
    public String getOuterHtml() {
        return getAttribute("outerHTML");
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        String rtn = null;
        if (elementIsOk("getText", isAutoReport())) {
            if (webElement.getTagName().equalsIgnoreCase("input") ||
                    webElement.getTagName().equalsIgnoreCase("textarea")) {
                rtn = webElement.getAttribute("value");
            } else {
                rtn = webElement.getText();
            }
        }
        return rtn;
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout the timeout
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper setTimeout(final int timeout) {
        this.timeout = timeout;
        return getSelf();
    }

    /**
     * Mouse over.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper mouseOver() {
        try {
            if (elementIsOk("mouseOver", isAutoReport())) {
                final Actions builder = new Actions(driver.getDriver());
                builder.moveToElement(webElement).build().perform();
            }
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with mouseOver for: " + by, e);
        }
        return getSelf();
    }

    /**
     * Mouse over and click.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper mouseOverAndClick() {
        try {
            if (elementIsOk("mouseOver", isAutoReport())) {
                final Actions builder = new Actions(driver.getDriver());
                builder.moveToElement(webElement).click().build().perform();
            }
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with mouseOver for: " + by, e);
        }
        return getSelf();
    }

    /**
     * Mouse over and click.
     *
     * @param elementToClick the element to click
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper mouseOverAndClick(final By elementToClick) {
        try {
            final AbstractWebElementWrapper clickElement = driver.getWebElement(elementToClick);
            if (clickElement.elementIsOk("mouseOverAndClick", true)) {
                if (elementIsOk("mouseOver", isAutoReport())) {
                    final Actions builder = new Actions(driver.getDriver());
                    builder.moveToElement(webElement).click(clickElement.getDriverWebElement()).build().perform();
                }
            }
        } catch (final Exception e) {
            TS.asserts().unExpectedException("Issue Occurred with mouseOver for: " + by, e);
        }
        return getSelf();
    }

    /**
     * Pause.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper pause() {
        return pause(1000L);
    }

    /**
     * Pause.
     *
     * @param milliseconds the milliseconds
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper pause(final Long milliseconds) {
        TS.util().pause("Element pause between actions", milliseconds);
        return getSelf();
    }

    /**
     * Type text.
     *
     * @param value the value
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper typeText(final String value) {
        if (elementIsOk("typeText", isAutoReport())) {
            TS.step().action().createInfo("typeText", "By[" + by + "] Type Text: " + value);
            webElement.sendKeys(value);
        }
        return getSelf();
    }

    /**
     * Verfify elements with in.
     *
     * @param locator the locator
     * @return true, if successful
     */
    public boolean verfifyElementsWithIn(final By locator) {
        return (getElementsWithIn(locator, true, false).size() > 0);
    }

    /**
     * Verify element with in.
     *
     * @param locator the locator
     * @return true, if successful
     */
    public boolean verifyElementWithIn(final By locator) {
        return (null != getElementWithIn(locator, false, false));
    }

    /**
     * Verify found.
     *
     * @return true, if successful
     */
    public boolean verifyFound() {
        return elementIsOk("assertFound", false);
    }

    /**
     * Verify notfound.
     *
     * @return true, if successful
     */
    public boolean verifyNotfound() {
        return TS.verify().isNull("verifyNotfound WebElement[" + by + "]", getDriverWebElement());
    }

    /**
     * Wait till attribute equals.
     *
     * @param attributeName the attribute name
     * @param value         the value
     * @param timeout       the timeout
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillAttributeEquals(final String attributeName, final String value,
                                                             final int timeout) {
        for (int count = 1; count <= timeout; count++) {
            if (verifyAttributeValue(attributeName, value)) {
                break;
            }
            TS.util().pause("waitTillAttributeEquals", count);
        }
        return getSelf();
    }

    /**
     * Verifyt attribute value.
     *
     * @param attributeName          the attribute name
     * @param attributeExpectedValue the attribute expected value
     * @return true, if successful
     */
    public boolean verifyAttributeValue(final String attributeName, final String attributeExpectedValue) {
        return TS.verify().equalsTo("verifyAttributeValue", attributeExpectedValue, getAttribute(attributeName));
    }

    /**
     * Wait till gone will wait up to the timeout, default value pulled from Testah properties, for the element to no longer be found or be
     * displayed. Types of usecases where this would be called is if clicking a close button, and wait for it to go away before moving forward.
     *
     * <p>Once timeout is exceeded or the element is not found or not displated an assert will be triggered since this methods assumes you want it
     * to no longer to be found.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillGone() {
        return waitTillGone(timeout);
    }

    /**
     * Wait till gone will wait up to the timeout for the element to no longer be found or be displayed. Types of usecases where this would be
     * called is if clicking a close button, and wait for it to go away before moving forward.
     *
     * <p>Once timeout is exceeded or the element is not found or not displated an assert will be triggered since this methods assumes you want it
     * to no longer to be found.
     *
     * @param timeout the timeout
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillGone(final int timeout) {
        try {
            for (int count = 1; count <= timeout; count++) {
                if (null == getDriverWebElement() || !isDisplayed(false)) {
                    break;
                }
                TS.util().pause("waitTillGone", count);
            }
        } catch (final Exception e) {
            TS.log().debug(e);
        }
        TS.asserts().isNull("Expected Element[" + by + "] to be gone or not displayed", getDriverWebElement());
        return getSelf();
    }

    /**
     * Wait till not displayed.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillNotDisplayed() {
        return waitTillNotDisplayed(timeout);
    }

    /**
     * Wait till not displayed.
     *
     * @param timeout the timeout
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper waitTillNotDisplayed(final int timeout) {
        try {
            for (int count = 1; count <= timeout; count++) {
                if (null == getDriverWebElement()) {
                    resetElement();
                }
                if (null != getDriverWebElement() && !isDisplayed(false)) {
                    break;
                }
                TS.util().pause("waitTillNotDisplayed", count);
            }
        } catch (final Exception e) {
            TS.log().debug(e);
        }
        TS.asserts().isFalse("Expected Element[" + by + "] to Not be displayed", isDisplayed(false));
        return getSelf();
    }

    /**
     * Scroll into view.
     *
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper scrollIntoView() {
        return scrollIntoView(100, 10);
    }

    /**
     * Scroll into view.
     *
     * @param amountToScrollDownBy the amount to scroll down by
     * @param numberOfIterations   the number of iterations
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper scrollIntoView(final int amountToScrollDownBy, final int numberOfIterations) {
        for (int count = 0; count < 10; count++) {
            try {
                if (isDisplayed(false)) {
                    return getSelf();
                }
            } catch (Exception e) {
                TS.log().debug("Had issue going to scroll. Attempt: " + count);
            }
            TS.browser().scrollDown(100);
        }
        return getSelf();
    }

}
