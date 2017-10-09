package org.testah.runner.httpLoad;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpActor extends UntypedActor {
    private static HashMap<Long, List<ResponseDto>> results = new HashMap<Long, List<ResponseDto>>();
    public static final int UKNOWN_ERROR_STATUS = 700;

    private final ActorRef workerRouter;
    private final int nrOfWorkers;
    private final int numOfAttempts;
    private final Long hashId;

    public HttpActor(final int nrOfWorkers, final int numOfAttempts, final Long hashId) {
        this.hashId = hashId;
        results.put(hashId, new ArrayList<ResponseDto>());
        this.nrOfWorkers = nrOfWorkers;
        this.numOfAttempts = numOfAttempts;
        workerRouter = this.getContext()
            .actorOf(new Props(HttpWorker.class).withRouter(new RoundRobinRouter(nrOfWorkers)), "workerRouter");
    }

    @SuppressWarnings("unchecked")
    public void onReceive(final Object message) throws Exception {
        try {
            if (message instanceof ResponseDto) {
                results.get(hashId).add((ResponseDto) message);
            } else if (message instanceof List) {
                for (final Class<?> test : (List<Class<?>>) message) {
                    workerRouter.tell(test, getSelf());
                }
            } else if (message instanceof AbstractRequestDto) {
                for (int start = 1; start <= numOfAttempts; start++) {
                    workerRouter.tell(message, getSelf());
                }
            } else if (message instanceof ConcurrentLinkedQueue) {
                for (int start = 1; start <= numOfAttempts; start++) {
                    workerRouter.tell(message, getSelf());
                }
            } else if (message instanceof Throwable) {
                results.get(hashId).add(getUnExpectedErrorResponseDto((Throwable) message));
            } else {
                TS.log().info("Issue should of not made it here, message was " + message);

            }
        } catch (Throwable throwable) {
            results.get(hashId).add(getUnExpectedErrorResponseDto(throwable));
        }
    }

    private ResponseDto getUnExpectedErrorResponseDto(final Throwable throwable) {
        ResponseDto response = new ResponseDto();
        response.setStatusCode(UKNOWN_ERROR_STATUS);
        response.setStatusText(String.format("Unexpected Error[%s]", throwable.getMessage()));
        response.setResponseBody(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(throwable));
        return response;
    }

    public ActorRef getWorkerRouter() {

        return workerRouter;
    }

    public static List<ResponseDto> getResults(final Long hashId) {
        return getResults().get(hashId);
    }

    public static HashMap<Long, List<ResponseDto>> getResults() {
        if (null == results) {
            resetResults();
        }
        return results;
    }

    public static HashMap<Long, List<ResponseDto>> resetResults() {
        results = new HashMap<Long, List<ResponseDto>>();
        return results;
    }

    public int getNrOfWorkers() {
        return nrOfWorkers;
    }

}
