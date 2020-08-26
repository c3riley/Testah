package org.testah.runner.testPlan;

import akka.actor.UntypedAbstractActor;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.testah.framework.annotations.TestPlanJUnit5;
import org.testah.framework.dto.ResultDto;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestPlanWorker extends UntypedAbstractActor {

    /**
     * Override the onReceive method in Untyped Actor.
     *
     * @see akka.actor.UntypedAbstractActor#onReceive(java.lang.Object)
     */
    public void onReceive(final Object arg0) throws Exception {
        final Class<?> testClass = (Class<?>) arg0;
        getSender().tell(launch(testClass).setClassName(
                testClass.toString().replace("class ", "")), getSelf());
    }

    public static ResultDto launch(Class junitClass) {
        if (junitClass.getAnnotation(TestPlanJUnit5.class) != null) {
            return new ResultDto(launchJUnit5(junitClass));
        } else {
            return new ResultDto(launchJUnit4(junitClass));
        }
    }

    public static Result launchJUnit4(Class junitClass) {
        final Request request = Request.classes(junitClass);
        return new JUnitCore().run(request);
    }

    public static TestExecutionSummary launchJUnit5(Class junitClass) {
        final LauncherDiscoveryRequest request =
                LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectClass(junitClass))
                        .build();
        final Launcher launcher = LauncherFactory.create();
        final SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return listener.getSummary();
    }
}
