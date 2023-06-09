package com.asinking.com.openapi.sdk.samples;

import com.asinking.com.openapi.sdk.entity.Result;
import com.asinking.com.openapi.sdk.okhttp.AKRestClientBuild;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessTokenDemo {

    /**
     * <p>
     *     这个demo之合适 参数是以query-param形式传参的，如果有body形式传参参考orderDemo
     *     示例中的 appId，appSecret需要替换成客户自己申请的appId，appSecret endpoint
     * </p>
     */
    public static void main(String[] args) throws Exception {
        String appId = "";
        // 如果用postman等其他工具调试时，需要将appSecret用urlencode.encode()进行转义
        String appSecret = "";
        Result result = AKRestClientBuild.builder().endpoint("").getAccessToken(appId, appSecret);
        log.info("token:{}", result);
    }

}
