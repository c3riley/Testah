
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
@JsonPropertyOrder({ "globalId", "application", "relationship", "object" })
public class RemoteIssueLinkDto {

    @JsonProperty("id")
    private int id = 0;
    @JsonProperty("globalId")
    private String globalId;
    @JsonProperty("application")
    private Application application;
    @JsonProperty("relationship")
    private String relationship;
    @JsonProperty("object")
    private RemoteIssueLinkObject object;
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     *
     * @return The globalId
     */
    @JsonProperty("globalId")
    public String getGlobalId() {
        return globalId;
    }

    /**
     *
     * @param globalId
     *            The globalId
     */
    @JsonProperty("globalId")
    public RemoteIssueLinkDto setGlobalId(final String globalId) {
        this.globalId = globalId;
        return this;
    }

    /**
     *
     * @return The application
     */
    @JsonProperty("application")
    public Application getApplication() {
        return application;
    }

    /**
     *
     * @param application
     *            The application
     */
    @JsonProperty("application")
    public RemoteIssueLinkDto setApplication(final Application application) {
        this.application = application;
        return this;
    }

    /**
     *
     * @return The relationship
     */
    @JsonProperty("relationship")
    public String getRelationship() {
        return relationship;
    }

    /**
     *
     * @param relationship
     *            The relationship
     */
    @JsonProperty("relationship")
    public RemoteIssueLinkDto setRelationship(final String relationship) {
        this.relationship = relationship;
        return this;
    }

    /**
     *
     * @return The object
     */
    @JsonProperty("object")
    public RemoteIssueLinkObject getObject() {
        return object;
    }

    /**
     *
     * @param object
     *            The object
     */
    @JsonProperty("object")
    public RemoteIssueLinkDto setObject(final RemoteIssueLinkObject object) {
        this.object = object;
        return this;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public RemoteIssueLinkDto setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    public int getId() {
        return id;
    }

    public RemoteIssueLinkDto setId(final int id) {
        this.id = id;
        return this;
    }

}
