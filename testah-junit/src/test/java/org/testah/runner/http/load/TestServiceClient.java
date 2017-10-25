package org.testah.runner.http.load;

public class TestServiceClient {

    public TestServiceClient() {
        //
    }

    public String getUrlGet(int statusCode) {
        return "https://httpstat.us/" + statusCode;
    }

    public String getUrlPost() {
        return "https://httpbin.org/post";
    }
}
