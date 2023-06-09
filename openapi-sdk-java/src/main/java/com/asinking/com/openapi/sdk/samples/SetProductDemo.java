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
public class SetProductDemo {
    public static void main(String[] args) throws Exception {
        String appId = "xxx";
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("timestamp", System.currentTimeMillis() / 1000 + "");
        queryParam.put("access_token", "e076103f-86a7-42de-808f-58e051a5890e");
        queryParam.put("app_key", appId);
        Map<String, Object> body = new HashMap<>();
        body.put("bg_customs_export_name", "铣刀");
        body.put("bg_customs_import_name", "Milling cutter");
        body.put("cg_product_length", 10);
        body.put("bg_customs_import_price", 0.7);
        body.put("product_name", "【检】1/2刀柄*1刀刃圆底刀银色");
        body.put("cg_product_net_weight", 70);
        body.put("cg_product_height", 3);
        body.put("cg_product_width", 3);
        body.put("currency", "USD");
        body.put("sku", "HA1776H");
        body.put("pic_url", "http://120.238.246.137:3937/fastDFS/group1/M00/3E/9B/wKgB21yR_SWAX66uAAG8F2ty7CU9384.png");
        body.put("category", "13.2.2.1.3.3 立铣刀");
        body.put("cg_price", "4.90");
        body.put("status", 1);
        Map<String, Object> signMap = new HashMap<>();
        signMap.putAll(queryParam);
        signMap.putAll(body);
        String sign = ApiSign.sign(signMap, appId);
        queryParam.put("sign", sign);
        log.info("sign:{}", sign);
        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .method(HttpMethod.POST)
                .endpoint("xxxxx")
                .path("erp/sc/storage/product/set")
                .queryParams(queryParam)
                .json(JSON.toJSONString(body))
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        log.info("execute:{}", execute.readEntity(Object.class));
    }
}
