package org.testah.framework.testPlan;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.MDC;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
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
import org.testah.framework.dto.StepAction;
import org.testah.framework.dto.TestDtoHelper;
import org.testah.runner.TestahJUnitRunner;
import org.testah.runner.testPlan.TestPlanActor;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * The Class AbstractTestPlan.
 */
@ContextHierarchy({@ContextConfiguration(classes = TestConfiguration.class)})
public abstract class AbstractTestPlan extends AbstractJUnit4SpringContextTests {

    /**
     * The test plan.
     */
    private static ThreadLocal<TestPlanDto> testPlan = new ThreadLocal<>();

    /**
     * The test case.
     */
    private static ThreadLocal<TestCaseDto> testCase = new ThreadLocal<>();

    /**
     * The test step.
     */
    private static ThreadLocal<TestStepDto> testStep = new ThreadLocal<>();

    /**
     * The test plan start.
     */
    private static ThreadLocal<Boolean> testPlanStart = new ThreadLocal<>();

    /**
     * The test filter.
     */
    private static TestFilter testFilter = null;

    /**
     * The ignored tests.
     */
    private static ThreadLocal<HashMap<String, String>> ignoredTests = null;

    /**
     * The name.
     */
    public TestName name = new TestName();

    /**
     * The assume true.
     */
    private boolean assumeTrue = false;

    /**
     * The global timeout.
     */
    public TestRule globalTimeout = Timeout.millis(100000L);


    public TestRule getGlobalTimeout() {
        return globalTimeout;
    }

    /**
     * The description.
     */
    private Description description;

    /**
     * The initialize.
     */
    public ExternalResource initialize = new ExternalResource() {

        protected void before() throws Throwable {
            filterTest(description);
            initlizeTest();
        }

        protected void after() {
            tearDownTest();
        }

        ;
    };

    /**
     * Initlize test.
     */
    public abstract void initlizeTest();

    /**
     * Tear down test.
     */
    public abstract void tearDownTest();

    /**
     * The filter.
     */
    public TestWatcher filter = new TestWatcher() {

        public Statement apply(final Statement base, final Description description) {
            setDescription(description);
            return super.apply(base, description);
        }
    };

    /**
     * Filter test.
     *
     * @param description the description
     */
    public void filterTest(final Description description) {
        final String name = description.getClassName() + "#" + description.getMethodName();
        final KnownProblem kp = description.getAnnotation(KnownProblem.class);
        setAssumeTrue(false);
        TestCaseDto test = new TestCaseDto();
        test = TestDtoHelper.fill(test, description.getAnnotation(TestCase.class), kp,
                description.getTestClass().getAnnotation(TestPlan.class));
        if (!getTestFilter().filterTestCase(test, name)) {
            addIgnoredTest(name, "METADATA_FILTER");
            setAssumeTrue(true);
            Assume.assumeTrue("Filtered out, For details use Trace level logging" +
                    "\nCheck your filter settings in Testah.propeties for filter_DEFAULT_filterIgnoreKnownProblem", false);
        }

        if (null != TS.params().getFilterIgnoreKnownProblem()) {
            if (null != kp) {
                if ("true".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                    setAssumeTrue(true);
                    addIgnoredTest(name, "KNOWN_PROBLEM_FILTER");
                    Assume.assumeTrue("Filtered out, KnownProblem found: " + kp.description() +
                            "\nCheck your filter settings in Testah.propeties for filter_DEFAULT_filterIgnoreKnownProblem", false);
                }
            } else if ("false".equalsIgnoreCase(TS.params().getFilterIgnoreKnownProblem())) {
                setAssumeTrue(true);
                addIgnoredTest(name, "KNOWN_PROBLEM_FILTER");
                Assume.assumeTrue(
                        "Filtered out, KnownProblem Not found and is required\nCheck your filter settings in Testah.propeties for filter_DEFAULT_filterIgnoreKnownProblem",
                        false);
            }
        }
    }

