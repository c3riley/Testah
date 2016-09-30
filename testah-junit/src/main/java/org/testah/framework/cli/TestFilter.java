package org.testah.framework.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.cxf.helpers.FileUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.reflections.Reflections;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.enums.TestType;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestPlan;

import groovy.lang.GroovyClassLoader;

/**
 * The Class TestFilter.
 */
public class TestFilter {

    /** The test classes. */
    private Set<Class<?>> testClasses;

    /** The test classes met filters. */
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
     */
    public void filterTestPlansToRun() {
        loadCompiledTestClase();
        loadUncompiledTestPlans();

        final Params filterParams = TS.params();

        if (null != testClasses) {
            TestPlan meta;
            String filter = null;

            boolean filterByUuid = true;
            if (null == TS.params().getFilterById() || TS.params().getFilterById().length() == 0) {
                filterByUuid = false;
            }
            Boolean filterByIgnoreKnownProblem = null;
            if (null != TS.params().getFilterIgnoreKnownProblem() && TS.params().getFilterIgnoreKnownProblem().length() > 0) {
                if ("true".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                    filterByIgnoreKnownProblem = true;
                } else {
                    filterByIgnoreKnownProblem = false;
                }
            }
            boolean filterByComponent = true;
            if (null == TS.params().getFilterByComponent() || TS.params().getFilterByComponent().length() == 0) {
                filterByComponent = false;
            }
            boolean filterByDevice = true;
            if (null == TS.params().getFilterByDevice() || TS.params().getFilterByDevice().length() == 0) {
                filterByDevice = false;
            }
            boolean filterByPlatform = true;
            if (null == TS.params().getFilterByPlatform() || TS.params().getFilterByPlatform().length() == 0) {
                filterByPlatform = false;
            }
            boolean filterByTag = true;
            if (null == TS.params().getFilterByTag() || TS.params().getFilterByTag().length() == 0) {
                filterByTag = false;
            }
            boolean filterByRunType = true;
            if (null == TS.params().getFilterByRunType() || TS.params().getFilterByRunType().length() == 0) {
                filterByRunType = false;
            }
            boolean filterByTestType = true;
            if (null == TS.params().getFilterByTestType()) {
                filterByTestType = false;
            }
            boolean filterByTestPlanNameStartsWith = true;
            if (null == TS.params().getFilterByTestPlanNameStartsWith()) {
                filterByTestPlanNameStartsWith = false;
            }

            for (final Class<?> test : testClasses) {

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
                        TS.log().trace("test[" + test.getName() + "] filtered out by isFilterByTestType");
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

                testClassesMetFilters.add(test);

            }
            TS.log().info(Cli.BAR_LONG);
            TS.log().info(Cli.BAR_WALL + "TestPlan Classes To Run: ( " + testClassesMetFilters.size() + " of "
                    + testClasses.size() + " )");
            TS.log().info(Cli.BAR_WALL);
            for (final Class<?> test : testClassesMetFilters) {
                TS.log().info(Cli.BAR_WALL + " " + test.getName());
            }
            TS.log().info("#");
            TS.log().info(Cli.BAR_LONG);
        }

    }

    /**
     * Filter test case.
     *
     * @param meta
     *            the meta
     * @param testCaseName
     *            the test case name
     * @return true, if successful
     */
    public boolean filterTestCase(final TestCaseDto meta, final String testCaseName) {
        final boolean ok = true;
        final Params filterParams = TS.params();

        if (null != meta) {

            String filter = null;

            boolean filterByUuid = true;
            if (null == TS.params().getFilterById() || TS.params().getFilterById().length() == 0) {
                filterByUuid = false;
            }

            boolean filterByComponent = true;
            if (null == TS.params().getFilterByComponent() || TS.params().getFilterByComponent().length() == 0) {
                filterByComponent = false;
            }
            boolean filterByDevice = true;
            if (null == TS.params().getFilterByDevice() || TS.params().getFilterByDevice().length() == 0) {
                filterByDevice = false;
            }
            boolean filterByPlatform = true;
            if (null == TS.params().getFilterByPlatform() || TS.params().getFilterByPlatform().length() == 0) {
                filterByPlatform = false;
            }
            boolean filterByTag = true;
            if (null == TS.params().getFilterByTag() || TS.params().getFilterByTag().length() == 0) {
                filterByTag = false;
            }
            boolean filterByRunType = true;
            if (null == TS.params().getFilterByRunType() || TS.params().getFilterByRunType().length() == 0) {
                filterByRunType = false;
            }
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
     * @param test
     *            the test
     * @return true, if is filter test name starts with
     */
    public boolean isFilterTestNameStartsWith(final Class<?> test) {
        return isFilterTestNameStartsWith(test, TS.params().getFilterByTestPlanNameStartsWith());
    }

    /**
     * Checks if is filter test name starts with.
     *
     * @param test
     *            the test
     * @param startsWith
     *            the starts with
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
     * @param testType
     *            the test type
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
     * @param id
     *            the id
     * @param values
     *            the values
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
     * @param lst
     *            the lst
     * @param values
     *            the values
     * @return true, if is filter check ok
     */
    private boolean isFilterCheckOk(final List<String> lst, final String values) {
        if (null != values && values.length() > 0) {
            boolean rtn = false;
            for (final String value : values.split(",")) {
                if (value.startsWith("~")) {
                    if (lst.contains(value)) {
                        return false; // Fail Not, failed filter
                    } else {
                        rtn = true; // Passed initially, still a Not could be
                                    // used
                    }
                } else if (lst.contains(value)) {
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
     * @param ary
     *            the ary
     * @param values
     *            the values
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
     * @param internalClass
     *            the internal class
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
     * @param testValue
     *            the test value
     * @return the test filter
     */
    public TestFilter loadTestPlans(final String testValue) {
        return loadUncompiledTestPlans(testValue);
    }

    /**
     * Load uncompiled test plans.
     *
     * @param externalValue
     *            the external value
     * @return the test filter
     */
    public TestFilter loadUncompiledTestPlans(final String externalValue) {
        try {
            // final String externalValue = TS.params().getLookAtExternalTests();

            if (null != externalValue && externalValue.length() > 0) {
                final List<File> files = new ArrayList<>();
                final ClassLoader parent = this.getClass().getClassLoader();
                try (final GroovyClassLoader loader = new GroovyClassLoader(parent)) {

                    for (final String path : externalValue.split(",")) {

                        File externalTests = new File(Params.addUserDir(path));
                        if (!externalTests.exists()) {
                            externalTests = new File(path);
                        }

                        if (!externalTests.exists()) {
                            if (loadCompiledTestClase(path) == 0) {
                                TS.log().error("Param LookAtExternalTests is set to a class/file/directory not found: "
                                        + externalTests.getAbsolutePath());
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
        } catch (final Exception e) {
            TS.log().warn("Issue loading uncompiled Tests, if groovy part of the porject?", e);
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
     * @param testClasses
     *            the new test classes
     */
    public void setTestClasses(final Set<Class<?>> testClasses) {
        this.testClasses = testClasses;
    }

}
