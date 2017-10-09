package org.testah.runner.httpLoad;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.runner.HttpAkkaRunner;

import akka.actor.UntypedActor;

public class HttpWorker extends UntypedActor {

    public void onReceive(final Object arg0) throws Exception {
        AbstractHttpWrapper httpWrapper = HttpAkkaRunner.getInstance().getHttpWrapper();
        if (arg0 instanceof AbstractRequestDto) {
            try {
                getSender().tell(httpWrapper.doRequest((AbstractRequestDto<?>) arg0), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable);
            }
        } else if (arg0 instanceof ConcurrentLinkedQueue) {
            PostRequestDto postRequestDto = (PostRequestDto) ((ConcurrentLinkedQueue<?>) arg0).poll();
            try {
                getSender().tell(httpWrapper.doRequest(postRequestDto, httpWrapper.isVerbose()), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable);
            }
        } else {
            throw new Exception("don't know what to do");
        }
    }
}
