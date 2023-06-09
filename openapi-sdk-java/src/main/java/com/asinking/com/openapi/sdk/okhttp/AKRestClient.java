package com.asinking.com.openapi.sdk.okhttp;

import com.asinking.com.openapi.sdk.core.Config;
import com.asinking.com.openapi.sdk.core.HttpMethod;
import com.asinking.com.openapi.sdk.core.HttpRequest;
import com.asinking.com.openapi.sdk.core.HttpResponse;
import com.asinking.com.openapi.sdk.entity.Result;

import java.util.Map;

public class AKRestClient {

    private Config config;

    private String endpoint;

    public AKRestClient(String endpoint, Config config) {
        this.config = config;
        this.endpoint = endpoint;
    }

    public Result getAccessToken(String appId, String appSecret) throws Exception {
        HttpRequest<Result> build = HttpRequest.builder(Result.class)
                .config(this.config)
                .method(HttpMethod.POST)
                .endpoint(this.endpoint)
                .path("api/auth-server/oauth/access-token")
                .queryParam("appId", appId)
                .queryParam("appSecret", appSecret)
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        return execute.readEntity(Result.class);
    }

    public Object refreshToken(String appId, String refreshToken) throws Exception {
        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .config(this.config)
                .method(HttpMethod.POST)
                .endpoint(this.endpoint)
                .path("api/auth-server/oauth/refresh")
                .queryParam("appId", appId)
                .queryParam("refreshToken", refreshToken)
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        return execute.readEntity(Object.class);
    }

    public Object sign(Map<String, Object> params) throws Exception {
        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .config(this.config)
                .method(HttpMethod.POST)
                .endpoint(this.endpoint)
                .path("api/auth-server/oauth/api/authorize")
                .queryParams(params)
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        return execute.readEntity(Object.class);
    }
}
