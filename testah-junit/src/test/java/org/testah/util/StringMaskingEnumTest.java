package org.testah.util;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringMaskingEnumTest
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
        StringMaskingConfigEnum.INSTANCE.reset();
        StringMaskingEnum.INSTANCE.reset();
    }

    @Test
    public void testInstance()
    {
        StringMaskingConfigEnum.INSTANCE.createInstance(7, 2, 2);
        StringMaskingEnum.INSTANCE.getInstance();
    }

    @Test
    public void testMasking()
    {
        StringMaskingEnum.INSTANCE.createInstance().addBulk(valShort, val, utfVal, valTrue, valtrue, valFalse, valfalse);
        assertEquals(valShort, StringMaskingEnum.INSTANCE.getInstance().getValue(valShort));
        assertEquals(valTrue, StringMaskingEnum.INSTANCE.getInstance().getValue(valTrue));
        assertEquals(valtrue, StringMaskingEnum.INSTANCE.getInstance().getValue(valtrue));
        assertEquals(valFalse, StringMaskingEnum.INSTANCE.getInstance().getValue(valFalse));
        assertEquals(valfalse, StringMaskingEnum.INSTANCE.getInstance().getValue(valfalse));
        assertEquals(valMasked, StringMaskingEnum.INSTANCE.getInstance().getValue(val));
        assertEquals(utfValMasked, StringMaskingEnum.INSTANCE.getInstance().getValue(utfVal));
        Map<String, String> expectedMaskedValuesMap = new HashMap<>();
        expectedMaskedValuesMap.put(val, valMasked);
        expectedMaskedValuesMap.put(utfVal, utfValMasked);
        assertEquals(expectedMaskedValuesMap, StringMaskingEnum.INSTANCE.getInstance().getMap());
    }

    @Test
    public void testSetMaskingPattern()
    {
        final String val9 = "123456789";
        final String val10 = "1234567890";

        StringMaskingConfigEnum.INSTANCE.createInstance(9, 3, 0);
        StringMaskingEnum.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("thi***", StringMaskingEnum.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMaskingEnum.INSTANCE.getInstance().getValue(val9));
        assertEquals("123***", StringMaskingEnum.INSTANCE.getInstance().getValue(val10));

        StringMaskingConfigEnum.INSTANCE.reset();
        StringMaskingEnum.INSTANCE.reset();
        StringMaskingConfigEnum.INSTANCE.createInstance(9, 0, 3);
        StringMaskingEnum.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("***ret", StringMaskingEnum.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMaskingEnum.INSTANCE.getInstance().getValue(val9));
        assertEquals("***890", StringMaskingEnum.INSTANCE.getInstance().getValue(val10));

        StringMaskingConfigEnum.INSTANCE.reset();
        StringMaskingEnum.INSTANCE.reset();
        StringMaskingConfigEnum.INSTANCE.createInstance(9, 2, 2);
        StringMaskingEnum.INSTANCE.createInstance().addBulk(val, val9, val10);
        assertEquals("th***et", StringMaskingEnum.INSTANCE.getInstance().getValue(val));
        assertEquals(val9, StringMaskingEnum.INSTANCE.getInstance().getValue(val9));
        assertEquals("12***90", StringMaskingEnum.INSTANCE.getInstance().getValue(val10));
    }

    @Test
    public void testAdd()
    {
        final String valShort = "abc";
        final String valLong = "abcdefghijklmnopqrstuvwxyz";

        StringMaskingConfigEnum.INSTANCE.createInstance(7, 2, 2);
        StringMaskingEnum.INSTANCE.createInstance().add(valShort);
        StringMaskingEnum.INSTANCE.getInstance().add(valLong);
        assertEquals("ab***yz", StringMaskingEnum.INSTANCE.getInstance().getValue(valLong));
        assertTrue(StringMaskingEnum.INSTANCE.getInstance().getValue(valShort).matches("..\\*\\*\\*.."));
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

        StringMaskingConfigEnum.INSTANCE.createInstance(7, 2, 2);
        StringMaskingEnum.INSTANCE.createInstance()
                .addLiteralExemptions(str0, str1, str2)
                .addRegexExemptions(regex1, regex2, regex3)
                .addBulk(str0, str1, str2, str3, str4, str5, str6, str7, str8, str9)
                .add(str10);
        assertEquals(str0, StringMaskingEnum.INSTANCE.getInstance().getValue(str0));
        assertEquals(str1, StringMaskingEnum.INSTANCE.getInstance().getValue(str1));
        assertEquals(str2, StringMaskingEnum.INSTANCE.getInstance().getValue(str2));
        assertEquals("aa***c3", StringMaskingEnum.INSTANCE.getInstance().getValue(str3));
        assertEquals("aa***cc", StringMaskingEnum.INSTANCE.getInstance().getValue(str4));
        assertEquals(str5, StringMaskingEnum.INSTANCE.getInstance().getValue(str5));
        assertEquals(str6, StringMaskingEnum.INSTANCE.getInstance().getValue(str6));
        assertEquals(str7, StringMaskingEnum.INSTANCE.getInstance().getValue(str7));
        assertEquals(str8, StringMaskingEnum.INSTANCE.getInstance().getValue(str8));
        assertEquals(str9, StringMaskingEnum.INSTANCE.getInstance().getValue(str9));
        assertEquals("ea***om", StringMaskingEnum.INSTANCE.getInstance().getValue(str10));

        assertEquals(Sets.newHashSet(str0, str1, str2, "true", "TRUE", "false", "FALSE"),
                StringMaskingEnum.INSTANCE.getInstance().getLiteralExemptions());
        assertEquals(Sets.newHashSet(regex1, regex2, regex3), StringMaskingEnum.INSTANCE.getInstance().getRegexExemptions());

        StringMaskingEnum.INSTANCE.getInstance().removeRegexExemption(regex3).addBulk(str8);
        assertEquals("in***01", StringMaskingEnum.INSTANCE.getInstance().getValue(str8));
        assertEquals(Sets.newHashSet(regex1, regex2), StringMaskingEnum.INSTANCE.getInstance().getRegexExemptions());
    }
}
