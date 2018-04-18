package org.testah.runner.performance;

import java.util.Arrays;
import java.util.List;

import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.util.TupleGenerator;

public abstract class AbstractPerformanceRestRequest {
    public AbstractPerformanceRestRequest(List<?>... lists) {
        tupleGenerator = new TupleGenerator(Arrays.stream(lists).mapToInt(list -> list.size()).toArray());
    }

    protected TupleGenerator tupleGenerator;

    public abstract AbstractRequestDto<?> next();

    /**
     * Get the number of distinct tuples.
     * @return count of distinct tuples
     */
    public int countDistinct() {
        return tupleGenerator.countDistinct();
    }
}
