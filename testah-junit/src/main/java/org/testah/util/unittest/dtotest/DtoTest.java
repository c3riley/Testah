package org.testah.util.unittest.dtotest;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.testah.TS;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * A utility class which allows for testing entity and transfer object classes. This is mainly for code coverage since
 * these types of objects are normally nothing more than getters and setters. If any logic exists in the method, then
 * the get method name should be sent in as an ignored field and a custom test function should be written.
 *
 * <p>https://github.com/Blastman/DtoTester/blob/master/src/test/java/com/objectpartners/DtoTest.java
 *
 * <p>Changes have been made to support running instantiated and not as abstract, and to handle dto that have a super
 */
public class DtoTest {

    /**
     * A map of default mappers for common objects.
     */
    private static final ImmutableMap<Class<?>, Supplier<?>> DEFAULT_MAPPERS;

    static {
        final Builder<Class<?>, Supplier<?>> mapperBuilder = ImmutableMap.builder();

        /* Primitives */
        mapperBuilder.put(int.class, () -> 0);
        mapperBuilder.put(double.class, () -> 0.0d);
        mapperBuilder.put(float.class, () -> 0.0f);
        mapperBuilder.put(long.class, () -> 0L);
        mapperBuilder.put(boolean.class, () -> true);
        mapperBuilder.put(short.class, () -> (short) 0);
        mapperBuilder.put(byte.class, () -> (byte) 0);
        mapperBuilder.put(char.class, () -> (char) 0);

        mapperBuilder.put(Integer.class, () -> Integer.valueOf(0));
        mapperBuilder.put(Double.class, () -> Double.valueOf(0.0));
        mapperBuilder.put(Float.class, () -> Float.valueOf(0.0f));
        mapperBuilder.put(Long.class, () -> Long.valueOf(0));
        mapperBuilder.put(Boolean.class, () -> Boolean.TRUE);
        mapperBuilder.put(Short.class, () -> Short.valueOf((short) 0));
        mapperBuilder.put(Byte.class, () -> Byte.valueOf((byte) 0));
        mapperBuilder.put(Character.class, () -> Character.valueOf((char) 0));

        mapperBuilder.put(BigDecimal.class, () -> BigDecimal.ONE);
        mapperBuilder.put(Date.class, () -> new Date());

        /* Collection Types. */
        mapperBuilder.put(Set.class, () -> Collections.emptySet());
        mapperBuilder.put(SortedSet.class, () -> Collections.emptySortedSet());
        mapperBuilder.put(List.class, () -> Collections.emptyList());
        mapperBuilder.put(Map.class, () -> Collections.emptyMap());
        mapperBuilder.put(SortedMap.class, () -> Collections.emptySortedMap());

        DEFAULT_MAPPERS = mapperBuilder.build();
    }

    /**
     * The get fields to ignore and not try to test.
     */
    private Set<String> ignoredGetFields = new HashSet<>();

    private Set<Class> annotationsToIgnore = new HashSet<>();

    private Map<Class<?>, Supplier<?>> customMappers;

    /**
     * A custom mapper. Normally used when the test class has abstract objects.
     */
    private ImmutableMap<Class<?>, Supplier<?>> mappers;

    /**
     * Creates an instance of {@link DtoTest} with the default ignore fields.
     */
    public DtoTest() {
        this(null, null);
    }

    /**
     * Creates an instance of {@link DtoTest} with ignore fields and additional custom mappers.
     *
     * @param customMappers Any custom mappers for a given class type.
     * @param ignoreFields  The getters which should be ignored (e.g., "getId" or "isActive").
     */
    public DtoTest(Map<Class<?>, Supplier<?>> customMappers, Set<String> ignoreFields) {
        if (ignoreFields != null) {
            this.ignoredGetFields.addAll(ignoreFields);
        }

        this.ignoredGetFields.add("getClass");
        this.customMappers = customMappers;

    }

