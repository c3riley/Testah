package org.testah.framework.testPlan

import org.junit.runner.Description
import org.testah.Junit5TestPlan
import org.testah.client.dto.TestCaseDto
import org.testah.util.unittest.dtotest.SystemOutCapture
import spock.lang.Specification
import spock.lang.Unroll

class TestSystemTest extends Specification {

    private static final long serialVersionUID = 5734772475048784881L


    def "tear Down Test System Exception encountered"() {
        given:
        TestSystem ts = Spy(new TestSystem())
        ts.getTestPlan() >> { throw new Exception("Issue encountered") }
        SystemOutCapture system = new SystemOutCapture()

        when:
        system.start()
        ts.tearDownTestSystem()

        then:
        system.getSystemOut().contains('tearDownTestSystem had an issue')
    }

    def "resetTestCase test"() {
        given:
        TestSystem ts = Spy(new TestSystem())
        ts.starting(Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations()))

        when:
        ts.resetTestCase('because')

        then:
        ts.getTestStep().name == 'Resetting TestCase And Going To Retry'
    }

    def "tearDownTestSystem test"() {
        given:
        TestSystem ts = Spy(new TestSystem())

        when:
        ts.starting(Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations()))

        then:
        ts.getTestPlanThreadLocal().get() != null

        when:
        ts.tearDownTestSystem()

        then:
        ts.getTestPlanThreadLocal().get() == null
    }

    @Unroll
    def "dataValue test"(String data, String expected) {
        given:
        TestSystem ts = Spy(new TestSystem())
        ts.starting(Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations()))

        when:
        ts.dataValue(data)

        then:
        ts.getTestCase().getDataValue() == expected

        where:
        data                                       | expected
        null                                       | ''
        ''                                         | ''
        'test'                                     | 'test'
        'this is very long zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' | 'this is very long zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz' +
                'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz'
    }

    @Unroll
    def "test finished"(final Boolean status, Object _) {
        given:
        TestSystem testSystem = Spy(new TestSystem())
        TestCaseDto testCaseDto = Spy(new TestCaseDto())
        testCaseDto.getStatus() >> { return status }
        testSystem.getTestCase() >> testCaseDto
        Object var = null
        Object expected = status == null ? null :
                status == true ? testSystem.doOnPassClosure : testSystem.doOnFailClosure
        if (status == null) {
            testSystem.runDoOnMethod(*_) >> { throw new AssertionError("Method should not get called") }
        } else {
            testSystem.runDoOnMethod(*_) >> { args -> var = args[1] }
        }

        when:
        testSystem.finished(Description.createTestDescription(Junit5TestPlan.class,
                "test", Junit5TestPlan.class.getAnnotations()))

        then:
        expected == var

        where:
        status | _
        null   | null
        true   | null
        false  | null
    }

}
