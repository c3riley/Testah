package org.testah.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param listSizes size of each list
     */
    public TupleGenerator(int... listSizes) {
        sizes = Arrays.stream(listSizes).boxed().collect(Collectors.toList());
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

    // recursively increment the index in each of the lists; idx is the index of the list, 
    private void increment(int idx) {
        // if the position in the current list is in range after increase...
        if (tuple.get(idx) < sizes.get(idx) - 1) {
            // bump the position
            tuple.set(idx, tuple.get(idx) + 1);
        } else {
            // if the position exceeds the range start again at 0
            tuple.set(idx, 0);
            // and bump the position in the next list, if there is one.
            if (idx + 1 < tuple.size()) {
                increment(idx + 1);
            }
        }
    }
}
