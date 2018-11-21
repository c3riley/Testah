package org.testah.framework.report.asserts.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.testah.framework.dto.base.AbstractDtoBase;
import org.testah.framework.report.VerboseAsserts;

/**
 * The type Assert history item.
 */
public class AssertHistoryItem extends AbstractDtoBase<AssertHistoryItem> {

    private Object expected;
    private Object expectedForHistory;
    private Object actual;
    private Object actualForHistory;
    private String message;
    private String assertName;

    /**
     * Instantiates a new Assert history item.
     */
    public AssertHistoryItem() {

    }

    /**
     * Instantiates a new Assert history item.
     *
     * @param actual the actual
     */
    public AssertHistoryItem(final Object actual) {
        this("", actual);
    }

    /**
     * Instantiates a new Assert history item.
     *
     * @param message the message
     * @param actual  the actual
     */
    public AssertHistoryItem(final String message, Object actual) {
        setActual(actual);
        this.message = message;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public AssertHistoryItem setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Gets actual.
     *
     * @return the actual
     */
    public Object getActual() {
        return actual;
    }

    /**
     * Sets actual.
     *
     * @param actual the actual
     * @return the actual
     */
    public AssertHistoryItem setActual(Object actual) {
        this.actual = actual;
        setActualForHistory(actual);
        return this;
    }

    /**
     * Gets expected.
     *
     * @return the expected
     */
    public Object getExpected() {
        return expected;
    }

    /**
     * Sets expected.
     *
     * @param expected the expected
     * @return the expected
     */
    public AssertHistoryItem setExpected(Object expected) {
        this.expected = expected;
        setExpectedForHistory(expected);
        return this;
    }

    /**
     * Gets assert name.
     *
     * @return the assert name
     */
    public String getAssertName() {
        return assertName;
    }

    /**
     * Sets assert name.
     *
     * @param assertName the assert name
     * @return the assert name
     */
    public AssertHistoryItem setAssertName(String assertName) {
        this.assertName = assertName;
        return this;
    }

    /**
     * Gets actual for history.
     *
     * @return the actual for history
     */
    public Object getActualForHistory() {
        return actualForHistory;
    }

    /**
     * Sets actual for history.
     *
     * @param actualForHistory the actual for history
     */
    public void setActualForHistory(Object actualForHistory) {
        this.actualForHistory = actualForHistory;
    }

    /**
     * Gets expected for history.
     *
     * @return the expected for history
     */
    public Object getExpectedForHistory() {
        return expectedForHistory;
    }

    /**
     * Sets expected for history.
     *
     * @param expectedForHistory the expected for history
     */
    public void setExpectedForHistory(Object expectedForHistory) {
        this.expectedForHistory = expectedForHistory;
    }

    /**
     * Add history for pass boolean.
     *
     * @param verboseAsserts the verbose asserts
     * @return the boolean
     */
    @JsonIgnore
    public Boolean addHistoryForPass(final VerboseAsserts verboseAsserts) {
        return addHistory(verboseAsserts, true, null);
    }

    /**
     * Add history boolean.
     *
     * @param verboseAsserts the verbose asserts
     * @param status         the status
     * @param throwable      the throwable
     * @return the boolean
     */
    @JsonIgnore
    public Boolean addHistory(final VerboseAsserts verboseAsserts, final Boolean status, final Throwable throwable) {
        return verboseAsserts.addAssertHistory(message, status, assertName, expectedForHistory,
                actualForHistory, throwable);
    }

    /**
     * Add history for fail boolean.
     *
     * @param verboseAsserts the verbose asserts
     * @param throwable      the throwable
     * @return the boolean
     */
    @JsonIgnore
    public Boolean addHistoryForFail(final VerboseAsserts verboseAsserts, final Throwable throwable) {
        return addHistory(verboseAsserts, false, throwable);
    }
}
