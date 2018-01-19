package org.testah.http;

import org.apache.http.client.ClientProtocolException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.driver.http.HttpWrapperV1;
import org.testah.driver.http.requests.GetRequestDto;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.testPlan.HttpTestPlan;

import java.io.File;
import java.io.IOException;

@TestPlan(description = "test Http")
public class TestHttp extends HttpTestPlan {

    @Before
    public void setup() {
        TS.http().doPost("http://www.google.com", null);
    }

    @TestCase
    @Test
    public void testWith2Gets() throws ClientProtocolException, IOException {
        step("Got to google");
        final HttpWrapperV1 http = new HttpWrapperV1();
        http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.google.com"));
        step("go to google again");
        http.setHttpClient().doRequestWithAssert(new GetRequestDto("http://www.google.com"));

    }

    @Ignore
    @TestCase(tags = "test")
    @Test()
    public void postWithNUll() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", null);
    }

    @TestCase
    @Test
    public void downloadAndUnZip() throws ClientProtocolException, IOException {
        final ResponseDto dto = TS.http().doGet(
                "https://github.com/c3riley/maven-repository/raw/master/org/testah/testah-junit/0.0.1/testah-junit-0.0.1.jar");
        final File jar = dto.saveToFile(new File(TS.params().getOutput(), "test.jar"));
        TS.asserts().isTrue(jar.exists());
        // final Archiver archiver =
        // ArchiverFactory.createArchiver(ArchiveFormat.JAR);
        // archiver.extract(jar, jar.getParentFile());
    }

    @Ignore
    @TestCase
    @Test
    public void postWithObject() throws ClientProtocolException, IOException {
        TS.http().doPost("http://www.google.com", new TestCaseDto());
    }
}
