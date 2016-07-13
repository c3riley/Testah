
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
@JsonPropertyOrder({ "url16x16", "title" })
public class Icon {

    @JsonProperty("url16x16")
    private String url16x16;
    @JsonProperty("title")
    private String title;
    @JsonIgnore
    private final Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     *
     * @return The url16x16
     */
    @JsonProperty("url16x16")
    public String getUrl16x16() {
        return url16x16;
    }

    /**
     *
     * @param url16x16
     *            The url16x16
     */
    @JsonProperty("url16x16")
    public Icon setUrl16x16(final String url16x16) {
        this.url16x16 = url16x16;
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
    public Icon setTitle(final String title) {
        this.title = title;
        return this;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public Icon setAdditionalProperty(final String name, final java.lang.Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
