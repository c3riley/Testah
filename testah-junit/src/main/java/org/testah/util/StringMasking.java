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

public class StringMasking
{
    private Set<String> literalMaskingExemptions;
    private Set<String> regexMaskingExemptions = new HashSet<>();
    private Map<String, String> maskedValuesMap = new HashMap<>();
    private final StringMaskingConfig stringMaskingConfig;

    // use volatile to fix double-checked locking:
    // https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
    private static volatile StringMasking instance;

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

    public Map<String, String> getMap()
    {
        return ImmutableMap.copyOf(maskedValuesMap);
    }

    public String getValue(String plainValue)
    {
        return maskedValuesMap.getOrDefault(plainValue, plainValue);
    }

    public StringMasking addRegexExemption(String plainValue)
    {
        regexMaskingExemptions.add(plainValue);
        return instance;
    }

    public StringMasking removeRegexExemption(String plainValue)
    {
        regexMaskingExemptions.remove(plainValue);
        return instance;
    }

    public StringMasking addRegexExemptions(Collection<String> plainValues)
    {
        regexMaskingExemptions.addAll(plainValues);
        return instance;
    }

    public Set<String> getRegexExemptions()
    {
        return ImmutableSet.copyOf(regexMaskingExemptions);
    }

    public StringMasking addLiteralExemption(String plainValue)
    {
        literalMaskingExemptions.add(plainValue);
        return instance;
    }

    public StringMasking addLiteralExemptions(Collection<String> plainValues)
    {
        literalMaskingExemptions.addAll(plainValues);
        return instance;
    }

    public Set<String> getLiteralExemptions()
    {
        return ImmutableSet.copyOf(literalMaskingExemptions);
    }

    public StringMasking addAll(Collection<String> plainValues)
    {
        plainValues.stream().forEach(this::add);
        return instance;
    }

    public StringMasking forceAdd(String plainValue)
    {
        String start;
        String end;
        if (plainValue.length() > stringMaskingConfig.minStringLength)
        {
            start = plainValue.substring(0, stringMaskingConfig.firstN);
            end = plainValue.substring(plainValue.length() - stringMaskingConfig.lastN);
        }
        else
        {
            start = RandomStringUtils.randomAscii(stringMaskingConfig.firstN);
            end = RandomStringUtils.randomAscii(stringMaskingConfig.lastN);
        }
        maskedValuesMap.put(plainValue, String.format(stringMaskingConfig.maskingPattern, start, end));
        return instance;
    }

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
        instance = null;
    }
}
