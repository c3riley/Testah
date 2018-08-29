package org.testah.framework.report.asserts;

import org.apache.commons.lang3.StringUtils;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;

import java.util.*;

public class AssertMaps<T, H> extends AbstractAssertBase<AssertMaps,Map<T, H>> {

    private final AssertCollections<T> actualKeySet;
    private final AssertCollections<H> actualValue;

    public AssertMaps(final Map<T, H> actual) {
        this(actual, TS.asserts());
    }

    public AssertMaps(final Map<T, H> actual, final VerboseAsserts asserts) {
        super(actual,asserts);
        if(actual!=null) {
            actualKeySet = new AssertCollections<T>(getActual().keySet(),asserts).setMessage("Map KeySet");
            actualValue = new AssertCollections<H>(getActual().values(),asserts).setMessage("Map Value Collection");
        } else {
            Set<T> nullKey = null;
            List<H> nullValues = null;
            actualKeySet = new AssertCollections<T>(nullKey, asserts).setMessage("Map KeySet");
            actualValue = new AssertCollections<H>(nullValues, asserts).setMessage("Map Value Collection");
        }
    }

    public AssertCollections<T> keySet() {
        return actualKeySet;
    }

    public AssertCollections<H> values() {
        return actualValue;
    }

    public AssertNumber size() {
        return new AssertNumber<Integer>(getActual().size(), getAsserts()).setMessage("Map Size");
    }

    public AssertMaps<T, H> isEmpty() {
        return isEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("");
            return getActual().isEmpty();
        });
    }

    public AssertMaps<T, H> isNotEmpty() {
        return isNotEmpty((expected, actual, history) -> {
            history.setExpectedForHistory("<Not Empty>");
            return !getActual().isEmpty();
        });
    }
}
