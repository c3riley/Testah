package org.testah.driver.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.testah.TS;

import java.nio.charset.Charset;

public class HttpAuthUtil {

    public static final String ISO_ENCODING = "ISO-8859-1";
    public static final String UTF8_ENCODING = "UTF-8";
    public static final String HEADER_NAME = "Authorization";

    private String userName;
    private String password;
    private String encoding;
    private boolean useMask;

    public HttpAuthUtil() {

    }

    public Header createBasicAuthHeader() {
        try {
            if (useMask) {
                TS.addMask(getUserName());
                TS.addMask(getPassword());
            }
        } catch (Throwable issueAddingMatch) {
            TS.log().trace("Adding match", issueAddingMatch);
        }
        return new BasicHeader(HEADER_NAME, "Basic "
                + Base64.encodeBase64String((getUserName() + ":" + getPassword()).getBytes(Charset.forName(getEncoding()))));
    }

    public String getUserName() {
        return userName;
    }

    public HttpAuthUtil setUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public HttpAuthUtil setPassword(final String password) {
        this.password = password;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public HttpAuthUtil setEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
    }

    public HttpAuthUtil useEncodingUtf8() {
        this.encoding = UTF8_ENCODING;
        return this;
    }

    public HttpAuthUtil useEncodingIso() {
        this.encoding = ISO_ENCODING;
        return this;
    }

    public boolean isUseMask() {
        return useMask;
    }

    public HttpAuthUtil setUseMask(final boolean useMask) {
        this.useMask = useMask;
        return this;
    }
}
