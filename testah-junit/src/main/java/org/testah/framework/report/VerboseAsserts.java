package org.testah.framework.report;

import java.util.Set;

import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testah.TS;
import org.testah.framework.dto.StepAction;

/**
 * The Class VerboseAsserts.
 */
@SuppressWarnings("deprecation")
public class VerboseAsserts {

	/** The record steps. */
	private boolean recordSteps = true;

	/** The throw exception on fail. */
	private boolean throwExceptionOnFail = true;

	/** The is verify only. */
	private boolean isVerifyOnly = false;

	/**
	 * Instantiates a new verbose asserts.
	 */
	public VerboseAsserts() {
		setRecordSteps(TS.params().isRecordSteps());
		throwExceptionOnFail = TS.params().isThrowExceptionOnFail();
	}

	/**
	 * Instantiates a new verbose asserts.
	 *
	 * @param throwExceptionOnFail
	 *            the throw exception on fail
	 */
	public VerboseAsserts(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	// final JsonNode jsonNode=new ObjectMapper().valueToTree(tp);
	/**
	 * Same json.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
	// final JSONAssert.
	public boolean sameJson(final Object expected, final Object actual) {
		return sameJson(expected, actual, true);
	}

	/**
	 * Same json.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param strict
	 *            the strict
	 * @return true, if successful
	 */
	public boolean sameJson(final Object expected, final Object actual, final boolean strict) {
		final JSONObject expectedJsonNode = new JSONObject(expected);
		final JSONObject actualJsonNode = new JSONObject(actual);
		return sameJson(expectedJsonNode, actualJsonNode, strict);
	}

	/**
	 * Same json.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param strict
	 *            the strict
	 * @return true, if successful
	 */
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

	/**
	 * Same.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Same.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Fail.
	 *
	 * @return true, if successful
	 */
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

	/**
	 * Fail.
	 *
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
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

	/**
	 * Not null.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Not null.
	 *
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
	public boolean notNull(final Object actual) {
		return notNull("", actual);
	}

	/**
	 * Checks if is null.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if is null
	 */
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

	/**
	 * Checks if is null.
	 *
	 * @param actual
	 *            the actual
	 * @return true, if is null
	 */
	public boolean isNull(final Object actual) {
		return isNull("", actual);
	}

	/**
	 * Not same.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Not same.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Un expected exception.
	 *
	 * @param msg
	 *            the msg
	 * @param error
	 *            the error
	 */
	public void unExpectedException(final String msg, final Throwable error) {
		addAssertHistory(msg, false, "unExpectedException", "", msg, error);
	}

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param arg3
	 *            the arg3
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param arg3
	 *            the arg3
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Checks if is false.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if is false
	 */
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

	/**
	 * Checks if is false.
	 *
	 * @param actual
	 *            the actual
	 * @return true, if is false
	 */
	public boolean isFalse(final boolean actual) {
		return isFalse("", actual);
	}

	/**
	 * Checks if is true.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if is true
	 */
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

	/**
	 * Checks if is true.
	 *
	 * @param actual
	 *            the actual
	 * @return true, if is true
	 */
	public boolean isTrue(final boolean actual) {
		return isTrue("", actual);
	}

	/**
	 * Not equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param unexpected
	 *            the unexpected
	 * @param actual
	 *            the actual
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param arg3
	 *            the arg3
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param arg3
	 *            the arg3
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param unexpected
	 *            the unexpected
	 * @param actual
	 *            the actual
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Not equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * That.
	 *
	 * @param <T>
	 *            the generic type
	 * @param actual
	 *            the actual
	 * @param matcher
	 *            the matcher
	 * @return true, if successful
	 */
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

	/**
	 * That.
	 *
	 * @param <T>
	 *            the generic type
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @param matcher
	 *            the matcher
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param arg3
	 *            the arg3
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expecteds
	 *            the expecteds
	 * @param actuals
	 *            the actuals
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expecteds
	 *            the expecteds
	 * @param actuals
	 *            the actuals
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expecteds
	 *            the expecteds
	 * @param actuals
	 *            the actuals
	 * @param delta
	 *            the delta
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Array equals.
	 *
	 * @param message
	 *            the message
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
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

	/**
	 * Checks if is empty.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if is empty
	 */
	public boolean isEmpty(final String message, final Set<?> actual) {
		try {
			Assert.assertNotNull(message, actual);
			Assert.assertTrue(message + " - Is Empty", actual.isEmpty());
			return addAssertHistory(message, true, "isEmpty", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "isEmpty", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	/**
	 * Checks if is not empty.
	 *
	 * @param message
	 *            the message
	 * @param actual
	 *            the actual
	 * @return true, if is not empty
	 */
	public boolean isNotEmpty(final String message, final Set<?> actual) {
		try {
			Assert.assertNotNull(message, actual);
			Assert.assertFalse(message + " - Is Not Empty", actual.isEmpty());
			return addAssertHistory(message, true, "isNotEmpty", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "isNotEmpty", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	/**
	 * Checks if is greater than.
	 *
	 * @param message
	 *            the message
	 * @param valueToBeGreaterThan
	 *            the value to be greater than
	 * @param actual
	 *            the actual
	 * @return true, if is greater than
	 */
	public boolean isGreaterThan(final String message, final int valueToBeGreaterThan, final int actual) {
		try {
			Assert.assertTrue(message + " - actual[" + actual + "] is greaater than " + valueToBeGreaterThan,
					valueToBeGreaterThan < actual);
			return addAssertHistory(message, true, "isGreaterThan", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "isGreaterThan", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	/**
	 * Checks if is less than.
	 *
	 * @param message
	 *            the message
	 * @param valueToBeLessThan
	 *            the value to be less than
	 * @param actual
	 *            the actual
	 * @return true, if is less than
	 */
	public boolean isLessThan(final String message, final int valueToBeLessThan, final int actual) {
		try {
			Assert.assertTrue(message + " - actual[" + actual + "] is less than " + valueToBeLessThan,
					valueToBeLessThan > actual);
			return addAssertHistory(message, true, "isLessThan", true, actual);
		} catch (final AssertionError e) {
			final boolean rtn = addAssertHistory(message, false, "isLessThan", true, actual, e);
			if (getThrowExceptionOnFail()) {
				throw e;
			}
			return rtn;
		}
	}

	/**
	 * *************************************************************************
	 * ***********.
	 *
	 * @return true, if successful
	 */

	public boolean pass() {
		return pass("");
	}

	/**
	 * Pass.
	 *
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
	public boolean pass(final String message) {
		return addAssertHistory(message, true, "pass", null, null, null);
	}

	/**
	 * Adds the assert history.
	 *
	 * @param message
	 *            the message
	 * @param status
	 *            the status
	 * @param assertMethod
	 *            the assert method
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @return true, if successful
	 */
	public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
			final Object expected, final Object actual) {
		return addAssertHistory(message, status, assertMethod, expected, actual, null);
	}

	/**
	 * Adds the assert history.
	 *
	 * @param message
	 *            the message
	 * @param status
	 *            the status
	 * @param assertMethod
	 *            the assert method
	 * @param expected
	 *            the expected
	 * @param actual
	 *            the actual
	 * @param exception
	 *            the exception
	 * @return true, if successful
	 */
	public boolean addAssertHistory(final String message, final Boolean status, final String assertMethod,
			final Object expected, final Object actual, final Throwable exception) {
		if (isVerifyOnly()) {
			StepAction.createVerifyResult(message, status, assertMethod, expected, actual, exception).add();
		} else {
			StepAction.createAssertResult(message, status, assertMethod, expected, actual, exception).add();
		}

		return status;
	}

	/**
	 * Gets the throw exception on fail.
	 *
	 * @return the throw exception on fail
	 */
	public boolean getThrowExceptionOnFail() {
		return throwExceptionOnFail;
	}

	/**
	 * Checks if is throw exception on fail.
	 *
	 * @return true, if is throw exception on fail
	 */
	public boolean isThrowExceptionOnFail() {
		return getThrowExceptionOnFail();
	}

	/**
	 * Only verfiy.
	 *
	 * @return the verbose asserts
	 */
	public VerboseAsserts onlyVerfiy() {
		this.recordSteps = false;
		this.throwExceptionOnFail = false;
		this.setVerifyOnly(true);
		return this;
	}

	/**
	 * Checks if is record steps.
	 *
	 * @return true, if is record steps
	 */
	public boolean isRecordSteps() {
		return recordSteps;
	}

	/**
	 * Sets the throw exception on fail.
	 *
	 * @param throwExceptionOnFail
	 *            the new throw exception on fail
	 */
	public void setThrowExceptionOnFail(final boolean throwExceptionOnFail) {
		this.throwExceptionOnFail = throwExceptionOnFail;
	}

	/**
	 * Sets the record steps.
	 *
	 * @param recordSteps
	 *            the new record steps
	 */
	public void setRecordSteps(final boolean recordSteps) {
		this.recordSteps = recordSteps;
	}

	/**
	 * Checks if is verify only.
	 *
	 * @return true, if is verify only
	 */
	public boolean isVerifyOnly() {
		return isVerifyOnly;
	}

	/**
	 * Sets the verify only.
	 *
	 * @param isVerifyOnly
	 *            the new verify only
	 */
	public void setVerifyOnly(final boolean isVerifyOnly) {
		this.isVerifyOnly = isVerifyOnly;
	}

}
