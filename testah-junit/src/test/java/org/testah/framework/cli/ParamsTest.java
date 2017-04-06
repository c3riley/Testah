package org.testah.framework.cli;

import org.junit.Assert;
import org.junit.Test;

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

}
