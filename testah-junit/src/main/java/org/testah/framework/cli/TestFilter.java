package org.testah.framework.cli;

import groovy.lang.GroovyClassLoader;
import org.apache.cxf.helpers.FileUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.reflections.Reflections;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestPlan;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class TestFilter.
 */
public class TestFilter {

    /**
     * The test classes.
     */
    private Set<Class<?>> testClasses;

    /**
     * The test classes met filters.
     */
    private final List<Class<?>> testClassesMetFilters;

    /**
     * Instantiates a new test filter.
     */
    public TestFilter() {
        testClasses = new HashSet<>();
        testClassesMetFilters = new ArrayList<>();
    }

    /**
     * Filter test plans to run.
     *
     * @return the list
     */
    public List<Class<?>> filterTestPlansToRun() {
        loadCompiledTestClase();
        loadUncompiledTestPlans();
        return filterTestPlansToRun(getTestClasses(), getTestClassesMetFilters());
    }

    /**
     * Filter test plans to run.
     *
     * @param testClassesToFilter the test classes to filter
     * @return the list
     */
    public List<Class<?>> filterTestPlansToRun(final Set<Class<?>> testClassesToFilter) {
        this.testClassesMetFilters.addAll(filterTestPlansToRun(testClassesToFilter, new ArrayList<>()));
        return this.testClassesMetFilters;
    }

