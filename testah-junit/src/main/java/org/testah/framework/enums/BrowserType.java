package org.testah.framework.enums;

import java.util.ArrayList;
import java.util.List;

import org.testah.TS;

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
		TS.log().warn("Browsr Value did not match defaulting to firefox");
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
		TS.log().warn("Browsr Value did not match defaulting to firefox");
		return BrowserType.FIREFOX;
	}

	public String getBrowserCode() {
		return browserCode;
	}

}
