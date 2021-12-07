package org.testah.framework.testPlan;

import org.junit.jupiter.api.Test;
import org.junit.runner.Description;
import org.testah.Junit5TestPlan;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestSystemTestJunit5 {

    @Test
    void runDoOnMethod() {
        TestSystem testSystem = new TestSystem();
        Description desc = Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations());
        List<Boolean> didItRun = new ArrayList<>();
        DoOnMethod closure = (description) -> {
            didItRun.add(true);
        };
        ThreadLocal<DoOnMethod> threadLocal = new ThreadLocal<>();
        threadLocal.set(closure);
        testSystem.runDoOnMethod(desc, threadLocal);

        assert didItRun.size() > 0;

    }

    @Test
    void runDoOnMethodNegative() {
        TestSystem testSystem = new TestSystem();
        Description desc = Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations());
        List<Boolean> didItRun = new ArrayList<>();
        ThreadLocal<DoOnMethod> threadLocal = new ThreadLocal<>();
        testSystem.runDoOnMethod(desc, threadLocal);

        assert didItRun.size() == 0;
    }


}