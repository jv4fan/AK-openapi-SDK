package com.asinking.com.openapi.sdk.core;

import com.asinking.com.openapi.sdk.constant.ClientConstants;
import okhttp3.Headers;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponseImpl implements HttpResponse {

    private static final Logger LOG = LoggerFactory.getLogger(HttpResponseImpl.class);
    private final Response response;

    private HttpResponseImpl(Response response) {
        this.response = response;
    }

    /**
     * Wrap the given Response
     *
     * @param response the response
     * @return the HttpResponse
     */
    public static HttpResponseImpl wrap(Response response) {
        return new HttpResponseImpl(response);
    }


    /**
     * Gets the status from the previous Request
     *
     * @return the status code
     */
    public int getStatus() {
        return response.code();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatusMessage() {
        return response.message();
    }

    /**
     * @return the input stream
     */
    public InputStream getInputStream() {
        return Objects.requireNonNull(response.body()).byteStream();
    }

    /**
     * Returns a Header value from the specified name key
     *
     * @param name the name of the header to query for
     * @return the header as a String or null if not found
     */
    public String header(String name) {
        return response.header(name);
    }

    /**
     * @return the a Map of Header Name to Header Value
     */
    public Map<String, String> headers() {
        Map<String, String> retHeaders = new HashMap<>();
        Headers headers = response.headers();

        for (String name : headers.names()) {
            retHeaders.put(name, headers.get(name));
        }
        return retHeaders;
    }

    @Override
    public <T> T readEntity(Class<T> typeToReadAs) {
        try {
            return ObjectMapperSingleton.getContext(typeToReadAs).reader(typeToReadAs).readValue(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void close() {
        if (response != null) {
            Objects.requireNonNull(response.body()).close();
        }
    }

    @Override
    public String getContentType() {
        return header(ClientConstants.HEADER_CONTENT_TYPE);
    }
}
