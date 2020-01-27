package org.testah;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.framework.cli.Params;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.AssertStrings;
import org.testah.util.StringMasking;
import org.testah.util.StringMaskingConfig;
import org.testah.util.unittest.dtotest.SystemOutCapture;

class TestTS
{
    String message;
    String log;

    @BeforeEach
    void setup()
    {
        StringMaskingConfig.INSTANCE.createInstance(6, 2, 2);
        message = TS.util().getResourceAsString("/util/message.txt");
        TS.addMaskBulk("fairest", "creatures", "decease");
        TS.params().setLevel(Level.INFO);
    }

    @Test
    void testSanitizeMessageContainsPass()
    {
        System.out.println(">>>>> " + TS.log().getLevel());
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            TS.asserts().contains("Check logging.", message, "we desire increase");
            log = systemOutCapture.getSystemOut();
        }
        validateMasking(log);
    }

    @Test
    void testSanitizeMessageContainsFail()
    {
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            Assertions.assertThrows(AssertionError.class, () -> {
                TS.asserts().contains("Check logging.", message, "string not there");
            });
            log = systemOutCapture.getSystemOut();
        }
        validateMasking(log);
    }

    @Test
    void testSanitizeMessageStartsWith()
    {
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            TS.asserts().startsWith("message", message, "From");
            log = systemOutCapture.getSystemOut();
        }
        validateMasking(log);
    }

    @Test
    void testSanitizeMessageEndsWith()
    {
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
            TS.asserts().startsWith("message", message, "From");
            log = systemOutCapture.getSystemOut();
        }
        validateMasking(log);
    }

    private void validateMasking(String log)
    {
        TS.log().info(log);
        TS.asserts().contains("Check log for expected string.", log, "we desire increase");
        TS.asserts().contains("Check log for expected string.", log, "fa***st");
        TS.asserts().contains("Check log for expected string.", log, "de***se");
        TS.asserts().contains("Check log for expected string.", log, "cr***es");
        Assert.assertTrue(new AssertStrings(log, new VerboseAsserts().onlyVerify()).notContains("fairest").isPassed());
        Assert.assertTrue(new AssertStrings(log, new VerboseAsserts().onlyVerify()).notContains("creatures").isPassed());
        Assert.assertTrue(new AssertStrings(log, new VerboseAsserts().onlyVerify()).notContains("decease").isPassed());
    }
}
