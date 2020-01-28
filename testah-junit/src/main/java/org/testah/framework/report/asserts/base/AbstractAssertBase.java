package org.testah.framework.report.asserts.base;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractAssertBase<H extends AbstractAssertBase, T> {

    protected static final Object SET_ACTUAL_HISTORY_FROM_CLOSURE_BOOLEAN = new Object();
    private final T actual;
    private final VerboseAsserts asserts;
    private String message = "";
    private Set<Boolean> status = new HashSet<>();

    public AbstractAssertBase(final T actual) {
        this(actual, TS.asserts());
    }

    public AbstractAssertBase(final T actual, final VerboseAsserts asserts) {
        this.asserts = asserts;
        this.actual = actual;
    }

    public H withMessage(String message) {
        return setMessage(message);
    }

    /**
     * Reset status assert collections.
     *
     * @return the assert collections
     */
    public H resetStatus() {
        getStatus().clear();
        return (H) this;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    protected Set<Boolean> getStatus() {
        return status;
    }

    protected AssertFunctionReturnBooleanActual<T> getAssertBlock(final Runnable runnableAssertBlock) {
        AssertFunctionReturnBooleanActual<T> assertStatement = (expected, actual, history) -> {
            runnableAssertBlock.run();
            return true;
        };
        return assertStatement;
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual) {
        return runAssert(message, assertName, runnableAssertBlock, expected, actual, false);
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowActualToBeNull) {
        return runAssert(message, assertName, runnableAssertBlock, expected, actual, allowActualToBeNull, null);
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowActualToBeNull, final Runnable onFailureRunnableBlock) {

        AssertHistoryItem assertHistoryItem = new AssertHistoryItem(actual).setAssertName(assertName)
                .setExpected(expected).setMessage(getMessage(message));

        if (allowActualToBeNull || canAssertRun(assertName)) {
            try {
                runnableAssertBlock.run(expected, actual, assertHistoryItem);
                getStatus().add(assertHistoryItem.addHistoryForPass(getAsserts()));
            } catch (final Throwable e) {
                getStatus().add(assertHistoryItem.addHistoryForFail(getAsserts(), e));
                if (onFailureRunnableBlock != null) {
                    onFailureRunnableBlock.run();
                }
                if (getAsserts().getThrowExceptionOnFail()) {
                    throw e;
                }
            }
        }
        return (H) this;
    }

    protected H runAssert(final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowActualToBeNull, final Runnable onFailureRunnableBlock) {
        return runAssert("", assertName, runnableAssertBlock, expected, actual,
                allowActualToBeNull, onFailureRunnableBlock);
    }


    protected boolean canAssertRun(final String assertName) {
        return canAssertRun(assertName, true);
    }

    protected boolean canAssertRun(final String assertName, final boolean throwRuntimeIfCannotRunAssert) {
        if (getActual() == null) {
            TS.log().debug("Unable to run assert[" + assertName + "] requested as actual is null");
            getStatus().add(false);
            //getAsserts().isNull(getMessage("Checking that actual is not null"));
            if (throwRuntimeIfCannotRunAssert) {
                throw new AssertNotAllowedWithNullActual(assertName);
            }
            return false;
        }
        return true;
    }

    public VerboseAsserts getAsserts() {
        return asserts;
    }

    public T getActual() {
        return actual;
    }

    public H equalsToWithReflection(T expected) {
        return addStatus(getAsserts().equalsToWithReflection(getMessage(), expected, getActual()));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(getActual());
    }

    /**
     * Class Equals should not be used for this type of assert class, please only use equalsTo.
     *
     * @param expected Object to compare, should be same class type as actual used.
     * @return boolean true is equalsTo else false
     * @deprecated equals is confusing for an assert class like this, it should be avoided
     */
    @SuppressFBWarnings
    @Override
    @Deprecated
    public boolean equals(Object expected) {
        T expectedWithCase = null;
        TS.log().debug("Please use equalsTo not the class equals");
        try {
            expectedWithCase = ((T) expected);
            return equalsTo(expectedWithCase).isPassed();
        } catch (ClassCastException castError) {
            throw new RuntimeException("Unable to case expected to be what the actual " +
                    "class is so equals will not work", castError);
        }
    }

    /**
     * Is passed boolean.
     *
     * @return the boolean
     */
    public boolean isPassed() {
        return !isFailed();
    }

    public H equalsTo(T expected) {

        return addStatus(getAsserts().equalsTo(getMessage(), expected, getActual()));
    }

    /**
     * Is failed boolean.
     *
     * @return the boolean
     */
    public boolean isFailed() {
        return getStatus().contains(false);
    }

    protected H addStatus(Boolean bool) {
        status.add(bool);
        return (H) this;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(final String msgSuffix) {
        return message + (msgSuffix != null ? " - " + msgSuffix : "");
    }

    public H setMessage(String message) {
        this.message = message;
        return (H) this;
    }

    protected H isEmpty(final AssertFunctionReturnBooleanActual<T> runnableAssertBlock) {
        AssertFunctionReturnBooleanActual<T> assertStatement = (expected, actual, history) -> {
            history.setMessage(String.format("expected %s[%s] to be empty", actual.getClass().toString(), actual.toString()));
            history.setActualForHistory(runnableAssertBlock.run(expected, actual, history));
            Assert.assertTrue(runnableAssertBlock.run(expected, actual, history));
            return true;
        };
        return runAssert("isEmpty", assertStatement, null, getActual(), false, null);
    }

    protected H isNotEmpty(final AssertFunctionReturnBooleanActual runnableAssertBlock) {
        AssertFunctionReturnBooleanActual<String> assertStatement = (expected, actual, history) -> {
            history.setMessage(String.format("expected %s[%s] to not be empty", actual.getClass().toString(), actual.toString()));
            boolean status = runnableAssertBlock.run(expected, actual, history);
            history.setActualForHistory(status);
            Assert.assertTrue(status);
            return true;
        };
        return runAssert("isNotEmpty", assertStatement, null, getActual(), false, null);
    }

    public H assertThat(Matcher matcher) {
        getAsserts().that(getActual(), matcher);
        return (H) this;
    }
}
