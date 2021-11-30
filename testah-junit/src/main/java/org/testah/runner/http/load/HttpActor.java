package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.performance.StepRequestQueueWrapper;
import org.testah.runner.performance.dto.SentCount;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpActor extends UntypedAbstractActor {
    public static final int UNKNOWN_ERROR_STATUS = 700;
    private static volatile Map<Long, LinkedBlockingQueue<ResponseDto>> results = new HashMap<>();
    private static volatile Map<Long, Long> receivedCount = new HashMap<>();
    private static volatile Map<Long, Long> sentCount = new HashMap<>();
    private final ActorRef workerRouter;
    private final ActorRef responseReceiver;
    private final ActorRef scheduler;
    private final int numOfWorkers;

    private final Long hashId;

    /**
     * Constructor.
     *
     * @param numOfWorkers   number of Akka workers
     * @param numOfSenders   number of Akka senders
     * @param hashId        Akka actor hash
     */
    public HttpActor(final int numOfWorkers, final int numOfSenders, final Long hashId) {
        this.hashId = hashId;
        results.put(hashId, new LinkedBlockingQueue<>());
        this.numOfWorkers = numOfWorkers;
        responseReceiver = this.getContext()
            .actorOf(Props.create(HttpReceiver.class, getSelf()).withRouter(new RoundRobinPool(numOfWorkers)), "responseReceiver");
        workerRouter = this.getContext()
            .actorOf(Props.create(HttpWorker.class, numOfSenders, hashId, getSelf(), responseReceiver)
                .withRouter(new RoundRobinPool(numOfWorkers)), "workerRouter");
        scheduler = this.getContext()
            .actorOf(Props.create(HttpScheduler.class, workerRouter, responseReceiver, numOfWorkers, hashId), "scheduler");
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
            resultsLocalPointer.put(hashId, new LinkedBlockingQueue<>());
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
     * @param increment number of additional requests sent
     */
    public synchronized void incrementSendCount(long hashId, int increment) {
        Long count = sentCount.get(hashId);
        if (count == null) {
            sentCount.put(hashId, (long) increment);
        } else {
            sentCount.put(hashId, count + increment);
        }
    }

    /**
     * Increment the count of received messages, file by thread hash.
     * @param hashId thread id
     */
    public synchronized void incrementReceiveCount(long hashId) {
        Long count = receivedCount.get(hashId);
        if (count == null) {
            receivedCount.put(hashId, 1L);
        } else {
            receivedCount.put(hashId, count + 1);
        }
    }

    /**
     * Get the count of received responses.
     * @return count of received responses
     */
    public static long getReceivedCount() {
        return receivedCount.values().stream().mapToLong(Long::longValue).sum();
    }

    public static long getSentCount() {
        return sentCount.values().stream().mapToLong(Long::longValue).sum();
    }

    public static void resetResults() {
        results = new HashMap<>();
    }

    public static void resetReceivedCount() {
        receivedCount = new HashMap<>();
    }

    public static void resetSentCount() {
        sentCount = new HashMap<>();
    }

    /**
     * Implementation of UntypedAbstractActor.onReceiver(...).
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    public void onReceive(final Object message) {
        try {
            if (message instanceof ResponseDto) {
                incrementReceiveCount(hashId);
                getResults(hashId).add((ResponseDto) message);
            } else if (message instanceof SentCount) {
                incrementSendCount(hashId, ((SentCount) message).getCount());
            } else if (message instanceof StepRequestQueueWrapper) {
                scheduler.tell(message, responseReceiver);
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

    public int getNumOfWorkers() {
        return numOfWorkers;
    }
}
