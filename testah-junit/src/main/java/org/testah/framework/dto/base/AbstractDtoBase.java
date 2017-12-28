package org.testah.framework.dto.base;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testah.TS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDtoBase<T> {

    @JsonIgnore
    public static final String MSG_UNKNOWN_JSON_PROP_FOUND = "@@@ Found Unknown Json Field @@@";
    @JsonIgnore
    private boolean allowUnknown = true;
    @JsonIgnore
    private ToStringStyle toStringStyle = ToStringStyle.JSON_STYLE;
    @JsonIgnore
    private List<String> toStringListOfFieldsToExclude = null;

    public AbstractDtoBase() {

    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @SuppressWarnings("unchecked")
    @JsonIgnore
    private T getSelf() {
        return (T) this;
    }

    @JsonIgnore
    public String toJson() {
        return TS.util().toJson(getSelf());
    }

    /**
     * Clone will return a new identical copy of the current object.
     *
     * @return Return this.
     */
    @SuppressWarnings("unchecked")
    @JsonIgnore
    public T clone() {
        try {
            TS.log().info(getSelf().getClass());
            return (T) TS.util().getMap().readValue(this.toJson(), getSelf().getClass());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Issue Trying to Clone: %s - err: %s", getClassPath(), e.getMessage()), e);
        }
    }

    @JsonIgnore
    public String getClassPath() {
        return getSelf().getClass().getCanonicalName();
    }

    public String toString() {
        return toString(ToStringStyle.JSON_STYLE);
    }

    public String toString(final ToStringStyle style) {
        ReflectionToStringBuilder toStringBuilder = new ReflectionToStringBuilder(this, style);
        toStringBuilder.setExcludeFieldNames(getToStringListOfFieldsToExclude().stream().toArray(String[]::new));
        return toStringBuilder.toString();
    }

    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(getSelf(), obj);
    }

    @JsonIgnore
    public boolean equalsVerbose(final Object obj) {
        try {
            if (obj instanceof AbstractDtoBase<?>) {
                JSONAssert.assertEquals(this.toJson(), ((AbstractDtoBase<?>) obj).toJson(), true);
            }
        } catch (Throwable throwable) {
            TS.log().debug("equalsVerbose: " + throwable.getMessage());
        }
        return equals(obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(getSelf());
    }

    @JsonIgnore
    public AbstractDtoBase<T> assertEquals(final AbstractDtoBase<?> actual) throws JSONException {
        return assertEquals(((AbstractDtoBase<?>) actual).toJson());
    }

    @JsonIgnore
    public AbstractDtoBase<T> assertEquals(final String actualJson) throws JSONException {
        JSONAssert.assertEquals(this.toJson(), actualJson, true);
        return this;
    }

    @JsonIgnore
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        TS.log().warn(String.format(MSG_UNKNOWN_JSON_PROP_FOUND + ": %s - Dto Class: %s", name,
                getSelf().getClass().getCanonicalName()));
        if (!isAllowUnknown()) {
            TS.asserts().isTrue(String.format("Expecting No unknown Fields for Dto but found field: %s with value: %s",
                    name, (null != value ? value.toString() : "null")), false);
        }
        this.additionalProperties.put(name, value);
    }

    @JsonIgnore
    protected boolean isAllowUnknown() {
        return allowUnknown;
    }

    @JsonIgnore
    protected T setAllowUnknown(final boolean allowUnknown) {
        this.allowUnknown = allowUnknown;
        return getSelf();
    }

    @JsonIgnore
    public ToStringStyle getToStringStyle() {
        return toStringStyle;
    }

    @JsonIgnore
    public void setToStringStyle(final ToStringStyle toStringStyle) {
        this.toStringStyle = toStringStyle;
    }

    @JsonIgnore
    public List<String> getToStringListOfFieldsToExclude() {
        if (null == toStringListOfFieldsToExclude) {
            toStringListOfFieldsToExclude = new ArrayList<String>();
            toStringListOfFieldsToExclude.add("toStringStyle");
            toStringListOfFieldsToExclude.add("toStringListOfFieldsToExclude");
            toStringListOfFieldsToExclude.add("allowUnknown");
            toStringListOfFieldsToExclude.add("additionalProperties");
        }
        return toStringListOfFieldsToExclude;
    }

    @JsonIgnore
    public void setToStringListOfFieldsToExclude(final List<String> toStringListOfFieldsToExclude) {
        this.toStringListOfFieldsToExclude = toStringListOfFieldsToExclude;
    }

}
