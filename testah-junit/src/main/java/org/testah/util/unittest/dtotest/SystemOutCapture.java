package org.testah.util.unittest.dtotest;

import org.junit.After;
import org.junit.Before;
import org.testah.TS;
import org.testah.util.Log;
import org.testah.util.SshLogger;

import java.io.*;

/**
 * The type System out capture.
 */
public class SystemOutCapture implements Closeable {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private String encoding = "UTF8";
    private boolean started = false;

    /**
     * Start system out capture.
     *
     * @return the system out capture
     */
    public SystemOutCapture start() {
        if (started) {
            TS.log().info("SystemOutCapture start");
            return this;
        }
        try {
            originalOut = System.out;
            originalErr = System.err;
            outContent = new ByteArrayOutputStream();
            errContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent, true, encoding));
            System.setErr(new PrintStream(errContent, true, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Issue trying to start SystemOutCapture", e);
        }
        started = true;
        return this;
    }

    @Override
    public void close() {
        if (!started) {
            return;
        }
        try {
            System.setOut(originalOut);
            System.setErr(originalErr);
            Log.resetLogger();
        } finally {
            outContent = null;
            errContent = null;
            started = false;
        }
    }

    /**
     * Gets system err.
     *
     * @return the system err
     */
    public String getSystemErr() {
        return getSystemErr(true);
    }

    /**
     * Gets system err.
     *
     * @param clearAfterReturningContent the clear after returning content
     * @return the system err
     */
    public String getSystemErr(final boolean clearAfterReturningContent) {
        return getContent(errContent, clearAfterReturningContent);
    }

    /**
     * Gets system out.
     *
     * @return the system out
     */
    public String getSystemOut() {
        return getSystemOut(true);
    }

    /**
     * Gets system out.
     *
     * @param clearAfterReturningContent the clear after returning content
     * @return the system out
     */
    public String getSystemOut(final boolean clearAfterReturningContent) {
        return getContent(outContent, clearAfterReturningContent);
    }

    /**
     * Clear system out system out capture.
     *
     * @return the system out capture
     */
    public SystemOutCapture clearSystemOut() {
        outContent.reset();
        return this;
    }

    /**
     * Clear system err system out capture.
     *
     * @return the system out capture
     */
    public SystemOutCapture clearSystemErr() {
        errContent.reset();
        return this;
    }

    private String getContent(final ByteArrayOutputStream byteArrayOutputStream, final boolean clearAfterReturningContent) {
        try {
            return byteArrayOutputStream.toString(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Issue trying to getSystemErr with encoding: " + encoding, e);
        } finally {
            if (clearAfterReturningContent) {
                byteArrayOutputStream.reset();
            }
        }
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
    public SystemOutCapture setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
}
