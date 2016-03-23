package org.testah;

import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.runner.HttpAkkaRunner;

public class TestHttpAkkaRunner {

    @Test
    public void test() {
        final HttpAkkaRunner a = new HttpAkkaRunner();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }
    
}
