
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
 * The Class RemoteIssueLinkObject.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "url", "title", "summary", "icon", "status" })
public class RemoteIssueLinkObject {

    /** The url. */
    @JsonProperty("url")
    private String url;
    
    /** The title. */
    @JsonProperty("title")
    private String title;
    
    /** The summary. */
    @JsonProperty("summary")
    private String summary;
    
    /** The icon. */
    @JsonProperty("icon")
    private Icon icon;
    
    /** The status. */
    @JsonProperty("status")
    private Status status;
    
    /** The additional properties. */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     * Gets the url.
     *
     * @return The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url            The url
     * @return the remote issue link object
     */
    @JsonProperty("url")
    public RemoteIssueLinkObject setUrl(final String url) {
        this.url = url;
        return this;
    }

    /**
     * Gets the title.
     *
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title            The title
     * @return the remote issue link object
     */
    @JsonProperty("title")
    public RemoteIssueLinkObject setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the summary.
     *
     * @return The summary
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * @param summary            The summary
     * @return the remote issue link object
     */
    @JsonProperty("summary")
    public RemoteIssueLinkObject setSummary(final String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * Gets the icon.
     *
     * @return The icon
     */
    @JsonProperty("icon")
    public Icon getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon            The icon
     * @return the remote issue link object
     */
    @JsonProperty("icon")
    public RemoteIssueLinkObject setIcon(final Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Gets the status.
     *
     * @return The status
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status            The status
     * @return the remote issue link object
     */
    @JsonProperty("status")
    public RemoteIssueLinkObject setStatus(final Status status) {
        this.status = status;
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
     * @param name the name
     * @param value the value
     * @return the remote issue link object
     */
    @JsonAnySetter
    public RemoteIssueLinkObject setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