    /**
     * The watchman2.
     */
    public TestWatcher watchman2 = new TestWatcher() {

        protected void failed(final Throwable e, final Description description) {
            StepAction.createAssertResult("Unexpected Error occured", false, "UnhandledExceptionFoundByJUnit", "",
                    e.getMessage(), e).add();

            TS.log().error("TESTCASE Status: failed", e);
            stopTestCase(false);
        }

        protected void succeeded(final Description description) {
            stopTestCase(null);
            TS.log().info("TESTCASE Status: " + getTestCase().getStatusEnum());
            try {
                final Test testAnnotation = description.getAnnotation(Test.class);
                if (null != testAnnotation && None.class == testAnnotation.expected()) {
                    if (null != getTestCase()) {
                        getTestCase().getAssertionError();
                        if (null == getTestCase().getStatus()) {
                            addIgnoredTest(description.getClassName() + "#" + description.getMethodName(),
                                    "NA_STATUS_NO_ASSERTS");
                        }
                    }
                }
            } catch (final AssertionError ae) {
                TS.log().error("Exception Thrown Looking at TestCase Assert History\n" + ae.getMessage());
                throw ae;
            }

        }

        protected void finished(final Description desc) {
            TS.log().info("TESTCASE Complete: " + desc.getDisplayName() + " - thread[" + Thread.currentThread().getId() + "]");
        }

        protected void starting(final Description desc) {
            if (!didTestPlanStart()) {
                TS.log().info("TESTPLAN started:" + desc.getTestClass().getName() + " - thread[" + Thread.currentThread().getId() + "]");
                final TestPlan testPlan = desc.getTestClass().getAnnotation(TestPlan.class);
                if (null == desc.getTestClass().getAnnotation(TestPlan.class)) {
                    TS.log().warn("Missing @TestPlan annotation!");
                }
                startTestPlan(desc, testPlan, desc.getTestClass().getAnnotation(KnownProblem.class));
                getTestPlan().setRunInfo(TestDtoHelper.createRunInfo());

                for (final Method m : desc.getTestClass().getDeclaredMethods()) {
                    if (null != m.getAnnotation(Ignore.class)) {
                        addIgnoredTest(desc.getClassName() + "#" + m.getName(), "JUNIT_IGNORE");
                    }
                }
                MDC.put("logFileName", "" + Thread.currentThread().getId());
            }
            TS.log().info(Cli.BAR_LONG);

            TS.log().info(
                    "TESTCASE started:" + desc.getDisplayName() + " - thread[" + Thread.currentThread().getId() + "]");
            startTestCase(desc, desc.getAnnotation(TestCase.class), desc.getTestClass().getAnnotation(TestPlan.class),
                    desc.getAnnotation(KnownProblem.class));
            getTestStep();
        }
    };

    /**
     * The chain.
     */
    @Rule
    public TestRule chain = RuleChain.outerRule(watchman2).around(initialize).around(name).around(filter);

    public TestRule getChain() {
        return chain;
    }

    /**
     * Setup abstract test plan.
     */
    @BeforeClass
    public static void setupAbstractTestPlan() {
        try {

        } catch (final Exception e) {

        }
    }

