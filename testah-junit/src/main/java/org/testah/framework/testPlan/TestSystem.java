package org.testah.framework.testPlan;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.Description;
import org.slf4j.MDC;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.client.dto.TestStepDto;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.cli.Cli;
import org.testah.framework.cli.TestFilter;
import org.testah.framework.dto.TestCaseAnnotationDto;
import org.testah.framework.dto.TestDtoHelper;
import org.testah.framework.dto.TestPlanAnnotationDto;
import org.testah.runner.TestahJUnitRunner;
import org.testah.runner.testPlan.TestPlanActor;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TestSystem {

    /**
     * The test plan.
     */
    protected ThreadLocal<TestPlanDto> testPlan = new ThreadLocal<>();

    /**
     * The test case.
     */
    protected ThreadLocal<TestCaseDto> testCase = new ThreadLocal<>();

    /**
     * The test step.
     */
    protected ThreadLocal<TestStepDto> testStep = new ThreadLocal<>();

    /**
     * The test plan start.
     */
    protected ThreadLocal<Boolean> testPlanStart = new ThreadLocal<>();

    /**
     * The test filter.
     */
    public TestFilter testFilter = null;

    /**
     * The ignored tests.
     */
    protected ThreadLocal<HashMap<String, String>> ignoredTests = null;

    /**
     * The description.
     */
    private Description description;

    /**
     * Starting of a testCase.
     *
     * @param description testCase description sent by junit.
     */
    public void starting(final Description description) {
        starting(description,
            TestPlanAnnotationDto.create(description.getTestClass().getAnnotation(TestPlan.class)),
            TestCaseAnnotationDto.create(description.getAnnotation(TestCase.class)));
    }

    /**
     * Starting of a testCase.
     *
     * @param desc testCase description sent by junit.
     * @param testPlan testPlan annotation info.
     * @param testCase testCase annotation info.
     */
    public void starting(final Description desc, TestPlanAnnotationDto testPlan, TestCaseAnnotationDto testCase) {
        setDescription(desc);
        if (!didTestPlanStart()) {

            setUpThreadLocals();

            TS.log().info("TESTPLAN started:" + desc.getTestClass().getName() + " - thread[" + Thread.currentThread().getId() + "]");
            if (null == testPlan) {
                TS.log().warn("Missing @TestPlan annotation!");
            }
            startTestPlan(desc, testPlan, desc.getTestClass().getAnnotation(KnownProblem.class));
            getTestPlan().setRunInfo(TestDtoHelper.createRunInfo());

            for (final Method m : desc.getTestClass().getDeclaredMethods()) {
                if (null != m.getAnnotation(Ignore.class) && null != m.getAnnotation(Disabled.class)) {
                    addIgnoredTest(desc.getDisplayName(), "JUNIT_IGNORE");
                }
            }
            MDC.put("logFileName", "" + Thread.currentThread().getId());
        }
        TS.log().info(Cli.BAR_LONG);

        TS.log().info(
            "TESTCASE started:" + desc.getDisplayName() + " - thread[" + Thread.currentThread().getId() + "]");
        startTestCase(desc, testCase, testPlan,
            desc.getAnnotation(KnownProblem.class));
        getTestStep();
    }

    /**
     * Failed called if a testCase Fails.
     *
     * @param e exception given by junit.
     * @param description testCase description sent by junit.
     */
    public void failed(final Throwable e, final Description description) {
        setDescription(description);
        TS.step().action().createAssertResult("Unexpected Error occurred", false, "UnhandledExceptionFoundByJUnit", "",
            e.getMessage(), e).log();

        TS.log().error("TESTCASE Status: failed", e);
        stopTestCase(false);
    }

    /**
     * Succeeded called if testCase succeeded.
     *
     * @param description testCase description sent by junit.
     */
    public void succeeded(final Description description) {
        setDescription(description);
        stopTestCase((TS.params().isResultIgnoredIfNoAssertsFound() ? null : true));
        TS.log().info("TESTCASE Status: " + getTestCase().getStatusEnum());
        try {
            final Test testAnnotation = description.getAnnotation(Test.class);
            if (null != testAnnotation && Test.None.class == testAnnotation.expected()) {
                if (null != getTestCase()) {
                    getTestCase().getAssertionError();
                    if (null == getTestCase().getStatus()) {
                        addIgnoredTest(description.getDisplayName(),
                            "NA_STATUS_NO_ASSERTS");
                        return;
                    }
                }
            }
        } catch (final AssertionError ae) {
            TS.log().error("Exception Thrown Looking at TestCase Assert History\n" + ae.getMessage());
            throw ae;
        }
    }

    /**
     * Finished called when the testCase completes.
     *
     * @param desc testCase description sent by junit.
     */
    public void finished(final Description desc) {
        setDescription(desc);
        TS.log().info("TESTCASE Complete: " + desc.getDisplayName() + " - thread[" + Thread.currentThread().getId() + "]");
        if (null == getTestCase().getStatus()) {
            return;
        } else if (getTestCase().getStatus()) {
            // doOnPass(); TODO find solution
        } else {
            // doOnFail(); TODO find solution
        }
    }

    /**
     * Setup abstract test plan.
     */
    public void setupTestSystem() {
        setUpThreadLocals();
    }

    /**
     * Sets up thread locals.
     */
    public void setUpThreadLocals() {
        setUpThreadLocals(false);
    }

    /**
     * Set up ThreadLocals.
     *
     * @param override the override
     */
    public void setUpThreadLocals(final boolean override) {
        if (override || !TestahJUnitRunner.isInUse()) {
            testPlan = new ThreadLocal<>();
            testCase = new ThreadLocal<>();
            testStep = new ThreadLocal<>();
            testPlanStart = new ThreadLocal<>();
            ignoredTests = new ThreadLocal<>();
        }
    }

    /**
     * Tear down abstract test plan.
     */
    public void tearDownTestSystem() {
        try {
            if (null != getTestPlan()) {
                getTestPlan().stop();
            }
            if (!TestPlanActor.isResultsInUse()) {
                TS.getTestPlanReporter().reportResults(getTestPlan());
            }
            if (!TestahJUnitRunner.isInUse()) {
                cleanUpTestplanThreadLocal();
            }

            if (!TestPlanActor.isResultsInUse()) {
                tearDownTestah();
            }
        } catch (final Exception e) {
            TS.log().error("tearDownTestSystem had an issue", e);
        }
    }

    /**
     * Gets the test plan.
     *
     * @return the test plan
     */
    public TestPlanDto getTestPlan() {
        return getTestPlanThreadLocal().get();
    }

    /**
     * Clean up all ThreadLocal.
     */
    public void cleanUpTestplanThreadLocal() {
        TS.cleanUpThreadLocal(testPlan);
        TS.cleanUpThreadLocal(testCase);
        TS.cleanUpThreadLocal(testStep);
        TS.cleanUpThreadLocal(testPlanStart);
        TS.cleanUpThreadLocal(ignoredTests);
    }

    /**
     * Clean up when Testah is done.
     */
    public void tearDownTestah() {
        if (TS.isBrowser()) {
            TS.browser().close();
        }
        TS.setBrowser(null);
        TS.tearDown();
    }

    /**
     * Gets the test plan thread local.
     *
     * @return the test plan thread local
     */
    protected ThreadLocal<TestPlanDto> getTestPlanThreadLocal() {
        if (null == testPlan) {
            testPlan = new ThreadLocal<>();
        }
        return testPlan;
    }

    /**
     * Stop test plan.
     */
    public void stopTestPlan() {
        setTestPlanStart(false);
    }

    /**
     * Stop test case.
     *
     * @param status the status
     */
    protected void stopTestCase(final Boolean status) {
        if (null != getTestCase()) {
            stopTestStep();
            getTestPlan().addTestCase(getTestCase().stop(status));
        }
    }

    /**
     * Stop test step.
     */
    protected void stopTestStep() {
        if (null != getTestStep()) {
            getTestCase().addTestStep(getTestStep().stop());
            testStep.set(null);
        }
    }

    /**
     * Gets the test step.
     *
     * @return the test step
     */
    public TestStepDto getTestStep() {

        if (null == getTestStepThreadLocal().get()) {
            testStep.set(new TestStepDto("Initial Step", "").start());
            TS.log().info("TESTSTEP - " + this.testStep.get().getName());
        }
        return getTestStepThreadLocal().get();
    }

    /**
     * Gets the test step thread local.
     *
     * @return the test step thread local
     */
    public ThreadLocal<TestStepDto> getTestStepThreadLocal() {
        if (null == testStep) {
            testStep = new ThreadLocal<>();
        }
        return testStep;
    }

    /**
     * Start test step.
     *
     * @param testStep the test step
     * @return the test step dto
     */
    public TestStepDto startTestStep(final TestStepDto testStep) {
        if (didTestPlanStart() && null != getTestCase()) {
            stopTestStep();
            getTestStepThreadLocal().set(testStep.start());
            TS.log().info("TESTSTEP - " + testStep.getName() + " " + testStep.getDescription());
        }
        return getTestStep();
    }

    /**
     * Gets the test filter.
     *
     * @return the test filter
     */
    public TestFilter getTestFilter() {
        if (null == testFilter) {
            testFilter = new TestFilter();
        }
        return testFilter;
    }

    /**
     * Sets the test filter.
     *
     * @param testFilter the new test filter
     */
    public void setTestFilter(final TestFilter testFilter) {
        this.testFilter = testFilter;
    }

    /**
     * Adds the ignored test.
     *
     * @param testCaseName the test case name
     * @param reason       the reason
     */
    public void addIgnoredTest(final String testCaseName, final String reason) {
        getIgnoredTests().put(testCaseName, reason);
    }

    /**
     * Add Ignore Test.
     *
     * @param description testCase description sent by junit.
     * @param reason reason why the test was ignored.
     */
    public void addIgnoredTest(final Description description, final String reason) {
        addIgnoredTest(description.getDisplayName(), reason);
    }


    /**
     * Gets the ignored tests.
     *
     * @return the ignored tests
     */
    public HashMap<String, String> getIgnoredTests() {
        final ThreadLocal<HashMap<String, String>> ignoredTestsTmp;
        if (null == ignoredTests) {
            ignoredTestsTmp = new ThreadLocal<>();
            ignoredTestsTmp.set(new HashMap<String, String>());
            ignoredTests = ignoredTestsTmp;
        }
        if (null == ignoredTests.get()) {
            ignoredTests.set(new HashMap<String, String>());
        }
        return ignoredTests.get();
    }

    /**
     * Sets the test plan start.
     *
     * @param testPlanStart the new test plan start
     */
    protected void setTestPlanStart(final boolean testPlanStart) {
        this.testPlanStart.set(testPlanStart);
    }

    /**
     * Did test plan start.
     *
     * @return true, if successful
     */
    protected boolean didTestPlanStart() {
        if (null == testPlanStart.get()) {
            testPlanStart.set(false);
        }
        return testPlanStart.get();
    }

    /**
     * Gets the test case thread local.
     *
     * @return the test case thread local
     */
    protected ThreadLocal<TestCaseDto> getTestCaseThreadLocal() {
        if (null == testCase) {
            testCase = new ThreadLocal<>();
        }
        return testCase;
    }

    /**
     * Gets the test case.
     *
     * @return the test case
     */
    public TestCaseDto getTestCase() {
        return getTestCaseThreadLocal().get();
    }

    /**
     * filterTest to see what should run.
     * @param description junit 4 style Description.
     */
    public void filterTest(final Description description) {
        filterTest(description,
            TestPlanAnnotationDto.create(description.getTestClass().getAnnotation(TestPlan.class)),
            TestCaseAnnotationDto.create(description.getAnnotation(TestCase.class)));
    }

    /**
     * filterTest test to run.
     *
     * @param description the description
     * @param testPlan    testplan info from the annotation
     * @param testCase    testcase info from the annotation
     */
    public void filterTest(final Description description, TestPlanAnnotationDto testPlan, TestCaseAnnotationDto testCase) {
        final String name = description.getDisplayName();
        final KnownProblem kp = description.getAnnotation(KnownProblem.class);
        TestCaseDto test = new TestCaseDto();
        test = TestDtoHelper.fill(test, testCase, kp, testPlan);
        if (!getTestFilter().filterTestCase(test, name)) {
            addIgnoredTest(name, "METADATA_FILTER");
            Assume.assumeTrue("Filtered out, For details use Trace level logging" +
                "\nCheck your filter settings in Testah.properties for " +
                "filter_DEFAULT_filterIgnoreKnownProblem", false);
        }

        if (null != TS.params().getFilterIgnoreKnownProblem()) {
            if (null != kp) {
                if ("true".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                    addIgnoredTest(name, "KNOWN_PROBLEM_FILTER");
                    Assume.assumeTrue("Filtered out, KnownProblem found: " + kp.description() +
                        "\nCheck your filter settings in Testah.properties for " +
                        "filter_DEFAULT_filterIgnoreKnownProblem", false);
                }
            } else if ("false".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                addIgnoredTest(name, "KNOWN_PROBLEM_FILTER");
                Assume.assumeTrue(
                    "Filtered out, KnownProblem Not found and is required\nCheck your filter" +
                        " settings in Testah.properties for filter_DEFAULT_filterIgnoreKnownProblem",
                    false);
            }
        }
    }

    /**
     * Start test plan.
     *
     * @param desc        the desc
     * @param testPlan    the test plan
     * @param knowProblem the know problem
     * @return the test plan dto
     */
    public TestPlanDto startTestPlan(final Description desc, final TestPlanAnnotationDto testPlan, final KnownProblem knowProblem) {
        getTestPlanThreadLocal().set(TestDtoHelper.createTestPlanDto(desc, testPlan, knowProblem).start());
        setTestPlanStart(true);
        return this.testPlan.get();
    }

    /**
     * Start test case.
     *
     * @param desc        the desc
     * @param testCase    the test case
     * @param testPlan    the test plan
     * @param knowProblem the know problem
     * @return the test case dto
     */
    public TestCaseDto startTestCase(final Description desc, final TestCaseAnnotationDto testCase, final TestPlanAnnotationDto testPlan,
                                     final KnownProblem knowProblem) {
        if (didTestPlanStart()) {
            getTestCaseThreadLocal()
                .set(TestDtoHelper.createTestCaseDto(desc, testCase, knowProblem, testPlan).start());
        }
        return getTestCase();
    }

    /**
     * Step.
     *
     * @return the test step dto
     */
    public TestStepDto step() {
        return step("Step");
    }

    /**
     * Step.
     *
     * @param name the name
     * @return the test step dto
     */
    public TestStepDto step(final String name) {
        final TestStepDto s = new TestStepDto();
        s.setName(name);
        return startTestStep(s);
    }

    /**
     * Step.
     *
     * @param name        the name
     * @param description the description
     * @return the test step dto
     */
    public TestStepDto step(final String name, final String description) {
        final TestStepDto s = new TestStepDto();
        s.setName(name);
        s.setDescription(description);
        return startTestStep(s);
    }

    /**
     * Step action.
     *
     * @return the step action dto
     */
    public StepActionDto stepAction() {
        return new StepActionDto();
    }

    /**
     * Step action info.
     *
     * @param message1 the message1
     * @return the step action dto
     */
    public StepActionDto stepActionInfo(final String message1) {
        return TS.step().action().createInfo(message1);
    }

    /**
     * Data value.
     *
     * @param value the value
     * @return the abstract test plan
     */
    public TestSystem dataValue(final String value) {
        if (null == value) {
            getTestCase().setDataValue("");
        } else if (value.length() > 255) {
            TS.log().debug("Data Value can only be 255 chars, truncating value");
            getTestCase().setDataValue(value.substring(0, 254));
        } else {
            getTestCase().setDataValue(value);
        }
        return this;
    }

    /**
     * Reset the test case.
     *
     * @param reasonWhy explanation why test case was reset
     * @return this object
     */
    public TestSystem resetTestCase(final String reasonWhy) {
        getTestCase().getTestSteps().clear();
        getTestStepThreadLocal().set(new TestStepDto("Resetting TestCase And Going To Retry", reasonWhy).start());
        return this;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}
