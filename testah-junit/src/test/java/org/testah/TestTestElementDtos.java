package org.testah;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.testah.framework.dto.StepActionDto;
import org.testah.framework.dto.TestStepDto;

public class TestTestElementDtos {
    
    @Test
    public void testStepDto() throws JsonGenerationException, JsonMappingException, IOException {
        final TestStepDto step = new TestStepDto();
        step.start();
        step.stop();
        step.setStatus();
        step.addStepActions(new StepActionDto());
        System.out.println(new ObjectMapper().writeValueAsString(step));
        
    }
}
