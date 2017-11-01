package org.testah.runner.http.load;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.HttpAkkaRunner;

import akka.actor.UntypedActor;

public class HttpWorker extends UntypedActor {

    public void onReceive(final Object arg0) throws Exception {
        AbstractHttpWrapper httpWrapper = HttpAkkaRunner.getInstance().getHttpWrapper();
        if (arg0 instanceof AbstractRequestDto) {
            try {
                getSender().tell(httpWrapper.doRequest((AbstractRequestDto<?>) arg0), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable, getSelf());
            }
        } else if (arg0 instanceof ConcurrentLinkedQueue) {
            AbstractRequestDto<?> requestDto = (AbstractRequestDto<?>) ((ConcurrentLinkedQueue<?>) arg0).poll();
            try {
                getSender().tell(httpWrapper.doRequest(requestDto, httpWrapper.isVerbose()), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable, getSelf());
            }
        } else {
            throw new Exception("don't know what to do");
        }
    }
}
