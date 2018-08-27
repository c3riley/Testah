package org.testah.util.mail;

import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

public class ExchangeMailTest extends ExchangeEmailUtil {

    public ExchangeMailTest() {
        super("https://owa.testah.com/ews/exchange.asmx");
        setAuth();
        setExchangeVersion(ExchangeVersion.Exchange2010_SP1);
    }

}
