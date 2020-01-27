package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

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

    /**
     * Instantiates a new Assert collections.
     *
     * @param actual the actual
     */
    public AssertCollections(final Collection<T> actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param actual  the actual
     * @param asserts the asserts
     */
    public AssertCollections(Collection<T> actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param actual the actual
     */
    public AssertCollections(final T[] actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert collections.
     *
     * @param actual  the actual
     * @param asserts the asserts
     */
    public AssertCollections(final T[] actual, final VerboseAsserts asserts) {
        this(Arrays.asList(actual), asserts);
    }

    /**
     * Size assert number.
     *
     * @return the assert number
     */
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
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory("Expect to contain[" + expectedValueContained + "]");
            Assert.assertTrue(getActual().contains(expectedValueContained));
            return true;
        };
        return runAssert("Check that actual[" + getActual() + "] contains " + expectedValueContained, "contains",
                assertRun, null, getActual());
    }

    /**
     * Does not contain assert collections.
     *
     * @param expectedValueNotContained the expected value not contained
     * @return the assert collections
     */
    public AssertCollections doesNotContain(final T expectedValueNotContained) {
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory("Expect to not contain [" + expectedValueNotContained + "]");
            Assert.assertFalse(getActual().contains(expectedValueNotContained));
            return true;
        };
        return runAssert("Check that actual [" + getActual() + "] does not contain [" + expectedValueNotContained + "].",
                "doesNotContain", assertRun, null, getActual());
    }

    /**
     * No duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections noDuplicates() {
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            HashSet<T> hashSet = new HashSet<T>(getActual());
            history.setExpectedForHistory(hashSet.size());
            history.setActualForHistory(actual.size());
            Assert.assertTrue(hashSet.size() == actual.size());
            return true;
        };
        return runAssert("Check that actual[" + getActual() + "] has not duplicates", "noDuplicates",
                assertRun, null, getActual());
    }

    /**
     * Has duplicates assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections hasDuplicates() {
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            HashSet<T> hashSet = new HashSet<T>(getActual());
            history.setExpectedForHistory(hashSet.size());
            history.setActualForHistory(actual.size());
            Assert.assertTrue(hashSet.size() < getActual().size());
            return true;
        };
        return runAssert("Check that actual[" + getActual() + "] has duplicates", "hasDuplicates",
                assertRun, null, getActual());
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

    private AssertCollections equalsTo(final Collection<T> expectedCollection, final ReflectionComparatorMode... modes) {
        StringBuilder str = new StringBuilder();
        Stream.of(modes).forEach(it -> str.append(it.name()));
        final String msg = "Checking that the actual is equal with reflection and modes[" + str.toString() + "]";
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            ReflectionAssert.assertReflectionEquals(msg, expectedCollection, getActual(), modes);
            return true;
        };
        return runAssert(msg, "equalsToWithReflection",
                assertRun, expectedCollection, getActual());
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

    /**
     * Is empty assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections isEmpty() {
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory(true);
            history.setActualForHistory(getActual().isEmpty());
            Assert.assertTrue(getActual().isEmpty());
            return true;
        };
        return super.isEmpty(assertRun);
    }

    /**
     * Is not empty assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections isNotEmpty() {
        AssertFunctionReturnBooleanActual<Collection<T>> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory(false);
            history.setActualForHistory(getActual().isEmpty());
            Assert.assertFalse(getActual().isEmpty());
            return true;
        };
        return super.isNotEmpty(assertRun);
    }
}
