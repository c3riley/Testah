package org.testah.framework.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.cxf.helpers.FileUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.reflections.Reflections;
import org.testah.TS;
import org.testah.framework.annotations.TestPlan;

import groovy.lang.GroovyClassLoader;

public class TestPlanFilter {

    private Set<Class<?>>        testClasses;
    private final List<Class<?>> testClassesMetFilters;

    public TestPlanFilter() {
        testClasses = new HashSet<Class<?>>();
        testClassesMetFilters = new ArrayList<Class<?>>();
    }

    public void filterTestPlansToRun() {
        loadCompiledTestClase();
        loadUncompiledTestPlans();
        if (null != testClasses) {
            TestPlan meta;

            for (final Class<?> test : testClasses) {

                meta = test.getAnnotation(TestPlan.class);
                if (!isFilterTestNameStartsWith(test)) {
                    TS.log().trace("test[" + test.getName() + "] filtered out by isFilterTestNameStartsWith");
                    continue;
                }
                if (null == meta) {
                    TS.log().trace("test[" + test.getName() + "] filtered out by no TestMeta Annotation");
                    continue;
                }
                if (!isFilterTestPlanByTestType(meta)) {
                    TS.log().trace("test[" + test.getName() + "] filtered out by isFilterTestPlanHasMetadata");
                    continue;
                }

                testClassesMetFilters.add(test);

            }
            TS.log().info("###################################################");
            TS.log().info("# TestPlan Classes To Run: ( " + testClassesMetFilters.size() + " of " + testClasses.size()
                    + " )");
            TS.log().info("#");
            for (final Class<?> test : testClassesMetFilters) {
                TS.log().info("#  " + test.getName());
            }
            TS.log().info("#");
            TS.log().info("###################################################");
        }

    }

    public List<Class<?>> getTestClassesMetFilters() {
        return testClassesMetFilters;
    }

    public boolean isFilterTestNameStartsWith(final Class<?> test) {
        return isFilterTestNameStartsWith(test, TS.params().getFilterByNameStartsWith());
    }

    public boolean isFilterTestNameStartsWith(final Class<?> test, final String startsWith) {
        if (null != startsWith && startsWith.length() > 0) {
            return test.getName().startsWith(startsWith);
        }
        return true;
    }

    public boolean isFilterTestPlanByTestType(final TestPlan meta) {
        if (meta.testType() != TS.params().getFilterByTestType()) {
            return false;
        }
        return true;
    }

    public TestPlanFilter loadCompiledTestClase() {
        if (null != TS.params().getLookAtInternalTests() && TS.params().getLookAtInternalTests().length() > 0) {
            final Reflections reflections = new Reflections(TS.params().getLookAtInternalTests());
            testClasses.addAll(reflections.getTypesAnnotatedWith(TestPlan.class));
        }
        return this;
    }

    public TestPlanFilter loadUncompiledTestPlans() {
        final String externalValue = TS.params().getLookAtExternalTests();

        if (null != externalValue && externalValue.length() > 0) {
            final List<File> files = new ArrayList<File>();
            final ClassLoader parent = this.getClass().getClassLoader();
            try (final GroovyClassLoader loader = new GroovyClassLoader(parent)) {

                for (final String path : externalValue.split(",")) {

                    final File externalTests = new File(Params.addUserDir(path));

                    if (!externalTests.exists()) {
                        TS.log().error("Param LookAtExternalTests is set to a directory not found: "
                                + externalTests.getAbsolutePath());
                    }
                    if (externalTests.isDirectory()) {
                        files.addAll(FileUtils.getFilesRecurse(externalTests, "(.?)*\\.groovy"));
                        files.addAll(FileUtils.getFilesRecurse(externalTests, "(.?)*\\.java"));
                    } else {
                        files.add(externalTests);
                    }
                }
                for (final File c : files) {
                    Class<?> groovyClass;
                    try {
                        groovyClass = loader.parseClass(c);
                        if (groovyClass != null) {
                            testClasses.add(groovyClass);
                        }
                    } catch (CompilationFailedException | IOException e) {
                        TS.log().error("issue with external class: " + c.getAbsolutePath(), e);
                    }

                }
            } catch (final IOException e1) {
                TS.log().error("issue with external class loading", e1);
            }
        }
        return this;
    }

    public Set<Class<?>> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(final Set<Class<?>> testClasses) {
        this.testClasses = testClasses;
    }

}
