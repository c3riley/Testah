package org.testah.util;

import org.testah.TS;

import javax.naming.ConfigurationException;

public class StringMaskingConfig
{
    public static final String INFO_USE_DEFAULT_CONFIG = "No string masking configuration set explicitly set. Using default values.";

    private static final int defaultMinStringLength = 7;
    private static final int defaultFirstN = 2;
    private static final int defaultLastN = 2;
    private static final String defaultMaskingFiller = "***";
    public final int minStringLength;
    public final int firstN;
    public final int lastN;
    public final String maskingPattern;

    // use volatile to fix double-checked locking:
    // https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
    private static volatile StringMaskingConfig instance;

    public static StringMaskingConfig getInstance()
    {
        if (null == instance)
        {
            synchronized (StringMasking.class)
            {
                if (null == instance)
                {
                    try
                    {
                        throw new ConfigurationException(INFO_USE_DEFAULT_CONFIG);
                    }
                    catch(ConfigurationException x) {
                        TS.log().info(x.getMessage(), x);
                    }
                    finally
                    {
                        instance = new StringMaskingConfig(defaultMinStringLength, defaultFirstN, defaultLastN, defaultMaskingFiller);
                    }
                }
            }
        }
        return instance;
    }

    public static StringMaskingConfig getInstance(int minStringLength, int firstN, int lastN, String maskingFiller)
    {
        if (null == instance)
        {
            synchronized (StringMasking.class)
            {
                if (null == instance)
                {
                    instance = new StringMaskingConfig(minStringLength, firstN, lastN, maskingFiller);
                }
            }
        }
        return instance;
    }

    private StringMaskingConfig(int minStringLength, int firstN, int lastN, String maskingFiller)
    {
        this.firstN = firstN;
        this.lastN = lastN;
        this.maskingPattern = "%s" + maskingFiller + "%s";
        this.minStringLength = minStringLength;
    }

    protected void destroy()
    {
        instance = null;
    }
}
