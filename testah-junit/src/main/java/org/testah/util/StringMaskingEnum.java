package org.testah.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to handle masking of sensitive strings.
 * <p>
 * For singleton enum pattern see e.g.
 * https://www.callicoder.com/java-singleton-design-pattern-example/
 * https://www.baeldung.com/java-singleton
 * https://github.com/eugenp/tutorials/blob/master/patterns/design-patterns-creational/src/main/java/com/baeldung/singleton/EnumSingleton.java
 * </p>
 */
public enum StringMaskingEnum
{
    INSTANCE;

    private StringMaskingHelper stringMaskingHelper;
    private volatile Map<String, String> maskedValuesMap;

    StringMaskingEnum()
    {
        this.stringMaskingHelper = new StringMaskingHelper();
    }

    /**
     * Create the singleton instance of this class.
     * @return the singleton instance of this class
     */
    public StringMaskingEnum createInstance()
    {
        synchronized (stringMaskingHelper)
        {
            if (!stringMaskingHelper.isInitialized)
            {
                stringMaskingHelper.stringMaskingConfig = StringMaskingConfigEnum.INSTANCE.getInstance();
                stringMaskingHelper.literalMaskingExemptions = Stream.of("true", "TRUE", "false", "FALSE").collect(Collectors.toSet());
                stringMaskingHelper.regexMaskingExemptions = new HashSet<>();
                stringMaskingHelper.isInitialized = true;
                maskedValuesMap = new HashMap<>();
            }
        }
        return INSTANCE;
    }

    /**
     * Get the singleton instance of this class. Create one if it does not exist.
     * @return the singleton instance of this class
     */
    public StringMaskingEnum getInstance()
    {
        synchronized (stringMaskingHelper)
        {
            if (!stringMaskingHelper.isInitialized)
            {
                return createInstance();
            }
        }
        return INSTANCE;
    }

    /**
     * Get the plain text to masked values mapping.
     * @return the masking map
     */
    public Map<String, String> getMap()
    {
        return ImmutableMap.copyOf(maskedValuesMap);
    }

    /**
     * Get the masked value for a plain text string. If the plain text string is not registered for masking
     * return the plain text string.
     * @param plainValue the string to mask
     * @return the masked string or the original string if it should not be masked
     */
    public String getValue(String plainValue)
    {
        return maskedValuesMap.getOrDefault(plainValue, plainValue);
    }

    /**
     * Add a collection of regular expressions exempting string from being masked.
     * @param regexStrings the set of regular expressions to filter for strings that will not be masked
     * @return the singleton instance of this class
     */
    public StringMaskingEnum addRegexExemptions(String... regexStrings)
    {
        synchronized (stringMaskingHelper)
        {
            stringMaskingHelper.regexMaskingExemptions.addAll(Arrays.asList(regexStrings));
        }
        return INSTANCE;
    }

    /**
     * Remove a regular expression from exempting strings from being masked.
     * Regular expressions are mostly used when bulk loading strings, e.g. from a secret store.
     * For subsequent bulk loads from another store, the regular expression may apply anymore and
     * prevent actual secrets from being masked.
     * @param regex the regular expression to remove from filtering
     * @return the singleton instance of this class
     */
    public StringMaskingEnum removeRegexExemption(String regex)
    {
        synchronized (stringMaskingHelper)
        {
            stringMaskingHelper.regexMaskingExemptions.remove(regex);
        }
        return INSTANCE;
    }

    /**
     * Get the set of regular expressions to exempt strings from being masked.
     * @return the set of all regular expressions
     */
    public Set<String> getRegexExemptions()
    {
        return ImmutableSet.copyOf(stringMaskingHelper.regexMaskingExemptions);
    }

    /**
     * Exclude a specific set of strings from being masked. E.g. {@code org.testah} may not be so secret.
     * @param literals strings that should not be masked
     * @return the singleton instance of this class
     */
    public StringMaskingEnum addLiteralExemptions(String... literals)
    {
        synchronized (stringMaskingHelper)
        {
            stringMaskingHelper.literalMaskingExemptions.addAll(Arrays.asList(literals));
        }
        return INSTANCE;
    }

    /**
     * Get the set of literal strings exempt from being masked.
     * @return the singleton instance of this class
     */
    public Set<String> getLiteralExemptions()
    {
        return ImmutableSet.copyOf(stringMaskingHelper.literalMaskingExemptions);
    }

    /**
     * Add the given string to the masking map.
     * @param plainValue a string that will be masked
     * @return the singleton instance of this class
     */
    public StringMaskingEnum add(String plainValue)
    {
        String start = RandomStringUtils.randomAscii(stringMaskingHelper.stringMaskingConfig.getFirstN());
        String end = RandomStringUtils.randomAscii(stringMaskingHelper.stringMaskingConfig.getLastN());

        if (plainValue.length() > stringMaskingHelper.stringMaskingConfig.getMinStringLength())
        {
            start = plainValue.substring(0, stringMaskingHelper.stringMaskingConfig.getFirstN());
            end = plainValue.substring(plainValue.length() - stringMaskingHelper.stringMaskingConfig.getLastN());
        }
        maskedValuesMap.put(plainValue, String.format(StringMaskingConfigEnum.MASKING_PATTERN, start, end));
        return INSTANCE;
    }

    /**
     * Add the provided strings to the masking map, subject to the exemption rules.
     * @param plainValues a string that may be asked depending on exemptions
     * @return the singleton instance of this class
     */
    public StringMaskingEnum addBulk(String... plainValues)
    {
        for (String plainValue : plainValues)
        {
            if (needsMask(plainValue))
            {
                add(plainValue);
            }
        }
        return INSTANCE;
    }

    /**
     * Check whether a value should be masked according to the exemptions.
     * @param plainValue the original string
     * @return the masked value if masking is applicable or the original string
     */
    public boolean needsMask(String plainValue)
    {
        if (plainValue.length() <= stringMaskingHelper.stringMaskingConfig.getMinStringLength() ||
                stringMaskingHelper.literalMaskingExemptions.contains(plainValue))
        {
            return false;
        }
        for (String regex : stringMaskingHelper.regexMaskingExemptions)
        {
            if (plainValue.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Reset this enum to uninitialized. Only for unit testing purposes of this enum.
     * @return the singleton instance of this class
     */
    public StringMaskingEnum reset()
    {
        stringMaskingHelper.isInitialized = false;
        return this;
    }

    private static class StringMaskingHelper {
        private volatile Set<String> literalMaskingExemptions;
        private volatile Set<String> regexMaskingExemptions;
        private volatile StringMaskingConfigEnum stringMaskingConfig;
        private volatile boolean isInitialized = false;
    }
}
