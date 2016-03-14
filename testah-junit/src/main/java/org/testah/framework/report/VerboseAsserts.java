package org.testah.framework.report;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.testah.framework.dto.StepActionDto;
import org.testah.framework.testPlan.AbstractTestPlan;

@SuppressWarnings("deprecation")
public class VerboseAsserts {

	private boolean throwExceptionOnFail = true;
	private boolean recordHistory = true;

	public VerboseAsserts() {

	}

	public VerboseAsserts(final boolean throwExceptionOnFail) {
		setThrowExceptionOnFail(throwExceptionOnFail);
		;
	}

	public boolean same(final Object expected, final Object actual) {
		try {
			Assert.assertSame(expected, actual);
			return addAssertHistory("", true, "assertSame", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertSame", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean same(final String message, final Object expected, final Object actual) {
		try {
			Assert.assertSame(message, expected, actual);
			return addAssertHistory(message, true, "assertSame", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertSame", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean fail() {
		try {
			Assert.fail();
			return addAssertHistory("", false, "fail", null, null);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "fail", null, null, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean fail(final String message) {
		try {
			Assert.fail(message);
			return addAssertHistory(message, false, "fail", null, null);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "fail", null, null, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notNull(final String expected, final Object actual) {
		try {
			Assert.assertNotNull(expected, actual);
			return addAssertHistory("", true, "assertNotNull", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotNull", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notNull(final Object actual) {
		try {
			Assert.assertNotNull(actual);
			return addAssertHistory("", true, "assertNotNull", null, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotNull", null, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isNull(final String message, final Object actual) {
		try {
			Assert.assertNull(message, actual);
			return addAssertHistory(message, true, "assertNull", null, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNull", null, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isNull(final Object actual) {
		try {
			Assert.assertNull(actual);
			return addAssertHistory("", true, "assertNull", null, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNull", null, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notSame(final String message, final Object expected, final Object actual) {
		try {
			Assert.assertNotSame(message, expected, actual);
			return addAssertHistory(message, true, "assertNotSame", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotSame", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notSame(final Object expected, final Object actual) {
		try {
			Assert.assertNotSame(expected, actual);
			return addAssertHistory("", true, "assertNotSame", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotSame", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final double expected, final double actual) {
		try {
			Assert.assertEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final long expected, final long actual) {
		try {
			Assert.assertEquals(expected, actual);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final Object[] expected, final Object[] actual) {
		try {
			Assert.assertEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final Object expected, final Object actual) {
		try {
			Assert.assertEquals(expected, actual);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final Object[] expected, final Object[] actual) {
		try {
			Assert.assertEquals(expected, actual);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final double expected, final double actual, final double arg3) {
		try {
			Assert.assertEquals(message, expected, actual, arg3);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final Object expected, final Object actual) {
		try {
			Assert.assertEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final float expected, final float actual, final float delta) {
		try {
			Assert.assertEquals(expected, actual, delta);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final double expected, final double actual, final double delta) {
		try {
			Assert.assertEquals(expected, actual, delta);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final float expected, final float actual, final float arg3) {
		try {
			Assert.assertEquals(message, expected, actual, arg3);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final String message, final long expected, final long actual) {
		try {
			Assert.assertEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean equals(final double expected, final double actual) {
		try {
			Assert.assertEquals(expected, actual);
			return addAssertHistory("", true, "assertEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isFalse(final String expected, final boolean actual) {
		try {
			Assert.assertFalse(expected, actual);
			return addAssertHistory("", true, "assertFalse", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertFalse", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isFalse(final boolean actual) {
		try {
			Assert.assertFalse(actual);
			return addAssertHistory("", true, "assertFalse", false, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertFalse", false, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isTrue(final String expected, final boolean actual) {
		try {
			Assert.assertTrue(expected, actual);
			return addAssertHistory("", true, "assertTrue", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertTrue", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isTrue(final boolean actual) {
		try {
			Assert.assertTrue(actual);
			return addAssertHistory("", true, "assertTrue", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertTrue", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final Object expected, final Object actual) {
		try {
			Assert.assertNotEquals(expected, actual);
			return addAssertHistory("", true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final String message, final Object expected, final Object actual) {
		try {
			Assert.assertNotEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final float unexpected, final float actual, final float delta) {
		try {
			Assert.assertNotEquals(unexpected, actual, delta);
			return addAssertHistory("", true, "assertNotEquals", unexpected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotEquals", unexpected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final String message, final float expected, final float actual, final float arg3) {
		try {
			Assert.assertNotEquals(message, expected, actual, arg3);
			return addAssertHistory(message, true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final String message, final double expected, final double actual, final double arg3) {
		try {
			Assert.assertNotEquals(message, expected, actual, arg3);
			return addAssertHistory(message, true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final long expected, final long actual) {
		try {
			Assert.assertNotEquals(expected, actual);
			return addAssertHistory("", true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final double unexpected, final double actual, final double delta) {
		try {
			Assert.assertNotEquals(unexpected, actual, delta);
			return addAssertHistory("", true, "assertNotEquals", unexpected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertNotEquals", unexpected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notEquals(final String message, final long expected, final long actual) {
		try {
			Assert.assertNotEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertNotEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public <T> boolean that(final T actual, final Matcher<? super T> matcher) {
		try {
			Assert.assertThat(actual, matcher);
			return addAssertHistory("", true, "assertThat", actual, matcher);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertThat", actual, matcher, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public <T> boolean that(final String message, final T actual, final Matcher<? super T> matcher) {
		try {
			Assert.assertThat(message, actual, matcher);
			return addAssertHistory(message, true, "assertThat", actual, matcher);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertThat", actual, matcher, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final byte[] expected, final byte[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final byte[] expected, final byte[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final char[] expected, final char[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final double[] expected, final double[] actual,
			final double arg3) {
		try {
			Assert.assertArrayEquals(message, expected, actual, arg3);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final Object[] expected, final Object[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final Object[] expected, final Object[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final boolean[] expected, final boolean[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final boolean[] expected, final boolean[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final long[] expected, final long[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final double[] expecteds, final double[] actuals, final double delta) {
		try {
			Assert.assertArrayEquals(expecteds, actuals, delta);
			return addAssertHistory("", true, "assertArrayEquals", expecteds, actuals);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expecteds, actuals, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final float[] expecteds, final float[] actuals,
			final float delta) {
		try {
			Assert.assertArrayEquals(message, expecteds, actuals, delta);
			return addAssertHistory(message, true, "assertArrayEquals", expecteds, actuals);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expecteds, actuals, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final float[] expecteds, final float[] actuals, final float delta) {
		try {
			Assert.assertArrayEquals(expecteds, actuals, delta);
			return addAssertHistory("", true, "assertArrayEquals", expecteds, actuals);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expecteds, actuals, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final char[] expected, final char[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final short[] expected, final short[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final short[] expected, final short[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final int[] expected, final int[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final int[] expected, final int[] actual) {
		try {
			Assert.assertArrayEquals(expected, actual);
			return addAssertHistory("", true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory("", false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean arrayEquals(final String message, final long[] expected, final long[] actual) {
		try {
			Assert.assertArrayEquals(message, expected, actual);
			return addAssertHistory(message, true, "assertArrayEquals", expected, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertArrayEquals", expected, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	/***************************************************************************************/

	public boolean pass() {
		return pass("");
	}

	public boolean pass(final String message) {
		return addAssertHistory(message, true, "pass", null, null, null);
	}

	public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
			final Object actual, final Object expected) {
		return addAssertHistory(message, status, assertMethod, actual, expected, null);
	}

	public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
			final Object actual, final Object expected, final AssertionError exception) {

		if (isRecordHistory()) {
			AbstractTestPlan.addAssertHistory(
					new StepActionDto().addAssertResult(message, status, assertMethod, actual, expected, exception));
		}
		return status;
	}

	public boolean getThrowExceptionOnFail() {
		return throwExceptionOnFail;
	}

	public boolean isThrowExceptionOnFail() {
		return getThrowExceptionOnFail();
	}

	public void setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	public boolean isRecordHistory() {
		return recordHistory;
	}

	public void setRecordHistory(final boolean recordHistory) {
		this.recordHistory = recordHistory;
	}

	public VerboseAsserts onlyVerfiy() {
		this.recordHistory = false;
		this.throwExceptionOnFail = false;
		return this;
	}

}
