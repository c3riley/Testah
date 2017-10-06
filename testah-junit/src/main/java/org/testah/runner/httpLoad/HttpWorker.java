package org.testah.runner.httpLoad;

import akka.actor.UntypedActor;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.runner.HttpAkkaRunner;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpWorker extends UntypedActor {

    public void onReceive(final Object arg0) throws Exception {
        if(arg0 instanceof AbstractRequestDto) {
            try {
                getSender().tell(HttpAkkaRunner.getInstance().getHttpWrapper().doRequest((AbstractRequestDto<?>) arg0), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable);
            }
        }
        else if(arg0 instanceof ConcurrentLinkedQueue && !((List) arg0).isEmpty() && ((List) arg0).get(0) instanceof PostRequestDto) {
            PostRequestDto postRequestDto = (PostRequestDto) ((ConcurrentLinkedQueue) arg0).poll();
            try {
                getSender().tell(HttpAkkaRunner.getInstance().getHttpWrapper().doRequest(postRequestDto), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable);
            }
        }
        else {
            throw new Exception("don't know what to do");
        }
    }

}
