package org.testah.framework.testPlan;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.runner.Description;
import org.testah.TS;
import org.testah.framework.annotations.TestCaseJUnit5;
import org.testah.framework.annotations.TestCaseWithParamsJUnit5;
import org.testah.framework.annotations.TestPlanJUnit5;
import org.testah.framework.dto.TestCaseAnnotationDto;
import org.testah.framework.dto.TestPlanAnnotationDto;
import org.testah.util.LogUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * Provide Support for using JUnit5.
 * To use, simply annotate a test class with @TestPlanJUnit5
 * and the test methods with @TestCaseJUnit5 / @TestCaseWithParamsJUnit5
 */
public class Junit5TestPlanSupport implements BeforeTestExecutionCallback, AfterTestExecutionCallback,
        AfterAllCallback, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, TestWatcher {

    public static final int JUNIT_VERSION = 5;

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        TS.testSystem().finished(getDescription(context));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Description description = getDescription(context);
        Annotation testcase = description.getAnnotation(TestCaseJUnit5.class);
        if (testcase == null) {
            testcase = description.getAnnotation(TestCaseWithParamsJUnit5.class);
        }

        TS.testSystem().starting(description,
                TestPlanAnnotationDto.create(JUNIT_VERSION, description.getTestClass(),
                        description.getTestClass().getAnnotation(TestPlanJUnit5.class)),
                TestCaseAnnotationDto.create(testcase), JUNIT_VERSION);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        TS.testSystem().tearDownTestSystem();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        TS.testSystem().setupTestSystem();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println(">>>>>>>>>> Junit5TestPlanSupport.beforeEach context = " + LogUtil.prettyJson(context));
        Description description = getDescription(context);
        System.out.println(">>>>>>>>>> Junit5TestPlanSupport.beforeEach description = " + LogUtil.prettyJson(description));
        Annotation testcase = description.getAnnotation(TestCaseJUnit5.class);
        System.out.println(">>>>>>>>>> Junit5TestPlanSupport.beforeEach testcase 1 = " + LogUtil.prettyJson(testcase));
        if (testcase == null) {
            testcase = description.getAnnotation(TestCaseWithParamsJUnit5.class);
            System.out.println(">>>>>>>>>> Junit5TestPlanSupport.beforeEach testcase 2 = " + LogUtil.prettyJson(testcase));
        }
        System.out.println(">>>>>>>>>> Junit5TestPlanSupport.beforeEach testcase 3 = " + LogUtil.prettyJson(testcase));
        TS.testSystem().filterTest(description,
                TestPlanAnnotationDto.create(JUNIT_VERSION, description.getTestClass(),
                        description.getTestClass().getAnnotation(TestPlanJUnit5.class)),
                TestCaseAnnotationDto.create(testcase));
    }

    /**
     * getDescription from a junit 5 ExtensionContext.
     *
     * @param context ExtensionContext sent in by junit 5.
     * @return junit 4 style Description.
     */
    public static Description getDescription(ExtensionContext context) {
        Object testClass = context.getTestClass().orElse(null);
        AnnotatedElement testMethod = context.getElement().orElse(null);
        if (testClass == null || testMethod == null) {
            throw new RuntimeException("Issue with the context, unable to get the test class/method");
        }
        Description description = Description.createTestDescription(context.getTestClass().get(),
                context.getDisplayName(), testMethod.getAnnotations());
        return description;
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TS.testSystem().addIgnoredTest(getDescription(context).getDisplayName(),
            reason.orElseGet(() -> "Test Ignored with Ignore or Disabled Annotation"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        TS.testSystem().succeeded(getDescription(context));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        TS.testSystem().addIgnoredTest(getDescription(context).getDisplayName(), cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        TS.testSystem().failed(cause, getDescription(context));
    }
}
