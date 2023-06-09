package com.asinking.com.openapi.sdk.okhttp;

import com.asinking.com.openapi.sdk.constant.ClientConstants;
import com.asinking.com.openapi.sdk.core.Config;
import com.asinking.com.openapi.sdk.core.HttpMethod;
import com.asinking.com.openapi.sdk.core.HttpRequest;
import com.asinking.com.openapi.sdk.core.ObjectMapperSingleton;
import com.asinking.com.openapi.sdk.logger.HttpLoggingFilter;
import com.google.common.io.ByteStreams;
import okhttp3.*;
import okhttp3.internal.Util;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class HttpCommand<R> {

    private static final Logger logger = LoggerFactory.getLogger(HttpCommand.class);

    private final HttpRequest<R> request;
    private OkHttpClient client;
    private Request.Builder clientReq;
    private int retries;

    private HttpCommand(HttpRequest<R> request) {
        this.request = request;
    }

    /**
     * Creates a new HttpCommand from the given request
     *
     * @param request the request
     * @return the command
     */
    public static <R> HttpCommand<R> create(HttpRequest<R> request) {
        HttpCommand<R> command = new HttpCommand<R>(request);
        command.initialize();
        return command;
    }

    private void initialize() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        Config config = request.getConfig();

        if (config.getConnectTimeout() > 0)
            okHttpClientBuilder.connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS);

        if (config.getReadTimeout() > 0)
            okHttpClientBuilder.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);

        if (HttpLoggingFilter.isLoggingEnabled()) {
            okHttpClientBuilder.addInterceptor(new LoggingInterceptor());
        }
        client = okHttpClientBuilder.build();
        clientReq = new Request.Builder();
        populateHeaders(request);
        populateQueryParams(request);
    }

    /**
     * Executes the command and returns the Response
     *
     * @return the response
     * @throws Exception
     */
    public Response execute() throws Exception {
        RequestBody body = null;
        if (request.getEntity() != null) {
            if (InputStream.class.isAssignableFrom(request.getEntity().getClass())) {
                byte[] content = ByteStreams.toByteArray((InputStream) request.getEntity());
                body = RequestBody.Companion.create(content, MediaType.parse(request.getContentType()));
            } else {
                String content = ObjectMapperSingleton.getContext(request.getEntity().getClass()).writer().writeValueAsString(request.getEntity());
                body = RequestBody.Companion.create(content, MediaType.parse(request.getContentType()));
            }
        } else if (request.hasJson()) {
            body = RequestBody.Companion.create(request.getJson(), MediaType.parse(ClientConstants.CONTENT_TYPE_JSON));
        }
        //Added to address https://github.com/square/okhttp/issues/751
        //Set body as empty byte array if request is POST or PUT and body is sent as null
        if ((request.getMethod() == HttpMethod.POST || request.getMethod() == HttpMethod.PUT) && body == null) {
            body = RequestBody.Companion.create(Util.EMPTY_BYTE_ARRAY, null);
        }
        clientReq.method(request.getMethod().name(), body);
        Call call = client.newCall(clientReq.build());
        return call.execute();
    }

    /**
     * @return true if a request entity has been set
     */
    public boolean hasEntity() {
        return request.getEntity() != null;
    }

    /**
     * @return current retry execution count for this command
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @return incremement's the retry count and returns self
     */
    public HttpCommand<R> incrementRetriesAndReturn() {
        initialize();
        retries++;
        return this;
    }

    public HttpRequest<R> getRequest() {
        return request;
    }

    private void populateQueryParams(HttpRequest<R> request) {
        StringBuilder url = new StringBuilder();
        url.append(request.getEndpoint()).append("/").append(request.getPath());
        if (!request.hasQueryParams()) {
            clientReq.url(url.toString());
            return;
        }
        url.append("?");
        for (Map.Entry<String, String> entry : request.getQueryParams().entrySet()) {
            try {
                url.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                url.append("&");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (url.charAt(url.length() - 1) == '&') {
            url.deleteCharAt(url.length() - 1);
        }
        clientReq.url(url.toString());
    }

    private void populateHeaders(HttpRequest<R> request) {

        if (!request.hasHeaders()) return;

        for (Map.Entry<String, Object> h : request.getHeaders().entrySet()) {
            clientReq.addHeader(h.getKey(), String.valueOf(h.getValue()));
        }
    }

    static class LoggingInterceptor implements Interceptor {
        @NotNull
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            logger.info("Sending request url:{} ,connection:{},headers:{}", request.url(), chain.connection(), request.headers());
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            logger.info("Received response url:{} ,cost:{},headers:{}", response.request().url(), (t2 - t1) / 1e6d, response.headers());
            return response;
        }
    }
}
