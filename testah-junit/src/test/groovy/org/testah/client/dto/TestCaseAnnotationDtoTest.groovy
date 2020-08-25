package org.testah.client.dto

import org.testah.Junit5TestPlan
import org.testah.client.enums.TestType
import org.testah.framework.annotations.TestCaseJUnit5
import org.testah.framework.dto.TestCaseAnnotationDto
import spock.lang.Specification

import java.lang.annotation.Annotation
import java.lang.reflect.Method

class TestCaseAnnotationDtoTest extends Specification {

    def "Create Junit5TestPlan test method"() {
        given:
        Method testMethod = Junit5TestPlan.class.getMethod("test", null)

        when:
        TestCaseAnnotationDto dto = TestCaseAnnotationDto.create(testMethod)

        then:
        dto.name() == 'test for junit5 example'
    }

    def "TestCreate TestCaseJunit5 as Annotation"() {
        given:
        Method testMethod = Junit5TestPlan.class.getMethod("test", null)
        Annotation annotation = testMethod.getAnnotation(TestCaseJUnit5.class)

        when:
        TestCaseAnnotationDto dto = TestCaseAnnotationDto.create(annotation)

        then:
        dto.name() == 'test for junit5 example'
    }

    def "TestCreate TestCaseJunit5"() {
        given:
        Method testMethod = Junit5TestPlan.class.getMethod("test", null)
        TestCaseJUnit5 testCaseJunit5 = testMethod.getAnnotation(TestCaseJUnit5.class)

        when:
        TestCaseAnnotationDto dto = TestCaseAnnotationDto.create(testCaseJunit5)

        then:
        dto.name() == 'test for junit5 example'
    }

    def "test Fields"() {
        given:
        TestCaseAnnotationDto dto = new TestCaseAnnotationDto();

        expect:
        dto.id() == -1
        dto.name() == ""
        dto.description() == ""
        dto.devices() == []
        dto.platforms() == []
        dto.runTypes() == []
        dto.components() == []
        dto.relatedIds() == []
        dto.relatedLinks() == []
        dto.tags() == []
        dto.testType() == TestType.DEFAULT
    }

}
