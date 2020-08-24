package org.testah.framework.cli

import spock.lang.Specification
import spock.lang.Unroll

class TestFilterTest extends Specification {

    @Unroll
    def "test filter with runtypes"(final List<String> tagsFound, final String tagsFilter, final boolean found) {

        given:
        TestFilter testFilter = new TestFilter()

        when:
        boolean rtn = testFilter.isFilterCheckOk(tagsFound as String[], tagsFilter)

        then:
        rtn == found

        where:
        tagsFound                        | tagsFilter                 | found
        ["ACCEPTANCE"]                   | "ACCEPTANCE"               | true
        ["ACCEPTANCE"]                   | "NOT_FOUND"                | false
        ["ACCEPTANCE"]                   | "!ACCEPTANCE"              | true
        ["ACCEPTANCE"]                   | "!NOT_FOUND"               | false
        ["ACCEPTANCE", "NOT_IN_PROD"]    | "ACCEPTANCE,~NOT_IN_PROD"  | false
        ["ACCEPTANCE", "NOT_IN_PROD2"]   | "ACCEPTANCE,~NOT_IN_PROD"  | true
        ["ACCEPTANCE", "NOT_IN_PROD2"]   | "!ACCEPTANCE,~NOT_IN_PROD" | true
        ["ACCEPTANCE2", "NOT_IN_PROD2"]  | "!ACCEPTANCE,~NOT_IN_PROD" | false
        ["ACCEPTANCE", "PROD", "TEST"]   | "!ACCEPTANCE,PROD,~TEST"   | false
        ["ACCEPTANCE", "PROD", "TEST2"]  | "!ACCEPTANCE,PROD,~TEST"   | true
        ["ACCEPTANCE", "PROD", "TEST2"]  | "!ACCEPTANCE,!PROD,~TEST"  | true
        ["ACCEPTANCE", "PROD2", "TEST2"] | "!ACCEPTANCE,!PROD,~TEST"  | false
        ["ACCEPTANCE2", "PROD2"]         | "ACCEPTANCE,~PROD"         | false
        []                               | "ACCEPTANCE,~PROD"         | false
        []                               | ""                         | true
        ["ACCEPTANCE2", "PROD2"]         | ""                         | true
        ["ACCEPTANCE2", "PROD2"]         | "~PROD2"                   | false
        ["ACCEPTANCE2", "PROD2"]         | "!PROD2"                   | true
        ["ACCEPTANCE2", "PROD2"]         | "PROD2"                    | true
        ["ACCEPTANCE2", "PROD"]          | "~PROD2"                   | true
        []                               | "PROD2"                    | false
        []                               | ""                         | true
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
}
