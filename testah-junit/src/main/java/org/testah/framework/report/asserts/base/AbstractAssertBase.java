package org.testah.framework.report.asserts.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.AssertStrings;

import java.util.*;

public abstract class AbstractAssertBase<H extends AbstractAssertBase, T> {

    private final T actual;
    private String message = "";
    private final VerboseAsserts asserts;
    private Set<Boolean> status = new HashSet<>();
    protected static final Object SET_ACTUAL_HISTORY_FROM_CLOSURE_BOOLEAN = new Object();

    public AbstractAssertBase(final T actual) {
        this(actual, TS.asserts());
    }

    public AbstractAssertBase(final T actual, final VerboseAsserts asserts) {
        this.asserts = asserts;
        this.actual = actual;
    }

    public VerboseAsserts getAsserts() {
        return asserts;
    }

    public H setMessage(String message) {
        this.message = message;
        return (H) this;
    }

    public H withMessage(String message) {
        return setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(final String msgSuffix) {
        return message + (msgSuffix != null ? " - " + msgSuffix : "");
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    protected Set<Boolean> getStatus() {
        return status;
    }

    protected H addStatus(Boolean bool) {
        status.add(bool);
        return (H) this;
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
     * Is passed boolean.
     *
     * @return the boolean
     */
    public boolean isPassed() {
        return !isFailed();
    }

    /**
     * Is failed boolean.
     *
     * @return the boolean
     */
    public boolean isFailed() {
        return getStatus().contains(false);
    }

    protected boolean canAssertRun() {
        return canAssertRun("");
    }

    protected boolean canAssertRun(final String assertName) {
        return canAssertRun(assertName, false);
    }

    protected boolean canAssertRun(final boolean throwRuntimeIfCannotRunAssert) {
        return canAssertRun("", throwRuntimeIfCannotRunAssert);
    }

    protected boolean canAssertRun(final String assertName, final boolean throwRuntimeIfCannotRunAssert) {
        if (getActual() == null) {
            TS.log().debug("Unable to run assert requested as collection is null");
            getStatus().add(false);
            getAsserts().isNull(getMessage("Checking that actual is not null"));
            if (throwRuntimeIfCannotRunAssert) {
                throw new RuntimeException("Unable to run the assert attempted, the actual is null");
            }
            return false;
        }
        return true;
    }

    protected H runAssert(final String message, final String assertName,
                          final Runnable runnableAssertBlock) {
        return runAssert(message, assertName, runnableAssertBlock, false);
    }


    protected H runAssert(final String message, final String assertName,
                          final Runnable runnableAssertBlock, final boolean allowNull) {
        return runAssert(message, assertName, runnableAssertBlock, true, true);
    }

    protected H runAssert(final String message, final String assertName,
                          final Runnable runnableAssertBlock, final Object expectedObj,
                          final Object actualObj) {
        return runAssert(message, assertName, runnableAssertBlock, expectedObj, actualObj, false, null);
    }

    protected H runAssert(final String message, final String assertName,
                          final Runnable runnableAssertBlock, final Object expectedObj,
                          final Object actualObj, final boolean allowNull, final Runnable onFailureRunableBlock) {
        if (allowNull || canAssertRun()) {
            try {
                runnableAssertBlock.run();
                getStatus().add(getAsserts().addAssertHistory(message, true, assertName, expectedObj,
                        actualObj));
            } catch (final Throwable e) {
                getStatus().add(getAsserts().addAssertHistory(message, false, assertName, expectedObj,
                        actualObj, e));
                if (onFailureRunableBlock != null) {
                    onFailureRunableBlock.run();
                }
                if (getAsserts().getThrowExceptionOnFail()) {
                    throw e;
                }
            }
        }
        return (H) this;
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual) {
        return runAssert(message, assertName, runnableAssertBlock, expected, actual, false);
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowNull) {
        return runAssert(message, assertName, runnableAssertBlock, expected, actual, allowNull, null);
    }

    protected H runAssert(final String message, final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowNull, final Runnable onFailureRunableBlock) {

        AssertHistoyItem assertHistoyItem = new AssertHistoyItem(actual).setAssertName(assertName)
                .setExpected(expected).setMessage(getMessage(message));

        if (allowNull || canAssertRun()) {
            try {
                runnableAssertBlock.run(expected, actual, assertHistoyItem);
                getStatus().add(assertHistoyItem.addHistoryForPass(getAsserts()));
            } catch (final Throwable e) {
                getStatus().add(assertHistoyItem.addHistoryForFail(getAsserts(), e));
                if (onFailureRunableBlock != null) {
                    onFailureRunableBlock.run();
                }
                if (getAsserts().getThrowExceptionOnFail()) {
                    throw e;
                }
            }
        }
        return (H) this;
    }

    @Override
    public boolean equals(Object expected) {
        T expectedWithCase = null;
        try {
            expectedWithCase = ((T) expected);
            return equalsTo(expectedWithCase).isPassed();
        } catch (ClassCastException castError) {
            throw new RuntimeException("Unable to case expected to be what the actual " +
                    "class is so equals will not work", castError);
        }
    }


    public H equalsTo(T expected) {

        return addStatus(getAsserts().equalsTo(getMessage(), expected, getActual()));
    }

    public H equalsToWithReflection(T expected) {
        return addStatus(getAsserts().equalsToWithReflection(getMessage(), expected, getActual()));
    }

    public T getActual() {
        return actual;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(getActual());
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
            history.setActualForHistory(runnableAssertBlock.run(expected, actual, history));
            Assert.assertFalse(runnableAssertBlock.run(expected, actual, history));
            return true;
        };
        return runAssert("isNotEmpty", assertStatement, null, getActual(), false, null);
    }

    protected H runAssert(final String assertName,
                          final AssertFunctionReturnBooleanActual runnableAssertBlock, final T expected,
                          final T actual, final boolean allowNull, final Runnable onFailureRunableBlock) {

        AssertHistoyItem assertHistoyItem = new AssertHistoyItem(actual).setAssertName(assertName)
                .setExpected(expected).setMessage(getMessage(message));

        if (allowNull || canAssertRun()) {
            boolean expectedCheck = (allowNull || getAsserts().notNull("expected cannot be null for this assert",
                    getActual()));
            getStatus().add(expectedCheck);
            if (expectedCheck) {
                try {
                    runnableAssertBlock.run(expected, actual, assertHistoyItem);
                    getStatus().add(assertHistoyItem.addHistoryForPass(getAsserts()));
                } catch (final Throwable e) {
                    getStatus().add(assertHistoyItem.addHistoryForFail(getAsserts(), e));
                    if (onFailureRunableBlock != null) {
                        onFailureRunableBlock.run();
                    }
                    if (getAsserts().getThrowExceptionOnFail()) {
                        throw e;
                    }
                }
            }
        }
        return (H) this;
    }


}
