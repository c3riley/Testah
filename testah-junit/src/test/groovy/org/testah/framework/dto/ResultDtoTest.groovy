package org.testah.framework.dto

import org.junit.platform.launcher.listeners.TestExecutionSummary
import org.junit.runner.Description
import org.junit.runner.Result
import org.junit.runner.notification.Failure
import org.testah.Junit4TestPlan
import spock.lang.Specification

class ResultDtoTest extends Specification {

    def "Result JUnit 4"() {
        given:
        Result result = Mock(Result.class)
        result.getRunCount() >> 10
        result.getFailureCount() >> 3
        result.getIgnoreCount() >> 3
        result.getFailures() >> { return [new Failure(Description.createTestDescription(Junit4TestPlan.class, "Here is Failure info"), new RuntimeException("Here is Failure info"))] }
        when:
        ResultDto resultDto = new ResultDto(result)

        then:
        resultDto.getJunitResult() == result
        resultDto.getJunitCount() == 10
        resultDto.getJunitFailure() == 3
        resultDto.getJunitIgnore() == 3
        resultDto.getJunitPass() == 4
        resultDto.getJunitFailureMessage() == "[Here is Failure info(org.testah.Junit4TestPlan): Here is Failure info]"
    }

    def "TestExecutionSummary JUnit 5"() {
        given:
        TestExecutionSummary result = Mock(TestExecutionSummary.class);
        result.getTestsFoundCount() >> 10
        result.getTestsStartedCount() >> 10
        result.getTestsFailedCount() >> 3
        result.getTestsSkippedCount() >> 2
        result.getTestsAbortedCount() >> 1
        result.getTestsSucceededCount() >> 4
        result.printFailuresTo(*_) >> { PrintWriter pw -> pw.print("Here is Failure info") }

        when:
        ResultDto resultDto = new ResultDto(result)

        then:
        resultDto.getTestExecutionSummary() == result
        resultDto.getJunitCount() == 10
        resultDto.getJunitFailure() == 3
        resultDto.getJunitIgnore() == 3
        resultDto.getJunitPass() == 4
        resultDto.getJunitFailureMessage() == "Here is Failure info"
    }
}
