package org.testah.framework.dto.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Test;
import org.testah.driver.http.response.ResponseDto;

public class AbstractDtoBaseTest {

    @Test
    public void testHash() {
        TestAbstractDtoBaseDto dto1 = new TestAbstractDtoBaseDto();
        TestAbstractDtoBaseDto dto2 = new TestAbstractDtoBaseDto();

        Assert.assertEquals(dto1, dto2);
        Assert.assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setValue("testing");
        dto2.setValue("testing");
        Assert.assertEquals(dto1, dto2);
        Assert.assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setValue("testing1");
        dto2.setValue("testing2");
        Assert.assertNotEquals(dto1, dto2);
        Assert.assertNotEquals(dto1.hashCode(), dto2.hashCode());

    }

    @Test
    public void testToString() {
        TestAbstractDtoBaseDto dto1 = new TestAbstractDtoBaseDto();

        assertThat(dto1.toString(), containsString("{\"value\":null}"));
    }

    @Test
    public void testClone() {
        TestAbstractDtoBaseDto dto1 = new TestAbstractDtoBaseDto();
        TestAbstractDtoBaseDto dto2 = dto1.clone();

        Assert.assertEquals(dto1, dto2);
        Assert.assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setValue("testing");
        dto2.setValue("testing");
        Assert.assertEquals(dto1, dto2);
        Assert.assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setValue("testing1");
        dto2.setValue("testing2");
        Assert.assertNotEquals(dto1, dto2);
        Assert.assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEquals() {
        ResponseDto dto1 = new ResponseDto();
        ResponseDto dto2 = new ResponseDto();
        dto1.setStatusText("testing2");
        dto2.setStatusText("testing1");

        dto1.equalsVerbose(dto2);
        dto1.equals(dto2);
    }

}
