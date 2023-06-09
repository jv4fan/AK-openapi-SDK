package com.asinking.com.openapi.sdk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Token {

    private String accessToken;
    private String refreshToken;
    private String expiresIn;

    public Token() {
        log.info("午餐狗杂函数");
        this.accessToken = "hxh";
        this.refreshToken = "lzh";
        this.expiresIn = "11";
    }

    public Token(boolean inner) {
        log.info("有参构造函数");
        if(inner){
            this.accessToken = "hxh";
            this.refreshToken = "lzh";
            this.expiresIn = "11";
        }
    }

    public static void main(String[] args) {
        Token token = new Token(false);
        System.out.println(token.toString());
    }
}
