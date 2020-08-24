package org.testah.framework.report.jira.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class Status.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"resolved", "icon"})
public class Status {

    /**
     * The additional properties.
     */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();
    /**
     * The resolved.
     */
    @JsonProperty("resolved")
    private Boolean resolved;
    /**
     * The icon.
     */
    @JsonProperty("icon")
    private Icon2 icon;

    /**
     * Gets the resolved.
     *
     * @return The resolved
     */
    @JsonProperty("resolved")
    public Boolean getResolved() {
        return resolved;
    }

    /**
     * Sets the resolved.
     *
     * @param resolved The resolved
     * @return the status
     */
    @JsonProperty("resolved")
    public Status setResolved(final Boolean resolved) {
        this.resolved = resolved;
        return this;
    }

    /**
     * Gets the icon.
     *
     * @return The icon
     */
    @JsonProperty("icon")
    public Icon2 getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon The icon
     * @return the status
     */
    @JsonProperty("icon")
    public Status setIcon(final Icon2 icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Gets the additional properties.
     *
     * @return the additional properties
     */
    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Sets the additional property.
     *
     * @param name  the name
     * @param value the value
     * @return the status
     */
    @JsonAnySetter
    public Status setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
