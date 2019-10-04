package org.testah.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Collection;
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
     * Add a regular expression exempting string from being masked. E.g. adding {@code .*@gmail.com} will prevent
     * {@code me@gmail.com} from being masked in logs.
     * @param regexString the regex to filter for strings that will not be masked
     * @return the singleton instance of this class
     */
    public StringMasking addRegexExemption(String regexString)
    {
        regexMaskingExemptions.add(regexString);
        return instance;
    }

    /**
     * Add a collection of regular expressions exempting string from being masked.
     * @param regexStrings the set of regular expressions to filter for strings that will not be masked
     * @return the singleton instance of this class
     */
    public StringMasking addRegexExemptions(Collection<String> regexStrings)
    {
        regexMaskingExemptions.addAll(regexStrings);
        return instance;
    }

    /**
     * Remove a regular expression from exempting strings from being masked.
     * Regular expressions are mostly used when bulk loading strings, e.g. from a secret store.
     * For subsequent bulk loads from another store, the regular expression may apply anymore and
     * prevent actual secrets from being masked.
     * @param regexString the regular expression to remove from filtering
     * @return the singleton instance of this class
     */
    public StringMasking removeRegexExemption(String regexString)
    {
        regexMaskingExemptions.remove(regexString);
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
     * Exclude a specific string from being masked. E.g. {@code org.testah} may not be so secret.
     * @param plainValue string that should not be masked
     * @return the singleton instance of this class
     */
    public StringMasking addLiteralExemption(String plainValue)
    {
        literalMaskingExemptions.add(plainValue);
        return instance;
    }

    /**
     * Exclude a specific set of strings from being masked. E.g. {@code org.testah} may not be so secret.
     * @param plainValues strings that should not be masked
     * @return the singleton instance of this class
     */
    public StringMasking addLiteralExemptions(Collection<String> plainValues)
    {
        literalMaskingExemptions.addAll(plainValues);
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
     * Add the provided list of strings to the masking map. Each string is evaluated if it should be masked.
     * @param plainValues strings that are subject to masking, depending on filtering
     * @return the singleton instance of this class
     */
    public StringMasking addAll(Collection<String> plainValues)
    {
        plainValues.stream().forEach(this::add);
        return instance;
    }

    /**
     * Add the given string to the masking map, even if there are rules to exclude it.
     * @param plainValue a string that will be masked
     * @return the singleton instance of this class
     */
    public StringMasking forceAdd(String plainValue)
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
     * Add the provided string to the masking map, subject to the exemption rules.
     * @param plainValue a string that may be asked depending on exemptions
     * @return the singleton instance of this class
     */
    public StringMasking add(String plainValue)
    {
        if (plainValue.length() <= stringMaskingConfig.minStringLength)
        {
            return instance;
        }
        if (literalMaskingExemptions.contains(plainValue))
        {
            return instance;
        }
        for (String regex : regexMaskingExemptions)
        {
            if (plainValue.matches(regex)) {
                return instance;
            }
        }
        String start = plainValue.substring(0, stringMaskingConfig.firstN);
        String end = plainValue.substring(plainValue.length() - stringMaskingConfig.lastN);
        maskedValuesMap.put(plainValue, String.format(stringMaskingConfig.maskingPattern, start, end));
        return instance;
    }

    protected void destroy()
    {
        if (null != instance)
        {
            synchronized (instance)
            {
                instance = null;
            }
        }
    }
}