    /**
     * Tear down abstract test plan.
     */
    @AfterClass
    public static void tearDownAbstractTestPlan() {
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
            TS.log().error("after testplan", e);
        }
    }

    public static void tearDownTestah() {
        if (TS.isBrowser()) {
            TS.browser().close();
        }
        TS.setBrowser(null);
        TS.tearDown();
    }

    public static void cleanUpTestplanThreadLocal() {
        cleanUpThreadLocal(testPlan);
        cleanUpThreadLocal(testCase);
        cleanUpThreadLocal(testStep);
        cleanUpThreadLocal(testPlanStart);
        cleanUpThreadLocal(ignoredTests);
    }

    private static void cleanUpThreadLocal(final ThreadLocal<?> threadLocal) {
        try {
            if (null != threadLocal) {
                threadLocal.remove();
            }
        } catch (final Exception e) {
            TS.log().warn("Trying to remove thread local", e);
        }
    }

    /**
     * Do on fail.
     */
    public abstract void doOnFail();

    /**
     * Do on pass.
     */
    public abstract void doOnPass();

    /**
     * Did test plan start.
     *
     * @return true, if successful
     */
    private static boolean didTestPlanStart() {
        if (null == testPlanStart.get()) {
            testPlanStart.set(false);
        }
        return testPlanStart.get();
    }

    /**
     * Sets the test plan start.
     *
     * @param testPlanStart the new test plan start
     */
    private static void setTestPlanStart(final boolean testPlanStart) {
        AbstractTestPlan.testPlanStart.set(testPlanStart);
    }

    /**
     * Gets the test plan thread local.
     *
     * @return the test plan thread local
     */
    private static ThreadLocal<TestPlanDto> getTestPlanThreadLocal() {
        if (null == testPlan) {
            testPlan = new ThreadLocal<>();
        }
        return testPlan;
    }

    /**
     * Gets the test plan.
     *
     * @return the test plan
     */
    public static TestPlanDto getTestPlan() {
        return getTestPlanThreadLocal().get();
    }

    /**
     * Gets the test case thread local.
     *
     * @return the test case thread local
     */
    private static ThreadLocal<TestCaseDto> getTestCaseThreadLocal() {
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
    private static TestCaseDto getTestCase() {
        return getTestCaseThreadLocal().get();
    }

    /**
     * Gets the test step.
     *
     * @return the test step
     */
    public static TestStepDto getTestStep() {

        if (null == getTestStepTreadLocal().get() && null != getTestCase()) {
            AbstractTestPlan.testStep.set(new TestStepDto("Initial Step", "").start());
            TS.log().info("TESTSTEP - " + AbstractTestPlan.testStep.get().getName());
        }
        return getTestStepTreadLocal().get();
    }

    public static ThreadLocal<TestStepDto> getTestStepTreadLocal() {
        if (null == testStep) {
            testStep = new ThreadLocal<>();
        }
        return testStep;
    }

    /**
     * Gets the test step thread local.
     *
     * @return the test step thread local
     */
    public static ThreadLocal<TestStepDto> getTestStepThreadLocal() {
        if (null == testStep) {
            testStep = new ThreadLocal<>();
        }
        return testStep;
    }

    /**
     * Start test plan.
     *
     * @param desc        the desc
     * @param testPlan    the test plan
     * @param knowProblem the know problem
     * @return the test plan dto
     */
    private TestPlanDto startTestPlan(final Description desc, final TestPlan testPlan, final KnownProblem knowProblem) {
        getTestPlanThreadLocal().set(TestDtoHelper.createTestPlanDto(desc, testPlan, knowProblem).start());
        setTestPlanStart(true);
        return AbstractTestPlan.testPlan.get();
    }

    /**
     * Stop test plan.
     */
    public static void stopTestPlan() {
        setTestPlanStart(false);
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
    private TestCaseDto startTestCase(final Description desc, final TestCase testCase, final TestPlan testPlan,
                                      final KnownProblem knowProblem) {
        if (didTestPlanStart()) {
            getTestCaseThreadLocal()
                    .set(TestDtoHelper.createTestCaseDto(desc, testCase, knowProblem, testPlan).start());
        }
        return getTestCase();
    }

    /**
     * Stop test case.
     *
     * @param status the status
     * @return the boolean
     */
    private static void stopTestCase(final Boolean status) {
        if (null != getTestCase()) {
            stopTestStep();
            getTestPlan().addTestCase(getTestCase().stop(status));
        }
    }

    /**
     * Start test step.
     *
     * @param testStep the test step
     * @return the test step dto
     */
    public static TestStepDto startTestStep(final TestStepDto testStep) {
        if (didTestPlanStart() && null != getTestCase()) {
            stopTestStep();
            getTestStepThreadLocal().set(testStep.start());
            TS.log().info("TESTSTEP - " + testStep.getName() + " " + testStep.getDescription());
        }
        return getTestStep();
    }

    /**
     * Stop test step.
     */
    private static void stopTestStep() {
        if (null != getTestStep()) {
            getTestCase().addTestStep(getTestStep().stop());
            testStep.set(null);
        }
    }

    /**
     * Adds the step action.
     *
     * @param stepAction the step action
     * @return true, if successful
     */
    public static boolean addStepAction(final StepActionDto stepAction) {
        return addStepAction(stepAction, true);
    }

    /**
     * Adds the step action.
     *
     * @param stepAction the step action
     * @param writeToLog the write to log
     * @return true, if successful
     */
    public static boolean addStepAction(final StepActionDto stepAction, final boolean writeToLog) {
        if (null == getTestStep()) {
            return false;
        }
        if (null != stepAction) {
            getTestStep().addStepAction(stepAction);
            if (writeToLog) {
                final StringBuilder sb = new StringBuilder("StepAction - ");
                if (null != stepAction.getStatus()) {
                    sb.append("status:" + stepAction.getStatus() + " - ");
                }
                if (null != stepAction.getMessage1()) {
                    sb.append(" " + stepAction.getMessage1());
                }
                if (null != stepAction.getMessage2()) {
                    sb.append(" " + stepAction.getMessage2());
                }
                if (null != stepAction.getMessage3()) {
                    sb.append(" " + stepAction.getMessage3());
                }
                TS.log().info(sb.toString());
            }
        }
        return true;
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
        return StepAction.createInfo(message1);
    }

    /**
     * Data value.
     *
     * @param value the value
     * @return the abstract test plan
     */
    public AbstractTestPlan dataValue(final String value) {
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
     * Gets the test filter.
     *
     * @return the test filter
     */
    public static TestFilter getTestFilter() {
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
    public static void setTestFilter(final TestFilter testFilter) {
        AbstractTestPlan.testFilter = testFilter;
    }

    /**
     * Gets the ignored tests.
     *
     * @return the ignored tests
     */
    public static HashMap<String, String> getIgnoredTests() {
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
     * Adds the ignored test.
     *
     * @param testCaseName the test case name
     * @param reason       the reason
     */
    public static void addIgnoredTest(final String testCaseName, final String reason) {
        getIgnoredTests().put(testCaseName, reason);
    }

    /**
     * Checks if is assume true.
     *
     * @return true, if is assume true
     */
    public boolean isAssumeTrue() {
        return assumeTrue;
    }

    /**
     * Sets the assume true.
     *
     * @param assumeTrue the assume true
     * @return the abstract test plan
     */
    public AbstractTestPlan setAssumeTrue(final boolean assumeTrue) {
        this.assumeTrue = assumeTrue;
        return this;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     * @return the abstract test plan
     */
    public AbstractTestPlan setDescription(final Description description) {
        this.description = description;
        return this;
    }

    public AbstractTestPlan resetTestCase(final String reasonWhy) {
        getTestCase().getTestSteps().clear();
        getTestStepThreadLocal().set(new TestStepDto("Reseting TestCase And Going To Retry", reasonWhy).start());
        return this;
    }

    public static void setUpThreadLocals() {
        testPlan = new ThreadLocal<TestPlanDto>();
        testCase = new ThreadLocal<TestCaseDto>();
        testStep = new ThreadLocal<TestStepDto>();
        testPlanStart = new ThreadLocal<Boolean>();
        ignoredTests = new ThreadLocal<HashMap<String, String>>();
    }
}
