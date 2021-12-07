package org.testah.driver.web.browser

import org.openqa.selenium.By
import org.testah.TS
import spock.lang.Specification

class AbstractBrowserTest extends Specification {
    def "WaitTillElementIsPresent Not Found"() {
        given:
        TS.browser().goTo("https://www.google.com");

        when:
        Boolean rtn = TS.browser().waitTillElementIsPresent(By.id('NOT_FOUND'), 2, false)

        then:
        rtn == false
    }

    def "WaitTillElementIsPresent Not Found and Throw error"() {
        given:
        TS.browser().goTo("https://www.google.com");

        when:
        TS.browser().waitTillElementIsPresent(By.id('NOT_FOUND'), 2, true)

        then:
        AssertionError error = thrown(AssertionError.class)
        error.message == 'Timed out while Waiting for element: By.id: NOT_FOUND'
    }

    def "WaitTillElementIsPresent"() {
        given:
        TS.browser().goTo("https://www.google.com");

        when:
        boolean rtn = TS.browser().waitTillElementIsPresent(By.name('q'), 2, true)

        then:
        rtn == true
    }
}
