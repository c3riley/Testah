/*
#
# Copyright (c) 2014-2016 Cazena, Inc., as an unpublished work.
# This notice does not imply unrestricted or public access to these
# materials which are a trade secret of Cazena, Inc. or its
# subsidiaries or affiliates (together referred to as "Cazena"), and
# which may not be copied, reproduced, used, sold or transferred to any
# third party without Cazena's prior written consent.
#
# All rights reserved.
*/
package org.testah;

import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.cli.ParamLoader;
import org.testah.framework.cli.Params;

public class TestParamPropertiesFactory {

    @Test
    public void testParamPropertiesFactory() {
        final Params params = new ParamLoader().loadParamValues();
        Assert.assertNotNull(params);
        Assert.assertNotNull(params.getBrowser());
    }

}
