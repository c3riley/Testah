package org.testah.runner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.Assert;
import org.testah.TS;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.http.load.HttpActor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.currentTimeMillis;
import static org.testah.runner.http.load.HttpActor.getResults;

/**
 * The Class HttpAkkaRunner.
 */
public class HttpAkkaRunner {

    private static final String rptInfo = "[%d] %d [%s] - %s - %s";

    List<ActorSystem> actorSystemList = new ArrayList<>();

    private static HttpAkkaRunner httpAkkaRunner = new HttpAkkaRunner();
    private final Long hashId;
    private long receivedCount = 0;

    /**
     * The http wrapper.
     */
    private AbstractHttpWrapper httpWrapper;

    protected HttpAkkaRunner() {
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
        httpAkkaRunner = new HttpAkkaRunner();
    }

    public List<ActorSystem> getActorSystemList()
    {
        return actorSystemList;
    }

    /**
     * Run and report.
     *
     * @param responseQueue         queue to hold the responses
     * @param numConcurrent         the num concurrent
     * @param concurrentLinkedQueue ConcurrentLinkedQueue of PostRequestDto
     * @param isVerbose             if true the requests/responses are written to log
     */
    public void runAndReport(
        LinkedBlockingQueue<ResponseDto> responseQueue,
        final int numConcurrent,
        final ConcurrentLinkedQueue<?> concurrentLinkedQueue,
        boolean isVerbose)
    {
        runTests(responseQueue, numConcurrent, concurrentLinkedQueue, isVerbose);
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
     * Run and report.
     *
     * @param numConcurrent       the num concurrent
     * @param request             the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public LinkedBlockingQueue<ResponseDto> runAndReport(final int numConcurrent, final AbstractRequestDto<?> request,
                                                         final int numOfRequestsToMake)
    {
        final LinkedBlockingQueue<ResponseDto> responseQueue = runTests(numConcurrent, request, numOfRequestsToMake);
        if (TS.http().isVerbose())
        {
            int responseCount = 1;
            for (final ResponseDto response : responseQueue)
            {
                TS.log().info(String.format(rptInfo,
                    responseCount++,
                    response.getStatusCode(),
                    response.getStatusText(),
                    TS.util().toDateString(response.getStart()),
                    TS.util().toDateString(response.getEnd())));
            }
        }
        return responseQueue;
    }

    /**
     * Run tests.
     *
     * @param responseQueue         queue for the responses
     * @param numConcurrent         the num concurrent
     * @param concurrentLinkedQueue ConcurrentLinkedQueue of
     * @param isVerbose             TODO
     */
    public void runTests(
        LinkedBlockingQueue responseQueue,
        final int numConcurrent,
        final ConcurrentLinkedQueue<?> concurrentLinkedQueue,
        boolean isVerbose)
    {
        final Long hashId2 = Thread.currentThread().getId();
        Assert.assertEquals(hashId, hashId2);
        try
        {
            if (null == concurrentLinkedQueue || concurrentLinkedQueue.size() == 0)
            {
                TS.log().warn("No Request Found to Run!");
                return;
            }
            httpWrapper = new HttpWrapperV2();
            httpWrapper.setVerbose(isVerbose);
            httpWrapper.setConnectManagerDefaultPooling().setHttpClient();

            final ActorSystem system = ActorSystem.create("HttpAkkaRunner");
            actorSystemList.add(system);
            final ActorRef master = system.actorOf(Props.create(HttpActor.class, numConcurrent,
                concurrentLinkedQueue.size(), hashId), "master");

            master.tell(concurrentLinkedQueue, master);

            updateResponseQueue(responseQueue);
        } catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run tests.
     *
     * @param numConcurrent       the num concurrent
     * @param request             the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public LinkedBlockingQueue<ResponseDto> runTests(final int numConcurrent, final AbstractRequestDto<?> request,
                                                     final int numOfRequestsToMake)
    {
        try
        {
            if (null == request)
            {
                TS.log().warn("No Request Found to Run!");
                return null;
            }

            httpWrapper = new HttpWrapperV2();
            httpWrapper.setConnectManagerDefaultPooling().setHttpClient();

            final ActorSystem system = getActorSystem();
            final ActorRef master = system.actorOf(Props.create(HttpActor.class, numConcurrent, numOfRequestsToMake,
                hashId), "master");

            HttpActor.resetResults();

            master.tell(request, master);

            return getResults(hashId);
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
     * Wait for the expected number of responses, but no longer that the time specified
     * @param responseQueue queue that holds the responses so far received
     * @param expectedCount total count of expected responses
     * @param millis maximal time to wait in milliseconds
     * @return total number of received responses
     */
    public long waitForResponses(LinkedBlockingQueue<ResponseDto> responseQueue, long expectedCount, long millis)
    {
        long stopTimestamp = currentTimeMillis() + millis;
        while (currentTimeMillis() < stopTimestamp &&
            (HttpActor.getResults(hashId) == null || receivedCount < expectedCount))
        {
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            updateResponseQueue(responseQueue);
        }
        return receivedCount;
    }

    /**
     * Fill the response queue with received responses.
     * @param responseQueue response queue to update
     * @return the responses that were added to the queue
     */
    public List<ResponseDto> updateResponseQueue(LinkedBlockingQueue<ResponseDto> responseQueue) {
        List<ResponseDto> responseDtoList = new ArrayList<>();
        getResults(hashId).drainTo(responseDtoList);
        receivedCount += responseDtoList.size();
        responseQueue.addAll(responseDtoList);
        return responseDtoList;
    }

    /**
     * Return the count of received responses.
     * @return count of received responses
     */
    public long getReceiveCount()
    {
        return receivedCount;
    }

    /**
     * Terminate the actor systems.
     */
    public void terminateActorSystems() {
        for (ActorSystem actorSystem : getActorSystemList()) {
            actorSystem.terminate();
        }
    }
}
