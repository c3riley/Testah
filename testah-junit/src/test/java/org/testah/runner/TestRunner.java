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
package org.testah.runner;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.cli.TestFilter;

public class TestRunner {

    @Test
    public void testLoadCompiledTests() {
        final TestFilter r = new TestFilter();
        final Set<Class<?>> classes = r.loadCompiledTestClase().getTestClasses();
        Assert.assertNotNull(classes);
        Assert.assertEquals(5, classes.size());
        TS.util().toJson(classes);
    }
}
