package org.testah.driver.web.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testah.TS;

public abstract class AbstractWebElementWrapper {

    private final By   by;
    private WebElement webElement;
    private boolean    autoReport = true;
    private int        timeout    = TS.params().getDefaultWaitTime();
    
    public AbstractWebElementWrapper(final By by, final WebElement webElement) {
        this.by = by;
        this.webElement = webElement;
    }
    
    public AbstractWebElementWrapper assertFound() {
        elementIsOk("assertFound", true);
        return this;
    }

    public boolean verifyFound() {
        return elementIsOk("assertFound", false);
    }
    
    public AbstractWebElementWrapper assertNotFound() {
        TS.asserts().isNull("assertNotfound WebElement[" + by + "]", getDriverWebElement());
        return this;
    }
    
    public boolean verifyNotfound() {
        return TS.verify().isNull("verifyNotfound WebElement[" + by + "]", getDriverWebElement());
    }
    
    public AbstractWebElementWrapper click() {
        if (elementIsOk("click", isAutoReport())) {
            webElement.click();
        }
        return this;
    }

    public String getAttribute(final String attributeName) {
        if (elementIsOk("getAttribute", isAutoReport())) {
            return webElement.getAttribute(attributeName);
        }
        return null;
    }

    public AbstractWebElementWrapper assertAttributeValue(final String attributeName,
            final String attributeExpectedValue) {
        String value = null;
        if (elementIsOk("getAttribute", isAutoReport())) {
            value = webElement.getAttribute(attributeName);
        }
        TS.asserts().equals("assertAttributeValue", attributeExpectedValue, value);
        return this;
    }

    public boolean verifytAttributeValue(final String attributeName, final String attributeExpectedValue) {
        String value = null;
        if (elementIsOk("getAttribute", isAutoReport())) {
            value = webElement.getAttribute(attributeName);
        }
        return TS.verify().equals("verifytAttributeValue", attributeExpectedValue, value);
    }
    
    public AbstractWebElementWrapper assertAttributeExists(final String attributeName) {
        String value = null;
        if (elementIsOk("getAttribute", isAutoReport())) {
            value = webElement.getAttribute(attributeName);
        }
        TS.asserts().notNull("assertAttributeExists", value);
        return this;
    }

    public boolean verfifyElementsWithIn(final By locator) {
        return (getElementsWithIn(by, true, false).size() > 0);
    }
    
    public List<AbstractWebElementWrapper> getElementsWithIn(final By locator) {
        return getElementsWithIn(by, false, true);
    }
    
    public List<AbstractWebElementWrapper> getElementsWithInNoWait(final By locator) {
        return getElementsWithIn(by, true, true);
    }
    
    public List<AbstractWebElementWrapper> getElementsWithIn(final By locator, final boolean noWait,
            final boolean autoAssert) {
        assertFound();
        String error = "";
        for (int i = 1; i <= timeout; i++) {
            error = "";
            try {
                return getListOfWebelementsWrapped(by, webElement.findElements(by));
            }
            catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getElementsWithIn", i);
        }
        if (autoAssert) {
            TS.asserts().equals("Expected to find WebElements within Element[" + this.by + "] uisng By[" + by
                    + "] - error: " + error, true, false);
        }
        return new ArrayList<AbstractWebElementWrapper>();
    }

    public boolean verifyElementWithIn(final By locator) {
        return (null != getElementWithIn(by, false, false));
    }
    
    public AbstractWebElementWrapper getElementWithIn(final By locator) {
        return getElementWithIn(by, false, true);
    }
    
    public AbstractWebElementWrapper getElementWithInNoWait(final By locator) {
        return getElementWithIn(by, true, true);
    }
    
    public AbstractWebElementWrapper getElementWithIn(final By locator, final boolean noWait,
            final boolean autoAssert) {
        assertFound();
        String error = "";
        for (int i = 1; i <= timeout; i++) {
            error = "";
            try {
                return new WebElementWrapperV1(by, webElement.findElement(by));
            }
            catch (final Exception e) {
                error = e.getMessage();
            }
            if (noWait) {
                break;
            }
            TS.util().pause("getElementWithIn", i);
        }
        if (autoAssert) {
            TS.asserts().equals("Expected to find WebElements within Element[" + this.by + "] uisng By[" + by
                    + "] - error: " + error, true, false);
        }
        return null;
    }

    public List<AbstractWebElementWrapper> getListOfWebelementsWrapped(final By by,
            final List<WebElement> webElements) {
        final List<AbstractWebElementWrapper> lst = new ArrayList<AbstractWebElementWrapper>();
        if (null != webElements) {
            for (final WebElement e : webElements) {
                lst.add(new WebElementWrapperV1(by, e));
            }
        }
        return lst;
    }

    public AbstractWebElementWrapper typeText(final String value) {
        if (elementIsOk("typeText", isAutoReport())) {
            webElement.sendKeys(value);
        }
        return this;
    }
    
    public AbstractWebElementWrapper assertTypeText(final String value) {
        if (elementIsOk("typeText", isAutoReport())) {
            webElement.sendKeys(value);
            assertAttributeValue("value", value);
        }
        return this;
    }

    public AbstractWebElementWrapper waitTillAttributeEquals(final String attributeName, final String value,
            final int timeout) {
        for (int i = 1; i <= timeout; i++) {
            if (verifytAttributeValue(attributeName, value)) {
                break;
            }
            TS.util().pause("waitTillAttributeEquals", i);
        }
        return this;
    }
    
    public AbstractWebElementWrapper waitTillGone(final int timeout) {
        for (int i = 1; i <= timeout; i++) {
            if (null == getDriverWebElement()) {
                break;
            }
            TS.util().pause("waitTillAttributeEquals", i);
        }

        return this;
    }

    public WebElement getDriverWebElement() {
        try {
            webElement.isDisplayed(); // check is still working
        }
        catch (final Exception e) {
            webElement = null;
        }
        return webElement;
    }
    
    public boolean elementIsOk(final String activity, final boolean autoReport) {
        if (null == getDriverWebElement()) {
            final String msg = "Unable to preform activity[" + activity + "], webelement[" + by
                    + "] is null and not availible.";
            if (autoReport) {
                TS.asserts().notNull(msg, webElement);
            }
            else {
                TS.log().warn(msg);
            }
            return false;
        }
        return (null != getDriverWebElement());
    }
    
    public By getBy() {
        return by;
    }
    
    public boolean isAutoReport() {
        return autoReport;
    }
    
    public AbstractWebElementWrapper setAutoReport(final boolean autoReport) {
        this.autoReport = autoReport;
        return this;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public AbstractWebElementWrapper setTimeout(final int timeout) {
        this.timeout = timeout;
        return this;
    }

}
