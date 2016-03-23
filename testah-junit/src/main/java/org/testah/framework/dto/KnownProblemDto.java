package org.testah.framework.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.enums.TypeOfKnown;

public class KnownProblemDto {
    
    private List<String> linkedIds = new ArrayList<String>();

    private String description = "";
    
    private TypeOfKnown typeOfKnown = TypeOfKnown.DEFECT;
    
    public KnownProblemDto() {
    
    }
    
    public KnownProblemDto(final KnownProblem knownProblem) {
        this.linkedIds.addAll(Arrays.asList(knownProblem.linkedIds()));
        this.description = knownProblem.description();
        this.typeOfKnown = knownProblem.typeOfKnown();
    }

    public List<String> getLinkedIds() {
        return linkedIds;
    }

    public KnownProblemDto setLinkedIds(final List<String> linkedIds) {
        this.linkedIds = linkedIds;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public KnownProblemDto setDescription(final String description) {
        this.description = description;
        return this;
    }

    public TypeOfKnown getTypeOfKnown() {
        return typeOfKnown;
    }

    public KnownProblemDto setTypeOfKnown(final TypeOfKnown typeOfKnown) {
        this.typeOfKnown = typeOfKnown;
        return this;
    }
    
}
