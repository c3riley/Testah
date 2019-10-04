package org.testah.driver.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.testah.TS;

import java.nio.charset.Charset;

/**
 * The type Http auth util. Util to generate basic auth and bearer
 * header for use in requests on http driver
 */
public class HttpAuthUtil {

    /**
     * The constant ISO_ENCODING.
     */
    public static final String ISO_ENCODING = "ISO-8859-1";
    /**
     * The constant UTF8_ENCODING.
     */
    public static final String UTF8_ENCODING = "UTF-8";
    /**
     * The constant HEADER_NAME.
     */
    public static final String HEADER_NAME = "Authorization";

    private String userName;
    private String password;
    private String encoding;
    private boolean useMask;

    /**
     * Instantiates a new Http auth util.
     */
    public HttpAuthUtil() {

    }

    /**
     * Create bearer auth header header.
     *
     * @param token the token
     * @return the header
     */
    public Header createBearerAuthHeader(final String token) {
        return new BasicHeader(HEADER_NAME, "Bearer " + token);
    }

    /**
     * Create basic auth header header.
     *
     * @return the header
     */
    public Header createBasicAuthHeader() {
        final String basicAuthValue = Base64.encodeBase64String((getUserName() + ":" +
            getPassword()).getBytes(Charset.forName(getEncoding())));
        try {
            if (useMask) {
                TS.addMaskForce(getUserName());
                TS.addMaskForce(getPassword());
                TS.addMaskForce(basicAuthValue);
            }
        } catch (Throwable issueAddingMatch) {
            TS.log().trace("Adding match", issueAddingMatch);
        }
        return new BasicHeader(HEADER_NAME, "Basic " + basicAuthValue);
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     * @return the user name
     */
    public HttpAuthUtil setUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     * @return the password
     */
    public HttpAuthUtil setPassword(final String password) {
        this.password = password;
        return this;
    }

    /**
     * Gets encoding.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets encoding.
     *
     * @param encoding the encoding
     * @return the encoding
     */
    public HttpAuthUtil setEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Use encoding utf 8 http auth util.
     *
     * @return the http auth util
     */
    public HttpAuthUtil useEncodingUtf8() {
        this.encoding = UTF8_ENCODING;
        return this;
    }

    /**
     * Use encoding iso http auth util.
     *
     * @return the http auth util
     */
    public HttpAuthUtil useEncodingIso() {
        this.encoding = ISO_ENCODING;
        return this;
    }

    /**
     * Is use mask boolean.
     *
     * @return the boolean
     */
    public boolean isUseMask() {
        return useMask;
    }

    /**
     * Sets use mask.
     *
     * @param useMask the use mask
     * @return the use mask
     */
    public HttpAuthUtil setUseMask(final boolean useMask) {
        this.useMask = useMask;
        return this;
    }

}
