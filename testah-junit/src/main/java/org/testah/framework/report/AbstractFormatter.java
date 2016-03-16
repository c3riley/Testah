package org.testah.framework.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testah.TS;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.TestPlanDto;

public abstract class AbstractFormatter {

	public final static String DEFAULT_PACKAGE = "org/testah/templates/";
	public final String pathToTemplate;
	final TestPlanDto testPlan;

	public AbstractFormatter(final TestPlanDto testPlan, final String pathToTemplate) {
		this.testPlan = testPlan;
		this.pathToTemplate = pathToTemplate.replace("//", "/");
	}

	public VelocityContext getContextBase() {
		VelocityContext context = new VelocityContext();

		if (null != testPlan) {

			context.put("testPlan", testPlan);

			context = getContext(context);
		}

		return context;
	}

	public abstract VelocityContext getContext(final VelocityContext context);

	public String getReport() {
		return getReport(getContextBase());
	}

	public String getReport(final VelocityContext context) {

		try {

			final VelocityEngine ve = new VelocityEngine();
			ve.init();

			final InputStream in = this.getClass().getClassLoader().getResourceAsStream(pathToTemplate);

			final InputStreamReader reader = new InputStreamReader(in);

			final StringWriter writer = new StringWriter();
			ve.evaluate(context, writer, pathToTemplate, reader);

			return writer.toString();

		} catch (final Exception e) {
			TS.log().error(e);
			throw new RuntimeException("Velocity template", e);
		}

	}

	public abstract AbstractFormatter createReport();

	public AbstractFormatter createReport(final String reportName) {
		try {
			FileUtils.writeStringToFile(new File(Params.addUserDir(reportName)), getReport());
		} catch (final IOException e) {
			TS.log().error("issue creating report: " + reportName, e);
		}
		return this;
	}

}
