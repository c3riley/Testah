package org.testah.runner.testPlan;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import org.testah.TS;
import org.testah.framework.dto.ResultDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestPlanActor extends UntypedActor {
    private static List<ResultDto> results;
    private final ActorRef workerRouter;
    private final int nrOfWorkers;

    public TestPlanActor(final int nrOfWorkers) {
        results = new ArrayList<ResultDto>();
        this.nrOfWorkers = nrOfWorkers;
        workerRouter = this.getContext()
                .actorOf(new Props(TestPlanWorker.class).withRouter(new RoundRobinRouter(nrOfWorkers)), "workerRouter");
    }

    @SuppressWarnings("unchecked")
    public void onReceive(final Object message) throws Exception {
        if (message instanceof ResultDto) {
            TS.log().info(Thread.currentThread().getId());
            results.add((ResultDto) message);
        } else if (message instanceof Set) {
            for (final Class<?> test : (Set<Class<?>>) message) {
                workerRouter.tell(test, getSelf());
            }
        } else {
            for (int start = 0; start < nrOfWorkers; start++) {
                workerRouter.tell(message, getSelf());
            }
        }
    }

    public ActorRef getWorkerRouter() {

        return workerRouter;
    }

    public static List<ResultDto> getResults() {
        if (null == results) {
            results = new ArrayList<ResultDto>();
        }
        return results;
    }

    public static boolean isResultsInUse() {
        return (null != results);
    }

    public static void resetResults() {
        results = new ArrayList<ResultDto>();
    }

}
