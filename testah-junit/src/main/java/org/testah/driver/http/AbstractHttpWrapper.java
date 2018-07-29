package org.testah.driver.http;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.testah.TS;
import org.testah.driver.http.requests.*;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.testPlan.AbstractTestPlan;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The Class AbstractHttpWrapper.
 */
public abstract class AbstractHttpWrapper {

    /**
     * The default pool size.
     */
    private int defaultPoolSize = 100;

    /**
     * The Default max per route.
     */
    private int defaultMaxPerRoute = 100;

    /**
     * The default connection timeout.
     */
    private int defaultConnectionTimeout = 5000;

    /**
     * The verbose.getDefaultConnectionTimeout
     */
    private boolean verbose = true;

    /**
     * The share state.
     */
    private boolean shareState = true;

    /**
     * The ignore http error.
     */
    private boolean ignoreHttpError = false;

    /**
     * The cookie store.
     */
    private CookieStore cookieStore = null;

    /**
     * The cookie specs.
     */
    private String cookieSpecs = CookieSpecs.DEFAULT;

    /**
     * The request config.
     */
    private RequestConfig requestConfig = null;

    /**
     * The proxy.
     */
    private HttpHost proxy = null;

    /**
     * The default timeout.
     */
    private Integer defaultTimeout = null;

    /**
     * The connection keep alive strategy.
     */
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy = null;

    /**
     * The http client.
     */
    private HttpClient httpClient = null;

    /**
     * The sslcontext.
     */
    private SSLContext sslcontext = null;

    /**
     * The socket factory registry.
     */
    private Registry<ConnectionSocketFactory> socketFactoryRegistry = null;

    /**
     * The response parser factory.
     */
    private HttpMessageParserFactory<HttpResponse> responseParserFactory = null;

    /**
     * The dns resolver.
     */
    private DnsResolver dnsResolver = null;

    /**
     * The request writer factory.
     */
    private HttpMessageWriterFactory<HttpRequest> requestWriterFactory = null;

    /**
     * The trust all certs.
     */
    protected boolean trustAllCerts = true;

    private org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory = null;

    /**
     * The conn manager.
     */
    private PoolingHttpClientConnectionManager connManager;

    private HashMap<String, Header> customHeaders = new HashMap<String, Header>();


    private VerboseAsserts verboseAsserts;

    /**
     * Gets self.
     *
     * @return the self
     */
    protected abstract AbstractHttpWrapper getSelf();

    /**
     * Do request with assert.
     *
     * @param request the request
     * @return the response dto
     */
    public ResponseDto doRequestWithAssert(final AbstractRequestDto<?> request) {
        return doRequestWithAssert(request, request.getExpectedStatus());
    }

    /**
     * Do request with assert.
     *
     * @param request        the request
     * @param expectedStatus the expected status
     * @return the response dto
     */
    public ResponseDto doRequestWithAssert(final AbstractRequestDto<?> request, final int expectedStatus) {
        return doRequestWithAssert(request, new ResponseDto(expectedStatus));
    }

    /**
     * Do request with assert.
     *
     * @param request  the request
     * @param expected the expected
     * @return the response dto
     */
    public ResponseDto doRequestWithAssert(final AbstractRequestDto<?> request, final ResponseDto expected) {
        final ResponseDto response = doRequest(request);
        if (getVerboseAsserts().notNull("preformRequestWithAssert actual response is not null", response)
                && getVerboseAsserts().notNull("preformRequestWithAssert expected response is not null", expected)) {
            response.assertStatus(expected.getStatusCode());
        }
        return response;
    }

    /**
     * Do get.
     *
     * @param uri the uri
     * @return the response dto
     */
    public ResponseDto doGet(final String uri) {
        return doRequest(new GetRequestDto(uri));
    }

    /**
     * Do post.
     *
     * @param uri     the uri
     * @param payload the payload
     * @return the response dto
     */
    public ResponseDto doPost(final String uri, final String payload) {
        return doRequest(new PostRequestDto(uri, payload));
    }

    /**
     * Do post.
     *
     * @param uri     the uri
     * @param payload the payload
     * @return the response dto
     */
    public ResponseDto doPost(final String uri, final Object payload) {
        return doRequest(new PostRequestDto(uri, payload));
    }

    /**
     * Do put.
     *
     * @param uri     the uri
     * @param payload the payload
     * @return the response dto
     */
    public ResponseDto doPut(final String uri, final String payload) {
        return doRequest(new PutRequestDto(uri, payload));
    }

