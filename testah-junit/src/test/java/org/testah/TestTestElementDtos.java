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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.testah.client.dto.StepActionDto;
import org.testah.client.dto.TestStepDto;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestTestElementDtos {

    @Test
    public void testStepDto() throws JsonGenerationException, JsonMappingException, IOException {
        final TestStepDto step = new TestStepDto();
        step.start();
        step.stop();
        step.setStatus();
        step.addStepActions(new StepActionDto());
        Assert.assertEquals(1, step.getStepActions().size());
        Assert.assertNotNull(new ObjectMapper().writeValueAsString(step));

    }
}
