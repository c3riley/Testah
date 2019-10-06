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
 */
public class StringMasking
{
    private Set<String> literalMaskingExemptions;
    private Set<String> regexMaskingExemptions = new HashSet<>();
    private Map<String, String> maskedValuesMap = new HashMap<>();
    private final StringMaskingConfig stringMaskingConfig;

    // use volatile to fix double-checked locking:
    // https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
    private static volatile StringMasking instance;

    /**
     * Get the singlen instance of this class. Create one if it does not exist.
     * @return the singleton instance of this class
     */
    public static StringMasking getInstance()
    {
        if (null == instance)
        {
            synchronized (StringMasking.class)
            {
                if (null == instance)
                {
                    instance = new StringMasking();
                }
            }
        }
        return instance;
    }

    private StringMasking()
    {
        stringMaskingConfig = StringMaskingConfig.getInstance();
        literalMaskingExemptions = Stream.of("true", "TRUE", "false", "FALSE").collect(Collectors.toSet());
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
    public StringMasking addRegexExemptions(String... regexStrings)
    {
        regexMaskingExemptions.addAll(Arrays.asList(regexStrings));
        return instance;
    }

    /**
     * Remove a regular expression from exempting strings from being masked.
     * Regular expressions are mostly used when bulk loading strings, e.g. from a secret store.
     * For subsequent bulk loads from another store, the regular expression may apply anymore and
     * prevent actual secrets from being masked.
     * @param regex the regular expression to remove from filtering
     * @return the singleton instance of this class
     */
    public StringMasking removeRegexExemption(String regex)
    {
        regexMaskingExemptions.remove(regex);
        return instance;
    }

    /**
     * Get the set of regular expressions to exempt strings from being masked.
     * @return the set of all regular expressions
     */
    public Set<String> getRegexExemptions()
    {
        return ImmutableSet.copyOf(regexMaskingExemptions);
    }

    /**
     * Exclude a specific set of strings from being masked. E.g. {@code org.testah} may not be so secret.
     * @param literals strings that should not be masked
     * @return the singleton instance of this class
     */
    public StringMasking addLiteralExemptions(String... literals)
    {
        literalMaskingExemptions.addAll(Arrays.asList(literals));
        return instance;
    }

    /**
     * Get the set of literal strings exempt from being masked.
     * @return the singleton instance of this class
     */
    public Set<String> getLiteralExemptions()
    {
        return ImmutableSet.copyOf(literalMaskingExemptions);
    }

    /**
     * Add the given string to the masking map.
     * @param plainValue a string that will be masked
     * @return the singleton instance of this class
     */
    public StringMasking add(String plainValue)
    {
        String start;
        String end;
        if (plainValue.length() > stringMaskingConfig.minStringLength)
        {
            start = plainValue.substring(0, stringMaskingConfig.firstN);
            end = plainValue.substring(plainValue.length() - stringMaskingConfig.lastN);
        } else
        {
            start = RandomStringUtils.randomAscii(stringMaskingConfig.firstN);
            end = RandomStringUtils.randomAscii(stringMaskingConfig.lastN);
        }
        maskedValuesMap.put(plainValue, String.format(stringMaskingConfig.maskingPattern, start, end));
        return instance;
    }

    /**
     * Add the provided strings to the masking map, subject to the exemption rules.
     * @param plainValues a string that may be asked depending on exemptions
     * @return the singleton instance of this class
     */
    public StringMasking addBulk(String... plainValues)
    {
        for (String plainValue : plainValues)
        {
            if (isMustMask(plainValue))
            {
                add(plainValue);
            }
        }
        return instance;
    }

    /**
     * Destroy method.
     */
    public void destroy()
    {
        if (null != instance)
        {
            synchronized (instance)
            {
                instance = null;
            }
        }
    }

    private boolean isMustMask(String plainValue)
    {
        if (plainValue.length() <= stringMaskingConfig.minStringLength || literalMaskingExemptions.contains(plainValue))
        {
            return false;
        }
        for (String regex : regexMaskingExemptions)
        {
            if (plainValue.matches(regex)) {
                return false;
            }
        }
        return true;
    }
}
