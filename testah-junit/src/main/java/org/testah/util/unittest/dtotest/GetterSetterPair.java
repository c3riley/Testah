package org.testah.util.unittest.dtotest;

import java.lang.reflect.Method;

/**
 * A utility class which holds a related getter and setter method.
 * https://github.com/Blastman/DtoTester/blob/master/src/test/java/com/objectpartners/GetterSetterPair.java
 */
public class GetterSetterPair {
    /**
     * The get method.
     */
    private Method getter;

    /**
     * The set method.
     */
    private Method setter;

    /**
     * Returns the get method.
     *
     * @return The get method.
     */
    public Method getGetter() {
        return getter;
    }

    /**
     * Sets the get Method.
     *
     * @param getter The get Method.
     */
    public void setGetter(Method getter) {
        this.getter = getter;
    }

    /**
     * Returns the set method.
     *
     * @return The set method.
     */
    public Method getSetter() {
        return setter;
    }

    /**
     * Sets the set Method.
     *
     * @param setter The set Method.
     */
    public void setSetter(Method setter) {
        this.setter = setter;
    }

    /**
     * Returns if this has a getter and setting method set.
     *
     * @return If this has a getter and setting method set.
     */
    public boolean hasGetterAndSetter() {
        return this.getter != null && this.setter != null;
    }
}