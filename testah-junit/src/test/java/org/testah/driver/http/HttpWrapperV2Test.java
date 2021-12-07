package org.testah.driver.http;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HttpWrapperV2Test {

    HttpWrapperV2 client = new HttpWrapperV2();

    @Test
    public void getSelfTest() {
        assertThat(client.getSelf(), is(client));
    }

    @Test
    public void setDefaultConnectionTimeoutTest() {
        assertThat(client.getDefaultConnectionTimeout(), equalTo(60000));
        client.setDefaultConnectionTimeout(12);
        assertThat(client.getDefaultConnectionTimeout(), is(12));
    }

    @Test
    public void doRequestWithAssertTest() {
    }

    @Test
    public void doRequestWithAssert1Test() {
    }

    @Test
    public void doRequestWithAssert2Test() {
    }

    @Test
    public void doRequestTest() {
    }

    @Test
    public void doRequest1Test() {
    }

    @Test
    public void doRequest2Test() {
    }

    @Test
    public void getVerboseAssertsTest() {
        assertThat(client.getVerboseAsserts(), is(TS.asserts()));
        VerboseAsserts setterTest = new VerboseAsserts();
        client.setVerboseAsserts(setterTest);
        assertThat(client.getVerboseAsserts(), is(setterTest));
    }

    @Test
    public void setVerboseAssertsTest() {
    }

    @Test
    public void setVerboseAsserts1Test() {
    }

    @Test
    public void isIgnoreHttpErrorTest() {
    }

    @Test
    public void setIgnoreHttpErrorTest() {
    }

    @Test
    public void getRequestConfigTest() {
        String v2ExpectedDefault = "[expectContinueEnabled=false, proxy=null, " +
            "localAddress=null, cookieSpec=default, redirectsEnabled=true, relativeRedirectsAllowed=true, " +
            "maxRedirects=50, circularRedirectsAllowed=false, authenticationEnabled=true, " +
            "targetPreferredAuthSchemes=[NTLM, Digest], proxyPreferredAuthSchemes=[Basic], " +
            "connectionRequestTimeout=60000, connectTimeout=60000, socketTimeout=60000, contentCompressionEnabled=true]";

        assertThat(client.getRequestConfig().toString(), equalTo(v2ExpectedDefault));
        RequestConfig requestConfig = client.getRequestConfigDefault();
        assertThat(requestConfig, notNullValue());
        assertThat(requestConfig.toString(), equalTo(v2ExpectedDefault));

        client.setRequestConfig(requestConfig);
        assertThat(client.getRequestConfig(), is(requestConfig));
        client.setRequestConfig();
        assertThat(client.getRequestConfig(), not(requestConfig));

        client.setRequestConfig(client.getRequestConfigDefaultBuilder().setExpectContinueEnabled(true).build());

        assertThat(client.getRequestConfig().isExpectContinueEnabled(), equalTo(true));
    }

    @Test
    public void getCustomHeadersTest() {
    }

    @Test
    public void getHttpClientTest() {
        assertThat(client.getHttpClient(), notNullValue());
    }

    @Test
    public void setHttpClientTest() {
        assertThat(client.setHttpClient().getDefaultMaxPerRoute(), equalTo(100));
        HttpClient expectedClient = client.getHttpClientDefaultBuilder().setMaxConnTotal(123).build();
        client.setHttpClient(expectedClient);
        assertThat(client.getHttpClient(), is(expectedClient));
    }

    @Test
    public void getProxyTest() {
        assertThat(client.getProxy(), nullValue());
        HttpHost host = new HttpHost("hostname.com", 9012);
        client.setProxy(host);
        assertThat(client.getProxy(), is(host));
        client.setProxy("hostname2.com", 9013);
        assertThat(client.getProxy().getPort(), equalTo(9013));
        assertThat(client.getProxy().getHostName(), equalTo("hostname2.com"));
    }

    @Test
    public void getCookieStoreTest() {
        assertThat(client.getCookieStore(), nullValue());
        BasicCookieStore cookieStore =  new BasicCookieStore();
        client.setCookieStore(cookieStore);
        assertThat(client.getCookieStore(), is(cookieStore));
    }



    @Test
    public void getConnectionManagerTest() {
    }

    @Test
    public void getSslSocketFactoryTest() {
    }

    @Test
    public void setSslSocketFactoryTest() {
    }

    @Test
    public void getDefaultSslConnectionSocketFactoryTest() {
    }

    @Test
    public void doGetTest() {
        client.doGet("https://postman-echo.com/get").assertStatus();
    }

    @Test
    public void doPostTest() {
        client.doPost("https://postman-echo.com/post", "This is a test").assertStatus();
        String[] payload = new String[] {"test1", "test2"};
        client.doPost("https://postman-echo.com/post", payload);
    }

    @Test
    public void doPutTest() {
        client.doPut("https://postman-echo.com/put", "This is a test").assertStatus();
        String[] payload = new String[] {"test1", "test2"};
        client.doPut("https://postman-echo.com/put", payload);
    }

    @Test
    public void doDeleteTest() {
        client.doDelete("https://postman-echo.com/delete").assertStatus();
    }

    @Test
    public void getResponseDtoTest() {

    }

    @Test
    public void getCookieSpecsTest() {
    }

    @Test
    public void setCookieSpecsTest() {
    }

    @Test
    public void getResponseParserFactoryTest() {
    }

    @Test
    public void setResponseParserFactoryTest() {
    }

    @Test
    public void setResponseParserFactory1Test() {
    }

    @Test
    public void getDnsResolverTest() {
    }

    @Test
    public void setDnsResolverTest() {
    }

    @Test
    public void setDnsResolver1Test() {
    }

    @Test
    public void setRequestWriterFactoryTest() {
    }

    @Test
    public void setRequestWriterFactory1Test() {
    }

    @Test
    public void setConnectionManagerPoolingAdvancedTest() {
    }

    @Test
    public void getSocketFactoryRegistryTest() {
    }

    @Test
    public void setSocketFactoryRegistryTest() {
    }

    @Test
    public void setSocketFactoryRegistry1Test() {
    }

    @Test
    public void getDefaultMaxPerRouteTest() {
    }

    @Test
    public void getDefaultPoolSizeTest() {
    }

    @Test
    public void setDefaultPoolSizeTest() {
    }

    @Test
    public void getSslcontextTest() {
    }

    @Test
    public void setSslcontextTest() {
    }

    @Test
    public void setSslContextTest() {
    }

    @Test
    public void setConnectManagerDefaultPoolingTest() {
    }

    @Test
    public void setConnectionKeepAliveStrategyTest() {
    }

    @Test
    public void setConnectionKeepAliveStrategy1Test() {
    }

    @Test
    public void getConnectionKeepAliveStrategyTest() {
    }

    @Test
    public void setAllowAnyCertsTest() {
    }

    @Test
    public void isVerboseTest() {
    }

    @Test
    public void setVerboseTest() {
    }

    @Test
    public void isShareStateTest() {
    }

    @Test
    public void setShareStateTest() {
    }

    @Test
    public void setCookiesFromBrowserTest() {
    }

    @Test
    public void setCookiesFromBrowser1Test() {
    }

    @Test
    public void getDefaultCookieStoreTest() {
    }

    @Test
    public void getConnManagerTest() {
    }

    @Test
    public void setConnManagerTest() {
    }

    @Test
    public void getDefaultTimeoutTest() {
    }

    @Test
    public void setDefaultTimeoutTest() {
    }

    @Test
    public void closeHttpClientTest() {
    }

    @Test
    public void getRequestWriterFactoryTest() {
    }

    @Test
    public void isTrustAllCertsTest() {
    }

    @Test
    public void setTrustAllCertsTest() {
    }

    @Test
    public void addCustomTestHeaderTest() {
    }

    @Test
    public void addCustomTestHeader1Test() {
    }

    @Test
    public void addCustomHeaderTest() {
    }

    @Test
    public void addCustomHeader1Test() {
    }

    @Test
    public void clearCustomTestHeaderTest() {
    }

    @Test
    public void addBasicAuthTest() {
    }

    @Test
    public void addBasicAuth1Test() {
    }

    @Test
    public void addBasicAuth2Test() {
    }

    @Test
    public void addBearerAuthTest() {
    }

    @Test
    public void removeAuthTest() {
    }

    @Test
    public void removeCustomHeaderTest() {
    }
}