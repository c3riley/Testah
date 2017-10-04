package org.testah.runner.httpLoad;

import akka.actor.UntypedActor;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.HttpAkkaRunner;

public class HttpWorker extends UntypedActor {

    public void onReceive(final Object arg0) throws Exception {
        try {
            getSender().tell(HttpAkkaRunner.getInstance().getHttpWrapper().doRequest((AbstractRequestDto<?>) arg0), getSelf());
        } catch (Throwable throwable) {
            getSender().tell(throwable);
        }
    }

}
