package org.testah.util.mail;

import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

/**
 * The type Exchange mail test.
 */
public class ExchangeMailTest extends ExchangeEmailUtil {

    /**
     * Instantiates a new Exchange mail test.
     */
    public ExchangeMailTest() {
        super("https://owa.testah.com/ews/exchange.asmx");
        setAuth();
        setExchangeVersion(ExchangeVersion.Exchange2010_SP1);
    }

}
