package org.testah.driver.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.testah.TS;
import org.testah.driver.http.requests.AbstractRequestDto;
import org.testah.driver.http.response.ResponseDto;
import java.io.Closeable;
import java.util.concurrent.Future;

/**
 * The Class HttpAsynchWrapperV1.
 */
public class HttpAsynchWrapperV1 extends AbstractHttpWrapper implements Closeable {

    /**
     * The http asynch client.
     */
    private CloseableHttpAsyncClient httpAsynchClient;

    /**
     * Sets the http asynch client.
     *
     * @return the abstract http wrapper
     */
    public AbstractHttpWrapper setHttpAsynchClient() {
        final HttpAsyncClientBuilder hcb = HttpAsyncClients.custom();
        if (null != getProxy()) {
            hcb.setProxy(getProxy());
        }
        if (null != getRequestConfig()) {
            hcb.setDefaultRequestConfig(getRequestConfig());
        }
        if (null != getCookieStore()) {
            hcb.setDefaultCookieStore(getCookieStore());
        }
        try {
            if (null != getConnectionManager()) {
                final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
                final PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
                        ioReactor);
                connManager.setMaxTotal(100);
                hcb.setConnectionManager(connManager);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        hcb.setMaxConnPerRoute(getDefaultMaxPerRoute());
        hcb.setMaxConnTotal(getDefaultPoolSize());
        if (isTrustAllCerts()) {
            // hcb.setSSLHostnameVerifier(new NoopHostnameVerifier());
        }
        return setHttpAsyncClient(hcb.build());
    }

    /**
     * Do request asynch.
     *
     * @param request the request
     * @param verbose the verbose
     * @return the future
     */
    public Future<HttpResponse> doRequestAsynch(final AbstractRequestDto<?> request, final boolean verbose) {
        try {
            final HttpClientContext context = HttpClientContext.create();
            if (null != getCookieStore()) {
                context.setCookieStore(getCookieStore());
                context.setRequestConfig(getRequestConfig());
            }

            if (null != request.getCredentialsProvider()) {
                context.setCredentialsProvider(request.getCredentialsProvider());
            }

            new ResponseDto().setStart();
            if (verbose) {
                TS.step().action().add(request.createRequestInfoStep());
            }
            try {
                getHttpAsyncClient().start();
                final Future<HttpResponse> future = getHttpAsyncClient().execute(request.getHttpRequestBase(), context,
                        new FutureCallback<HttpResponse>() {

                            public void completed(final HttpResponse response) {
                                if (verbose) {
                                    final ResponseDto responseDto = getResponseDto(response, request);
                                    TS.step().action().add(responseDto.createResponseInfoStep(
                                            request.isTruncateResponseBodyInReport(), true,
                                            request.getTruncateResponseBodyInReportBy()));
                                }
                            }

                            public void failed(final Exception ex) {
                                if (verbose) {
                                    TS.step().action().createInfo(
                                            "ERROR - Issue with request " + request.getHttpRequestBase().getRequestLine(),
                                            ex.getMessage());
                                }
                            }

                            public void cancelled() {
                                if (verbose) {
                                    TS.step().action().createInfo("Canceled Request",
                                            request.getHttpRequestBase().getRequestLine().toString());
                                }
                            }
                        });
                return future;
            } finally {
                // need to complete try block, maybe move declaration of Future<HttpResponse> future = null out of try block
                // and return future here.
            }
        } catch (final Exception e) {
            TS.log().error(e);
            if (!isIgnoreHttpError()) {
                TS.asserts().equalsTo("Unexpected Exception thrown from preformRequest in IHttpWrapper", "",
                        e.getMessage());
            }
            return null;
        }
    }

    /**
     * Sets the http async client.
     *
     * @param httpAsynchClient the http asynch client
     * @return the http asynch wrapper v1
     */
    public HttpAsynchWrapperV1 setHttpAsyncClient(final CloseableHttpAsyncClient httpAsynchClient) {
        this.httpAsynchClient = httpAsynchClient;
        return this;
    }

    /**
     * Gets the http async client.
     *
     * @return the http async client
     */
    public CloseableHttpAsyncClient getHttpAsyncClient() {
        if (null == httpAsynchClient) {
            setHttpAsynchClient();
        }
        return httpAsynchClient;
    }

    /**
     * Gets the response dto from future.
     *
     * @param response the response
     * @return the response dto from future
     * @throws Exception the exception
     */
    public ResponseDto getResponseDtoFromFuture(final Future<HttpResponse> response) throws Exception {
        return getResponseDtoFromFuture(response, null);
    }

    /**
     * Gets the response dto from future.
     *
     * @param response the response
     * @param request  the request
     * @return the response dto from future
     * @throws Exception the exception
     */
    public ResponseDto getResponseDtoFromFuture(final Future<HttpResponse> response, final AbstractRequestDto<?> request)
            throws Exception {
        if (null != response) {
            try {
                TS.log().debug("Getting response from future, current done state is: " + response.isDone()
                        + " will block until done.");
                final HttpResponse responseFromFuture = response.get();
                return getResponseDto(responseFromFuture, request);
            } catch (final Exception e) {
                TS.log().debug(e);
            }
        }
        return null;
    }

    /**
     * Close the async HTTP client.
     *
     * @see java.io.Closeable#close()
     */
    public void close() {
        try {
            getHttpAsyncClient().close();
        } catch (final Exception e) {
            TS.log().warn(e);
        }
    }

    protected AbstractHttpWrapper getSelf() {
        return this;
    }

}
