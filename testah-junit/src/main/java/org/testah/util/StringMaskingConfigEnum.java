package org.testah.util;

import org.apache.logging.log4j.Level;
import org.testah.TS;

import javax.naming.ConfigurationException;

/**
 * When masking a String, each occurrence of that String is masked. When doing "bulk" masking, e.g. all values from
 * a secret store, in fact there may be Strings that should be not be marked for masking (e.g. a value may be {@code true},
 * and masking all {@code true} is probably not a good idea. Specifying a generic configuration for masking may help.
 * The configuration also allows to show a few characters of a secret to help with debugging a problem.
 * <p>
 * For singleton enum pattern see e.g.
 * https://www.callicoder.com/java-singleton-design-pattern-example/
 * https://www.baeldung.com/java-singleton
 * https://github.com/eugenp/tutorials/blob/master/patterns/design-patterns-creational/src/main/java/com/baeldung/singleton/EnumSingleton.java
 * </p>
 */
public enum StringMaskingConfigEnum
{
    INSTANCE;

    // system environment parameter name to override behavior, for testing only
    public static final String TEST_MODE = "STRING_MASKING_TEST_MODE";
    // Minimal length of string to be masked. Using the value 'e' as a secret and trying to mask it, is a terrible idea.
    public static final int DEFAULT_MIN_STRING_LENGTH = 7;
    public static final int DEFAULT_FIRST_N = 2;
    public static final int DEFAULT_LAST_N = 2;
    public static final String MASKING_PATTERN = "%s***%s";

    public static final String USING_DEFAULT_CONFIG = "No string masking configuration set. Using default configuration.";

    private ConfigOptions configOptions;

    StringMaskingConfigEnum()
    {
        this.configOptions = new ConfigOptions();
    }

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
    public StringMaskingConfigEnum createInstance(int minStringLength, int firstN, int lastN)
    {
        synchronized (configOptions)
        {
            // for testing purposed allow override of existing configuration
            if (!configOptions.isInitialized)
            {
                configOptions.minStringLength = minStringLength;
                configOptions.firstN = firstN;
                configOptions.lastN = lastN;
                configOptions.isInitialized = true;
            }
        }
        return INSTANCE;
    }

    /**
     * Get the configuration for masking strings. Before calling this method {@code createInstance} should have been called.
     * If not, the default configuration is used. A warning will be logged.
     * When using debug level, the stack trace of where the call was made before explicitly creating an instance is printed.
     * @return (singleton) instance of masking configuration
     */
    public StringMaskingConfigEnum getInstance()
    {
        synchronized (configOptions)
        {
            if (!configOptions.isInitialized)
            {
                log(USING_DEFAULT_CONFIG);
                createInstance(DEFAULT_MIN_STRING_LENGTH, DEFAULT_FIRST_N, DEFAULT_LAST_N);
            }
        }
        return INSTANCE;
    }

    /**
     * Get the number of starting characters that are not masked.
     * @return number of starting characters that are not masked
     */
    public int getFirstN()
    {
        return configOptions.firstN;
    }

    /**
     * Get the number of ending characters that are not masked.
     * @return number of ending characters that are not masked
     */
    public int getLastN()
    {
        return configOptions.lastN;
    }

    /**
     * Get the number of characters required for masking.
     * @return number of characters required for masking
     */
    public int getMinStringLength()
    {
        return configOptions.minStringLength;
    }

    private void log(String msg)
    {
        TS.log().info(msg);
        if (TS.log().getLevel().equals(Level.DEBUG))
        {
            try
            {
                throw new ConfigurationException(msg);
            } catch (ConfigurationException x)
            {
                TS.log().debug(x);
            }
        }
    }

    private static class ConfigOptions
    {
        private int minStringLength;
        private int firstN;
        private int lastN;
        private volatile boolean isInitialized = false;
    }

    /**
     * Reset this enum to uninitialized. Only for unit testing purposes of this enum.
     * @return the singleton instance of this class
     */
    public StringMaskingConfigEnum reset()
    {
        configOptions.isInitialized = false;
        return this;
    }
}
