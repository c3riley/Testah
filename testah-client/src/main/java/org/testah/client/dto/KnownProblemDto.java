package org.testah.client.dto;

import java.util.ArrayList;
import java.util.List;

import org.testah.client.enums.TypeOfKnown;


/**
 * The Class KnownProblemDto.
 */
public class KnownProblemDto {

    /** The linked ids. */
    private List<String> linkedIds   = new ArrayList<String>();

    /** The description. */
    private String       description = "";

    /** The type of known. */
    private TypeOfKnown  typeOfKnown = TypeOfKnown.DEFECT;

    /**
     * Instantiates a new known problem dto.
     */
    public KnownProblemDto() {

    }

    /**
     * Gets the linked ids.
     *
     * @return the linked ids
     */
    public List<String> getLinkedIds() {
        return linkedIds;
    }

    /**
     * Sets the linked ids.
     *
     * @param linkedIds the linked ids
     * @return the known problem dto
     */
    public KnownProblemDto setLinkedIds(final List<String> linkedIds) {
        this.linkedIds = linkedIds;
        return this;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return the known problem dto
     */
    public KnownProblemDto setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the type of known.
     *
     * @return the type of known
     */
    public TypeOfKnown getTypeOfKnown() {
        return typeOfKnown;
    }

    /**
     * Sets the type of known.
     *
     * @param typeOfKnown the type of known
     * @return the known problem dto
     */
    public KnownProblemDto setTypeOfKnown(final TypeOfKnown typeOfKnown) {
        this.typeOfKnown = typeOfKnown;
        return this;
    }

}
