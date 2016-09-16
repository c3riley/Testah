package org.testah.driver.web.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testah.driver.web.browser.AbstractBrowser;

/**
 * The Class WebElementWrapperV1.
 */
public class WebElementWrapperV1 extends AbstractWebElementWrapper {

    /**
     * Instantiates a new web element wrapper v1.
     *
     * @param by
     *            the by
     * @param webElement
     *            the web element
     * @param driver
     *            the driver
     */
    public WebElementWrapperV1(final By by, final WebElement webElement, final AbstractBrowser<?> driver) {
        super(by, webElement, driver);
    }

    protected AbstractWebElementWrapper getSelf() {
        return this;
    }

}
