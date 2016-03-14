package org.testah.driver.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
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
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;

public abstract class AbstractHttpWrapper {

	public CredentialsProvider defaultCredentialsProvider = null;
	private int defaultPoolSize = 100;
	private final int DefaultMaxPerRoute = 100;
	private int defaultConnectionTimeout = 5000;
	private ResponseDto expectedResponse = null;
	private boolean verbose = true;
	private boolean shareState = true;
	private boolean ignoreHttpError = false;
	private Header[] headers = null;
	private CookieStore cookieStore = null;
	private String cookieSpecs = CookieSpecs.DEFAULT;
	private RequestConfig requestConfig = null;
	private HttpHost proxy = null;
	private Integer defaultTimeout = null;
	private ConnectionKeepAliveStrategy connectionKeepAliveStrategy = null;
	private HttpClient httpClient = null;
	private SSLContext sslcontext = null;
	private Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
	private HttpMessageParserFactory<HttpResponse> responseParserFactory = null;
	private DnsResolver dnsResolver = null;
	private HttpMessageWriterFactory<HttpRequest> requestWriterFactory = null;
	private boolean trustAllCerts = true;

	private PoolingHttpClientConnectionManager connManager;

	public ResponseDto preformRequestWithAssert(final AbstractRequestDto request) {
		return preformRequestWithAssert(request, getExpectedResponse());
	}

	public ResponseDto preformRequestWithAssert(final AbstractRequestDto request, final int expectedStatus) {
		return preformRequestWithAssert(request, new ResponseDto(expectedStatus));
	}

	public ResponseDto preformRequestWithAssert(final AbstractRequestDto request, final ResponseDto expected) {
		final ResponseDto response = preformRequest(request);
		if (TS.asserts().notNull("preformRequestWithAssert expected", response)
				&& TS.asserts().notNull("preformRequestWithAssert expected", expected)) {
			response.assertStatus(expected.getStatusCode());
		}
		return response;
	}

	public ResponseDto preformRequest(final AbstractRequestDto request) {
		try {
			final HttpClientContext context = HttpClientContext.create();
			if (null != cookieStore) {
				context.setCookieStore(cookieStore);
				context.setRequestConfig(getRequestConfig());
			}

			if (null != request.getCredentialsProvider()) {
				context.setCredentialsProvider(defaultCredentialsProvider);
			}
			final ResponseDto responseDto = new ResponseDto().setStart();

			try (final CloseableHttpResponse response = (CloseableHttpResponse) getHttpClient()
					.execute(request.getHttpRequestBase(), context)) {
				responseDto.setEnd().setStatusCode(response.getStatusLine().getStatusCode());
				responseDto.setStatusText(response.getStatusLine().getReasonPhrase());
				responseDto.setResponseBody(EntityUtils.toString(response.getEntity()));
				responseDto.setUrl(request.getHttpRequestBase().getURI().toString());
				responseDto.setHeaders(response.getAllHeaders());
			}
			return responseDto;
		} catch (final Exception e) {
			TS.log().error(e);
			if (isIgnoreHttpError()) {
				TS.asserts().equals("Unexpeced Exception thrown from preformRequest in IHttpWrapper", "",
						e.getMessage());
			}
			return new ResponseDto(-1).setStatusText(e.toString()).setResponseBody(e.toString());
		}
	}

	public RequestConfig getRequestConfigDefault() {
		final RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(getCookieSpecs())
				.setExpectContinueEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
		return defaultRequestConfig;
	}

	public AbstractHttpWrapper setHttpClient() {

		final HttpClientBuilder hcb = HttpClients.custom();

		if (null != getProxy()) {
			hcb.setProxy(getProxy());
		}
		if (null != getRequestConfig()) {
			hcb.setDefaultRequestConfig(getRequestConfig());
		}
		if (null != getCredentialsProvider()) {
			hcb.setDefaultCredentialsProvider(getCredentialsProvider());
		}
		if (null != getCookieStore()) {
			hcb.setDefaultCredentialsProvider(getCredentialsProvider());
		}
		if (null != getConnectionManager()) {
			hcb.setConnectionManager(getConnectionManager());
		}
		if (trustAllCerts) {
			// hcb.setSSLHostnameVerifier(new NoopHostnameVerifier());
		}

		return setHttpClient(hcb.build());
	}

	public AbstractHttpWrapper setRequestConfig() {
		final Builder rcb = RequestConfig.custom();

		if (null != getDefaultConnectionTimeout()) {
			rcb.setSocketTimeout(getDefaultConnectionTimeout()).setConnectTimeout(getDefaultConnectionTimeout())
					.setConnectionRequestTimeout(getDefaultConnectionTimeout());
		}

		rcb.setCookieSpec(CookieSpecs.DEFAULT).setExpectContinueEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
		return setRequestConfig(rcb.build());
	}

