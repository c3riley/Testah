package org.testah.runner.http.load;

public class TestServiceClient {

    public TestServiceClient() {
        //
    }

    public String getUrl() {
        return "https://httpstat.us/";
    }

    public String getUrl(int statusCode) {
        return getUrl() + statusCode;
    }
}
