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

    BizExceptionEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private Integer status;

    private String message;

    @Override
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
