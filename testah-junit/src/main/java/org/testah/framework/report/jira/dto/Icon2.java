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
 * The Class Icon2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"url16x16", "title", "link"})
public class Icon2 {

    /**
     * The additional properties.
     */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();
    /**
     * The url16x16.
     */
    @JsonProperty("url16x16")
    private String url16x16;
    /**
     * The title.
     */
    @JsonProperty("title")
    private String title;
    /**
     * The link.
     */
    @JsonProperty("link")
    private String link;

    /**
     * Gets the url16x16.
     *
     * @return The url16x16
     */
    @JsonProperty("url16x16")
    public String getUrl16x16() {
        return url16x16;
    }

    /**
     * Sets the url16x16.
     *
     * @param url16x16 The url16x16
     * @return the icon2
     */
    @JsonProperty("url16x16")
    public Icon2 setUrl16x16(final String url16x16) {
        this.url16x16 = url16x16;
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
     * @param title The title
     * @return the icon2
     */
    @JsonProperty("title")
    public Icon2 setTitle(final String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the link.
     *
     * @return The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * Sets the link.
     *
     * @param link The link
     * @return the icon2
     */
    @JsonProperty("link")
    public Icon2 setLink(final String link) {
        this.link = link;
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
     * @return the icon2
     */
    @JsonAnySetter
    public Icon2 setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
