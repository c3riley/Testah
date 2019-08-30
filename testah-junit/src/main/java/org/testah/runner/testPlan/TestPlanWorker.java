package org.testah.runner.testPlan;

import akka.actor.UntypedAbstractActor;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.testah.framework.dto.ResultDto;

public class TestPlanWorker extends UntypedAbstractActor {

    /**
     * Override the onReceive method in Untyped Actor.
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    public void onReceive(final Object arg0) throws Exception {
        final Request request = Request.classes((Class<?>) arg0);
        getSender().tell(new ResultDto(new JUnitCore().run(request)).setClassName(
                arg0.toString().replace("class ", "")), getSelf());
    }

}
