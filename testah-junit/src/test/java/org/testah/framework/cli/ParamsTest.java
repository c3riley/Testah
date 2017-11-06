package org.testah.framework.cli;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ParamsTest {

    private static final String NA_N = "NaN";
    private static final String TEST = "TEST";

    @Test
    public void testGetValue() {

        Params params = new Params();
        Assert.assertNull(params.getValue(null));
        Assert.assertNull(params.getValue(null, null));
        params.getOther().put(TEST, null);
        Assert.assertNull(params.getValue(null));
        params.getOther().put(TEST, "TEST_VALUE");
        Assert.assertEquals("TEST_VALUE", params.getValue(TEST));
        System.setProperty(TEST, "TEST_VALUE_SYS");
        Assert.assertEquals("TEST_VALUE_SYS", params.getValue(TEST));
        Assert.assertNotNull(params.getValue("PATH"));

    }

    @Test
    public void injectLocalPropertiesTest() throws IOException {
        final ParamLoader paramLoader = new ParamLoader();
        Assert.assertEquals(NA_N, System.getProperty("PROP_TEST_AA1", NA_N));
        Assert.assertEquals(NA_N, System.getProperty("PROP_TEST_AA2", NA_N));

        File temp = File.createTempFile("local", ".properties");
        FileUtils.writeStringToFile(temp, "PROP_TEST_AA1=hello\nPROP_TEST_AA2=world", Charset.forName("UTF-8"));
        temp.deleteOnExit();

        paramLoader.injectLocalProperties(temp);

        Assert.assertEquals("hello", System.getProperty("PROP_TEST_AA1", NA_N));
        System.getProperties().remove("PROP_TEST_AA1");
        Assert.assertEquals("world", System.getProperty("PROP_TEST_AA2", NA_N));
        System.getProperties().remove("PROP_TEST_AA2");
    }

}
