package org.testah.framework.report.asserts;

import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Assert collections.
 * Assert Utility for all the types of asserts you might want with collections.
 * The goal is for verbose reporting so if an error does occur it is easy to figure out the root cause.
 * If you want a boolean response, can call at any time isPassed(),
 * if any of the asserts called failed this will be false.
 *
 * @param <T> the type parameter
 */
public class AssertCollections<T> {

    private final Collection<T> collection;
    private final VerboseAsserts asserts;
    /**
     * The Last assert status.
     */
    Set<Boolean> status = new HashSet<>();

    /**
     * Instantiates a new Assert collections.
     *
     * @param collection the collection
     */
    public AssertCollections(Collection<T> collection) {
        this(collection, TS.asserts());
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param collection the collection
     */
    public AssertCollections(T[] collection) {
        this(collection, TS.asserts());
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param collection the collection
     * @param asserts    the asserts
     */
    public AssertCollections(T[] collection, VerboseAsserts asserts) {
        this(Arrays.asList(collection), asserts);
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param collection the collection
     * @param asserts    the asserts
     */
    public AssertCollections(Collection<T> collection, VerboseAsserts asserts) {
        this.collection = collection;
        this.asserts = asserts;
        status.add(asserts.notNull("Checking that the collection is not null", this.collection));
    }

    /**
     * Has size assert collections.
     *
     * @param expectedSize the expected size
     * @return the assert collections
     */
    public AssertCollections hasSize(final int expectedSize) {
        if(canAssertRun()) {
            status.add(asserts.equalsTo("Check size matches", expectedSize, collection.size()));
        }
        return this;
    }

    /**
     * Getter for property 'asserts'.
     *
     * @return Value for property 'asserts'.
     */
    public VerboseAsserts getAsserts() {
        return asserts;
    }

    /**
     * Getter for property 'collection'.
     *
     * @return Value for property 'collection'.
     */
    public Collection<T> getCollection() {
        return this.collection;
    }

    /**
     * Contains assert collections.
     *
     * @param expectedValueContained the expected value contained
     * @return the assert collections
     */
    public AssertCollections contains(final T expectedValueContained) {
        if(canAssertRun()) {
            status.add(asserts.isTrue("Check that collection[" + collection + "] contains " + expectedValueContained,
                    collection.contains(expectedValueContained)));
        }
        return this;
    }

    /**
     * Does not contain assert collections.
     *
     * @param expectedValueNotContained the expected value not contained
     * @return the assert collections
     */
    public AssertCollections doesNotContain(final T expectedValueNotContained) {
        if(canAssertRun()) {
            status.add(asserts.isFalse("Check that collection[" + collection + "] not contains " + expectedValueNotContained,
                    collection.contains(expectedValueNotContained)));
        }
        return this;
    }

    /**
     * No duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections noDuplicates() {
        if(canAssertRun()) {
            HashSet<T> hashSet = new HashSet<T>(collection);
            status.add(asserts.isTrue("Check that collection[" + collection + "] has no duplicates",
                    hashSet.size() == collection.size()));
        }
        return this;
    }

    /**
     * Has duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections hasDuplicates() {
        if(canAssertRun()) {
            HashSet<T> hashSet = new HashSet<T>(collection);
            status.add(asserts.isTrue("Check that collection[" + collection + "] has no duplicates",
                    hashSet.size() < collection.size()));
        }
        return this;
    }

    /**
     * Equals assert collections.
     *
     * @param expectedCollection the expected collection
     * @return the assert collections
     */
    public AssertCollections equals(final T[] expectedCollection) {
        return equals(Arrays.asList(expectedCollection));
    }

    /**
     * Equals assert collections.
     *
     * @param expectedCollection the expected collection
     * @return the assert collections
     */
    public AssertCollections equals(final Collection<T> expectedCollection) {
        return equals(expectedCollection, true);
    }

    /**
     * Equals ignore order assert collections.
     *
     * @param expectedCollection the expected collection
     * @return the assert collections
     */
    public AssertCollections equalsIgnoreOrder(final T[] expectedCollection) {
        return equalsIgnoreOrder(Arrays.asList(expectedCollection));
    }

    /**
     * Equals ignore order assert collections.
     *
     * @param expectedCollection the expected collection
     * @return the assert collections
     */
    public AssertCollections equalsIgnoreOrder(final Collection<T> expectedCollection) {
        return equals(expectedCollection, false);
    }

    private AssertCollections equals(final Collection<T> expectedCollection, final boolean assertOrder) {
        if(canAssertRun()) {
            final boolean onError = asserts.getThrowExceptionOnFail();
            try {
                asserts.setThrowExceptionOnFail(false);

                final HashMap<T, List<Integer>> expected = getHashWithIndexes(expectedCollection);
                final HashMap<T, List<Integer>> actual = getHashWithIndexes(collection);

                asserts.equalsTo("Check sizes of the lists are the same", expectedCollection.size(), collection.size());

                expected.forEach((key, listOfIndexes) -> {
                    if (asserts.isTrue("Check that the item[" + key + "] is contained in the list", actual.containsKey(key))) {
                        List<Integer> actualIndexes = actual.get(key);
                        if (asserts.equalsTo("Check that the key[" + key + "] appears as many times in both lists",
                                listOfIndexes.size(), actualIndexes.size())) {
                            if (assertOrder) {
                                AtomicInteger actualIndex = new AtomicInteger();
                                listOfIndexes.forEach(expectedIndex -> {
                                    status.add(asserts.equalsTo("Check that the key[" + key + "] is at"
                                                    + " same index[" + expectedIndex + "]",
                                            expectedIndex,
                                            actualIndexes.get(actualIndex.getAndIncrement())));
                                });
                            }
                        } else {
                            status.add(false);
                        }
                    } else {
                        status.add(false);
                    }

                });
            } finally {
                asserts.setThrowExceptionOnFail(onError);
                if (status.contains(false)) {
                    asserts.fail("Failed asserts for equals of two collections");
                }
                return this;
            }
        }
        return this;
    }

    /**
     * Is empty assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections isEmpty() {
        if(canAssertRun()) {
            status.add(asserts.equalsTo("Checking that the collection is empty", 0, collection.size()));
        }
        return this;
    }

    public AssertCollections isNotEmpty() {
        if(canAssertRun()) {
            status.add(asserts.isGreaterThan("Checking that the collection is not empty", 0,
                    collection.size()));
        }
        return this;
    }

    private HashMap<T, List<Integer>> getHashWithIndexes(Collection<T> collectionToConvert) {
        HashMap<T, List<Integer>> hash = new HashMap<T, List<Integer>>();
        if(collectionToConvert!=null) {
            AtomicInteger index = new AtomicInteger();
            collectionToConvert.forEach(item -> {
                if (hash.get(item) == null) {
                    hash.put(item, new ArrayList<Integer>());
                }
                hash.get(item).add(index.getAndIncrement());
            });
        }
        return hash;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    private Set<Boolean> getStatus() {
        return status;
    }

    /**
     * Reset status assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections resetStatus() {
        getStatus().clear();
        return this;
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

    private boolean canAssertRun() {
        if(this.collection==null) {
            TS.log().debug("Unable to run assert as collection is null");
            getStatus().add(false);
            return false;
        }
        return true;
    }

}
