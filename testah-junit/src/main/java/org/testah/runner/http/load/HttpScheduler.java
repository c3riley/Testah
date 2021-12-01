package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.performance.RequestQueueWrapper;
import org.testah.runner.performance.StepRequestQueueWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpScheduler extends UntypedAbstractActor {
    private static Map<Long, LinkedBlockingQueue<ResponseDto>> results = new HashMap<>();
    private final ActorRef workerRouter;
    private final ActorRef responseReceiver;
    private final int numOfWorkers;
    private final Long hashId;

    /**
     * Constructor.
     *
     * @param workerRouter     the Akka worker actor
     * @param responseReceiver the Akka receiver actor
     * @param numOfWorkers     number of Akka workers
     * @param hashId           Akka actor hash
     */
    public HttpScheduler(final ActorRef workerRouter, final ActorRef responseReceiver, final int numOfWorkers, final Long hashId) {
        this.hashId = hashId;
        results.put(hashId, new LinkedBlockingQueue<>());
        this.numOfWorkers = numOfWorkers;
        this.responseReceiver = responseReceiver;
        this.workerRouter = workerRouter;
    }

    /**
     * Implementation of UntypedAbstractActor.onReceiver(...).
     *
     * @see UntypedAbstractActor#onReceive(Object)
     */
    public void onReceive(final Object message) {
        try {
            StepRequestQueueWrapper stepRequestQueueWrapper = (StepRequestQueueWrapper) message;
            while (System.currentTimeMillis() < stepRequestQueueWrapper.getStopTime()) {
                for (int workerCount = 0; workerCount < numOfWorkers; workerCount++) {
                    RequestQueueWrapper requestQueueWrapper = stepRequestQueueWrapper.getNext();
                    requestQueueWrapper.setStopTime(stepRequestQueueWrapper.getStopTime());
                    workerRouter.tell(requestQueueWrapper, responseReceiver);
                }
                Thread.sleep(stepRequestQueueWrapper.getMillisBetweenChunks());
            }
        } catch (Throwable throwable) {
            TS.log().info("Throwable thrown in HttpActor.onReceive()", throwable);
        }
    }

    public int getNumOfWorkers() {
        return numOfWorkers;
    }
}
