package org.testah.framework.report

import spock.lang.Specification

class SummaryHtmlFormatterTest extends Specification {

    def "CreateJsonReport"() {
        String actual
        given:
        SummaryHtmlFormatter summaryHtmlFormatter = Spy(new SummaryHtmlFormatter([], 4,4,2,1,1,120000L))
        summaryHtmlFormatter.createReport(*_) >> {args->
            actual = args[2]
            return null
        }
        when:
        summaryHtmlFormatter.createJsonReport()

        then:
        actual == '''{
  "totalTestCasesIgnored" : 1,
  "totalDuration" : 120000,
  "totalTestPlans" : 4,
  "totalTestCases" : 4,
  "totalTestCasesFailed" : 1,
  "totalTestCasesPassed" : 2
}'''
    }
}
