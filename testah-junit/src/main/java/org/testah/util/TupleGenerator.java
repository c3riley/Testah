package org.testah.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The purpose of this class is to facilitate a generator/factory method/class.
 * Given a number of lists/arrays, return a combination of elements, one from
 * each list. The combination is unique until the range of values in a
 * list/array is exhausted. At this point the respective list starts again at 0.
 */
public class TupleGenerator {
    private final List<Integer> sizes;
    private List<Integer> tuple;
    private final int distinctTuples;

    /**
     * Constructor.
     * 
     * @param listSizes
     *            size of each list
     */
    public TupleGenerator(Integer... listSizes) {
        this(Arrays.asList(listSizes));
    }

    /**
     * Constructor.
     * 
     * @param listSizes
     *            size of each list
     */
    public TupleGenerator(List<Integer> listSizes) {
        this.sizes = listSizes;
        tuple = new ArrayList<Integer>(Collections.nCopies(sizes.size(), 0));
        distinctTuples = sizes.stream().reduce(1, (a, b) -> a * b);
    }

    /**
     * Starting at 0, 0, ..., 0 cycle through the combination of list indices.
     * Assuming there are 3 lists with 2, 3, 4 elements, the first tuple returned
     * would be <code>0,0,0</code> then
     * <pre>
     * 1,0,0
     * 0,1,0
     * 1,1,0
     * 0,2,0
     * 1,2,0
     * 0,0,1
     * </pre>
     * etc. Eventually, <code>next()</code> start over again with
     * <code>0,0,0</code>.
     * 
     * @return the next combination of indices.
     */
    public List<Integer> nextTuple() {
        List<Integer> currentTuple = new ArrayList<>();
        currentTuple.addAll(tuple);
        increment(0);
        return currentTuple;
    }

    /**
     * Get the number of unique combinations (the product of all list sizes).
     * 
     * @return the number of distinct combinations
     */
    public int countDistinct() {
        return distinctTuples;
    }

    private void increment(int idx) {
        if (tuple.get(idx) < sizes.get(idx) - 1) {
            tuple.set(idx, tuple.get(idx) + 1);
        } else {
            tuple.set(idx, 0);
            if (idx + 1 < tuple.size()) {
                increment(idx + 1);
            }
        }
    }
}
