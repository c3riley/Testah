package org.testah.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.util.unittest.dtotest.SystemOutCapture;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class SshLoggerTest {

    SshLogger sshLogger = new SshLogger();

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
    public void log() throws UnsupportedEncodingException {
        try(SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            sshLogger.log(0, "Debug Message");
            assertEquals("DEBUG: Debug Message" + System.lineSeparator(), systemOutCapture.getSystemErr());

            sshLogger.log(1, "Info Message");
            assertEquals("INFO: Info Message" + System.lineSeparator(), systemOutCapture.getSystemErr());

            sshLogger.log(2, "Warn Message");
            assertEquals("WARN: Warn Message" + System.lineSeparator(), systemOutCapture.getSystemErr());

            sshLogger.log(3, "Error Message");
            assertEquals("ERROR: Error Message" + System.lineSeparator(), systemOutCapture.getSystemErr());

            sshLogger.log(4, "Fatal Message");
            assertEquals("FATAL: Fatal Message" + System.lineSeparator(), systemOutCapture.getSystemErr());
        }
    }
}