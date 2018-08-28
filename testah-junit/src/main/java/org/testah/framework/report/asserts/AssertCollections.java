package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.*;

/**
 * The type Assert collections.
 * Assert Utility for all the types of asserts you might want with collections.
 * The goal is for verbose reporting so if an error does occur it is easy to figure out the root cause.
 * If you want a boolean response, can call at any time isPassed(),
 * if any of the asserts called failed this will be false.
 *
 * @param <T> the type parameter
 */
public class AssertCollections<T> extends AbstractAssertBase<AssertCollections, Collection<T>> {

    public AssertCollections(final Collection<T> actual) {
        this(actual, TS.asserts());
    }

    public AssertCollections(final T[] actual) {
        this(actual, TS.asserts());
    }

    public AssertCollections(final T[] actual, final VerboseAsserts asserts) {
        this(Arrays.asList(actual), asserts);
    }

    public AssertCollections(Collection<T> actual, VerboseAsserts asserts) {
        super(actual,asserts);
    }

    public AssertNumber<Integer> size() {
        return new AssertNumber<Integer>(getActual().size(), getAsserts()).setMessage("Collection Size");
    }

    /**
     * Contains assert collections.
     *
     * @param expectedValueContained the expected value contained
     * @return the assert collections
     */
    public AssertCollections contains(final T expectedValueContained) {
        return runAssert("Check that actual[" + getActual() + "] contains " + expectedValueContained, "contains",
                () -> {
                    Assert.assertTrue(getActual().contains(expectedValueContained));
                }, expectedValueContained, getActual());
    }

    /**
     * Does not contain assert collections.
     *
     * @param expectedValueNotContained the expected value not contained
     * @return the assert collections
     */
    public AssertCollections doesNotContain(final T expectedValueNotContained) {
        return runAssert("Check that actual[" + getActual() + "] does not contain " + expectedValueNotContained, "doesNotContain",
                () -> {
                    Assert.assertFalse(getActual().contains(expectedValueNotContained));
                }, expectedValueNotContained, getActual());
    }

    /**
     * No duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections noDuplicates() {
        HashSet<T> hashSet = new HashSet<T>(getActual());
        return runAssert("Check that actual[" + getActual() + "] has not duplicates", "noDuplicates",
                () -> {
                    Assert.assertTrue(hashSet.size() == getActual().size());
                }, getActual().size(), hashSet.size());
    }

    /**
     * Has duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections hasDuplicates() {
        HashSet<T> hashSet = new HashSet<T>(getActual());
        return runAssert("Check that actual[" + getActual() + "] has duplicates", "hasDuplicates",
                () -> {
                    Assert.assertTrue(hashSet.size() < getActual().size());
                }, getActual().size(), hashSet.size());
    }

    /**
     * Equals assert collections.
     *
     * @param expectedCollection the expected actual
     * @return the assert collections
     */
    public AssertCollections equalsTo(final T[] expectedCollection) {
        return equalsTo(Arrays.asList(expectedCollection));
    }

    /**
     * Equals assert collections.
     *
     * @param expectedCollection the expected actual
     * @return the assert collections
     */
    public AssertCollections equalsTo(final Collection<T> expectedCollection) {
        return equalsTo(expectedCollection, new ReflectionComparatorMode[]{});
    }

    /**
     * Equals ignore order assert collections.
     *
     * @param expectedCollection the expected actual
     * @return the assert collections
     */
    public AssertCollections equalsToIgnoreOrder(final T[] expectedCollection) {
        return equalsToIgnoreOrder(Arrays.asList(expectedCollection));
    }

    /**
     * Equals ignore order assert collections.
     *
     * @param expectedCollection the expected actual
     * @return the assert collections
     */
    public AssertCollections equalsToIgnoreOrder(final Collection<T> expectedCollection) {
        return equalsTo(expectedCollection, ReflectionComparatorMode.LENIENT_ORDER);
    }



    private AssertCollections equalsTo(final Collection<T> expectedCollection, final ReflectionComparatorMode... modes) {
        final String msg = "Checking that the actual is equal with reflection and modes[" + modes.toString() + "]";
        return runAssert(msg, "equals",
            () -> {
                ReflectionAssert.assertReflectionEquals(msg, expectedCollection, getActual(), modes);
            }, expectedCollection, getActual());
    }

    /**
     * Is empty assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections isEmpty() {
        return runAssert("Checking that the actual is empty", "isEmpty",
                () -> {
                    Assert.assertTrue(getActual().isEmpty());
                }, true, null);
    }

    public AssertCollections isNotEmpty() {
        final boolean actual = getActual().isEmpty();
        return runAssert("Checking that the actual is not empty", "isNotEmpty",
                () -> {
                    Assert.assertFalse(actual);
                }, false, SET_ACTUAL_HISTORY_FROM_CLOSURE_BOOLEAN);
    }
}
