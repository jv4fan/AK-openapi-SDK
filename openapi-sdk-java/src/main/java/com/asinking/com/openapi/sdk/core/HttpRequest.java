package com.asinking.com.openapi.sdk.core;

import com.asinking.com.openapi.sdk.constant.ClientConstants;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest<R> {

    String region;
    String endpoint;
    String path;
    Class<R> returnType;
    Object entity;
    String contentType = ClientConstants.CONTENT_TYPE_JSON;
    HttpMethod method = HttpMethod.GET;
    String json;
    private Config config;
    private Map<String, String> queryParams;
    private final Map<String, Object> headers = new HashMap<>();

    public HttpRequest() {
    }

    /**
     * Creates a new HttpRequest
     *
     * @param endpoint the endpoint URI
     * @param path     the path which will be appended to the endpoint URI
     * @param method   the method the method type to invoke
     */
    public HttpRequest(String endpoint, String path, HttpMethod method) {
        this.endpoint = endpoint;
        this.path = path;
        this.method = method;
    }

    /**
     * A build for creating HttpRequest objects
     *
     * @return the request builder
     */
    public static RequestBuilder<Void> builder() {
        return new RequestBuilder<>(Void.class);
    }

    /**
     * A build for creating HttpRequest objects
     *
     * @param <R>        the expected return type
     * @param returnType the return type
     * @return the request builder
     */
    public static <R> RequestBuilder<R> builder(Class<R> returnType) {
        return new RequestBuilder<>(returnType);
    }

    /**
     * @return the method this request will use
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @return the content type for the request
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return the endpoint URI
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @return the http path
     */
    public String getPath() {
        return path;
    }

    /**
     * If JSON is explicitly set vs an entity then this method will return a JSON String otherwise Empty
     *
     * @return JSON String form or Empty
     */
    public String getJson() {
        return (json == null) ? "" : json;
    }

    /**
     * @return true, if a JSON Object has been set
     */
    public boolean hasJson() {
        return (json != null);
    }

    /**
     * @return the return type expected after invocation
     */
    public Class<R> getReturnType() {
        return returnType;
    }

    /**
     * @return the entity to post
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * @return true, if query params have been added
     */
    public boolean hasQueryParams() {
        return queryParams != null && !queryParams.isEmpty();
    }

    /**
     * @return the request query params
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * @return the headers to apply
     */
    public Map<String, Object> getHeaders() {
        return headers;
    }

    /**
     * @return true, if headers have been added
     */
    public boolean hasHeaders() {
        return !headers.isEmpty();
    }

    public RequestBuilder<R> toBuilder() {
        return new RequestBuilder<>(this);
    }

    /**
     * @return the client configuration associated with this request
     */
    public Config getConfig() {
        return config != null ? config : Config.DEFAULT;
    }

    public String getRegion() {
        return region;
    }

    public static final class RequestBuilder<R> {

        HttpRequest<R> request;

        public RequestBuilder(HttpRequest<R> request) {
            this.request = request;
        }

        public RequestBuilder(Class<R> returnType) {
            request = new HttpRequest<>();
            request.returnType = returnType;
        }

        /**
         * @see HttpRequest#getEndpoint()
         */
        public RequestBuilder<R> endpoint(String endpoint) {
            request.endpoint = endpoint;
            return this;
        }

        /**
         * @see HttpRequest#getPath()
         */
        public RequestBuilder<R> path(String path) {
            request.path = path;
            return this;
        }

        /**
         * @see HttpRequest#getMethod()
         */
        public RequestBuilder<R> method(HttpMethod method) {
            request.method = method;
            return this;
        }

        /**
         * Flags the request method as PUT
         *
         * @return the request builder
         */
        public RequestBuilder<R> methodPut() {
            request.method = HttpMethod.PUT;
            return this;
        }

        /**
         * Flags the request method as GET
         *
         * @return the request builder
         */
        public RequestBuilder<R> methodGet() {
            request.method = HttpMethod.GET;
            return this;
        }

        /**
         * Flags the request method as DELETE
         *
         * @return the request builder
         */
        public RequestBuilder<R> methodDelete() {
            request.method = HttpMethod.DELETE;
            return this;
        }

        /**
         * Flags the request method as POST
         *
         * @return the request builder
         */
        public RequestBuilder<R> methodPost() {
            request.method = HttpMethod.POST;
            return this;
        }

        /**
         * @see HttpRequest#getEntity()
         */
        public RequestBuilder<R> entity(Object entity) {
            request.entity = entity;
            return this;
        }

        /**
         * Sets a client configuration to use with this session
         */
        public RequestBuilder<R> config(Config config) {
            request.config = config;
            return this;
        }

        /**
         * Pushes the Map of Headers into the existing headers for this request
         *
         * @param headers the headers to append
         * @return the request builder
         */
        public RequestBuilder<R> headers(Map<String, ? extends Object> headers) {
            request.getHeaders().putAll(headers);
            return this;
        }

        /**
         * Adds a new Header to the request
         *
         * @param name  the header name
         * @param value the header value
         * @return the request builder
         */
        public RequestBuilder<R> header(String name, Object value) {
            request.getHeaders().put(name, value);
            return this;
        }

        public RequestBuilder<R> queryParams(Map<String, Object> params) {
            params.forEach(this::queryParam);
            return this;
        }

        /**
         * Adds a Key/Value based Query Param
         *
         * @param key   the key
         * @param value the value
         * @return the request builder
         */
        public RequestBuilder<R> queryParam(String key, Object value) {
            if (value == null)
                return this;
            if (request.queryParams == null) {
                request.queryParams = new HashMap<>();
            }
            request.queryParams.put(key, String.valueOf(value));
            return this;
        }

        /**
         * AdHoc JSON object to Post/Put
         *
         * @param json the JSON object in String form
         * @return the request builder
         */
        public RequestBuilder<R> json(String json) {
            request.json = json;
            return this;
        }

        /**
         * Overrides the default content type for the request
         *
         * @param contentType the content type to use in the request
         * @return the request builder
         */
        public RequestBuilder<R> contentType(String contentType) {
            if (contentType != null)
                request.contentType = contentType;
            return this;
        }

        /**
         * Builds the HttpRequest
         *
         * @return HttpRequest
         */
        public HttpRequest<R> build() {
            return request;
        }

    }
}
