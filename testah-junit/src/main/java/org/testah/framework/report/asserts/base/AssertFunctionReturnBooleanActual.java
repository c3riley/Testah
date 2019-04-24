package org.testah.framework.report.asserts.base;

public interface AssertFunctionReturnBooleanActual<T> {

    Boolean run(T expected, T actual, AssertHistoryItem history);

}
