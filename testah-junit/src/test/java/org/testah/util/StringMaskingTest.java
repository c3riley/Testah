package org.testah.util;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.TS;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringMaskingTest
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

    /**
     * Set up for testing.
     */
    @BeforeEach
    public void setup() {
        StringMaskingConfig.INSTANCE.reset();
        StringMasking.INSTANCE.reset();
    }

    @Test
    public void testInstance()
    {
        StringMaskingConfig.INSTANCE.createInstance(7, 2, 2);
        StringMasking.INSTANCE.getInstance();
    }

    @Test
    public void testMasking()
    {
        StringMasking.INSTANCE.createInstance().addBulk(valShort, val, utfVal, valTrue, valtrue, valFalse, valfalse);
        assertEquals(valShort, StringMasking.INSTANCE.getInstance().getValue(valShort));
        assertEquals(valTrue, StringMasking.INSTANCE.getInstance().getValue(valTrue));
        assertEquals(valtrue, StringMasking.INSTANCE.getInstance().getValue(valtrue));
        assertEquals(valFalse, StringMasking.INSTANCE.getInstance().getValue(valFalse));
        assertEquals(valfalse, StringMasking.INSTANCE.getInstance().getValue(valfalse));
        assertEquals(valMasked, StringMasking.INSTANCE.getInstance().getValue(val));
        assertEquals(utfValMasked, StringMasking.INSTANCE.getInstance().getValue(utfVal));
        Map<String, String> expectedMaskedValuesMap = new HashMap<>();
        expectedMaskedValuesMap.put(val, valMasked);
        expectedMaskedValuesMap.put(utfVal, utfValMasked);
        assertEquals(expectedMaskedValuesMap, StringMasking.INSTANCE.getInstance().getMap());
    }

    @Test
    public void testSetMaskingPattern()
    {
        final String val9 = "123456789";
        final String val10 = "1234567890";

        StringMaskingConfig.INSTANCE.createInstance(9, 3, 0);
        StringMasking.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("thi***", StringMasking.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMasking.INSTANCE.getInstance().getValue(val9));
        assertEquals("123***", StringMasking.INSTANCE.getInstance().getValue(val10));

        StringMaskingConfig.INSTANCE.reset();
        StringMasking.INSTANCE.reset();
        StringMaskingConfig.INSTANCE.createInstance(9, 0, 3);
        StringMasking.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("***ret", StringMasking.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMasking.INSTANCE.getInstance().getValue(val9));
        assertEquals("***890", StringMasking.INSTANCE.getInstance().getValue(val10));

        StringMaskingConfig.INSTANCE.reset();
        StringMasking.INSTANCE.reset();
        StringMaskingConfig.INSTANCE.createInstance(9, 2, 2);
        StringMasking.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("th***et", StringMasking.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMasking.INSTANCE.getInstance().getValue(val9));
        assertEquals("12***90", StringMasking.INSTANCE.getInstance().getValue(val10));
    }

    @Test
    public void testAdd()
    {
        final String valShort = "abc";
        final String valLong = "abcdefghijklmnopqrstuvwxyz";

        StringMaskingConfig.INSTANCE.createInstance(7, 2, 2);
        StringMasking.INSTANCE.createInstance().add(valShort);
        StringMasking.INSTANCE.getInstance().add(valLong);
        assertEquals("ab***yz", StringMasking.INSTANCE.getInstance().getValue(valLong));
        assertTrue(StringMasking.INSTANCE.getInstance().getValue(valShort).matches("..\\*\\*\\*.."));
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

        StringMaskingConfig.INSTANCE.createInstance(7, 2, 2);
        StringMasking.INSTANCE.createInstance()
                .addLiteralExemptions(str0, str1, str2)
                .addRegexExemptions(regex1, regex2, regex3)
                .addBulk(str0, str1, str2, str3, str4, str5, str6, str7, str8, str9)
                .add(str10);
        assertEquals(str0, StringMasking.INSTANCE.getInstance().getValue(str0));
        assertEquals(str1, StringMasking.INSTANCE.getInstance().getValue(str1));
        assertEquals(str2, StringMasking.INSTANCE.getInstance().getValue(str2));
        assertEquals("aa***c3", StringMasking.INSTANCE.getInstance().getValue(str3));
        assertEquals("aa***cc", StringMasking.INSTANCE.getInstance().getValue(str4));
        assertEquals(str5, StringMasking.INSTANCE.getInstance().getValue(str5));
        assertEquals(str6, StringMasking.INSTANCE.getInstance().getValue(str6));
        assertEquals(str7, StringMasking.INSTANCE.getInstance().getValue(str7));
        assertEquals(str8, StringMasking.INSTANCE.getInstance().getValue(str8));
        assertEquals(str9, StringMasking.INSTANCE.getInstance().getValue(str9));
        assertEquals("ea***om", StringMasking.INSTANCE.getInstance().getValue(str10));

        assertEquals(Sets.newHashSet(str0, str1, str2, "true", "TRUE", "false", "FALSE"),
                StringMasking.INSTANCE.getInstance().getLiteralExemptions());
        assertEquals(Sets.newHashSet(regex1, regex2, regex3), StringMasking.INSTANCE.getInstance().getRegexExemptions());

        StringMasking.INSTANCE.getInstance().removeRegexExemption(regex3).addBulk(str8);
        assertEquals("in***01", StringMasking.INSTANCE.getInstance().getValue(str8));
        assertEquals(Sets.newHashSet(regex1, regex2), StringMasking.INSTANCE.getInstance().getRegexExemptions());
    }

    @Test
    void testSanitizeMessage()
    {
        StringMaskingConfig.INSTANCE.createInstance(6, 2, 2);
        String message = TS.util().getResourceAsString("/util/message.txt");
        String sanitizedMsgSanitized = TS.util().getResourceAsString("/util/message_sanitized.txt");
        TS.addMaskBulk("fairest", "creatures", "decease");
        TS.getMaskValues();
        TS.asserts().equalsTo(sanitizedMsgSanitized, TS.sanitizeString(message));
    }
}
