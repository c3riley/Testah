package org.testah.runner;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.TS;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.httpLoad.HttpActor;
import org.testah.runner.httpLoad.HttpAkkaStats;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/**
 * The Class HttpAkkaRunner.
 */
public class HttpAkkaRunner {

    private static HttpAkkaRunner httpAkkaRunner = new HttpAkkaRunner();

    public static HttpAkkaRunner getInstance() {
        return httpAkkaRunner;
    }

    private HttpAkkaRunner() {
    }

    public static HttpAkkaRunner getHttpAkkaRunner() {
        return httpAkkaRunner;
    }

    public static void setHttpAkkaRunner(HttpAkkaRunner httpAkkaRunner) {
        HttpAkkaRunner.httpAkkaRunner = httpAkkaRunner;
    }

    /**
     * The http wrapper.
     */
    private AbstractHttpWrapper httpWrapper;

    /**
     * Run and report.
     *
     * @param numConcurrent       the num concurrent
     * @param request             the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public List<ResponseDto> runAndReport(final int numConcurrent, final AbstractRequestDto<?> request,
                                          final int numOfRequestsToMake)
    {
        final List<ResponseDto> responses = runTests(numConcurrent, request, numOfRequestsToMake);
        if (TS.http().isVerbose()) {
            int iResponse = 1;
            for (final ResponseDto response : responses) {
                TS.log().info("[" + iResponse++ + "] " + response.getStatusCode() + " [" + response.getStatusText() + "] - "
                    + TS.util().toDateString(response.getStart()) + " - " + TS.util().toDateString(response.getEnd()));
            }
        }
        TS.util().toJsonPrint(new HttpAkkaStats(responses));
        return responses;
    }

    /**
     * Run and report.
     *
     * @param numConcurrent         the num concurrent
     * @param concurrentLinkedQueue ConcurrentLinkedQueue of PostRequestDto
     * @param isVerbose             TODO
     * @return the list
     */
    public List<ResponseDto> runAndReport(final int numConcurrent, final ConcurrentLinkedQueue<?> concurrentLinkedQueue, boolean isVerbose) {
        final List<ResponseDto> responses = runTests(numConcurrent, concurrentLinkedQueue, false);
        if (TS.http().isVerbose()) {
            int iResponse = 1;
            for (final ResponseDto response : responses) {
                TS.log().info("[" + iResponse++ + "] " + response.getStatusCode() + " [" + response.getStatusText() + "] - "
                    + TS.util().toDateString(response.getStart()) + " - " + TS.util().toDateString(response.getEnd()));
            }
        }
        return responses;
    }

    /**
     * Run tests.
     *
     * @param numConcurrent       the num concurrent
     * @param request             the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public List<ResponseDto> runTests(final int numConcurrent, final AbstractRequestDto<?> request,
                                      final int numOfRequestsToMake)
    {
        final Long hashId = Thread.currentThread().getId();
        try {
            if (null == request) {
                TS.log().warn("No Request Found to Run!");
                return null;
            }

            httpWrapper = new HttpWrapperV1();
            httpWrapper.setConnectManagerDefaultPooling().setHttpClient();

            final ActorSystem system = ActorSystem.create("HttpAkkaRunner");
            final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
                private static final long serialVersionUID = 1L;

                public UntypedActor create() {
                    return new HttpActor(numConcurrent, numOfRequestsToMake, hashId);
                }
            }), "master");

            HttpActor.resetResults();

            master.tell(request, master);

            while (null == HttpActor.getResults(hashId) || HttpActor.getResults(hashId).size() < numOfRequestsToMake) {
                TS.log().info(HttpActor.getResults().size());
                Thread.sleep(500);
            }
            system.shutdown();

            return HttpActor.getResults(hashId);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run tests.
     *
     * @param numConcurrent         the num concurrent
     * @param concurrentLinkedQueue ConcurrentLinkedQueue of
     * @param isVerbose             TODO
     * @return the list
     */
    public List<ResponseDto> runTests(final int numConcurrent, final ConcurrentLinkedQueue<?> concurrentLinkedQueue, boolean isVerbose) {
        final Long hashId = Thread.currentThread().getId();
        int numOfRequestsToMake = 0;
        try {
            if (null == concurrentLinkedQueue || concurrentLinkedQueue.size() == 0) {
                TS.log().warn("No Request Found to Run!");
                return null;
            }
            numOfRequestsToMake = concurrentLinkedQueue.size();
            httpWrapper = new HttpWrapperV1();
            httpWrapper.setVerbose(isVerbose);
            httpWrapper.setConnectManagerDefaultPooling().setHttpClient();

            final ActorSystem system = ActorSystem.create("HttpAkkaRunner");
            final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
                private static final long serialVersionUID = 1L;

                public UntypedActor create() {
                    return new HttpActor(numConcurrent, concurrentLinkedQueue.size(), hashId);
                }
            }), "master");

            HttpActor.resetResults();

            master.tell(concurrentLinkedQueue, master);

            while (HttpActor.getResults(hashId) == null || HttpActor.getResults(hashId).size() < numOfRequestsToMake) {
                Thread.sleep(500);
            }
            system.shutdown();

            return HttpActor.getResults(hashId);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the http wrapper.
     *
     * @return the http wrapper
     */
    public AbstractHttpWrapper getHttpWrapper() {
        if (null == httpWrapper) {
            final AbstractHttpWrapper httpWrapperTmp = new HttpWrapperV1();
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
    public void setHttpWrapper(final AbstractHttpWrapper httpWrapper) {
        HttpAkkaRunner.getInstance().httpWrapper = httpWrapper;
    }

}
