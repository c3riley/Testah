package org.testah.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class TupleGeneratorTest {

    @Test
    public void testOneListOneElement() {
        TupleGenerator generator = new TupleGenerator(1);
        assertEquals(1, generator.countDistinct());
        for (int count = 0; count < 10; count++) {
            assertEquals(Arrays.asList(0), generator.nextTuple());
            assertEquals(Arrays.asList(0), generator.nextTuple());
            assertEquals(Arrays.asList(0), generator.nextTuple());
            assertEquals(Arrays.asList(0), generator.nextTuple());
        }
    }

    @Test
    public void testOneListMultipleElements() {
        TupleGenerator generator = new TupleGenerator(3);
        assertEquals(3, generator.countDistinct());
        for (int count = 0; count < 3; count++) {
            assertEquals(Arrays.asList(0), generator.nextTuple());
            assertEquals(Arrays.asList(1), generator.nextTuple());
            assertEquals(Arrays.asList(2), generator.nextTuple());
        }
    }

    @Test
    public void testMultipleListsListsOneElement() {
        TupleGenerator generator = new TupleGenerator(1, 1, 1, 1);
        assertEquals(1, generator.countDistinct());
        for (int count = 0; count < 10; count++) {
            assertEquals(Arrays.asList(0, 0, 0, 0), generator.nextTuple());
        }
    }

    @Test
    public void testMultipleListsListsMultipleElements() {
        int maxIdx0 = 2;
        int maxIdx1 = 4;
        int maxIdx2 = 3;
        int maxIdx3 = 5;
        TupleGenerator generator = new TupleGenerator(maxIdx0, maxIdx1, maxIdx2, maxIdx3);
        assertEquals((long) maxIdx0 * maxIdx1 * maxIdx2 * maxIdx3, (long) generator.countDistinct());
        for (int count = 0; count < 4; count++) {
            for (int idx3 = 0; idx3 < maxIdx3; idx3++) {
                for (int idx2 = 0; idx2 < maxIdx2; idx2++) {
                    for (int idx1 = 0; idx1 < maxIdx1; idx1++) {
                        for (int idx0 = 0; idx0 < maxIdx0; idx0++) {
                            assertEquals(Arrays.asList(idx0, idx1, idx2, idx3), generator.nextTuple());
                        }
                    }
                }
            }
        }
    }
}
