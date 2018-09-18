package org.testah.framework.report.asserts;

import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Assert maps.
 *
 * @param <T> the type parameter
 * @param <H> the type parameter
 */
public class AssertMaps<T, H> extends AbstractAssertBase<AssertMaps, Map<T, H>> {

    private final AssertCollections<T> actualKeySet;
    private final AssertCollections<H> actualValue;

    /**
     * Instantiates a new Assert maps.
     *
     * @param actual the actual
     */
    public AssertMaps(final Map<T, H> actual) {
        this(actual, TS.asserts());
    }

    /**
     * Instantiates a new Assert maps.
     *
     * @param actual  the actual
     * @param asserts the asserts
     */
    public AssertMaps(final Map<T, H> actual, final VerboseAsserts asserts) {
        super(actual, asserts);
        if (actual != null) {
            actualKeySet = new AssertCollections<T>(getActual().keySet(), asserts).setMessage("Map KeySet");
            actualValue = new AssertCollections<H>(getActual().values(), asserts).setMessage("Map Value Collection");
        } else {
            Set<T> nullKey = null;
            List<H> nullValues = null;
            actualKeySet = new AssertCollections<T>(nullKey, asserts).setMessage("Map KeySet");
            actualValue = new AssertCollections<H>(nullValues, asserts).setMessage("Map Value Collection");
        }
    }

    /**
     * Key set assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections<T> keySet() {
        return actualKeySet;
    }

    /**
     * Values assert collections.
     *
     * @return the assert collections
     */
    public AssertCollections<H> values() {
        return actualValue;
    }

    /**
     * Size assert number.
     *
     * @return the assert number
     */
    public AssertNumber size() {
        return new AssertNumber<Integer>(getActual().size(), getAsserts()).setMessage("Map Size");
    }

    /**
     * Is empty assert maps.
     *
     * @return the assert maps
     */
    public AssertMaps<T, H> isEmpty() {
        return isEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("");
            return getActual().isEmpty();
        });
    }

    /**
     * Is not empty assert maps.
     *
     * @return the assert maps
     */
    public AssertMaps<T, H> isNotEmpty() {
        return isNotEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("<Not Empty>");
            return !getActual().isEmpty();
        });
    }
}
