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

public class ParamLoader {

	private PropertiesConfiguration paramsFromProperties = null;

	private static final String fieldPrefix = "param.";
	private final String pathToParamPropFile;

	public ParamLoader() {
		this(System.getProperty("user.dir") + File.separator + "testah.properties");
	}

	public static String getDefaultPropFilePath() {
		return System.getProperty("user.dir") + File.separator + "testah.properties";
	}

	public ParamLoader(final String pathToParamPropFile) {
		this.pathToParamPropFile = pathToParamPropFile;
	}

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
						TS.log().warn(e);
					} finally {
						field.setAccessible(accessible);
					}
				}

				// getCustomParamProperties
				final PropertiesConfiguration config = getCustomParamProperties(f);

				for (final Field field : Params.class.getDeclaredFields()) {

					accessible = field.isAccessible();
					try {
						field.setAccessible(true);
						if (null != config.getProperty(fieldPrefix + field.getName())) {
							if (field.getType().isAssignableFrom(String.class)) {

								field.set(params, config.getProperty(fieldPrefix + field.getName()));
							} else if (field.getType().isAssignableFrom(int.class)) {
								field.setInt(params,
										Integer.parseInt((String) config.getProperty(fieldPrefix + field.getName())));
							} else if (field.getType().isAssignableFrom(Long.class)) {
								field.set(params,
										(Long.parseLong((String) config.getProperty(fieldPrefix + field.getName()))));
							} else if (field.getType().isAssignableFrom(boolean.class)) {
								field.setBoolean(params, Boolean
										.parseBoolean((String) config.getProperty(fieldPrefix + field.getName())));
							} else if (((Class<?>) field.getType()).isEnum()) {
								field.set(params, Enum.valueOf((Class<Enum>) field.getType(),
										(String) config.getProperty(fieldPrefix + field.getName())));
							} else if (field.getType() == Level.class) {
								field.set(params,
										Level.toLevel((String) config.getProperty(fieldPrefix + field.getName())));
							} else {
								field.set(params, config.getProperty(fieldPrefix + field.getName()));
							}
						}
					} catch (final Exception e) {
						TS.log().warn(e);
					} finally {
						try {
							TS.log().trace(field.getName() + " = " + field.get(params));
						} catch (final Exception e2) {

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

	public ParamLoader overwriteDefaultConfig() {
		try {
			getDefaultParamProperties().save(new File(pathToParamPropFile));
		} catch (final ConfigurationException e) {
			TS.log().warn(e);
		}
		return this;
	}

	public PropertiesConfiguration getParams() {
		return paramsFromProperties;
	}

	public PropertiesConfiguration getCustomParamProperties(final File customPropfile) throws ConfigurationException {
		return new PropertiesConfiguration(customPropfile);
	}

	public PropertiesConfiguration getDefaultParamProperties() {
		final PropertiesConfiguration defaultConfig = new PropertiesConfiguration();
		final PropertiesConfigurationLayout layout = defaultConfig.getLayout();

		boolean accessible;
		final Params params = new Params();
		Comment comment = null;
		for (final Field f : Params.class.getDeclaredFields()) {
			accessible = f.isAccessible();
			try {
				f.setAccessible(true);
				defaultConfig.addProperty(fieldPrefix + f.getName(), f.get(params));
				comment = f.getAnnotation(Comment.class);
				if (null != comment) {
					if (f.getType().isEnum()) {
						layout.setComment(fieldPrefix + f.getName(),
								comment.info() + "values: " + Arrays.toString(f.getType().getEnumConstants()));
					} else {
						layout.setComment(fieldPrefix + f.getName(), comment.info());
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
