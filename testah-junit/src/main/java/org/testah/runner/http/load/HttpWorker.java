package org.testah.runner.http.load;

import akka.actor.UntypedAbstractActor;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.HttpAkkaRunner;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpWorker extends UntypedAbstractActor {

    /**
     * Wrapper of UntypedAbstractActor.onReceive(...).
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
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
