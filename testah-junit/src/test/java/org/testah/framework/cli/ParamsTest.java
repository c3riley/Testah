package org.testah.framework.cli;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ParamsTest {

    @Test
    public void testGetValue() {

        Params params = new Params();
        Assert.assertNull(params.getValue(null));
        Assert.assertNull(params.getValue(null, null));
        params.getOther().put("TEST", null);
        Assert.assertNull(params.getValue(null));
        params.getOther().put("TEST", "TEST_VALUE");
        Assert.assertEquals("TEST_VALUE", params.getValue("TEST"));
        System.setProperty("TEST", "TEST_VALUE_SYS");
        Assert.assertEquals("TEST_VALUE_SYS", params.getValue("TEST"));
        Assert.assertNotNull(params.getValue("PATH"));

    }

    @Test
    public void injectLocalPropertiesTest() throws IOException {
        ParamLoader paramLoader = new ParamLoader();
        Assert.assertEquals("NaN", System.getProperty("PROP_TEST_AA1", "NaN"));
        Assert.assertEquals("NaN", System.getProperty("PROP_TEST_AA2", "NaN"));

        File temp = File.createTempFile("local", ".properties");
        FileUtils.writeStringToFile(temp, "PROP_TEST_AA1=hello\nPROP_TEST_AA2=world");
        temp.deleteOnExit();

        paramLoader.injectLocalProperties(temp);

        Assert.assertEquals("hello", System.getProperty("PROP_TEST_AA1", "NaN"));
        System.getProperties().remove("PROP_TEST_AA1");
        Assert.assertEquals("world", System.getProperty("PROP_TEST_AA2", "NaN"));
        System.getProperties().remove("PROP_TEST_AA2");
    }

}
