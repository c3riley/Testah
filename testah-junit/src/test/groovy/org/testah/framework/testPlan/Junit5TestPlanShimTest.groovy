package org.testah.framework.testPlan


import org.junit.jupiter.api.extension.ExtensionContext
import org.testah.Junit5TestPlan
import org.testah.TS
import org.testah.framework.dto.TestCaseAnnotationDto
import org.testah.framework.dto.TestPlanAnnotationDto
import spock.lang.Specification

class Junit5TestPlanShimTest extends Specification {

    ExtensionContext setup() {
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()
        ExtensionContext context = Mock(ExtensionContext.class)
        context.getDisplayName() >> 'test'
        context.getTestClass() >> Optional.of(Junit5TestPlan.class)
        context.getElement() >> Optional.of(Junit5TestPlan.getMethod('test', null))
        TS.testSystem().startTestPlan(Junit5TestPlanSupport.getDescription(context),new TestPlanAnnotationDto(),null )
        TS.testSystem().startTestCase(Junit5TestPlanSupport.getDescription(context), new TestCaseAnnotationDto(), new TestPlanAnnotationDto(),null)
        return context
    }


    def "AfterTestExecution"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.afterTestExecution(setup())

    }

    def "BeforeTestExecution"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.beforeTestExecution(setup())
    }

    def "AfterAll"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.afterAll(setup())
    }

    def "AfterEach"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.afterEach(setup())
    }

    def "BeforeAll"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.beforeAll(setup())
    }

    def "BeforeEach"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.beforeEach(setup())
    }

    def "GetDescription"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.getDescription(setup())
    }

    def "TestDisabled"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.testDisabled(setup(), Optional.of("Disabled"))
    }

    def "TestSuccessful"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.testSuccessful(setup())
    }

    def "TestAborted"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.testAborted(setup(), new RuntimeException("Aborted"))
    }

    def "TestFailed"() {
        given:
        Junit5TestPlanSupport junit5TestPlanShim = new Junit5TestPlanSupport()

        expect:
        junit5TestPlanShim.testFailed(setup(), new RuntimeException("Failed"))
    }
}