    /**
     * Calls a getter and verifies the result is what is expected.
     *
     * @param fieldName The field name (used for error messages).
     * @param getter    The get {@link Method}.
     * @param instance  The test instance.
     * @param expected  The expected result.
     * @throws IllegalAccessException    if this Method object is enforcing Java language access control and the underlying
     *                                   method is inaccessible.
     * @throws IllegalArgumentException  if the method is an instance method and the specified object argument is not an
     *                                   instance of the class or interface declaring the underlying method (or of a subclass or implementor
     *                                   thereof); if the number of actual and formal parameters differ; if an unwrapping conversion for
     *                                   primitive arguments fails; or if, after possible unwrapping, a parameter value cannot be converted to
     *                                   the corresponding formal parameter type by a method invocation conversion.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    private <T> void callGetter(T instance, String fieldName, Method getter, Object expected)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        final Object getResult = getter.invoke(instance);
        try {
            if (getter.getReturnType().isPrimitive()) {
                /* Calling assetEquals() here due to autoboxing of primitive to object type. */
                assertEquals(fieldName + " is different", expected, getResult);
            } else if (getter.getReturnType() == Date.class) {
                assertNotNull(fieldName + " of Date expected", expected);
                assertTrue(fieldName + " of Date ReturnType is different", expected.equals(getResult));
            } else {
                /* This is a normal object. The object passed in should be the exactly same object we get back. */
                assertSame(fieldName + " is different", expected, getResult);
            }
        } catch (AssertionError fail) {
            TS.log().debug("Failed callGetter for fieldName[" + fieldName + "] with method[" + getter.getName() + "]");
            throw fail;
        }
    }

    /**
     * Creates an object for the given {@link Class}.
     *
     * @param fieldName The name of the field.
     * @param clazz     The {@link Class} type to create.
     * @return A new instance for the given {@link Class}.
     * @throws InstantiationException If this Class represents an abstract class, an interface, an array class, a
     *                                primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails
     *                                for some other reason.
     * @throws IllegalAccessException If the class or its nullary constructor is not accessible.
     */
    private Object createObject(String fieldName, Class<?> clazz)
        throws InstantiationException, IllegalAccessException {
        try {
            final Supplier<?> supplier = getMappers().get(clazz);
            if (supplier != null) {
                return supplier.get();
            }

            if (clazz.isEnum()) {
                return clazz.getEnumConstants()[0];
            }

            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to create objects for field '" + fieldName + "'.", e);
        }
    }

    private boolean isIgnore(final Method method) {
        if (method != null && method.getDeclaredAnnotations() != null) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (getAnnotationsToIgnore().contains(annotation.annotationType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tests all the getters and setters. Verifies that when a set method is called, that the get method returns the
     * same thing. This will also use reflection to set the field if no setter exists (mainly used for user immutable
     * entities but Hibernate normally populates).
     *
     * @param <T>      the type parameter
     * @param instance the instance
     * @throws Exception If an expected error occurs.
     */
    public <T> void testGettersAndSetters(T instance) throws Exception {
        /* Sort items for consistent test runs. */
        final SortedMap<String, GetterSetterPair> getterSetterMapping = new TreeMap<>();

        TS.log().info("Running testGettersAndSetters for " + instance.getClass().getCanonicalName());

        for (final Method method : instance.getClass().getMethods()) {
            final String methodName = method.getName();

            if (this.ignoredGetFields.contains(methodName)) {
                continue;
            }

            if (isIgnore(method)) {
                continue;
            }

            String objectName;
            if (methodName.startsWith("get") && method.getParameters().length == 0) {
                /* Found the get method. */
                objectName = methodName.substring("get".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setGetter(method);
            } else if (methodName.startsWith("set") && method.getParameters().length == 1) {
                /* Found the set method. */
                objectName = methodName.substring("set".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setSetter(method);
            } else if (methodName.startsWith("is") && method.getParameters().length == 0) {
                /* Found the is method, which really is a get method. */
                objectName = methodName.substring("is".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setGetter(method);
            } else if (methodName.startsWith("with") && method.getParameters().length == 0) {
                /* Found the is method, which really is a get method. */
                objectName = methodName.substring("with".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setGetter(method);
            }
        }

        /*
         * Found all our mappings. Now call the getter and setter or set the field via reflection and call the getting
         * it doesn't have a setter.
         */
        for (final Entry<String, GetterSetterPair> entry : getterSetterMapping.entrySet()) {
            final GetterSetterPair pair = entry.getValue();

            final String objectName = entry.getKey();
            final String fieldName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);

            if (pair.hasGetterAndSetter()) {
                /* Create an object. */
                final Class<?> parameterType = pair.getSetter().getParameterTypes()[0];
                final Object newObject = createObject(fieldName, parameterType);
                try {
                    pair.getSetter().invoke(instance, newObject);
                } catch (Exception e) {
                    TS.log().error("Issue with invoke for setter: " + pair.getSetter().getName());
                    throw e;
                }
                try {
                    callGetter(instance, fieldName, pair.getGetter(), newObject);
                } catch (Exception e) {
                    TS.log().error("Issue with invoke for getter: " + pair.getGetter().getName());
                    throw e;
                }
            } else if (pair.getGetter() != null) {
                /*
                 * Object is immutable (no setter but Hibernate or something else sets it via reflection). Use
                 * reflection to set object and verify that same object is returned when calling the getter.
                 */
                final Object newObject = createObject(fieldName, pair.getGetter().getReturnType());
                final Field field = getField(fieldName, instance.getClass());
                if (field == null) {
                    throw new NoSuchFieldException("Unable to find field[" + fieldName + "] in the object or its supers");
                }

                field.setAccessible(true);
                field.set(instance, newObject);

                callGetter(instance, fieldName, pair.getGetter(), newObject);
            }
        }
    }

    private Field getField(final String fieldName, final Class instance) {
        if (instance != null) {
            try {
                return instance.getDeclaredField(fieldName);
            } catch (final Exception fieldNotFound) {
                return getField(fieldName, instance.getSuperclass());
            }
        }
        return null;
    }

    /**
     * Gets ignored get fields.
     *
     * @return the ignored get fields
     */
    public Set<String> getIgnoredGetFields() {
        return ignoredGetFields;
    }

    /**
     * Add to ignored get fields dto test.
     *
     * @param ignoredGetField the ignored get field
     * @return the dto test
     */
    public DtoTest addToIgnoredGetFields(final String ignoredGetField) {
        this.ignoredGetFields.add(ignoredGetField);
        return this;
    }

    /**
     * Gets annotations to ignore.
     *
     * @return the annotations to ignore
     */
    public Set<Class> getAnnotationsToIgnore() {
        return annotationsToIgnore;
    }

    /**
     * Sets annotations to ignore.
     *
     * @param annotationsToIgnore the annotations to ignore
     * @return the annotations to ignore
     */
    public DtoTest setAnnotationsToIgnore(final Set<Class> annotationsToIgnore) {
        this.annotationsToIgnore = annotationsToIgnore;
        return this;
    }

    /**
     * Add to annotations to ignore dto test.
     *
     * @param annotation the annotation
     * @return the dto test
     */
    public DtoTest addToAnnotationsToIgnore(final Class annotation) {
        this.annotationsToIgnore.add(annotation);
        return this;
    }

    /**
     * Gets custom mappers.
     *
     * @return the custom mappers
     */
    public Map<Class<?>, Supplier<?>> getCustomMappers() {
        return customMappers;
    }

    /**
     * Sets custom mappers.
     *
     * @param customMappers the custom mappers
     */
    public void setCustomMappers(Map<Class<?>, Supplier<?>> customMappers) {
        this.customMappers = customMappers;
    }

    private ImmutableMap<Class<?>, Supplier<?>> getMappers() {
        if (mappers == null) {
            final Builder<Class<?>, Supplier<?>> builder = ImmutableMap.builder();
            if (getCustomMappers() != null) {
                builder.putAll(getCustomMappers());
            }
            builder.putAll(DEFAULT_MAPPERS);
            this.mappers = builder.build();
        }
        return mappers;
    }

}