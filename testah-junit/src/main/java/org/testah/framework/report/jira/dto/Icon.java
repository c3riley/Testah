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
 * The Class Icon.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"url16x16", "title"})
public class Icon {

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
     * The additional properties.
     */
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

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
     * @return the icon
     */
    @JsonProperty("url16x16")
    public Icon setUrl16x16(final String url16x16) {
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
     * @return the icon
     */
    @JsonProperty("title")
    public Icon setTitle(final String title) {
        this.title = title;
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
     * @return the icon
     */
    @JsonAnySetter
    public Icon setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
