package org.testah.client.enums;

import java.util.ArrayList;
import java.util.List;

public enum BrowserType {

    FIREFOX(""), CHROME(""), IE(""), PHANTOMJS("");

    String browserCode;

    BrowserType(final String browserCode) {
        this.browserCode = browserCode;
    }

    public static List<BrowserType> getBrowserTypes(final String[] browserValues) {
        if (null != browserValues) {
            final List<BrowserType> browserTypes = new ArrayList<BrowserType>();
            for (final String b : browserValues) {
                browserTypes.add(getBrowserType(b));
            }
            return browserTypes;
        }
        return null;
    }

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

    public String getBrowserCode() {
        return browserCode;
    }

}
