package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.testah.TS;
import org.testah.driver.http.AbstractHttpWrapper;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.runner.HttpAkkaRunner;
import org.testah.runner.performance.dto.SentCount;

public class HttpSender extends UntypedAbstractActor
{
    private final ActorRef httpActor;
    private final ActorRef responseReceiver;

    public HttpSender(final ActorRef httpActor, final ActorRef responseReceiver) {
        this.httpActor = httpActor;
        this.responseReceiver = responseReceiver;
    }

    @Override
    public void onReceive(final Object message) throws Throwable
    {
        AbstractHttpWrapper httpWrapper = HttpAkkaRunner.getInstance().getHttpWrapper();
        if (message instanceof AbstractRequestDto) {
            try {
                httpActor.tell(new SentCount(1), getSelf());
                responseReceiver.tell(httpWrapper.doRequest((AbstractRequestDto<?>) message), getSelf());
            } catch (Throwable throwable) {
                getSender().tell(throwable, getSelf());
            }
        } else {
            throw new Exception("don't know what to do with message " + message.toString());
        }
    }
}
