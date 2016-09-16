package org.testah.framework.cli;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.logging.log4j.Level;
import org.testah.TS;
import org.testah.framework.annotations.Comment;

/**
 * The Class ParamLoader.
 */
public class ParamLoader {

    /** The params from properties. */
    private PropertiesConfiguration paramsFromProperties = null;

    /** The Constant fieldPrefix. */
    private static final String fieldPrefix = "param_";

    /** The path to param prop file. */
    private final String pathToParamPropFile;

    /**
     * Instantiates a new param loader.
     */
    public ParamLoader() {
        this(System.getProperty("user.dir") + File.separator + "testah.properties");
    }

    /**
     * Gets the default prop file path.
     *
     * @return the default prop file path
     */
    public static String getDefaultPropFilePath() {
        return System.getProperty("user.dir") + File.separator + "testah.properties";
    }

    /**
     * Instantiates a new param loader.
     *
     * @param pathToParamPropFile
     *            the path to param prop file
     */
    public ParamLoader(final String pathToParamPropFile) {
        this.pathToParamPropFile = pathToParamPropFile;
    }

    /**
     * Load param values.
     *
     * @return the params
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Params loadParamValues() {

        final Params params = new Params();
        if (null == pathToParamPropFile) {
            TS.log().warn("unable to use param.properties, value is null!");
            return params;
        }
        try {
            paramsFromProperties = getDefaultParamProperties();

            final File f = new File(pathToParamPropFile);
            if (f.exists()) {

                // paramsFromProperties
                boolean accessible;
                for (final Field field : Params.class.getDeclaredFields()) {

                    accessible = field.isAccessible();
                    try {
                        field.setAccessible(true);
                        field.set(params, paramsFromProperties.getProperty(fieldPrefix + field.getName()));
                    } catch (final Exception e) {
                        TS.log().trace(e);
                    } finally {
                        field.setAccessible(accessible);
                    }
                }

                // getCustomParamProperties
                final PropertiesConfiguration config = getCustomParamProperties(f);

                // Get all params even unknown or added ones and put into a hash for use.
                config.getKeys().forEachRemaining(key -> {
                    if (null != key) {
                        params.getOther().put(key, config.getString(key));
                    }
                });

                String filterSchema = (String) config.getProperty("param_usefilterSchema");
                if (null == filterSchema || filterSchema.length() == 0) {
                    filterSchema = "NOT_USED";
                }

                String key;
                config.getKeys().forEachRemaining(value -> {
                    System.setProperty(value, config.getString(value));
                });

                Object propValue = null;
                String propName;
                for (final Field field : Params.class.getDeclaredFields()) {
                    if (field.getName().startsWith("filter")) {
                        propName = "filter_" + filterSchema + "_" + field.getName();
                        propValue = System.getProperty(propName, System.getenv(propName));
                        if (null == propValue) {
                            propValue = config.getProperty(propName);
                        } else {
                            TS.log().debug("Loaded from system: " + propName + " = " + propValue);
                        }
                    } else {
                        propName = fieldPrefix + field.getName();
                        propValue = System.getProperty(propName, System.getenv(propName));
                        if (null == propValue) {
                            propValue = config.getProperty(propName);
                        } else {
                            TS.log().debug("Loaded from system: " + propName + " = " + propValue);
                        }
                    }
                    accessible = field.isAccessible();
                    try {
                        field.setAccessible(true);
                        if (null != propValue) {
                            if (field.getType().isAssignableFrom(String.class)) {
                                field.set(params, propValue);
                            } else if (field.getType().isAssignableFrom(int.class)) {
                                field.setInt(params, Integer.parseInt((String) propValue));
                            } else if (field.getType().isAssignableFrom(Long.class)) {
                                field.set(params, (Long.parseLong((String) propValue)));
                            } else if (field.getType().isAssignableFrom(boolean.class)) {
                                field.setBoolean(params, Boolean.parseBoolean((String) propValue));
                            } else if (field.getType().isAssignableFrom(Boolean.class)) {
                                TS.log().info(field.getName());
                                if (0 == propValue.toString().trim().length()) {
                                    field.set(params, Boolean.parseBoolean((String) propValue));
                                } else {
                                    field.setBoolean(params, Boolean.parseBoolean((String) propValue));
                                }
                            } else if (((Class<?>) field.getType()).isEnum()) {
                                field.set(params, Enum.valueOf((Class<Enum>) field.getType(), (String) propValue));
                            } else if (field.getType() == Level.class) {
                                field.set(params, Level.toLevel((String) propValue));
                            } else {
                                field.set(params, propValue);
                            }
                        }
                    } catch (final Exception e) {
                        TS.log().warn(e);
                    } finally {
                        try {
                            TS.log().trace(field.getName() + " = " + field.get(params));
                        } catch (final Exception e2) {
                            TS.log().trace(e2);
                        }
                        field.setAccessible(accessible);
                    }
                }

            } else {
                TS.log().warn("Issue loading custom properties[" + f.getAbsolutePath()
                        + "] - was not found, will create one for the next runs use");
                try {
                    paramsFromProperties.save(f);
                } catch (final Exception e) {
                    TS.log().warn("Issue saving custom Propfile[" + f.getAbsolutePath() + "]", e);
                }
            }
        } catch (

        final ConfigurationException e)

        {
            TS.log().warn("Issues with testah.properties");
        }
        return params;

    }

    /**
     * Overwrite default config.
     *
     * @return the param loader
     */
    public ParamLoader overwriteDefaultConfig() {
        try {
            getDefaultParamProperties().save(new File(pathToParamPropFile));
        } catch (final ConfigurationException e) {
            TS.log().warn(e);
        }
        return this;
    }

