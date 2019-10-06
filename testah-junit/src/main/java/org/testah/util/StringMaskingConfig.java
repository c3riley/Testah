package org.testah.util;

import org.apache.logging.log4j.Level;
import org.testah.TS;

import javax.naming.ConfigurationException;

/**
 * When masking a String, each occurrence of that String is masked. When doing "bulk" masking, e.g. all values from
 * a secret store, in fact there may be Strings that should be not be marked for masking (e.g. a value may be {@code true},
 * and masking all {@code true} is probably not a good idea. Specifying a generic configuration for masking may help.
 * The configuration also allows to show a few characters of a secret to help with debugging a problem.
 */
public class StringMaskingConfig
{
    public static final String INFO_USE_DEFAULT_CONFIG = "No string masking configuration set. Using default configuration.";

    private static final int defaultMinStringLength = 7;
    private static final int defaultFirstN = 2;
    private static final int defaultLastN = 2;
    public final int minStringLength;
    public final int firstN;
    public final int lastN;
    public static final String maskingPattern = "%s***%s";

    // use volatile to fix double-checked locking:
    // https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
    private static volatile StringMaskingConfig instance;

    /**
     * Create configuration for masking strings.
     * Strings shorter than {@code minStringLength} is not masked unless {@code StringMasking.add(...)} is used.
     * The first {@code firstN} characters and {@code lastN} characters are not masked.
     * Using the default configuration, string {@code mySecretString} is masked as {@code my***ng}.
     * @param minStringLength minimal length of a String for being masked (unless using {@code StringMasking.add(...)})
     * @param firstN number of characters at the beginning of String that are shown in plain text
     * @param lastN number of characters at the end of String that are shown in plain text
     * @return (singleton) instance of masking configuration
     */
    public static StringMaskingConfig createInstance(int minStringLength, int firstN, int lastN)
    {
        if (null == instance)
        {
            synchronized (StringMasking.class)
            {
                if (null == instance)
                {
                    instance = new StringMaskingConfig(minStringLength, firstN, lastN);
                }
            }
        }
        return instance;
    }

    /**
     * Create configuration for masking strings using default values.
     * @return (singleton) instance of masking configuration
     */
    public static StringMaskingConfig createInstance()
    {
        return createInstance(defaultMinStringLength, defaultFirstN, defaultLastN);
    }

    /**
     * Get the configuration for masking strings. Before calling this method {@code createInstance} should have been called.
     * If not the default configuration is used. A warning will be logged.
     * When using debug level, the stack trace of where the call was made before explicitly creating an instance is printed.
     * @return (singleton) instance of masking configuration
     */
    public static StringMaskingConfig getInstance()
    {
        if (null == instance)
        {
            TS.log().warn(INFO_USE_DEFAULT_CONFIG);
            if (TS.log().getLevel().equals(Level.DEBUG))
            {
                printStackTrace(INFO_USE_DEFAULT_CONFIG);
            }
            createInstance();
        }
        return instance;
    }

    private static void printStackTrace(String message)
    {
        try
        {
            throw new ConfigurationException(message);
        } catch (ConfigurationException x)
        {
            TS.log().debug(x);
        }
    }

    private StringMaskingConfig(int minStringLength, int firstN, int lastN)
    {
        this.firstN = firstN;
        this.lastN = lastN;
        this.minStringLength = minStringLength;
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
