package org.testah.framework.testPlan;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.dto.TestStepDto;

/**
 * The Class AbstractTestPlan.
 */
public abstract class AbstractTestPlan {

    /**
     * The name.
     */
    public TestName name = new TestName();

    /**
     * The global timeout.
     */
    private TestRule globalTimeout = new Timeout(100000);
    /**
     * The description.
     */
    private Description description;
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
     * The initialize.
     */
    private ExternalResource initialize = new ExternalResource() {

        protected void before() throws Throwable {
            TS.testSystem().filterTest(description);
            initializeTest();
        }

        protected void after() {
            tearDownTest();
        }

        ;
    };
    /**
     * The watchman2.
     */
    private TestWatcher watchman2 = new TestWatcher() {

        protected void failed(final Throwable e, final Description description) {
            TS.testSystem().failed(e, description);
        }

        protected void succeeded(final Description description) {
            TS.testSystem().succeeded(description);
        }

        protected void finished(final Description desc) {
            TS.testSystem().finished(desc);
            if (null == TS.testSystem().getTestCase().getStatus()) {
                return;
            } else if (TS.testSystem().getTestCase().getStatus()) {
                doOnPass();
            } else {
                doOnFail();
            }
        }

        protected void starting(final Description desc) {
            TS.testSystem().starting(desc);
        }

    };
    /**
     * The chain.
     */
    @Rule
    public TestRule chain = RuleChain.outerRule(watchman2).around(initialize).around(name).around(filter);

    /**
     * Setup abstract test plan.
     */
    @BeforeClass
    public static void setupAbstractTestPlan() {
        TS.testSystem().setUpThreadLocals();
    }

    /**
     * Tear down abstract test plan.
     */
    @AfterClass
    public static void tearDownAbstractTestPlan() {
        TS.testSystem().tearDownTestSystem();
    }

    /**
     * Gets global timeout.
     *
     * @return the global timeout
     */
    public TestRule getGlobalTimeout() {
        return globalTimeout;
    }

    /**
     * Initialize test.
     */
    public abstract void initializeTest();

    /**
     * Tear down test.
     */
    public abstract void tearDownTest();

    /**
     * Gets chain.
     *
     * @return the chain
     */
    public TestRule getChain() {
        return chain;
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
        return TS.testSystem().step(name);
    }

    /**
     * Step.
     *
     * @param name        the name
     * @param description the description
     * @return the test step dto
     */
    public TestStepDto step(final String name, final String description) {
        return TS.testSystem().step(name, description);
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
    public AbstractTestPlan dataValue(final String value) {
        TS.testSystem().dataValue(value);
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

    /**
     * Reset the test case.
     *
     * @param reasonWhy explanation why test case was reset
     * @return this object
     */
    public AbstractTestPlan resetTestCase(final String reasonWhy) {
        TS.testSystem().resetTestCase(reasonWhy);
        return this;
    }
}
