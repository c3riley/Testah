package org.testah.runner.testPlan;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestPlanActor extends UntypedAbstractActor {
    private static List<ResultDto> results = null;
    private final ActorRef workerRouter;
    private final int nrOfWorkers;

    /**
     * Constructor.
     *
     * @param nrOfWorkers number of workers
     */
    public TestPlanActor(final int nrOfWorkers) {


        this.nrOfWorkers = nrOfWorkers;
        workerRouter = this.getContext()
                .actorOf(Props.create(TestPlanWorker.class).withRouter(new RoundRobinPool(nrOfWorkers)),
                        "workerRouter");
    }

    /**
     * Check it results is null.
     *
     * @return return true if not null
     */
    public static boolean isResultsInUse() {
        return (null != results);
    }

    /**
     * Reset the results to null.
     */
    public static void resetResults() {
        results = null;
    }

    /**
     * Get the results.
     *
     * @return list of results
     */
    public static List<ResultDto> getResults() {
        if (null == results) {
            results = new ArrayList<ResultDto>();
        }
        return results;
    }

    /**
     * Override onReceive in UntypedAbstractActor.
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void onReceive(final Object message) throws Exception {
        if (message instanceof ResultDto) {
            TS.log().info(Thread.currentThread().getId());
            getResults().add((ResultDto) message);
        } else if (message instanceof Set) {
            for (final Class<?> test : (Set<Class<?>>) message) {
                workerRouter.tell(test, getSelf());
            }
        } else if (message instanceof List) {
            for (final Class<?> test : (List<Class<?>>) message) {
                workerRouter.tell(test, getSelf());
            }
        } else {
            for (int start = 0; start < nrOfWorkers; start++) {
                workerRouter.tell(message, getSelf());
            }
        }
    }

    /**
     * Get the worker router.
     *
     * @return the worker router
     */
    public ActorRef getWorkerRouter() {
        return workerRouter;
    }

}
