package org.testah.http;

import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.requests.OptionRequestDto;

public class OptionCall {

    @Test
    public void test() throws Exception {

        TS.http().addCustomTestHeader("option-test").doRequest(new OptionRequestDto("https://www.test-cors.org")).assertStatus();

    }


}