    /**
     * Do put.
     *
     * @param uri     the uri
     * @param payload the payload
     * @return the response dto
     */
    public ResponseDto doPut(final String uri, final Object payload) {
        return doRequest(new PutRequestDto(uri, payload));
    }

    /**
     * Do delete.
     *
     * @param uri the uri
     * @return the response dto
     */
    public ResponseDto doDelete(final String uri) {
        return doRequest(new DeleteRequestDto(uri));
    }

    /**
     * Do request.
     *
     * @param request the request
     * @return the response dto
     */
    public ResponseDto doRequest(final AbstractRequestDto<?> request) {
        return doRequest(request, verbose);
    }

    /**
     * Do request.
     *
     * @param request the request
     * @param verbose the verbose
     * @return the response dto
     */
    public ResponseDto doRequest(final AbstractRequestDto<?> request, final boolean verbose) {
        return doRequest(request, verbose, isIgnoreHttpError());
    }

    /**
     * Do request.
     *
     * @param request         the request
     * @param verbose         the verbose
     * @param ignoreHttpError the ignore http error
     * @return the response dto
     */
    public ResponseDto doRequest(final AbstractRequestDto<?> request, final boolean verbose,
                                 final boolean ignoreHttpError) {
        try {
            final HttpClientContext context = HttpClientContext.create();
            if (null != cookieStore) {
                context.setCookieStore(cookieStore);
                context.setRequestConfig(getRequestConfig());
            }

            if (null != request.getCredentialsProvider()) {
                context.setCredentialsProvider(request.getCredentialsProvider());
            }

            if (!getCustomHeaders().isEmpty()) {
                TS.log().trace("adding custom headers");
                getCustomHeaders().values().forEach(value -> {
                    request.addHeader(value);
                });
            }

            final ResponseDto responseDto = new ResponseDto().setStart();
            if (verbose) {
                AbstractTestPlan.addStepAction(request.createRequestInfoStep(), false);
            }
            try (final CloseableHttpResponse response = (CloseableHttpResponse) getHttpClient()
                    .execute(request.getHttpRequestBase(), context)) {
                final HttpEntity entity = response.getEntity();
                responseDto.setEnd().setStatusCode(response.getStatusLine().getStatusCode());
                responseDto.setStatusText(response.getStatusLine().getReasonPhrase());
                if (null != entity) {
                    responseDto.setResponseBytes(EntityUtils.toByteArray(entity));
                    responseDto.setResponseBody(new String(responseDto.getResponseBytes(), "UTF-8"));
                }
                responseDto.setUrl(request.getHttpRequestBase().getURI().toString());
                responseDto.setHeaders(response.getAllHeaders()).setRequestType(request.getHttpMethod());
                responseDto.setRequestUsed(request);
            }
            if (verbose) {
                AbstractTestPlan.addStepAction(responseDto.createResponseInfoStep(true, true, 500), false);
            }
            if (request.isAutoAssert() && request.getExpectedStatus() > 0) {
                responseDto.assertStatus(request.getExpectedStatus());
            }
            return responseDto;
        } catch (final IOException e) {
            TS.log().error(e);
            if (!ignoreHttpError) {
                getVerboseAsserts().equalsTo("Unexpected Exception thrown from preformRequest in IHttpWrapper", "",
                        e.getMessage());
            }
            return new ResponseDto(-1).setStatusText(e.toString()).setResponseBody(e.toString());
        }
    }

    /**
     * Gets the response dto.
     *
     * @param response the response
     * @param request  the request
     * @return the response dto
     */
    public ResponseDto getResponseDto(final HttpResponse response, final AbstractRequestDto<?> request) {
        if (null != response) {
            try {
                final HttpEntity entity = response.getEntity();
                final ResponseDto responseDto = new ResponseDto();
                responseDto.setStatusCode(response.getStatusLine().getStatusCode());
                responseDto.setStatusText(response.getStatusLine().getReasonPhrase());
                responseDto.setResponseBytes(EntityUtils.toByteArray(entity));
                responseDto.setResponseBody(new String(responseDto.getResponseBytes(), "UTF-8"));
                if (null != request) {
                    responseDto.setUrl(request.getHttpRequestBase().getURI().toString());
                    responseDto.setHeaders(response.getAllHeaders()).setRequestType(request.getHttpMethod());
                }
                return responseDto;
            } catch (final Exception e) {
                TS.log().debug(e);
            }
        }
        return null;
    }

