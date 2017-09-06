package org.testah.runner.httpLoad;

import akka.actor.UntypedActor;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.HttpAkkaRunner;

public class HttpWorker extends UntypedActor {

    public void onReceive(final Object arg0) throws Exception {
        TS.log().info("1 HttpWorker " + Thread.currentThread().getId());
        getSender().tell(HttpAkkaRunner.getHttpWrapper().doRequest((AbstractRequestDto<?>) arg0), getSelf());
        TS.log().info("2 HttpWorker " + Thread.currentThread().getId());
    }

}
