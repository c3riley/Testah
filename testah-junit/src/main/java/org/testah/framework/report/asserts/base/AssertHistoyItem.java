package org.testah.framework.report.asserts.base;

import org.testah.framework.dto.base.AbstractDtoBase;
import org.testah.framework.report.VerboseAsserts;

public class AssertHistoyItem extends AbstractDtoBase<AssertHistoyItem> {

    private Object expected;
    private Object expectedForHistory;
    private Object actual;
    private Object actualForHistory;
    private String message;
    private String assertName;

    public AssertHistoyItem(final Object actual) {
        this("",actual);
    }

    public AssertHistoyItem(final String message, Object actual) {
        this.actual = actual;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public AssertHistoyItem setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getActual() {
        return actual;
    }

    public AssertHistoyItem setActual(Object actual) {
        this.actual = actual;
        setActualForHistory(actual);
        return this;
    }

    public Object getExpected() {
        return expected;
    }

    public AssertHistoyItem setExpected(Object expected) {
        this.expected = expected;
        setExpectedForHistory(expected);
        return this;
    }

    public String getAssertName() {
        return assertName;
    }

    public AssertHistoyItem setAssertName(String assertName) {
        this.assertName = assertName;
        return this;
    }

    public Object getActualForHistory() {
        return actualForHistory;
    }

    public void setActualForHistory(Object actualForHistory) {
        this.actualForHistory = actualForHistory;
    }

    public Object getExpectedForHistory() {
        return expectedForHistory;
    }

    public void setExpectedForHistory(Object expectedForHistory) {
        this.expectedForHistory = expectedForHistory;
    }

    public Boolean addHistoryForPass(final VerboseAsserts verboseAsserts) {
        return addHistory(verboseAsserts, true, null);
    }

    public Boolean addHistoryForFail(final VerboseAsserts verboseAsserts, final Throwable throwable) {
        return addHistory(verboseAsserts, false, throwable);
    }

    public Boolean addHistory(final VerboseAsserts verboseAsserts, final Boolean status, final Throwable throwable) {
        return verboseAsserts.addAssertHistory(message, status, assertName, expectedForHistory,
                actualForHistory, throwable);
    }
}
