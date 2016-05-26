
package org.testah;

import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.cli.ParamLoader;
import org.testah.framework.cli.Params;

public class TestParamPropertiesFactory {

	@Test
	public void testParamPropertiesFactory() {
		final Params params = new ParamLoader().loadParamValues();
		Assert.assertNotNull(params);
		Assert.assertNotNull(params.getBrowser());
	}

}
