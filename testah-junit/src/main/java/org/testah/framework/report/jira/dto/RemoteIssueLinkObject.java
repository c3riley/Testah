
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
@JsonPropertyOrder({ "url", "title", "summary", "icon", "status" })
public class RemoteIssueLinkObject {

    @JsonProperty("url")
    private String url;
    @JsonProperty("title")
    private String title;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("icon")
    private Icon icon;
    @JsonProperty("status")
    private Status status;
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     *
     * @return The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *            The url
     */
    @JsonProperty("url")
    public RemoteIssueLinkObject setUrl(final String url) {
        this.url = url;
        return this;
    }

    /**
     *
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     *            The title
     */
    @JsonProperty("title")
    public RemoteIssueLinkObject setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     *
     * @return The summary
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     *            The summary
     */
    @JsonProperty("summary")
    public RemoteIssueLinkObject setSummary(final String summary) {
        this.summary = summary;
        return this;
    }

    /**
     *
     * @return The icon
     */
    @JsonProperty("icon")
    public Icon getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     *            The icon
     */
    @JsonProperty("icon")
    public RemoteIssueLinkObject setIcon(final Icon icon) {
        this.icon = icon;
        return this;
    }

    /**
     *
     * @return The status
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *            The status
     */
    @JsonProperty("status")
    public RemoteIssueLinkObject setStatus(final Status status) {
        this.status = status;
        return this;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public RemoteIssueLinkObject setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
