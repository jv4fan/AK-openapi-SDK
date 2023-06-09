package com.asinking.com.openapi.sdk.samples;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asinking.com.openapi.sdk.core.HttpMethod;
import com.asinking.com.openapi.sdk.core.HttpRequest;
import com.asinking.com.openapi.sdk.core.HttpResponse;
import com.asinking.com.openapi.sdk.okhttp.HttpExecutor;
import com.asinking.com.openapi.sdk.sign.ApiSign;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SetCategoryDemo {
    public static void main(String[] args) throws Exception {
        String appId = "xxx";
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("timestamp", System.currentTimeMillis() / 1000 + "");
        queryParam.put("app_key", appId);
        queryParam.put("access_token", "0a482d2d-8280-48da-b69b-8f7c7d5c3c21");
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> bodyParam = new ArrayList<>();
        Map<String, String> body_content = new HashMap<>();
        body_content.put("title","Weaving Loom 织机产品线");
        bodyParam.add(body_content);
        body.put("data", JSONObject.toJSONString(bodyParam));
        Map<String, Object> signMap = new HashMap<>();
        signMap.putAll(queryParam);
        signMap.putAll(body);
        body.put("data", bodyParam);
        String sign = ApiSign.sign(signMap, appId);
        queryParam.put("sign", sign);
        log.info("sign:{}", sign);
        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .method(HttpMethod.POST)
                .endpoint("xxx")
                .path("erp/sc/routing/storage/category/set")
                .queryParams(queryParam)
                .json(JSON.toJSONString(body))
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        log.info("execute:{}", execute.readEntity(Object.class));
    }
}