    /**
     * Filter test plans to run.
     *
     * @param testClassesToFilter        the test classes to filter
     * @param testClassesMetFiltersToUse the test classes met filters to use
     * @return the list
     */
    public List<Class<?>> filterTestPlansToRun(final Set<Class<?>> testClassesToFilter, final List<Class<
            ?>> testClassesMetFiltersToUse) {

        final Params filterParams = TS.params();

        if (null != testClassesToFilter) {

            TestPlan meta;
            String filter = null;

            boolean filterByUuid = isFilterOn(TS.params().getFilterById());

            Boolean filterByIgnoreKnownProblem = null;
            if (isFilterOn(TS.params().getFilterIgnoreKnownProblem())) {
                if ("true".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                    filterByIgnoreKnownProblem = true;
                } else {
                    filterByIgnoreKnownProblem = false;
                }
            }
            boolean filterByComponent = isFilterOn(TS.params().getFilterByComponent());

            boolean filterByDevice = isFilterOn(TS.params().getFilterByDevice());

            boolean filterByPlatform = isFilterOn(TS.params().getFilterByPlatform());

            boolean filterByTag = isFilterOn(TS.params().getFilterByTag());

            boolean filterByRunType = isFilterOn(TS.params().getFilterByRunType());

            boolean filterByTestType = true;
            if (null == TS.params().getFilterByTestType()) {
                filterByTestType = false;
            }
            boolean filterByTestPlanNameStartsWith = isFilterOn(TS.params().getFilterByTestPlanNameStartsWith());

            for (final Class<?> test : testClassesToFilter) {

                meta = test.getAnnotation(TestPlan.class);

                if (null == meta) {
                    TS.log().trace("test[" + test.getName() + "] filtered out by no TestMeta Annotation");
                    continue;
                }
                if (null != test.getAnnotation(KnownProblem.class) && null != filterByIgnoreKnownProblem) {
                    if (filterByIgnoreKnownProblem) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by filterByIgnoreKnownProblem");
                        continue;
                    }
                } else if (null != filterByIgnoreKnownProblem && !filterByIgnoreKnownProblem) {
                    TS.log().trace("test[" + test.getName() + "] filtered out by filterByIgnoreKnownProblem");
                    continue;
                }
                if (filterByUuid) {
                    filter = filterParams.getFilterById();
                    if (!isFilterById(meta.id(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by filterByUuid");
                        continue;
                    }
                }
                if (filterByTestPlanNameStartsWith) {
                    if (!isFilterTestNameStartsWith(test)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by filterByTestPlanNameStartsWith");
                        continue;
                    }
                }
                if (filterByTestType) {
                    if (!isFilterByTestType(meta.testType())) {
                        TS.log().trace(
                                "test[" + test.getName() + "] filtered out by isFilterByTestType[" + TS.params().getFilterByTestType() + "]");
                        continue;
                    }
                }
                if (filterByTag) {
                    filter = filterParams.getFilterByTag();
                    if (!isFilterCheckOk(meta.tags(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by getFilterByTag");
                        continue;
                    }
                }
                if (filterByComponent) {
                    filter = filterParams.getFilterByComponent();
                    if (!isFilterCheckOk(meta.components(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by getFilterByComponent");
                        continue;
                    }
                }
                if (filterByDevice) {
                    filter = filterParams.getFilterByDevice();
                    if (!isFilterCheckOk(meta.devices(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by getFilterByDevice");
                        continue;
                    }
                }
                if (filterByPlatform) {
                    filter = filterParams.getFilterByPlatform();
                    if (!isFilterCheckOk(meta.platforms(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by getFilterByPlatform");
                        continue;
                    }
                }
                if (filterByRunType) {
                    filter = filterParams.getFilterByRunType();
                    if (!isFilterCheckOk(meta.runTypes(), filter)) {
                        TS.log().trace("test[" + test.getName() + "] filtered out by getFilterByRunType");
                        continue;
                    }
                }

                testClassesMetFiltersToUse.add(test);

            }
            TS.log().info(Cli.BAR_LONG);
            TS.log().info(String.format("%s TestPlan Classes To Run: ( %d of %d )", Cli.BAR_WALL,
                    testClassesMetFiltersToUse.size(), testClassesToFilter.size()));
            TS.log().info(Cli.BAR_WALL);
            for (final Class<?> test : testClassesMetFiltersToUse) {
                TS.log().info(Cli.BAR_WALL + " " + test.getName());
            }
            TS.log().info("#");
            TS.log().info(Cli.BAR_LONG);
        }
        return testClassesMetFiltersToUse;

    }

    private boolean isFilterOn(final String filterValue) {
        return (null != filterValue && filterValue.length() > 0);
    }

    /**
     * Filter test case.
     *
     * @param meta         the meta
     * @param testCaseName the test case name
     * @return true, if successful
     */
    public boolean filterTestCase(final TestCaseDto meta, final String testCaseName) {
        final boolean ok = true;
        final Params filterParams = TS.params();

        if (null != meta) {

            String filter = null;

            boolean filterByUuid = isFilterOn(TS.params().getFilterById());

            boolean filterByComponent = isFilterOn(TS.params().getFilterByComponent());

            boolean filterByDevice = isFilterOn(TS.params().getFilterByDevice());

            boolean filterByPlatform = isFilterOn(TS.params().getFilterByPlatform());

            boolean filterByTag = isFilterOn(TS.params().getFilterByTag());

            boolean filterByRunType = isFilterOn(TS.params().getFilterByRunType());

            boolean filterByTestType = true;
            if (null == TS.params().getFilterByTestType()) {
                filterByTestType = false;
            }

            if (filterByUuid) {
                filter = filterParams.getFilterById();
                if (!isFilterById(meta.getId(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by filterByUuid");
                    return false;
                }
            }

            if (filterByTag) {
                filter = filterParams.getFilterByTag();
                if (!isFilterCheckOk(meta.getTags(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by getFilterByTag");
                    return false;
                }
            }
            if (filterByComponent) {
                filter = filterParams.getFilterByComponent();
                if (!isFilterCheckOk(meta.getComponents(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by getFilterByComponent");
                    return false;
                }
            }
            if (filterByDevice) {
                filter = filterParams.getFilterByDevice();
                if (!isFilterCheckOk(meta.getDevices(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by getFilterByDevice");
                    return false;
                }
            }
            if (filterByPlatform) {
                filter = filterParams.getFilterByPlatform();
                if (!isFilterCheckOk(meta.getPlatforms(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by getFilterByPlatform");
                    return false;
                }
            }
            if (filterByRunType) {
                filter = filterParams.getFilterByRunType();
                if (!isFilterCheckOk(meta.getRunTypes(), filter)) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by getFilterByRunType");
                    return false;
                }
            }

            if (filterByTestType) {
                if (!isFilterByTestType(meta.getTestType())) {
                    TS.log().trace("test[" + testCaseName + "] filtered out by isFilterByTestType");
                    return false;
                }
            }

        }
        return ok;

    }

    /**
     * Gets the test classes met filters.
     *
     * @return the test classes met filters
     */
    public List<Class<?>> getTestClassesMetFilters() {
        return testClassesMetFilters;
    }

    /**
     * Checks if is filter test name starts with.
     *
     * @param test the test
     * @return true, if is filter test name starts with
     */
    public boolean isFilterTestNameStartsWith(final Class<?> test) {
        return isFilterTestNameStartsWith(test, TS.params().getFilterByTestPlanNameStartsWith());
    }

    /**
     * Checks if is filter test name starts with.
     *
     * @param test       the test
     * @param startsWith the starts with
     * @return true, if is filter test name starts with
     */
    public boolean isFilterTestNameStartsWith(final Class<?> test, final String startsWith) {
        if (null != startsWith && startsWith.length() > 0) {
            return test.getName().startsWith(startsWith);
        }
        return true;
    }

    /**
     * Checks if is filter by test type.
     *
     * @param testType the test type
     * @return true, if is filter by test type
     */
    public boolean isFilterByTestType(final TestType testType) {
        if (testType != TS.params().getFilterByTestType()) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is filter by id.
     *
     * @param id     the id
     * @param values the values
     * @return true, if is filter by id
     */
    public boolean isFilterById(final int id, final String values) {
        for (final String value : values.split(",")) {
            try {
                if (Integer.parseInt(value) == id) {
                    return true;
                }
            } catch (final Exception e) {
                TS.log().warn("Param filter value for id had issue", e);
            }
        }
        return false;
    }

    /**
     * Checks if is filter check ok.
     *
     * @param lst    the lst
     * @param values the values
     * @return true, if is filter check ok
     */
    private boolean isFilterCheckOk(final List<String> lst, final String values) {
        if (null != values && values.length() > 0) {
            boolean rtn = false;
            String valueToCompare;
            for (final String value : values.split(",")) {
                valueToCompare = value.trim();
                if (value.startsWith("~")) {
                    String valueToCompareMatch = valueToCompare.replace("~", "");
                    if (lst.stream().anyMatch(str -> str.equalsIgnoreCase(valueToCompareMatch))) {
                        return false; // Fail Not, failed filter
                    } else {
                        rtn = true; // Passed initially, still a Not could be
                        // used
                    }
                } else if (lst.contains(valueToCompare)) {
                    rtn = true; // Passed initially, still a Not could be used
                }
            }
            return rtn;
        }
        return true; // Filter is Off
    }

    /**
     * Checks if is filter check ok.
     *
     * @param ary    the ary
     * @param values the values
     * @return true, if is filter check ok
     */
    private boolean isFilterCheckOk(final String[] ary, final String values) {
        if (null != values && values.length() > 0) {
            final List<String> lst;
            if (null != ary) {
                lst = Arrays.asList(ary);
            } else {
                lst = new ArrayList<>();
            }
            return isFilterCheckOk(lst, values);
        }
        return true; // Filter is Off
    }

    /**
     * Load compiled test clase.
     *
     * @return the test filter
     */
    public TestFilter loadCompiledTestClase() {
        loadCompiledTestClase(TS.params().getLookAtInternalTests());
        return this;
    }

    /**
     * Load compiled test clase.
     *
     * @param internalClass the internal class
     * @return the int
     */
    public int loadCompiledTestClase(final String internalClass) {
        if (null != internalClass && internalClass.length() > 0) {
            final Reflections reflections = new Reflections(internalClass);
            Set<Class<?>> lst = reflections.getTypesAnnotatedWith(TestPlan.class);
            testClasses.addAll(lst);
            return lst.size();
        }
        return 0;
    }

    /**
     * Load uncompiled test plans.
     *
     * @return the test filter
     */
    public TestFilter loadUncompiledTestPlans() {
        return loadUncompiledTestPlans(TS.params().getLookAtExternalTests());
    }

    /**
     * Load test plans.
     *
     * @param testValue the test value
     * @return the test filter
     */
    public TestFilter loadTestPlans(final String testValue) {
        return loadUncompiledTestPlans(testValue);
    }

    /**
     * Load uncompiled test plans.
     *
     * @param externalValue the external value
     * @return the test filter
     */
    public TestFilter loadUncompiledTestPlans(final String externalValue) {
        try {
            // final String externalValue =
            // TS.params().getLookAtExternalTests();

            if (null != externalValue && externalValue.length() > 0) {
                final List<File> files = new ArrayList<>();
                try (
                    final GroovyClassLoader loader =
                        (GroovyClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
                            final ClassLoader parent = this.getClass().getClassLoader();
                            public Object run() {
                                return new GroovyClassLoader(parent);
                            }
                        });
                ) {

                    for (final String path : externalValue.split(",")) {

                        File externalTests = new File(Params.addUserDir(path));
                        if (!externalTests.exists()) {
                            externalTests = new File(path);
                        }

                        if (!externalTests.exists()) {
                            if (loadCompiledTestClase(path) == 0) {
                                TS.log().error(
                                        "Param LookAtExternalTests is set to a class/file/directory not found: " +
                                                externalTests.getAbsolutePath());
                            }
                        } else if (externalTests.isDirectory()) {
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
        } catch (final RuntimeException e) {
            TS.log().warn("Issue loading uncompiled Tests, if groovy part of the porject?", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * Gets the test classes.
     *
     * @return the test classes
     */
    public Set<Class<?>> getTestClasses() {
        return testClasses;
    }

    /**
     * Sets the test classes.
     *
     * @param testClasses the new test classes
     * @return the test filter
     */
    public TestFilter setTestClasses(final Set<Class<?>> testClasses) {
        this.testClasses = testClasses;
        return this;
    }

    /**
     * Reset test classes met filters.
     *
     * @return the test filter
     */
    public TestFilter resetTestClassesMetFilters() {
        testClassesMetFilters.clear();
        return this;
    }

    /**
     * Reset test classes.
     *
     * @return the test filter
     */
    public TestFilter resetTestClasses() {
        testClasses.clear();
        return this;
    }

}
