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
import org.testah.client.enums.TypeOfKnown;
import org.testah.framework.annotations.KnownProblem;

@KnownProblem(description = "test")
public class TestKnownProblem {

    @SuppressWarnings("deprecation")
    @Test
    public void testKnownProblemTestPlan() {
        final KnownProblem k = this.getClass().getAnnotation(KnownProblem.class);
        Assert.assertEquals("test", k.description());
        Assert.assertEquals(new String[] {}, k.linkedIds());
        Assert.assertEquals(TypeOfKnown.DEFECT, k.typeOfKnown());
    }

}
