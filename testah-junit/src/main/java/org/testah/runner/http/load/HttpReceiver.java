package org.testah.runner.http.load;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.performance.dto.SentCount;

public class HttpReceiver extends UntypedAbstractActor
{
    private final ActorRef httpActor;

    public HttpReceiver(final ActorRef httpActor) {
        this.httpActor = httpActor;
    }

    @Override
    public void onReceive(final Object message) throws Throwable
    {
        if (message instanceof ResponseDto) {
            httpActor.tell(((ResponseDto) message).setResponseBody(""), getSelf());
        } else if (message instanceof SentCount) {
            httpActor.tell(message, getSelf());
        } else {
            throw new Exception(String.format("Unhandled message type '%s', message :'%s'",
                message.getClass().getName(), message));
        }
    }
}
