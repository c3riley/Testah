package org.testah.client.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * The Enum BrowserType.
 */
public enum BrowserType {

    /** The firefox. */
    FIREFOX_GECKO(""),
    /** The chrome. */
    FIREFOX(""),
    /** The chrome. */
    CHROME(""),
    /** The ie. */
    IE(""),
    /** The phantomjs. */
    PHANTOMJS(""), JBROWSER("");

    /** The browser code. */
    String browserCode;

    /**
     * Instantiates a new browser type.
     *
     * @param browserCode
     *            the browser code
     */
    BrowserType(final String browserCode) {
        this.browserCode = browserCode;
    }

    /**
     * Gets the browser types.
     *
     * @param browserValues
     *            the browser values
     * @return the browser types
     */
    public static List<BrowserType> getBrowserTypes(final String[] browserValues) {
        if (null != browserValues) {
            final List<BrowserType> browserTypes = new ArrayList<>();
            for (final String b : browserValues) {
                browserTypes.add(getBrowserType(b));
            }
            return browserTypes;
        }
        return null;
    }

    /**
     * Gets the browser type.
     *
     * @param browserValue
     *            the browser value
     * @return the browser type
     */
    public static BrowserType getBrowserType(final String browserValue) {
        if (null != browserValue) {
            for (final BrowserType b : BrowserType.values()) {
                if (browserValue.equalsIgnoreCase(b.getBrowserCode())) {
                    return b;
                }
            }
        }
        return BrowserType.FIREFOX;
    }

    /**
     * Gets the browser code.
     *
     * @return the browser code
     */
    public String getBrowserCode() {
        return browserCode;
    }

}
