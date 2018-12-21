package org.testah.driver.http.response;

import java.util.Map;

public class PostManDto {

    private String url;
    private Map<String, String> headers;
    private Map<String, String> args;

    public PostManDto() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
