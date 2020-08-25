package org.testah.http;

import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.requests.HeadRequestDto;
import org.testah.driver.http.requests.OptionRequestDto;

public class HeadCall {

    @Test
    public void test() throws Exception {

        TS.http().addCustomTestHeader("option-test").doRequest(new HeadRequestDto("https://www.test-cors.org")).assertStatus();

    }


}
