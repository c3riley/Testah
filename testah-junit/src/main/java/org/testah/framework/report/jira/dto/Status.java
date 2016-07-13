
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "resolved", "icon" })
public class Status {

    @JsonProperty("resolved")
    private Boolean resolved;
    @JsonProperty("icon")
    private Icon2 icon;
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     *
     * @return The resolved
     */
    @JsonProperty("resolved")
    public Boolean getResolved() {
        return resolved;
    }

    /**
     *
     * @param resolved
     *            The resolved
     */
    @JsonProperty("resolved")
    public Status setResolved(final Boolean resolved) {
        this.resolved = resolved;
        return this;
    }

    /**
     *
     * @return The icon
     */
    @JsonProperty("icon")
    public Icon2 getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     *            The icon
     */
    @JsonProperty("icon")
    public Status setIcon(final Icon2 icon) {
        this.icon = icon;
        return this;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public Status setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
