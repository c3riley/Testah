package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.performance.RequestQueueWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class HttpWorker extends UntypedAbstractActor {

    private final ActorRef senderRouter;
    private final int numOfSenders;
    private Map<Long, LinkedBlockingQueue<ResponseDto>> results = new HashMap<>();

    /**
     * Constructor.
     *
     * @param numOfSenders      number of Akka workers
     * @param hashId            Akka actor hash
     * @param httpActor         master Akka actor
     * @param responseReceiver  response receiver Akka actor
     */
    public HttpWorker(final int numOfSenders, final Long hashId, final ActorRef httpActor, ActorRef responseReceiver) {
        results.put(hashId, new LinkedBlockingQueue<>());
        this.numOfSenders = numOfSenders;
        senderRouter = this.getContext()
            .actorOf(Props.create(HttpSender.class, httpActor, responseReceiver)
                .withRouter(new RoundRobinPool(numOfSenders)), "senderRouter");
    }

    /**
     * Wrapper of UntypedAbstractActor.onReceive(...).
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    public void onReceive(final Object message) throws Exception {
        if (message instanceof RequestQueueWrapper) {
            RequestQueueWrapper requestQueueWrapper = (RequestQueueWrapper) message;
            AbstractRequestDto<?> requestDto;
            while ((requestDto = requestQueueWrapper.getQueue().poll()) != null &&
                System.currentTimeMillis() < requestQueueWrapper.getStopTime()) {
                try {
                    senderRouter.tell(requestDto, getSender());
                } catch (Throwable throwable) {
                    getSender().tell(throwable, getSelf());
                }
            }
            Thread.sleep(requestQueueWrapper.getMillisBetweenChunks());
        } else {
            throw new Exception("don't know what to do");
        }
    }

    public int getNumOfSenders() {
        return numOfSenders;
    }
}