	public CredentialsProvider getCredentialsProvider() {
		return new BasicCredentialsProvider();
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public CookieStore getDefaultCookieStore() {
		return new BasicCookieStore();
	}

	public AbstractHttpWrapper setResponseParserFactory() {
		/*
		 * responseParserFactory = new DefaultHttpResponseParserFactory() {
		 *
		 *
		 * public HttpMessageParser<HttpResponse> create(final
		 * SessionInputBuffer buffer, final MessageConstraints constraints) {
		 * final LineParser lineParser = new BasicLineParser() {
		 *
		 *
		 * public Header parseHeader(final CharArrayBuffer buffer) { try {
		 * return super.parseHeader(buffer); } catch (final ParseException ex) {
		 * return new BasicHeader(buffer.toString(), null); } }
		 *
		 * }; return new DefaultHttpResponseParser(buffer, lineParser,
		 * DefaultHttpResponseFactory.INSTANCE, constraints) {
		 *
		 *
		 * protected boolean reject(final CharArrayBuffer line, final int count)
		 * { return false; }
		 *
		 * }; }
		 *
		 * };
		 */
		return this;
	}

	public AbstractHttpWrapper setDnsResolver() {
		dnsResolver = new SystemDefaultDnsResolver() {

			public InetAddress[] resolve(final String host) throws UnknownHostException {
				if (host.equalsIgnoreCase("localhost")) {
					return new InetAddress[] { InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}

		};
		return this;
	}

	public AbstractHttpWrapper setRequestWriterFactory() {
		requestWriterFactory = new DefaultHttpRequestWriterFactory();
		return this;
	}

	public AbstractHttpWrapper setConnectionManagerPoolingAdvanced()
			throws NoSuchAlgorithmException, KeyStoreException {

		final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory);

		final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				setSocketFactoryRegistry().getSocketFactoryRegistry(), connFactory, dnsResolver);

		connManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute());
		connManager.setMaxTotal(getDefaultPoolSize());

		return setConnManager(connManager);
	};

