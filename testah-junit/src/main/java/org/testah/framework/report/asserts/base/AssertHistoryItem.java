package org.testah.framework.report.asserts.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.testah.framework.dto.base.AbstractDtoBase;
import org.testah.framework.report.VerboseAsserts;

public class AssertHistoryItem extends AbstractDtoBase<AssertHistoryItem> {

    private Object expected;
    private Object expectedForHistory;
    private Object actual;
    private Object actualForHistory;
    private String message;
    private String assertName;

    public AssertHistoryItem(){

    }

    public AssertHistoryItem(final Object actual) {
        this("",actual);
    }

    public AssertHistoryItem(final String message, Object actual) {
        this.actual = actual;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public AssertHistoryItem setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getActual() {
        return actual;
    }

    public AssertHistoryItem setActual(Object actual) {
        this.actual = actual;
        setActualForHistory(actual);
        return this;
    }

    public Object getExpected() {
        return expected;
    }

    public AssertHistoryItem setExpected(Object expected) {
        this.expected = expected;
        setExpectedForHistory(expected);
        return this;
    }

    public String getAssertName() {
        return assertName;
    }

    public AssertHistoryItem setAssertName(String assertName) {
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

    @JsonIgnore
    public Boolean addHistoryForPass(final VerboseAsserts verboseAsserts) {
        return addHistory(verboseAsserts, true, null);
    }

    @JsonIgnore
    public Boolean addHistoryForFail(final VerboseAsserts verboseAsserts, final Throwable throwable) {
        return addHistory(verboseAsserts, false, throwable);
    }

    @JsonIgnore
    public Boolean addHistory(final VerboseAsserts verboseAsserts, final Boolean status, final Throwable throwable) {
        return verboseAsserts.addAssertHistory(message, status, assertName, expectedForHistory,
                actualForHistory, throwable);
    }
}
