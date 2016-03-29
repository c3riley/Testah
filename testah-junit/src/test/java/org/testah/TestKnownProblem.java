package org.testah;

import org.junit.Assert;
import org.junit.Test;
import org.testah.framework.annotations.KnownProblem;
import org.testah.client.enums.TypeOfKnown;

@KnownProblem(description = "test")
public class TestKnownProblem {

	@SuppressWarnings("deprecation")
	@Test
	public void testKnownProblemTestPlan() {
		final KnownProblem k = this.getClass().getAnnotation(KnownProblem.class);
		Assert.assertEquals("test", k.description());
		Assert.assertEquals(new String[] {}, k.linkedIds());
		Assert.assertEquals(TypeOfKnown.DEFECT, k.typeOfKnown());
	}

}
