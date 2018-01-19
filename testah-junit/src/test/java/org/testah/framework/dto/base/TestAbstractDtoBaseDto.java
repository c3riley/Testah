package org.testah.framework.dto.base;

public class TestAbstractDtoBaseDto extends AbstractDtoBase<TestAbstractDtoBaseDto> {

    private String value;

    public TestAbstractDtoBaseDto() {
        super();
    }

    public String getValue() {
        return value;
    }

    public TestAbstractDtoBaseDto setValue(final String value) {
        this.value = value;
        return this;
    }

}