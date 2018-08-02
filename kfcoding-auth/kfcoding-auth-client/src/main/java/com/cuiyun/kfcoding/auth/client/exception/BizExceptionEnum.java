package com.cuiyun.kfcoding.auth.client.exception;

import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * @program: kfcoding-cloud
 * @description: 异常枚举
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
public enum BizExceptionEnum implements ServiceExceptionEnum{
    CLIENT_FORBIDDEN(10000, "Client is Forbidden!"),
    CLIENT_INVALID(10000, "Client not found or Client secret is error!"),
    TOKEN_EXPIRED(10001, "Client token expired!"),
    TOKEN_SIGNATURE(10001, "Client token signature error!"),
    TOKEN_EMPTY(10001, "Client token is null or empty!"),

    ;

    BizExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