    /**
     * Gets the params.
     *
     * @return the params
     */
    public PropertiesConfiguration getParams() {
        return paramsFromProperties;
    }

    /**
     * Gets the custom param properties.
     *
     * @param customPropfile
     *            the custom propfile
     * @return the custom param properties
     * @throws ConfigurationException
     *             the configuration exception
     */
    public PropertiesConfiguration getCustomParamProperties(final File customPropfile) throws ConfigurationException {
        return new PropertiesConfiguration(customPropfile);
    }

    /**
     * Gets the default param properties.
     *
     * @return the default param properties
     */
    public PropertiesConfiguration getDefaultParamProperties() {
        final PropertiesConfiguration defaultConfig = new PropertiesConfiguration();
        final PropertiesConfigurationLayout layout = defaultConfig.getLayout();
        layout.setHeaderComment(
                Cli.BAR_LONG + "\nTestah Properties - version: " + Cli.version + " - File Created: " + TS.util().now()
                        + "\nNo values are required. Leaving a key empty will not use the value, turning the property off."
                        + "\n" + Cli.BAR_LONG);
        boolean accessible;
        final Params params = new Params();
        Comment comment = null;
        String propName = null;
        String commentValue = null;
        for (final Field f : Params.class.getDeclaredFields()) {
            accessible = f.isAccessible();
            try {
                f.setAccessible(true);
                if (f.getName().startsWith("filter")) {
                    propName = "filter_DEFAULT_" + f.getName();
                } else {
                    propName = fieldPrefix + f.getName();
                }
                defaultConfig.addProperty(propName, f.get(params));
                comment = f.getAnnotation(Comment.class);
                if (null != comment) {
                    commentValue = comment.info().replace("[BAR1]", "\n\n" + Cli.BAR_SHORT + "\n").replace("[BAR2]",
                            "\n" + Cli.BAR_SHORT + "\n\n");
                    if (f.getType().isEnum()) {
                        layout.setComment(propName,
                                commentValue + "values: " + Arrays.toString(f.getType().getEnumConstants()));
                    } else {
                        layout.setComment(propName, commentValue);
                    }
                }
            } catch (final Exception e) {
                TS.log().warn(e);
            } finally {
                f.setAccessible(accessible);
            }
        }
        return defaultConfig;
    }

}
