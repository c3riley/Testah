package org.testah.runner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.testah.TS;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.http.load.HttpActor;
import org.testah.runner.performance.StepRequestQueueWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.testah.runner.http.load.HttpActor.getResults;

/**
 * The Class HttpAkkaRunner.
 */
public class HttpAkkaRunner {

    private static final String rptInfo = "HttpAkkaRunner.rptInfo [%d] %d [%s] - %s - %s";
    private static HttpAkkaRunner httpAkkaRunner = new HttpAkkaRunner();
    private final Long hashId;
    List<ActorSystem> actorSystemList = new ArrayList<>();

    /**
     * The http wrapper.
     */
    private AbstractHttpWrapper httpWrapper;

    protected HttpAkkaRunner() {
        httpWrapper = new HttpWrapperV2();
        httpWrapper.setConnectManagerDefaultPooling().setHttpClient();

        hashId = Thread.currentThread().getId();
    }

    public static HttpAkkaRunner getInstance()
    {
        return httpAkkaRunner;
    }

    /**
     * Reset the HttpActor results map and the received count map and
     * instantiate a new HttpAkkaRunner.
     */
    public static void reset()
    {
        HttpActor.resetResults();
        HttpActor.resetReceivedCount();
        HttpActor.resetSentCount();
        httpAkkaRunner = new HttpAkkaRunner();
    }

    public List<ActorSystem> getActorSystemList()
    {
        return actorSystemList;
    }

    /**
     * Execute tests and report results.
     * @param responseQueue queue holding responses
     * @param numOfWorkers number of 'worker' Akka actors
     * @param numOfSenders number of 'sender' Akka actors
     * @param stepRequestQueueWrapper wrapper of request queues
     * @param isVerbose provide additional information if true
     */
    public void runAndReport(
        LinkedBlockingQueue<ResponseDto> responseQueue,
        final int numOfWorkers,
        final int numOfSenders,
        final StepRequestQueueWrapper stepRequestQueueWrapper,
        boolean isVerbose)
    {
        runTests(responseQueue, numOfWorkers, numOfSenders, stepRequestQueueWrapper, isVerbose);
        if (isVerbose) {
            int responseCount = 1;
            for (final ResponseDto response : responseQueue)
                TS.log().info(String.format(rptInfo,
                    responseCount++,
                    response.getStatusCode(),
                    response.getStatusText(),
                    TS.util().toDateString(response.getStart()),
                    TS.util().toDateString(response.getEnd())));
        }
    }

    /**
     * Run tests.
     *
     * @param responseQueue               queue for the responses
     * @param numOfWorkers                the num concurrent chunks of requests
     * @param numOfSenders                the num concurrent threads to handle a chunk;
     *                                    the requests are synchronous
     * @param stepRequestQueueWrapperList wrapper of ConcurrentLinkedQueue for this step
     * @param isVerbose                   verbose sending of requests
     */
    public void runTests(
        LinkedBlockingQueue<ResponseDto> responseQueue,
        final int numOfWorkers,
        final int numOfSenders,
        final StepRequestQueueWrapper stepRequestQueueWrapperList,
        boolean isVerbose)
    {
        try
        {
            httpWrapper.setVerbose(isVerbose);

            final ActorSystem system = ActorSystem.create("HttpAkkaRunner");
            actorSystemList.add(system);
            final ActorRef master = system.actorOf(Props.create(HttpActor.class, numOfWorkers, numOfSenders, hashId), "master");

            master.tell(stepRequestQueueWrapperList, master);

            updateResponseQueue(responseQueue);
        } catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public ActorSystem getActorSystem()
    {
        return ActorSystem.create("HttpAkkaRunner");
    }

    /**
     * Gets the http wrapper.
     *
     * @return the http wrapper
     */
    public AbstractHttpWrapper getHttpWrapper()
    {
        if (null == httpWrapper)
        {
            final AbstractHttpWrapper httpWrapperTmp = new HttpWrapperV2();
            httpWrapperTmp.setVerbose(false);
            httpWrapperTmp.setConnectManagerDefaultPooling().setHttpClient();
            httpWrapper = httpWrapperTmp;
        }
        return httpWrapper;
    }

    /**
     * Sets the http wrapper.
     *
     * @param httpWrapper the new http wrapper
     */
    public void setHttpWrapper(final AbstractHttpWrapper httpWrapper)
    {
        HttpAkkaRunner.getInstance().httpWrapper = httpWrapper;
    }

    /**
     * Fill the response queue with received responses.
     *
     * @param responseQueue response queue to update
     * @return the responses that were added to the queue
     */
    public List<ResponseDto> updateResponseQueue(LinkedBlockingQueue<ResponseDto> responseQueue)
    {
        List<ResponseDto> responseDtoList = new ArrayList<>();
        getResults(hashId).drainTo(responseDtoList);
        responseQueue.addAll(responseDtoList);
        return responseDtoList;
    }

    /**
     * Terminate the actor systems.
     */
    public void terminateActorSystems()
    {
        for (ActorSystem actorSystem : getActorSystemList())
        {
            actorSystem.terminate();
        }
    }
}
