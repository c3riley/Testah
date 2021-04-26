package org.testah.util;

import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

public class TestahTestInfo implements TestInfo
{
    String displayName;
    Class testClass;
    Method testMethod;

    public TestahTestInfo(String displayName, Class testClass, Method testMethod)
    {
        this.displayName = displayName;
        this.testClass = testClass;
        this.testMethod = testMethod;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public Set<String> getTags()
    {
        return null;
    }

    @Override
    public Optional<Class<?>> getTestClass()
    {
        return Optional.ofNullable(testClass);
    }

    @Override
    public Optional<Method> getTestMethod()
    {
        return Optional.ofNullable(testMethod);
    }
}
