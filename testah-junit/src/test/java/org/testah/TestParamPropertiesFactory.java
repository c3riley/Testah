package org.testah;

import org.junit.Test;
import org.testah.framework.cli.ParamLoader;

public class TestParamPropertiesFactory {

	@Test
	public void testParamPropertiesFactory() {
		new ParamLoader().loadParamValues();
		TS.log().info("RRRRRRRRRRRRRRRRRR");
	}

}
