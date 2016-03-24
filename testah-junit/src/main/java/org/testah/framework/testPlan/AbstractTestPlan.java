package org.testah.framework.testPlan;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.testah.TS;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.dto.StepActionDto;
import org.testah.framework.dto.TestCaseDto;
import org.testah.framework.dto.TestPlanDto;
import org.testah.framework.dto.TestStepDto;
import org.testah.framework.report.TestPlanReporter;
import org.testah.runner.testPlan.TestPlanActor;

@ContextHierarchy({ @ContextConfiguration(classes = TestConfiguration.class) })
public abstract class AbstractTestPlan extends AbstractJUnit4SpringContextTests {

    private static ThreadLocal<TestPlanDto> testPlan;
    private static ThreadLocal<TestCaseDto> testCase;
    private static ThreadLocal<TestStepDto> testStep;
    private static ThreadLocal<Boolean>     testPlanStart = new ThreadLocal<Boolean>();;

    public TestName                         name          = new TestName();

    public TestRule                         globalTimeout = Timeout.millis(100000L);

    public ExternalResource                 initialize    = new ExternalResource() {

                                                              protected void before() throws Throwable {
                                                                  initlizeTest();
                                                              }

                                                              protected void after() {
                                                                  tearDownTest();
                                                              };
                                                          };

    public abstract void initlizeTest();

    public abstract void tearDownTest();

    public TestWatcher filter    = new TestWatcher() {

                                     public Statement apply(final Statement base, final Description description) {
                                         final String onlyRun = System.getProperty("only_run");
                                         Assume.assumeTrue(onlyRun == null || Arrays.asList(onlyRun.split(","))
                                                 .contains(description.getTestClass().getSimpleName()));
                                         final String mth = System.getProperty("method");
                                         Assume.assumeTrue(mth == null || Arrays.asList(mth.split(","))
                                                 .contains(description.getMethodName()));
                                         return super.apply(base, description);
                                     }
                                 };

    public TestWatcher watchman2 = new TestWatcher() {

                                     protected void failed(final Throwable e, final Description description) {
                                         StepActionDto
                                                 .createAssertResult("Unexpected Error occured", false,
                                                         "UnhandledExceptionFoundByJUnit", "", e.getMessage(), null)
                                                 .setException(e).add();
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
                                                 }
                                             }
                                         } catch (final AssertionError ae) {
                                             TS.log().error("Exception Thrown Looking at TestCase Assert History\n"
                                                     + ae.getMessage());
                                             throw ae;
                                         }

                                     }

                                     protected void finished(final Description desc) {
                                         TS.log().info("TESTCASE Complete: " + desc.getDisplayName() + " - thread["
                                                 + Thread.currentThread().getId() + "]");
                                     }

                                     protected void starting(final Description desc) {

                                         if (!didTestPlanStart()) {
                                             TS.log().info("TESTPLAN started:" + desc.getTestClass().getName()
                                                     + " - thread[" + Thread.currentThread().getId() + "]");
                                             startTestPlan(desc, desc.getTestClass().getAnnotation(TestPlan.class));
                                         }
                                         TS.log().info(
                                                 "###############################################################################");
                                         TS.log().info("TESTCASE started:" + desc.getDisplayName() + " - thread["
                                                 + Thread.currentThread().getId() + "]");
                                         startTestCase(desc, desc.getAnnotation(TestCase.class),
                                                 desc.getTestClass().getAnnotation(TestPlan.class));
                                     }
                                 };

    @Rule
    public TestRule    chain     = RuleChain.outerRule(watchman2).around(initialize).around(name).around(filter);

    @BeforeClass
    public static void setupAbstractTestPlan() {
        try {

        } catch (final Exception e) {

        }
    }

    @AfterClass
    public static void tearDownAbstractTestPlan() {
        try {
            if (TS.isBrowser()) {

                if (!TestPlanActor.isResultsInUse()) {
                    TS.browser().close();
                }

            }
            if (null != getTestPlan()) {
                getTestPlan().stop();
            }
            if (!TestPlanActor.isResultsInUse()) {
                TestPlanReporter.reportResults(getTestPlan());
            }

        } catch (final Exception e) {
            TS.log().error("after testplan", e);
        }
    }

    public abstract void doOnFail();

    public abstract void doOnPass();

    private static boolean didTestPlanStart() {
        if (null == testPlanStart.get()) {
            testPlanStart.set(false);
        }
        return testPlanStart.get();
    }

    private static void setTestPlanStart(final boolean testPlanStart) {
        AbstractTestPlan.testPlanStart.set(testPlanStart);
    }

    private static ThreadLocal<TestPlanDto> getTestPlanThreadLocal() {
        if (null == testPlan) {
            testPlan = new ThreadLocal<TestPlanDto>();
        }
        return testPlan;
    }

    public static TestPlanDto getTestPlan() {
        return getTestPlanThreadLocal().get();
    }

    private static ThreadLocal<TestCaseDto> getTestCaseThreadLocal() {
        if (null == testCase) {
            testCase = new ThreadLocal<TestCaseDto>();
        }
        return testCase;
    }

    private static TestCaseDto getTestCase() {
        return getTestCaseThreadLocal().get();
    }

    public static TestStepDto getTestStep() {
        if (null == testStep) {
            testStep = new ThreadLocal<TestStepDto>();
        }
        if (null == testStep.get() && null != getTestCase()) {
            AbstractTestPlan.testStep.set(new TestStepDto("Initial Step", "").start());
        }
        return testStep.get();
    }

    public static ThreadLocal<TestStepDto> getTestStepThreadLocal() {
        if (null == testStep) {
            testStep = new ThreadLocal<TestStepDto>();
        }
        return testStep;
    }

    private TestPlanDto startTestPlan(final Description desc, final TestPlan testPlan) {
        getTestPlanThreadLocal().set(new TestPlanDto(desc, testPlan).start());
        setTestPlanStart(true);
        return AbstractTestPlan.testPlan.get();
    }

    public static void stopTestPlan() {
        setTestPlanStart(false);
    }

    private TestCaseDto startTestCase(final Description desc, final TestCase testCase, final TestPlan testPlan) {
        if (didTestPlanStart()) {
            getTestCaseThreadLocal().set(new TestCaseDto(desc, testCase, testPlan).start());
        }
        return getTestCase();
    }

    private static Boolean stopTestCase(final Boolean status) {
        if (null != getTestCase()) {
            stopTestStep();
            getTestPlan().addTestCase(getTestCase().stop(status));
            return getTestCase().getStatus();
        }
        return null;
    }

    private static TestStepDto startTestStep(final TestStepDto testStep) {
        if (didTestPlanStart() && null != getTestCase()) {
            stopTestStep();
            getTestStepThreadLocal().set(testStep.start());
        }
        return getTestStep();
    }

    private static void stopTestStep() {
        if (null != getTestStep()) {
            getTestCase().addTestStep(getTestStep().stop());
            testStep = null;
        }
    }

    public static boolean addStepAction(final StepActionDto stepAction) {
        if (null == getTestStep()) {
            return false;
        }
        getTestStep().addStepAction(stepAction);
        return true;
    }

    public TestStepDto step() {
        return step("Step");
    }

    public TestStepDto step(final String name) {
        TestStepDto s = new TestStepDto();
        s.setName(name);
        return startTestStep(s);
    }

    public StepActionDto stepAction() {
        return new StepActionDto();
    }

    public StepActionDto stepActionInfo(final String message1) {
        return StepActionDto.createInfo(message1);
    }

}
