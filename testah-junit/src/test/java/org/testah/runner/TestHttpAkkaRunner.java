package org.testah.runner;

import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;

public class TestHttpAkkaRunner {

    @Test
    public void InvalidUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("htp:/www.goeeeeogle.com"), 5);
    }

    @Test
    public void WrongUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.goeeeeogle.com"), 5);
    }

    @Test
    public void HappyPath() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }

}
