package org.testah.runner.testPlan;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.testah.framework.dto.ResultDto;

import akka.actor.UntypedActor;

public class TestPlanWorker extends UntypedActor {

	public void onReceive(final Object arg0) throws Exception {
		final Request request = Request.classes((Class<?>) arg0);
		getSender().tell(new ResultDto(new JUnitCore().run(request)), getSelf());
	}

}
