package com.asinking.com.openapi.sdk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {

    /**
     * 自定义业务码
     */
    private String code;

    /**
     * 自定义业务提示说明
     */
    private String msg;

    /**
     * 自定义返回 数据结果集
     */
    private T data;

    @Override
    public String toString() {
        return "Result{" +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
