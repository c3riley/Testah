package org.testah.framework.dto.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.junit.Test;

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
        assertThat(dto1.toString(), containsString("org.testah.framework.dto.base.TestAbstractDtoBaseDto@"));
        assertThat(dto1.toString(), containsString("[value=<null>,allowUnknown=true,additionalProperties={}]"));
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

    }

}
