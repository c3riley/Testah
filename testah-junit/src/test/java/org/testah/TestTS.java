package org.testah;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.AssertStrings;
import org.testah.util.StringMaskingConfig;
import org.testah.util.unittest.dtotest.SystemOutCapture;

public class TestTS
{
    String message;
    String log;

    // avoid concurrent test execution issues by using only one test case
    @Test(expected = AssertionError.class)
    public void testAll()
    {
        try
        {
            StringMaskingConfig.INSTANCE.createInstance(6, 2, 2);
            message = TS.util().getResourceAsString("/util/message.txt");
            TS.resetMaskValueMap();
            TS.addMaskBulk("fairest", "creatures", "decease");
            TS.params().setLevel(Level.INFO);

            try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
                TS.asserts().contains("Check logging.", message, "we desire increase");
                log = systemOutCapture.getSystemOut();
            }
            validateMasking(log);

            try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
                TS.asserts().startsWith("message", message, "From");
                log = systemOutCapture.getSystemOut();
            }
            validateMasking(log);

            try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {
                TS.asserts().contains("Check logging.", message, "string not there");
                log = systemOutCapture.getSystemOut();
            }
            validateMasking(log);
        } finally
        {
            TS.params().setLevel(Level.DEBUG);
            TS.resetMaskValueMap();
        }
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