    /**
     * Gets the request config default.
     *
     * @return the request config default
     */
    public RequestConfig getRequestConfigDefault() {
        final RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(getCookieSpecs())
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        return defaultRequestConfig;
    }

    /**
     * Sets the http client.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setHttpClient() {
        final HttpClientBuilder hcb = HttpClients.custom();

        if (null != getProxy()) {
            hcb.setProxy(getProxy());
        }
        if (null != getRequestConfig()) {
            hcb.setDefaultRequestConfig(getRequestConfig());
        }

        if (null != getCookieStore()) {
            hcb.setDefaultCookieStore(getCookieStore());
        }
        if (null != getConnectionManager()) {
            hcb.setConnectionManager(getConnectionManager());
        }
        if (trustAllCerts) {
            hcb.setSSLSocketFactory(getSslSocketFactory());
            // hcb.setSSLHostnameVerifier(new NoopHostnameVerifier());
        }

        return setHttpClient(hcb.build());
    }

    /**
     * Sets the http client.
     *
     * @param httpClient the http client
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
        return getSelf();
    }

    /**
     * Sets the request config.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setRequestConfig() {
        final Builder rcb = RequestConfig.custom();

        if (null != getDefaultConnectionTimeout()) {
            rcb.setSocketTimeout(getDefaultConnectionTimeout()).setConnectTimeout(getDefaultConnectionTimeout())
                    .setConnectionRequestTimeout(getDefaultConnectionTimeout());
        }

        rcb.setCookieSpec(CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC));
        return setRequestConfig(rcb.build());
    }

    /**
     * Sets the request config.
     *
     * @param requestConfig the request config
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setRequestConfig(final RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return getSelf();
    }

    /**
     * Gets the cookie store.
     *
     * @return the cookie store
     */
    public CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * Gets the default cookie store.
     *
     * @return the default cookie store
     */
    public CookieStore getDefaultCookieStore() {
        return new BasicCookieStore();
    }

    /**
     * Gets the response parser factory.
     *
     * @return the response parser factory
     */
    public HttpMessageParserFactory<HttpResponse> getResponseParserFactory() {
        return responseParserFactory;
    }

