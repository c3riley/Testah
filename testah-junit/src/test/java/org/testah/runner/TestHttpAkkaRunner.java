package org.testah.runner;

import org.junit.Test;
import org.testah.driver.http.requests.GetRequestDto;

public class TestHttpAkkaRunner {

    @Test
    public void invalidUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("htp:/www.goeeeeogle.com"), 5);
    }

    @Test
    public void wrongUrl() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.goeeeeogle.com"), 5);
    }

    @Test
    public void happyPath() {
        final HttpAkkaRunner a = HttpAkkaRunner.getInstance();
        a.runAndReport(5, new GetRequestDto("http://www.google.com"), 5);
    }

}
