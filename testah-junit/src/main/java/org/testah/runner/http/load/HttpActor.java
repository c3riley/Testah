package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpActor extends UntypedAbstractActor {
    public static final int UNKNOWN_ERROR_STATUS = 700;
    private static Map<Long, LinkedBlockingQueue<ResponseDto>> results = new HashMap<>();
    private final ActorRef workerRouter;
    private final int nrOfWorkers;
    private final int numOfAttempts;
    private final Long hashId;
    private static volatile Map<Long, Long> receivedCount = new HashMap<>();

    /**
     * Constructor.
     *
     * @param nrOfWorkers   number of Akka workers
     * @param numOfAttempts number of attempts
     * @param hashId        Akka actor hash
     */
    public HttpActor(final int nrOfWorkers, final int numOfAttempts, final Long hashId) {
        this.hashId = hashId;
        results.put(hashId, new LinkedBlockingQueue<>());
        this.nrOfWorkers = nrOfWorkers;
        this.numOfAttempts = numOfAttempts;
        workerRouter = this.getContext()
                .actorOf(Props.create(HttpWorker.class).withRouter(new RoundRobinPool(nrOfWorkers)), "workerRouter");
    }

    /**
     * Get the responses for a particular Akka actor.
     *
     * @param hashId of Akka actor
     * @return list of responses
     */
    public static LinkedBlockingQueue<ResponseDto> getResults(final Long hashId) {
        Map<Long, LinkedBlockingQueue<ResponseDto>> resultsLocalPointer = getResults();
        if (!resultsLocalPointer.containsKey(hashId)) {
            resultsLocalPointer.put(hashId, new LinkedBlockingQueue<ResponseDto>());
        }
        return resultsLocalPointer.get(hashId);
    }

    /**
     * Get responses per Akka actor hash.
     *
     * @return map of hash id to list of responses.
     */
    public static Map<Long, LinkedBlockingQueue<ResponseDto>> getResults() {
        if (null == results) {
            resetResults();
        }
        return results;
    }

    /**
     * Increment the count of received messages, file by thread hash.
     * @param hashId thread id
     */
    public synchronized void incrementReceiveCount(long hashId) {
        Long count = receivedCount.get(hashId);
        if (count == null) {
            receivedCount.put(hashId, 0L);
        } else {
            receivedCount.put(hashId, count + 1);
        }
    }

    /**
     * Get the count of received responses.
     * @param hashId thread id
     * @return count of received responses
     */
    public static long getReceivedCount(long hashId) {
        Long count = receivedCount.get(hashId);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    public static void resetResults() {
        results = new HashMap<>();
    }

    public static void resetReceivedCount() {
        receivedCount = new HashMap<>();
    }

    /**
     * Implementation of UntypedAbstractActor.onReceiver(...).
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void onReceive(final Object message) throws Exception {
        try {
            if (message instanceof ResponseDto) {
                incrementReceiveCount(hashId);
                getResults(hashId).add((ResponseDto) message);
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
                TS.log().info("Issue, should not have made it here, message is " + message);

            }
        } catch (Throwable throwable) {
            TS.log().info("Throwable thrown in HttpActor.onReceive()", throwable);
            results.get(hashId).add(getUnExpectedErrorResponseDto(throwable));
        }
    }

    private ResponseDto getUnExpectedErrorResponseDto(final Throwable throwable) {
        ResponseDto response = new ResponseDto();
        response.setStatusCode(UNKNOWN_ERROR_STATUS);
        response.setStatusText(String.format("Unexpected Error[%s]", throwable.getMessage()));
        response.setResponseBody(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(throwable));
        return response;
    }

    public ActorRef getWorkerRouter() {

        return workerRouter;
    }

    public int getNrOfWorkers() {
        return nrOfWorkers;
    }
}