	public AbstractHttpWrapper setSocketFactoryRegistry() {
		return setSocketFactoryRegistry(RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(getSslcontext())).build());
	}

	public AbstractHttpWrapper setSSLContextTrustAll() {
		try {
			/*
			 * final SSLHostnameVerifier sSLHostnameVerifier = new
			 * NoopHostnameVerifier();
			 * 
			 * final TrustStrategy ts = new TrustSelfSignedStrategy(); return
			 * setSslcontext(SSLContexts.custom().loadTrustMaterial(new
			 * TrustSelfSignedStrategy()).build());
			 */
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public AbstractHttpWrapper setSSLContext() {
		return setSslcontext(SSLContexts.createSystemDefault());
	}

	public AbstractHttpWrapper setConnectManagerDefaultPooling() {
		final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute());
		connManager.setMaxTotal(getDefaultPoolSize());
		return setConnManager(connManager);
	}

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

	public AbstractHttpWrapper setDefaultPoolSize(final int defaultPoolSize) {
		this.defaultPoolSize = defaultPoolSize;
		return this;
	}

	public AbstractHttpWrapper setBasicAuthCredentials(final String userName, final String password,
			final AuthScope authScope) {
		defaultCredentialsProvider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
		defaultCredentialsProvider.setCredentials(authScope, creds);
		return this;
	}

	public AbstractHttpWrapper setBasicAuthCredentials(final String userName, final String password) {
		return setBasicAuthCredentials(userName, password, AuthScope.ANY);
	}

	public AbstractHttpWrapper setAllowAnyCerts() {
		return this;
	};

	public Integer getDefaultConnectionTimeout() {
		return defaultConnectionTimeout;
	}

	public AbstractHttpWrapper setDefaultConnectionTimeout(final int defaultConnectionTimeout) {
		this.defaultConnectionTimeout = defaultConnectionTimeout;
		return this;
	}

	public CredentialsProvider getDefaultCredentialsProvider() {
		return defaultCredentialsProvider;
	}

	public AbstractHttpWrapper setDefaultCredentialsProvider(final CredentialsProvider defaultCredentialsProvider) {
		this.defaultCredentialsProvider = defaultCredentialsProvider;
		return this;
	}

	public ResponseDto getExpectedResponse() {
		return expectedResponse;
	}

	public AbstractHttpWrapper setExpectedResponse(final ResponseDto expectedResponse) {
		this.expectedResponse = expectedResponse;
		return this;
	}

	public AbstractHttpWrapper setExpectedResponse(final int statusCode) {
		this.expectedResponse = new ResponseDto(statusCode);
		return this;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public AbstractHttpWrapper setVerbose(final boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	public boolean isShareState() {
		return shareState;
	}

	public AbstractHttpWrapper setShareState(final boolean shareState) {
		this.shareState = shareState;
		return this;
	}

	public int getDefaultPoolSize() {
		return defaultPoolSize;
	}

	public AbstractHttpWrapper setCookiesFromBrowser(final Set<org.openqa.selenium.Cookie> browserCookies) {

		this.setShareState(false);
		if (null != browserCookies) {
			final Iterator<org.openqa.selenium.Cookie> iter = browserCookies.iterator();
			while (iter.hasNext()) {
				final org.openqa.selenium.Cookie c = iter.next();
				final BasicClientCookie basicClientCookie = new BasicClientCookie(c.getName(), c.getValue());
				basicClientCookie.setDomain(c.getDomain());
				basicClientCookie.setExpiryDate(c.getExpiry());
				basicClientCookie.setPath(c.getPath());
				this.getCookieStore().addCookie(basicClientCookie);
				TS.log().trace("Add cookie " + c.getName() + " = " + c.getValue());
			}
			setCookieStore(this.getCookieStore());
		}
		return this;
	}

	public AbstractHttpWrapper setCookieStore(final CookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}

	public PoolingHttpClientConnectionManager getConnManager() {
		return connManager;
	}

	public AbstractHttpWrapper setConnManager(final PoolingHttpClientConnectionManager connManager) {
		this.connManager = connManager;
		return this;
	}

	public int getDefaultMaxPerRoute() {
		return DefaultMaxPerRoute;
	}

	public String getCookieSpecs() {
		return cookieSpecs;
	}

	public AbstractHttpWrapper setCookieSpecs(final String cookieSpecs) {
		this.cookieSpecs = cookieSpecs;
		return this;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(final Header[] headers) {
		this.headers = headers;
	}

	public AbstractHttpWrapper setRequestConfig(final RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
		return this;
	}

	public HttpHost getProxy() {
		return proxy;
	}

	public AbstractHttpWrapper setProxy(final HttpHost proxy) {
		this.proxy = proxy;
		return this;
	}

	public AbstractHttpWrapper setProxy(final String host, final int port) {
		return setProxy(new HttpHost(host, port));
	}

	public Integer getDefaultTimeout() {
		return defaultTimeout;
	}

	public AbstractHttpWrapper setDefaultTimeout(final Integer defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
		return this;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public AbstractHttpWrapper setConnectionKeepAliveStrategy(
			final ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
		this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
		return this;
	}

	public AbstractHttpWrapper setHttpClient(final HttpClient httpClient) {
		this.httpClient = httpClient;
		return this;
	}

	public ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
		return connectionKeepAliveStrategy;
	}

	public HttpClient getHttpClient() {
		if (null == httpClient) {
			setHttpClient();
		}
		return httpClient;
	}

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

	public PoolingHttpClientConnectionManager getConnectionManager() {
		return connManager;
	}

	public SSLContext getSslcontext() {
		return sslcontext;
	}

	public AbstractHttpWrapper setSslcontext(final SSLContext sslcontext) {
		this.sslcontext = sslcontext;
		return this;
	}

	public Registry<ConnectionSocketFactory> getSocketFactoryRegistry() {
		return socketFactoryRegistry;
	}

	public AbstractHttpWrapper setSocketFactoryRegistry(final Registry<ConnectionSocketFactory> socketFactoryRegistry) {
		this.socketFactoryRegistry = socketFactoryRegistry;
		return this;
	}

	public HttpMessageParserFactory<HttpResponse> getResponseParserFactory() {
		return responseParserFactory;
	}

	public void setResponseParserFactory(final HttpMessageParserFactory<HttpResponse> responseParserFactory) {
		this.responseParserFactory = responseParserFactory;
	}

	public DnsResolver getDnsResolver() {
		return dnsResolver;
	}

	public void setDnsResolver(final DnsResolver dnsResolver) {
		this.dnsResolver = dnsResolver;
	}

	public HttpMessageWriterFactory<HttpRequest> getRequestWriterFactory() {
		return requestWriterFactory;
	}

	public void setRequestWriterFactory(final HttpMessageWriterFactory<HttpRequest> requestWriterFactory) {
		this.requestWriterFactory = requestWriterFactory;
	}

	public boolean isIgnoreHttpError() {
		return ignoreHttpError;
	}

	public void setIgnoreHttpError(final boolean ignoreHttpError) {
		this.ignoreHttpError = ignoreHttpError;
	}

	public boolean isTrustAllCerts() {
		return trustAllCerts;
	}

	public void setTrustAllCerts(final boolean trustAllCerts) {
		this.trustAllCerts = trustAllCerts;
	}

}
