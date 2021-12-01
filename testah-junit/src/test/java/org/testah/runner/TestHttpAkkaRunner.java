package org.testah.runner;

import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.driver.http.HttpWrapperV2;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.requests.PostRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.runner.http.load.HttpAkkaStats;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestHttpAkkaRunner {

    @Before
    public void setup() {
        HttpAkkaRunner.reset();
    }

    @Test
    public void testHttpWrapper() {
        HttpWrapperV2 http = new HttpWrapperV2();
        HttpAkkaRunner.getInstance().setHttpWrapper(http);
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), is(http));

        HttpAkkaRunner.getInstance().setHttpWrapper(null);
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), notNullValue());
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), not(http));
        assertThat(HttpAkkaRunner.getInstance().getHttpWrapper(), instanceOf(HttpWrapperV2.class));
    }
}
