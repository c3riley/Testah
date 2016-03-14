package org.testah.framework.cli;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.testah.TS;
import org.testah.framework.annotations.Comment;

public class ParamLoader {

	private PropertiesConfiguration paramsFromProperties = null;
	private CompositeConfiguration config = null;
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
				config = new CompositeConfiguration();
				config.addConfiguration(paramsFromProperties);
				config.addConfiguration(getCustomParamProperties(f));
				boolean accessible;
				for (final Field field : Params.class.getDeclaredFields()) {
					accessible = field.isAccessible();
					try {
						field.setAccessible(true);
						field.set(params, config.getProperty(fieldPrefix + field.getName()));
					} catch (final Exception e) {
						TS.log().warn(e);
					} finally {
						field.setAccessible(accessible);
					}
				}

			} else {
				TS.log().warn("Issue loading custom Propfile[" + f.getAbsolutePath()
						+ "] - was not found, will create one for the next runs use");
				try {
					paramsFromProperties.save(f);
				} catch (final Exception e) {
					TS.log().warn("Issue saving custom Propfile[" + f.getAbsolutePath() + "]", e);
				}
			}
		} catch (final ConfigurationException e) {
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
