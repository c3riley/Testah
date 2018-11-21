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

/**
 * The type Abstract dto base.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractDtoBase<T> {

    /**
     * The constant MSG_UNKNOWN_JSON_PROP_FOUND.
     */
    @JsonIgnore
    public static final String MSG_UNKNOWN_JSON_PROP_FOUND = "@@@ Found Unknown Json Field @@@";
    @JsonIgnore
    private boolean allowUnknown = true;
    @JsonIgnore
    private ToStringStyle toStringStyle = ToStringStyle.JSON_STYLE;
    @JsonIgnore
    private List<String> toStringListOfFieldsToExclude = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Instantiates a new Abstract dto base.
     */
    public AbstractDtoBase() {

    }

    /**
     * Equals verbose boolean.
     *
     * @param obj the obj
     * @return the boolean
     */
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(getSelf());
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(getSelf(), obj);
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

    @Override
    public String toString() {
        return toString(ToStringStyle.JSON_STYLE);
    }

    /**
     * To string string.
     *
     * @param style the style
     * @return the string
     */
    public String toString(final ToStringStyle style) {
        ReflectionToStringBuilder toStringBuilder = new ReflectionToStringBuilder(this, style);
        toStringBuilder.setExcludeFieldNames(getToStringListOfFieldsToExclude().stream().toArray(String[]::new));
        return toStringBuilder.toString();
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    private T getSelf() {
        return (T) this;
    }

    /**
     * To json string.
     *
     * @return the string
     */
    @JsonIgnore
    public String toJson() {
        return TS.util().toJson(getSelf());
    }

    /**
     * Gets class path.
     *
     * @return the class path
     */
    @JsonIgnore
    public String getClassPath() {
        return getSelf().getClass().getCanonicalName();
    }

    /**
     * Gets to string list of fields to exclude.
     *
     * @return the to string list of fields to exclude
     */
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

    /**
     * Sets to string list of fields to exclude.
     *
     * @param toStringListOfFieldsToExclude the to string list of fields to exclude
     */
    @JsonIgnore
    public void setToStringListOfFieldsToExclude(final List<String> toStringListOfFieldsToExclude) {
        this.toStringListOfFieldsToExclude = toStringListOfFieldsToExclude;
    }

    /**
     * Assert equals abstract dto base.
     *
     * @param actual the actual
     * @return the abstract dto base
     * @throws JSONException the json exception
     */
    @JsonIgnore
    public AbstractDtoBase<T> assertEquals(final AbstractDtoBase<?> actual) throws JSONException {
        return assertEquals(((AbstractDtoBase<?>) actual).toJson());
    }

    /**
     * Assert equals abstract dto base.
     *
     * @param actualJson the actual json
     * @return the abstract dto base
     * @throws JSONException the json exception
     */
    @JsonIgnore
    public AbstractDtoBase<T> assertEquals(final String actualJson) throws JSONException {
        JSONAssert.assertEquals(this.toJson(), actualJson, true);
        return this;
    }

    /**
     * Gets additional properties.
     *
     * @return the additional properties
     */
    @JsonIgnore
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Sets additional property.
     *
     * @param name  the name
     * @param value the value
     */
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

    /**
     * Is allow unknown boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    protected boolean isAllowUnknown() {
        return allowUnknown;
    }

    /**
     * Sets allow unknown.
     *
     * @param allowUnknown the allow unknown
     * @return the allow unknown
     */
    @JsonIgnore
    protected T setAllowUnknown(final boolean allowUnknown) {
        this.allowUnknown = allowUnknown;
        return getSelf();
    }

    /**
     * Gets to string style.
     *
     * @return the to string style
     */
    @JsonIgnore
    public ToStringStyle getToStringStyle() {
        return toStringStyle;
    }

    /**
     * Sets to string style.
     *
     * @param toStringStyle the to string style
     */
    @JsonIgnore
    public void setToStringStyle(final ToStringStyle toStringStyle) {
        this.toStringStyle = toStringStyle;
    }

}
