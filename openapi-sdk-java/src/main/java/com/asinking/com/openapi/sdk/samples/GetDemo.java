package com.asinking.com.openapi.sdk.samples;

import com.alibaba.fastjson.JSON;
import com.asinking.com.openapi.sdk.core.HttpMethod;
import com.asinking.com.openapi.sdk.core.HttpRequest;
import com.asinking.com.openapi.sdk.core.HttpResponse;
import com.asinking.com.openapi.sdk.okhttp.HttpExecutor;
import com.asinking.com.openapi.sdk.sign.ApiSign;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GetDemo {

    public static void main(String[] args) throws Exception {
        String appId = "ak_boWbe9uTtCURT";

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("timestamp", 1639734344);
        queryParam.put("access_token", "a4d671b9-778b-487d-b169-fda4806826a1");
        queryParam.put("app_key", appId);
//        queryParam.put("offset", 0);
//        queryParam.put("length", 20);

        String sign = ApiSign.sign(queryParam, appId);
        queryParam.put("sign", sign);
        log.info("sign:{}", sign);

        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .method(HttpMethod.GET)
                .endpoint("xxxx")
                .path("erp/sc/data/local_inventory/brand")
                .queryParams(queryParam)
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        log.info("execute:{}", execute.readEntity(Object.class));
    }
}
