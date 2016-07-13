
package org.testah.framework.report.jira.dto;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Application.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "type", "name" })
public class Application {

    /** The type. */
    @JsonProperty("type")
    private String type;

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The additional properties. */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     * Gets the type.
     *
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            The type
     * @return the application
     */
    @JsonProperty("type")
    public Application setType(final String type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the name.
     *
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            The name
     * @return the application
     */
    @JsonProperty("name")
    public Application setName(final String name) {
        this.name = name;
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
     * @param name
     *            the name
     * @param value
     *            the value
     * @return the application
     */
    @JsonAnySetter
    public Application setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
