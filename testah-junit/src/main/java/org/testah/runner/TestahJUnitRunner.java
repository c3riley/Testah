package org.testah.runner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;
import org.testah.runner.testPlan.TestPlanActor;

import java.util.List;

/**
 * The Class TestahJUnitRunner.
 */
public class TestahJUnitRunner {

    private static boolean inUse = false;

    /**
     * Is in use boolean.
     *
     * @return the boolean
     */
    public static boolean isInUse() {
        return inUse;
    }

    private static void setInUse(final boolean inUse) {
        TestahJUnitRunner.inUse = inUse;
        TestPlanActor.resetResults();
        if (inUse) {
            TestPlanActor.getResults();
        }
    }

    /**
     * Run tests list.
     *
     * @param numConcurrent        the num concurrent
     * @param junitTestPlanClasses the junit test plan classes
     * @return the list
     */
    public List<ResultDto> runTests(final int numConcurrent, final List<Class<?>> junitTestPlanClasses) {
        return runTests(numConcurrent, junitTestPlanClasses, true);
    }

    /**
     * Run tests.
     *
     * @param numConcurrent        the num concurrent
     * @param junitTestPlanClasses the junit test plan classes
     * @param onlyUniqueTests      the only unique tests
     * @return the list
     */
    public List<ResultDto> runTests(final int numConcurrent, final List<Class<?>> junitTestPlanClasses, final boolean onlyUniqueTests) {
        setInUse(true);
        try {
            if (null != junitTestPlanClasses) {
                if (onlyUniqueTests) {
                    return runTestsInternal(numConcurrent, Lists.newArrayList(Sets.newHashSet(junitTestPlanClasses)));
                } else {
                    return runTestsInternal(numConcurrent, junitTestPlanClasses);
                }
            }
        } finally {
            setInUse(false);
        }
        return null;
    }

    /**
     * Run tests.
     *
     * @param numConcurrent        the num concurrent
     * @param junitTestPlanClasses the junit test plan classes
     * @return the list
     */
    private List<ResultDto> runTestsInternal(final int numConcurrent, final List<Class<?>> junitTestPlanClasses) {
        try {
            if (null == junitTestPlanClasses || junitTestPlanClasses.size() == 0) {
                TS.log().warn("No TestPlans Found to Run!");
                return null;
            }
            final int numOfTests = junitTestPlanClasses.size();
            final ActorSystem system = ActorSystem.create("TestahJunitRunner");
            final ActorRef master = system.actorOf(Props.create(TestPlanActor.class, numConcurrent), "master");

            TestPlanActor.resetResults();
            //Setup thread locals to be used
            TS.testSystem().setUpThreadLocals(true);

            master.tell(junitTestPlanClasses, master);

            while (TestPlanActor.getResults().size() < numOfTests) {
                TS.log().trace(TestPlanActor.getResults().size());
                Thread.sleep(1000);
            }

            system.terminate();

            TS.testSystem().cleanUpTestplanThreadLocal();
            return TestPlanActor.getResults();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
