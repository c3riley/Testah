package org.testah.framework.report;

import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testah.TS;
import org.testah.framework.dto.StepActionDto;

@SuppressWarnings("deprecation")
public class VerboseAsserts {

	private boolean recordSteps = true;
	private boolean throwExceptionOnFail = true;
	private boolean isVerifyOnly = false;

	public VerboseAsserts() {
		setRecordSteps(TS.params().isRecordSteps());
		throwExceptionOnFail = TS.params().isThrowExceptionOnFail();
	}

	public VerboseAsserts(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	// final JsonNode jsonNode=new ObjectMapper().valueToTree(tp);
	// final JSONAssert.
	public boolean sameJson(final Object expected, final Object actual) {
		return sameJson(expected, actual, true);
	}

	public boolean sameJson(final Object expected, final Object actual, final boolean strict) {
		final JSONObject expectedJsonNode = new JSONObject(expected);
		final JSONObject actualJsonNode = new JSONObject(actual);
		return sameJson(expectedJsonNode, actualJsonNode, strict);
	}

	public boolean sameJson(final JSONObject expected, final JSONObject actual, final boolean strict) {
		try {
			JSONAssert.assertEquals(expected, actual, strict);
			return addAssertHistory("", true, "assertSameJson", expected, actual);
		} catch (final Exception e) {
			final boolean rtn = addAssertHistory("", false, "assertSame", expected.toString(), actual.toString(), e);
			if (getThrowExceptionOnFail()) {
				throw new AssertionError(e);
			}
			return rtn;
		}
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

	public boolean notNull(final String message, final Object actual) {
		try {
			Assert.assertNotNull(message, actual);
			return addAssertHistory(message, true, "assertNotNull", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertNotNull", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean notNull(final Object actual) {
		return notNull("", actual);
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
		return isNull("", actual);
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

	public boolean isFalse(final String message, final boolean actual) {
		try {
			Assert.assertFalse(message, actual);
			return addAssertHistory(message, true, "assertFalse", false, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertFalse", false, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isFalse(final boolean actual) {
		return isFalse("", actual);
	}

	public boolean isTrue(final String message, final boolean actual) {
		try {
			Assert.assertTrue(message, actual);
			return addAssertHistory(message, true, "assertTrue", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "assertTrue", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	public boolean isTrue(final boolean actual) {
		return isTrue("", actual);
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
			final Object expected, final Object actual) {
		return addAssertHistory(message, status, assertMethod, expected, actual, null);
	}

	public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
			final Object expected, final Object actual, final Throwable exception) {
		if (isVerifyOnly()) {
			StepActionDto.createVerifyResult(message, status, assertMethod, expected, actual, exception).add();
		} else {
			StepActionDto.createAssertResult(message, status, assertMethod, expected, actual, exception).add();
		}

		return status;
	}

	public boolean getThrowExceptionOnFail() {
		return throwExceptionOnFail;
	}

	public boolean isThrowExceptionOnFail() {
		return getThrowExceptionOnFail();
	}

	public VerboseAsserts onlyVerfiy() {
		this.recordSteps = false;
		this.throwExceptionOnFail = false;
		this.setVerifyOnly(true);
		return this;
	}

	public boolean isRecordSteps() {
		return recordSteps;
	}

	public void setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	public void setRecordSteps(final boolean recordSteps) {
		this.recordSteps = recordSteps;
	}

	public boolean isVerifyOnly() {
		return isVerifyOnly;
	}

	public void setVerifyOnly(final boolean isVerifyOnly) {
		this.isVerifyOnly = isVerifyOnly;
	}

}
