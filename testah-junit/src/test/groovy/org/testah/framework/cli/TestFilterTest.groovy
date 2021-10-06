package org.testah.framework.cli

import org.junit.After
import org.testah.Junit4TestPlan
import org.testah.Junit5TestPlan
import org.testah.TS
import spock.lang.Specification
import spock.lang.Unroll

class TestFilterTest extends Specification {

    @Unroll
    def "test filter with runtypes"(final List<String> tagsFound, final String tagsFilter, final boolean found) {

        given:
        TestFilter testFilter = new TestFilter()

        when:
        boolean rtn = testFilter.isFilterCheckOk(tagsFound, tagsFilter)
        println "tagsFound[${}] == tagsFilter[${tagsFilter}]"
        then:
        rtn == found

        where:
        tagsFound                        | tagsFilter                       | found
        ["ACCEPTANCE"]                   | "ACCEPTANCE"                     | true
        ["ACCEPTANCE"]                   | "NOT_FOUND"                      | false
        ["ACCEPTANCE"]                   | "!ACCEPTANCE"                    | true
        ["ACCEPTANCE"]                   | "!NOT_FOUND"                     | false
        ["ACCEPTANCE", "NOT_IN_PROD"]    | "ACCEPTANCE,~NOT_IN_PROD"        | false
        ["ACCEPTANCE", "NOT_IN_PROD2"]   | "ACCEPTANCE,~NOT_IN_PROD"        | true
        ["ACCEPTANCE", "NOT_IN_PROD2"]   | "!ACCEPTANCE,~NOT_IN_PROD"       | true
        ["ACCEPTANCE2", "NOT_IN_PROD2"]  | "!ACCEPTANCE,~NOT_IN_PROD"       | false
        ["ACCEPTANCE", "PROD", "TEST"]   | "!ACCEPTANCE,PROD,~TEST"         | false
        ["ACCEPTANCE", "PROD", "TEST2"]  | "!ACCEPTANCE,PROD,~TEST"         | true
        ["ACCEPTANCE", "PROD", "TEST2"]  | "!ACCEPTANCE,!PROD,~TEST"        | true
        ["ACCEPTANCE", "PROD2", "TEST2"] | "!ACCEPTANCE,!PROD,~TEST"        | false
        ["ACCEPTANCE2", "PROD2"]         | "ACCEPTANCE,~PROD"               | false
        []                               | "ACCEPTANCE,~PROD"               | false
        []                               | ""                               | true
        ["ACCEPTANCE2", "PROD2"]         | ""                               | true
        ["ACCEPTANCE2", "PROD2"]         | "~PROD2"                         | false
        ["ACCEPTANCE2", "PROD2"]         | "!PROD2"                         | true
        ["ACCEPTANCE2", "PROD2"]         | "PROD2"                          | true
        ["ACCEPTANCE2", "PROD"]          | "~PROD2"                         | true
        []                               | "PROD2"                          | false
        []                               | ""                               | true
        ['pp']                           | "~zz,~zzz,~SYSTEM,PR,~dd,~ss,pp" | true
        ['ACCEPTANCE']                   | "~zz,~zzz,~SYSTEM,PR,~dd,~ss,pp" | false
    }

    @Unroll
    def "test filter with runtypes test filter off"(final List<String> tagsFound, final String tagsFilter, final boolean found) {

        given:
        TestFilter testFilter = new TestFilter()

        when:
        boolean rtn = testFilter.isFilterCheckOk(tagsFound, tagsFilter)

        then:
        rtn == found

        where:
        tagsFound | tagsFilter | found
        []        | ""         | true
    }

    @Unroll
    def "test is filter on"(final String tagsFilter, final boolean found) {

        given:
        TestFilter testFilter = new TestFilter()

        when:
        boolean rtn = testFilter.isFilterOn(tagsFilter)

        then:
        rtn == found

        where:
        tagsFilter | found
        null       | false
        ""         | false
        "   "      | false
        "test"     | true
    }

    @Unroll
    def "test is testplan with testcase matching JUnit4"(final String runTypeFilter, final boolean found) {

        given:
        println 'RunType Used is ' + runTypeFilter
        TestFilter testFilter = new TestFilter()
        TS.params().setFilterByRunType(runTypeFilter)

        when:
        boolean rtn = testFilter.filterTestPlansToRun([Junit4TestPlan] as Set)

        then:
        rtn == found

        where:
        runTypeFilter | found
        "r1"          | true
        "r2"          | true
        "r"           | false
    }

    @Unroll
    def "test is testplan with testcase matching JUnit5"(final String runTypeFilter, final boolean found) {

        given:
        println 'RunType Used is ' + runTypeFilter
        TestFilter testFilter = new TestFilter()
        TS.params().setFilterByRunType(runTypeFilter)

        when:
        boolean rtn = testFilter.filterTestPlansToRun([Junit5TestPlan] as Set)

        then:
        rtn == found

        where:
        runTypeFilter | found
        "r1"          | true
        "r2"          | true
        "r"           | false
    }

    @After
    public void teardown() {
        TS.params().setFilterByRunType(null)
    }
}