    /**
     * Sets the response parser factory.
     *
     * @param responseParserFactory the new response parser factory
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setResponseParserFactory(
            final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        this.responseParserFactory = responseParserFactory;
        return getSelf();
    }

    /**
     * Sets the response parser factory.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setResponseParserFactory() {
        /*
         * responseParserFactory = new DefaultHttpResponseParserFactory() {
         *
         *
         * public HttpMessageParser<HttpResponse> create(final SessionInputBuffer buffer, final MessageConstraints constraints) { final
         * LineParser lineParser = new BasicLineParser() {
         *
         *
         * public Header parseHeader(final CharArrayBuffer buffer) { try { return super.parseHeader(buffer); } catch (final ParseException ex) {
         * return new BasicHeader(buffer.toString(), null); } }
         *
         * }; return new DefaultHttpResponseParser(buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
         *
         *
         * protected boolean reject(final CharArrayBuffer line, final int count) { return false; }
         *
         * }; }
         *
         * };
         */
        return getSelf();
    }

    /**
     * Gets the dns resolver.
     *
     * @return the dns resolver
     */
    public DnsResolver getDnsResolver() {
        return dnsResolver;
    }

    /**
     * Sets the dns resolver.
     *
     * @param dnsResolver the new dns resolver
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setDnsResolver(final DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
        return getSelf();
    }

    /**
     * Sets the dns resolver.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setDnsResolver() {
        dnsResolver = new SystemDefaultDnsResolver() {

            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("localhost")) {
                    return new InetAddress[]{InetAddress.getByAddress(new byte[]{127, 0, 0, 1})};
                } else {
                    return super.resolve(host);
                }
            }

        };
        return getSelf();
    }

    /**
     * Sets the request writer factory.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setRequestWriterFactory() {
        requestWriterFactory = new DefaultHttpRequestWriterFactory();
        return getSelf();
    }

    /**
     * Sets the request writer factory.
     *
     * @param requestWriterFactory the new request writer factory
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setRequestWriterFactory(
            final HttpMessageWriterFactory<HttpRequest> requestWriterFactory) {
        this.requestWriterFactory = requestWriterFactory;
        return getSelf();
    }

    /**
     * Sets the connection manager pooling advanced.
     *
     * @return the abstract http wrapper
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyStoreException        the key store exception
     */
    public AbstractHttpWrapper setConnectionManagerPoolingAdvanced()
            throws NoSuchAlgorithmException, KeyStoreException {

        final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);

        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                setSocketFactoryRegistry().getSocketFactoryRegistry(), connFactory, dnsResolver);

        connManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute());
        connManager.setMaxTotal(getDefaultPoolSize());

        return setConnManager(connManager);
    }

    /**
     * Gets the socket factory registry.
     *
     * @return the socket factory registry
     */
    public Registry<ConnectionSocketFactory> getSocketFactoryRegistry() {
        return socketFactoryRegistry;
    }

    /**
     * Sets the socket factory registry.
     *
     * @param socketFactoryRegistry the socket factory registry
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setSocketFactoryRegistry(final Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        this.socketFactoryRegistry = socketFactoryRegistry;
        return getSelf();
    }

    /**
     * Sets the socket factory registry.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setSocketFactoryRegistry() {
        return setSocketFactoryRegistry(RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(getSslcontext())).build());
    }

    /**
     * Sets the ssl context trust all.
     *
     * @return the abstract http wrapper
     */
    public SSLConnectionSocketFactory getDefaultSslConnectionSocketFactory() {
        try {
            final org.apache.http.ssl.SSLContextBuilder contextB = SSLContextBuilder.create();

            contextB.loadTrustMaterial(new org.apache.http.conn.ssl.TrustSelfSignedStrategy());

            final SSLContext sslContext = contextB.build();

            return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Sets the ssl context.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setSslContext() {
        return setSslcontext(SSLContexts.createSystemDefault());
    }

    /**
     * Sets the connect manager default pooling.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setConnectManagerDefaultPooling() {
        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute());
        connManager.setMaxTotal(getDefaultPoolSize());
        return setConnManager(connManager);
    }

    /**
     * Sets the connection keep alive strategy.
     *
     * @return the abstract http wrapper
     */
    // http://www.baeldung.com/httpclient-connection-management
    public AbstractHttpWrapper setConnectionKeepAliveStrategy() {
        final ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
                final HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    final HeaderElement he = it.nextElement();
                    final String param = he.getName();
                    final String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                return 5 * 1000;
            }
        };
        return setConnectionKeepAliveStrategy(myStrategy);
    }

    /**
     * Sets the connection keep alive strategy.
     *
     * @param connectionKeepAliveStrategy the connection keep alive strategy
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setConnectionKeepAliveStrategy(
            final ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
        return getSelf();
    }

    /**
     * Gets the connection keep alive strategy.
     *
     * @return the connection keep alive strategy
     */
    public ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        return connectionKeepAliveStrategy;
    }

    /**
     * Sets the default pool size.
     *
     * @param defaultPoolSize the default pool size
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setDefaultPoolSize(final int defaultPoolSize) {
        this.defaultPoolSize = defaultPoolSize;
        return getSelf();
    }

    /**
     * Sets the allow any certs.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setAllowAnyCerts() {
        return getSelf();
    }

    ;

    /**
     * Gets the default connection timeout.
     *
     * @return the default connection timeout
     */
    public Integer getDefaultConnectionTimeout() {
        return defaultConnectionTimeout;
    }

    /**
     * Sets the default connection timeout.
     *
     * @param defaultConnectionTimeout the default connection timeout
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setDefaultConnectionTimeout(final int defaultConnectionTimeout) {
        this.defaultConnectionTimeout = defaultConnectionTimeout;
        return getSelf();
    }

    /**
     * Checks if is verbose.
     *
     * @return true, if is verbose
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the verbose.
     *
     * @param verbose the verbose
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setVerbose(final boolean verbose) {
        this.verbose = verbose;
        return getSelf();
    }

    /**
     * Checks if is share state.
     *
     * @return true, if is share state
     */
    public boolean isShareState() {
        return shareState;
    }

    /**
     * Sets the share state.
     *
     * @param shareState the share state
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setShareState(final boolean shareState) {
        this.shareState = shareState;
        return getSelf();
    }

    /**
     * Gets the default pool size.
     *
     * @return the default pool size
     */
    public int getDefaultPoolSize() {
        return defaultPoolSize;
    }

    /**
     * Sets the cookies from browser.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setCookiesFromBrowser() {
        return setCookiesFromBrowser(TS.browser().getDriver().manage().getCookies());
    }

    /**
     * Sets the cookies from browser.
     *
     * @param browserCookies the browser cookies
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setCookiesFromBrowser(final Set<org.openqa.selenium.Cookie> browserCookies) {

        this.setShareState(false);
        if (null != browserCookies) {
            final Iterator<org.openqa.selenium.Cookie> iter = browserCookies.iterator();
            final CookieStore cookieStore = this.getDefaultCookieStore();
            while (iter.hasNext()) {
                final org.openqa.selenium.Cookie c = iter.next();
                final BasicClientCookie basicClientCookie = new BasicClientCookie(c.getName(), c.getValue());
                basicClientCookie.setDomain(c.getDomain());
                basicClientCookie.setExpiryDate(c.getExpiry());
                basicClientCookie.setPath(c.getPath());
                cookieStore.addCookie(basicClientCookie);
                TS.log().trace("Add cookie " + c.getName() + " = " + c.getValue());
            }
            setCookieStore(cookieStore);
        }
        return getSelf();
    }

    /**
     * Sets the cookie store.
     *
     * @param cookieStore the cookie store
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setCookieStore(final CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return getSelf();
    }

    /**
     * Gets the conn manager.
     *
     * @return the conn manager
     */
    public PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }

    /**
     * Sets the conn manager.
     *
     * @param connManager the conn manager
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setConnManager(final PoolingHttpClientConnectionManager connManager) {
        this.connManager = connManager;
        return getSelf();
    }

    /**
     * Gets the default max per route.
     *
     * @return the default max per route
     */
    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    /**
     * Gets the cookie specs.
     *
     * @return the cookie specs
     */
    public String getCookieSpecs() {
        return cookieSpecs;
    }

    /**
     * Sets the cookie specs.
     *
     * @param cookieSpecs the cookie specs
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setCookieSpecs(final String cookieSpecs) {
        this.cookieSpecs = cookieSpecs;
        return getSelf();
    }

    /**
     * Gets the proxy.
     *
     * @return the proxy
     */
    public HttpHost getProxy() {
        return proxy;
    }

    /**
     * Sets the proxy.
     *
     * @param proxy the proxy
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setProxy(final HttpHost proxy) {
        this.proxy = proxy;
        return getSelf();
    }

    /**
     * Sets the proxy.
     *
     * @param host the host
     * @param port the port
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setProxy(final String host, final int port) {
        return setProxy(new HttpHost(host, port));
    }

    /**
     * Gets the default timeout.
     *
     * @return the default timeout
     */
    public Integer getDefaultTimeout() {
        return defaultTimeout;
    }

    /**
     * Sets the default timeout.
     *
     * @param defaultTimeout the default timeout
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setDefaultTimeout(final Integer defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
        return getSelf();
    }

    /**
     * Gets the request config.
     *
     * @return the request config
     */
    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    /**
     * Gets the http client.
     *
     * @return the http client
     */
    public HttpClient getHttpClient() {
        if (null == httpClient) {
            setHttpClient();
        }
        return httpClient;
    }

    /**
     * Close http client.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper closeHttpClient() {
        if (null != httpClient) {
            try {
                if (httpClient instanceof CloseableHttpClient) {
                    ((CloseableHttpClient) httpClient).close();
                } else if (httpClient instanceof CloseableHttpAsyncClient) {
                    ((CloseableHttpAsyncClient) httpClient).close();
                }
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return setHttpClient(null);
    }

    /**
     * Gets the connection manager.
     *
     * @return the connection manager
     */
    public PoolingHttpClientConnectionManager getConnectionManager() {
        return connManager;
    }

    /**
     * Gets the sslcontext.
     *
     * @return the sslcontext
     */
    public SSLContext getSslcontext() {
        return sslcontext;
    }

    /**
     * Sets the sslcontext.
     *
     * @param sslcontext the sslcontext
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setSslcontext(final SSLContext sslcontext) {
        this.sslcontext = sslcontext;
        return getSelf();
    }

    /**
     * Gets the request writer factory.
     *
     * @return the request writer factory
     */
    public HttpMessageWriterFactory<HttpRequest> getRequestWriterFactory() {
        return requestWriterFactory;
    }

    /**
     * Checks if is ignore http error.
     *
     * @return true, if is ignore http error
     */
    public boolean isIgnoreHttpError() {
        return ignoreHttpError;
    }

    /**
     * Sets the ignore http error.
     *
     * @param ignoreHttpError the new ignore http error
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setIgnoreHttpError(final boolean ignoreHttpError) {
        this.ignoreHttpError = ignoreHttpError;
        return getSelf();
    }

    /**
     * Checks if is trust all certs.
     *
     * @return true, if is trust all certs
     */
    public boolean isTrustAllCerts() {
        return trustAllCerts;
    }

    /**
     * Sets the trust all certs.
     *
     * @param trustAllCerts the new trust all certs
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setTrustAllCerts(final boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
        return getSelf();
    }

    /**
     * Gets ssl socket factory.
     *
     * @return the ssl socket factory
     */
    public org.apache.http.conn.ssl.SSLConnectionSocketFactory getSslSocketFactory() {
        if (null == sslSocketFactory) {
            this.sslSocketFactory = getDefaultSslConnectionSocketFactory();
        }
        return sslSocketFactory;
    }

    /**
     * Sets ssl socket factory.
     *
     * @param sslSocketFactory the ssl socket factory
     */
    public void setSslSocketFactory(final org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    /**
     * Add custom test header abstract http wrapper.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addCustomTestHeader() {
        return addCustomTestHeader("testah-e2e");
    }

    /**
     * Add custom test header abstract http wrapper.
     *
     * @param value the value
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addCustomTestHeader(final String value) {
        return addCustomHeader("X-Application-Id", value);
    }

    /**
     * Gets custom test header.
     *
     * @return the custom test header
     */
    public HashMap<String, Header> getCustomHeaders() {
        return customHeaders;
    }

    /**
     * Clear custom test header abstract http wrapper.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper clearCustomTestHeader() {
        getCustomHeaders().clear();
        return getSelf();
    }

    /**
     * Add custom header abstract http wrapper.
     *
     * @param name  the name
     * @param value the value
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addCustomHeader(final String name, final String value) {
        return addCustomHeader(new BasicHeader(name, (value == null ? "" : value)));
    }

    /**
     * Sets custom test header.
     *
     * @param customHeader the custom test header
     * @return the custom test header
     */
    public AbstractHttpWrapper addCustomHeader(final Header customHeader) {
        getCustomHeaders().put(customHeader.getName(), customHeader);
        return getSelf();
    }

    /**
     * Remove custom header abstract http wrapper.
     *
     * @param name the name
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper removeCustomHeader(final String name) {
        getCustomHeaders().remove(name);
        return getSelf();
    }

    /**
     * Add basic auth abstract http wrapper.
     *
     * @param userName the user name
     * @param password the password
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addBasicAuth(final String userName, final String password) {
        return addBasicAuth(userName, password, HttpAuthUtil.UTF8_ENCODING);

    }

    /**
     * Add basic auth abstract http wrapper.
     *
     * @param userName the user name
     * @param password the password
     * @param encoding the encoding
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addBasicAuth(final String userName, final String password, final String encoding) {
        return addBasicAuth(userName, password, encoding, true);

    }

    /**
     * Add basic auth abstract http wrapper.
     *
     * @param userName the user name
     * @param password the password
     * @param encoding the encoding
     * @param useMask  the use mask
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addBasicAuth(final String userName, final String password, final String encoding, final boolean useMask) {
        return addCustomHeader(new HttpAuthUtil().setUserName(userName).setPassword(password).useEncodingUtf8().setUseMask(useMask)
                .createBasicAuthHeader());
    }

    /**
     * Add bearer auth abstract http wrapper.
     *
     * @param token the token
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper addBearerAuth(final String token) {
        return addCustomHeader(new HttpAuthUtil().createBearerAuthHeader(token));
    }

    /**
     * Remove basic auth abstract http wrapper.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper removeAuth() {
        return removeCustomHeader(HttpAuthUtil.HEADER_NAME);
    }

    /**
     * Sets verbose asserts.
     * Defaults verboseAsserts to a new instance of verboseAsserts
     *
     * @return the verbose asserts
     */
    public AbstractHttpWrapper setVerboseAsserts() {
        this.verboseAsserts = new VerboseAsserts();
        return getSelf();
    }

    /**
     * Sets verbose asserts.
     *
     * @param verboseAsserts the verbose asserts
     * @return the verbose asserts
     */
    public AbstractHttpWrapper setVerboseAsserts(final VerboseAsserts verboseAsserts) {
        this.verboseAsserts = verboseAsserts;
        return getSelf();
    }

    /**
     * Gets verbose asserts.
     * If Null, will use TS.asserts() as default
     *
     * @return the verbose asserts
     */
    public VerboseAsserts getVerboseAsserts() {
        if (this.verboseAsserts == null) {
            this.verboseAsserts = TS.asserts();
        }
        return this.verboseAsserts;
    }

}
