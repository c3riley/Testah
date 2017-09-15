package org.testah.runner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;
import org.testah.framework.testPlan.AbstractTestPlan;
import org.testah.runner.testPlan.TestPlanActor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class TestahJUnitRunner.
 */
public class TestahJUnitRunner {

    private static boolean inUse = false;

    /**
     * Run tests.
     *
     * @param numConcurrent        the num concurrent
     * @param jUnitTestPlanClasses the j unit test plan classes
     * @return the list
     */
    public List<ResultDto> runTests(final int numConcurrent, final List<Class<?>> jUnitTestPlanClasses) {
        setInUse(true);
        if (null != jUnitTestPlanClasses) {
            return runTests(numConcurrent, new HashSet<Class<?>>(jUnitTestPlanClasses));
        }
        setInUse(false);
        return null;
    }

    /**
     * Run tests.
     *
     * @param numConcurrent        the num concurrent
     * @param jUnitTestPlanClasses the j unit test plan classes
     * @return the list
     */
    private List<ResultDto> runTests(final int numConcurrent, final Set<Class<?>> jUnitTestPlanClasses) {
        try {
            if (null == jUnitTestPlanClasses || jUnitTestPlanClasses.size() == 0) {
                TS.log().warn("No TestPlans Found to Run!");
                return null;
            }
            final int numOfTests = jUnitTestPlanClasses.size();
            final ActorSystem system = ActorSystem.create("TestahJunitRunner");
            final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
                private static final long serialVersionUID = 1L;

                public UntypedActor create() {
                    return new TestPlanActor(numConcurrent);
                }
            }), "master");

            TestPlanActor.resetResults();
            //Setup thread locals to be used
            AbstractTestPlan.setUpThreadLocals();

            master.tell(jUnitTestPlanClasses, master);

            while (TestPlanActor.getResults().size() < numOfTests) {
                TS.log().trace(TestPlanActor.getResults().size());
                Thread.sleep(1000);
            }

            system.shutdown();

            AbstractTestPlan.cleanUpTestplanThreadLocal();
            return TestPlanActor.getResults();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInUse() {
        return inUse;
    }

    private static void setInUse(final boolean inUse) {
        TestahJUnitRunner.inUse = inUse;
        TestPlanActor.resetResults();
    }
}
