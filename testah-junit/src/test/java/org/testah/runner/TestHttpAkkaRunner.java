package org.testah.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;

public class TestHttpAkkaRunner {

    @Ignore
    @Test
    public void test() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }

}
