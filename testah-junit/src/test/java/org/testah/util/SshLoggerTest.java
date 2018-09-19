package org.testah.util;

import com.jcraft.jsch.Logger;
import org.apache.http.util.Asserts;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class SshLoggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    SshLogger sshLogger = new SshLogger();

    @Before
    public void setUp() throws Exception {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void logLevels() {
        Assert.assertSame(SshLogger.logLevels.get(0), "DEBUG: ");
        Assert.assertSame(SshLogger.logLevels.get(1), "INFO: ");
        Assert.assertSame(SshLogger.logLevels.get(2), "WARN: ");
        Assert.assertSame(SshLogger.logLevels.get(3), "ERROR: ");
        Assert.assertSame(SshLogger.logLevels.get(4), "FATAL: ");
    }

    @Test
    public void isEnabled() {
        Assert.assertTrue(sshLogger.isEnabled(0));
        Assert.assertTrue(sshLogger.isEnabled(1));
        Assert.assertTrue(sshLogger.isEnabled(2));
        Assert.assertTrue(sshLogger.isEnabled(3));
        Assert.assertTrue(sshLogger.isEnabled(4));
        Assert.assertFalse(sshLogger.isEnabled(99));
    }

    @Test
    public void log() {
        sshLogger.log(0,"Debug Message");
        assertEquals("DEBUG: Debug Message\r\n", errContent.toString());
        errContent.reset();

        sshLogger.log(1,"Info Message");
        assertEquals("INFO: Info Message\r\n", errContent.toString());
        errContent.reset();

        sshLogger.log(2,"Warn Message");
        assertEquals("WARN: Warn Message\r\n", errContent.toString());
        errContent.reset();

        sshLogger.log(3,"Error Message");
        assertEquals("ERROR: Error Message\r\n", errContent.toString());
        errContent.reset();

        sshLogger.log(4,"Fatal Message");
        assertEquals("FATAL: Fatal Message\r\n", errContent.toString());
        errContent.reset();
    }
}