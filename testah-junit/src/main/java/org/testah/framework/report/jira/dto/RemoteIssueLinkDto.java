package org.testah.framework.report.jira.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class RemoteIssueLinkDto.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"globalId", "application", "relationship", "object"})
public class RemoteIssueLinkDto {

    /**
     * The additional properties.
     */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();
    /**
     * The id.
     */
    @JsonProperty("id")
    private int id = 0;
    /**
     * The global id.
     */
    @JsonProperty("globalId")
    private String globalId;
    /**
     * The application.
     */
    @JsonProperty("application")
    private Application application;
    /**
     * The relationship.
     */
    @JsonProperty("relationship")
    private String relationship;
    /**
     * The object.
     */
    @JsonProperty("object")
    private RemoteIssueLinkObject object;

    /**
     * Gets the global id.
     *
     * @return The globalId
     */
    @JsonProperty("globalId")
    public String getGlobalId() {
        return globalId;
    }

    /**
     * Sets the global id.
     *
     * @param globalId The globalId
     * @return the remote issue link dto
     */
    @JsonProperty("globalId")
    public RemoteIssueLinkDto setGlobalId(final String globalId) {
        this.globalId = globalId;
        return this;
    }

    /**
     * Gets the application.
     *
     * @return The application
     */
    @JsonProperty("application")
    public Application getApplication() {
        return application;
    }

    /**
     * Sets the application.
     *
     * @param application The application
     * @return the remote issue link dto
     */
    @JsonProperty("application")
    public RemoteIssueLinkDto setApplication(final Application application) {
        this.application = application;
        return this;
    }

    /**
     * Gets the relationship.
     *
     * @return The relationship
     */
    @JsonProperty("relationship")
    public String getRelationship() {
        return relationship;
    }

    /**
     * Sets the relationship.
     *
     * @param relationship The relationship
     * @return the remote issue link dto
     */
    @JsonProperty("relationship")
    public RemoteIssueLinkDto setRelationship(final String relationship) {
        this.relationship = relationship;
        return this;
    }

    /**
     * Gets the object.
     *
     * @return The object
     */
    @JsonProperty("object")
    public RemoteIssueLinkObject getObject() {
        return object;
    }

    /**
     * Sets the object.
     *
     * @param object The object
     * @return the remote issue link dto
     */
    @JsonProperty("object")
    public RemoteIssueLinkDto setObject(final RemoteIssueLinkObject object) {
        this.object = object;
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
     * @return the remote issue link dto
     */
    @JsonAnySetter
    public RemoteIssueLinkDto setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     * @return the remote issue link dto
     */
    public RemoteIssueLinkDto setId(final int id) {
        this.id = id;
        return this;
    }

}
