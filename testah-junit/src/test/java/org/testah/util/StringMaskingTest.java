package org.testah.util;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.jupiter.api.Test;
import org.testah.util.unittest.dtotest.SystemOutCapture;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.testah.util.StringMaskingConfig.INFO_USE_DEFAULT_CONFIG;

class StringMaskingTest
{
    private static final String valShort = "abc";
    private static final String valTrue = "TRUE";
    private static final String valtrue = "true";
    private static final String valFalse = "FALSE";
    private static final String valfalse = "false";
    //utfVal = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6\u03B7\u03B8\u03B9\u03BA\u03BB";
    private static final String utfVal = "αβγδεζηθικλ";
    //utfValMasked = "\u03B1\u03B2***\u03BA\u03BB";
    private static final String utfValMasked = "αβ***κλ";
    private static final String val = "this is a regular secret";
    private static final String valMasked = "th***et";

    @Test
    void testMasking()
    {
        StringMaskingConfig.getInstance().destroy();
        StringMasking.getInstance().destroy();
        StringMasking.getInstance().addBulk(valShort, val, utfVal, valTrue, valtrue, valFalse, valfalse);
        assertEquals(valShort, StringMasking.getInstance().getValue(valShort));
        assertEquals(valTrue, StringMasking.getInstance().getValue(valTrue));
        assertEquals(valtrue, StringMasking.getInstance().getValue(valtrue));
        assertEquals(valFalse, StringMasking.getInstance().getValue(valFalse));
        assertEquals(valfalse, StringMasking.getInstance().getValue(valfalse));
        assertEquals(valMasked, StringMasking.getInstance().getValue(val));
        assertEquals(utfValMasked, StringMasking.getInstance().getValue(utfVal));
        Map<String, String> expectedMaskedValuesMap = new HashMap<>();
        expectedMaskedValuesMap.put(val, valMasked);
        expectedMaskedValuesMap.put(utfVal, utfValMasked);
        assertEquals(expectedMaskedValuesMap, StringMasking.getInstance().getMap());
    }

    @Test
    void testNotification() throws Exception
    {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        StringMasking.getInstance().destroy();
        StringMaskingConfig.getInstance().destroy();
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start())
        {
            config.getRootLogger().setLevel(Level.DEBUG);
            StringMasking.getInstance();
            assertThat(systemOutCapture.getSystemOut(), containsString(INFO_USE_DEFAULT_CONFIG));
            StringMasking.getInstance().destroy();
            StringMasking.getInstance();
            assertEquals(systemOutCapture.getSystemOut(), "");
        } finally
        {
            config.getRootLogger().setLevel(Level.INFO);
        }
    }

    @Test
    void testSetMaskingPattern()
    {
        final String val9 = "123456789";
        final String val10 = "1234567890";

        StringMaskingConfig.getInstance().destroy();
        StringMaskingConfig.createInstance(9, 3, 0);
        StringMasking.getInstance().destroy();
        StringMasking.getInstance().addBulk(val, val9, val10);
        assertEquals("thi***", StringMasking.getInstance().getValue(val));
        assertEquals(val9, StringMasking.getInstance().getValue(val9));
        assertEquals("123***", StringMasking.getInstance().getValue(val10));

        StringMaskingConfig.getInstance().destroy();
        StringMaskingConfig.createInstance(9, 0, 3);
        StringMasking.getInstance().destroy();
        StringMasking.getInstance().addBulk(val, val9, val10);
        assertEquals("***ret", StringMasking.getInstance().getValue(val));
        assertEquals(val9, StringMasking.getInstance().getValue(val9));
        assertEquals("***890", StringMasking.getInstance().getValue(val10));

        StringMaskingConfig.getInstance().destroy();
        StringMaskingConfig.createInstance(9, 2, 2);
        StringMasking.getInstance().destroy();
        StringMasking.getInstance().addBulk(val, val9, val10);
        assertEquals("th***et", StringMasking.getInstance().getValue(val));
        assertEquals(val9, StringMasking.getInstance().getValue(val9));
        assertEquals("12***90", StringMasking.getInstance().getValue(val10));
    }

    @Test
    void testAdd()
    {
        final String valForceShort = "abc";
        final String valForceLong = "abcdefghijklmnopqrstuvwxyz";

        StringMaskingConfig.getInstance().destroy();
        StringMaskingConfig.createInstance(7, 2, 2);
        StringMasking.getInstance().destroy();
        StringMasking.getInstance().add(valForceShort);
        StringMasking.getInstance().add(valForceLong);
        assertEquals("ab***yz", StringMasking.getInstance().getValue(valForceLong));
        assertTrue(StringMasking.getInstance().getValue(valForceShort).matches("..\\*\\*\\*.."));
    }

    @Test
    public void testExemptions()
    {
        final String str0 = "aaabbbccc";
        final String str1 = "aaabbbccc1";
        final String str2 = "aaabbbccc2";
        final String str3 = "aaabbbccc3";
        final String str4 = "aaabbccc";
        final String str5 = "eastusma";
        final String str6 = "az.eastus.region";
        final String str7 = "eagleinvsys.org";
        final String str8 = "in 2019-01-01";
        final String str9 = "2018-01-01";
        final String str10 = "eagleinvsys.com";
        final String regex1 = ".*eastus.*";
        final String regex2 = ".*eagle.*";
        final String regex3 = ".*[0-9]{4}-[0-9]{2}-[0-9]{2}.*";

        StringMaskingConfig.getInstance().destroy();
        StringMaskingConfig.createInstance(7, 2, 2);
        StringMasking.getInstance().destroy();
        StringMasking.getInstance()
                .addLiteralExemptions(str0, str1, str2)
                .addRegexExemptions(regex1, regex2, regex3)
                .addBulk(str0, str1, str2, str3, str4, str5, str6, str7, str8, str9)
                .add(str10);
        assertEquals(str0, StringMasking.getInstance().getValue(str0));
        assertEquals(str1, StringMasking.getInstance().getValue(str1));
        assertEquals(str2, StringMasking.getInstance().getValue(str2));
        assertEquals("aa***c3", StringMasking.getInstance().getValue(str3));
        assertEquals("aa***cc", StringMasking.getInstance().getValue(str4));
        assertEquals(str5, StringMasking.getInstance().getValue(str5));
        assertEquals(str6, StringMasking.getInstance().getValue(str6));
        assertEquals(str7, StringMasking.getInstance().getValue(str7));
        assertEquals(str8, StringMasking.getInstance().getValue(str8));
        assertEquals(str9, StringMasking.getInstance().getValue(str9));
        assertEquals("ea***om", StringMasking.getInstance().getValue(str10));

        assertEquals(Sets.newHashSet(str0, str1, str2, "true", "TRUE", "false", "FALSE"),
                StringMasking.getInstance().getLiteralExemptions());
        assertEquals(Sets.newHashSet(regex1, regex2, regex3), StringMasking.getInstance().getRegexExemptions());

        StringMasking.getInstance().removeRegexExemption(regex3).addBulk(str8);
        assertEquals("in***01", StringMasking.getInstance().getValue(str8));
        assertEquals(Sets.newHashSet(regex1, regex2), StringMasking.getInstance().getRegexExemptions());
    }
}