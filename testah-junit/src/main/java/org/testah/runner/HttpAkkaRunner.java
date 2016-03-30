package org.testah.runner;

import java.util.List;

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

    /** The http wrapper. */
    public static AbstractHttpWrapper httpWrapper;

    /**
     * Run and report.
     *
     * @param numConcurrent the num concurrent
     * @param request the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public List<ResponseDto> runAndReport(final int numConcurrent, final AbstractRequestDto request,
            final int numOfRequestsToMake) {
        final List<ResponseDto> responses = runTests(numConcurrent, request, numOfRequestsToMake);
        int i = 1;
        for (final ResponseDto response : responses) {
            TS.log().info(i++ + "] " + response.getStatusCode() + " [" + response.getStatusText() + "] - "
                    + TS.util().toDateString(response.getStart()) + " - " + TS.util().toDateString(response.getEnd()));
        }

        TS.util().toJsonPrint(new HttpAkkaStats(responses));
        return responses;
    }

    /**
     * Run tests.
     *
     * @param numConcurrent the num concurrent
     * @param request the request
     * @param numOfRequestsToMake the num of requests to make
     * @return the list
     */
    public List<ResponseDto> runTests(final int numConcurrent, final AbstractRequestDto request,
            final int numOfRequestsToMake) {
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
     * Gets the http wrapper.
     *
     * @return the http wrapper
     */
    public static AbstractHttpWrapper getHttpWrapper() {
        if (null == httpWrapper) {
            httpWrapper = new HttpWrapperV1();
            httpWrapper.setVerbose(false);
            httpWrapper.setConnectManagerDefaultPooling().setHttpClient();
        }
        return httpWrapper;
    }

    /**
     * Sets the http wrapper.
     *
     * @param httpWrapper the new http wrapper
     */
    public static void setHttpWrapper(final AbstractHttpWrapper httpWrapper) {
        HttpAkkaRunner.httpWrapper = httpWrapper;
    }

}
