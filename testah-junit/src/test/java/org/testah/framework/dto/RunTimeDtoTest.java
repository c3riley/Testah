package org.testah.framework.dto;

import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.RunTimeDto;

public class RunTimeDtoTest {

    @Test
    public void testWithDefaultTimeZone() {
        TS.params().setTimezone(null);
        RunTimeDto runTimeDto = new RunTimeDto();
        runTimeDto.start();
        runTimeDto.stop();
        Assert.assertNotNull(runTimeDto.getStartDate());
        Assert.assertNotNull(runTimeDto.getStartTime());

    }

    @Test
    public void testWithDefinedTimeZone() {
        TS.params().setTimezone("America/New_York");
        RunTimeDto runTimeDto = new RunTimeDto();
        runTimeDto.start();
        runTimeDto.stop();
        Assert.assertNotNull(runTimeDto.getStartDate());
        Assert.assertNotNull(runTimeDto.getStartTime());
    }


}
