package org.testah.client.dto

import org.testah.Junit5TestPlan
import org.testah.TS
import org.testah.client.enums.TestType
import org.testah.framework.annotations.TestPlanJUnit5
import org.testah.framework.dto.TestPlanAnnotationDto
import org.unitils.reflectionassert.ReflectionComparatorMode
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.annotation.Annotation

class TestPlanAnnotationDtoTest extends Specification {
    def "Create Junit5TestPlan test method"() {
        given:
        Class testPlanClass = Junit5TestPlan.class

        when:
        TestPlanAnnotationDto dto = TestPlanAnnotationDto.create(testPlanClass)

        then:
        dto.name() == 'test plan for junit5 example'
    }

    def "TestCreate TestCaseJunit5 as Annotation"() {
        given:
        Annotation annotation = Junit5TestPlan.class.getAnnotation(TestPlanJUnit5.class)

        when:
        TestPlanAnnotationDto dto = TestPlanAnnotationDto.create(null, annotation)

        then:
        dto.name() == 'test plan for junit5 example'
    }

    def "TestCreate TestCaseJunit5"() {
        given:
        TestPlanJUnit5 testPlanJunit5 = Junit5TestPlan.class.getAnnotation(TestPlanJUnit5.class)

        when:
        TestPlanAnnotationDto dto = TestPlanAnnotationDto.create(null, testPlanJunit5)

        then:
        dto.name() == 'test plan for junit5 example'
    }

    def "test Fields"() {
        given:
        TestPlanAnnotationDto dto = new TestPlanAnnotationDto();

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
        dto.owner() == ""
    }

    @Unroll
    def "TestCreate TestCaseJunit5 as Annotation"(final String[] array1, final String[] array2, final String[] expectedArray) {
        when:
        String[] actualArray = TestPlanAnnotationDto.appendAndDedupArray(array1, array2)

        then:
        TS.asserts().equalsToWithReflection("Check", actualArray, expectedArray, ReflectionComparatorMode.LENIENT_ORDER)

        where:
        array1    | array2    | expectedArray
        []        | []        | []
        ['test']  | ['test']  | ['test']
        []        | ['test']  | ['test']
        ['test']  | []        | ['test']
        ['test1'] | ['test2'] | ['test1', 'test2']
    }
}
