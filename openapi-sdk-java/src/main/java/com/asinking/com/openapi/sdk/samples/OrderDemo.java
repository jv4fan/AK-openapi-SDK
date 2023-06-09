package com.asinking.com.openapi.sdk.samples;

import com.alibaba.fastjson.JSON;
import com.asinking.com.openapi.sdk.core.Config;
import com.asinking.com.openapi.sdk.core.HttpMethod;
import com.asinking.com.openapi.sdk.core.HttpRequest;
import com.asinking.com.openapi.sdk.core.HttpResponse;
import com.asinking.com.openapi.sdk.entity.Result;
import com.asinking.com.openapi.sdk.okhttp.AKRestClientBuild;
import com.asinking.com.openapi.sdk.okhttp.HttpExecutor;
import com.asinking.com.openapi.sdk.sign.AesUtil;
import com.asinking.com.openapi.sdk.sign.ApiSign;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OrderDemo {

    public static void main(String[] args) throws Exception {
        String appId = "xx";

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("timestamp", System.currentTimeMillis() / 1000 + "");
        queryParam.put("access_token", "805181e7-4365-468c-bf5c-8c0762a1d358");
        queryParam.put("app_key", appId);

        Map<String, Object> body = new HashMap<>();
        body.put("sid", 1);
        body.put("start_date", "2021-07-03");
        body.put("end_date", "2021-08-04");

        Map<String, Object> signMap = new HashMap<>();
        signMap.putAll(queryParam);
        signMap.putAll(body);

        String sign = ApiSign.sign(signMap, appId);
        queryParam.put("sign", sign);
        log.info("sign:{}", sign);

        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .method(HttpMethod.POST)
                .endpoint("xxx")
                .path("erp/sc/data/mws_report/allOrders")
                .queryParams(queryParam)
                .json(JSON.toJSONString(body))
                .config(Config.DEFAULT.withConnectionTimeout(30000).withReadTimeout(30000))
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        log.info("execute:{}", execute.readEntity(Object.class));
    }

}
