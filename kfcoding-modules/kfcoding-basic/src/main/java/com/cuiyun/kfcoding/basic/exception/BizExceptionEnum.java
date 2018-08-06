package com.cuiyun.kfcoding.basic.exception;

import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * @program: kfcoding-cloud
 * @description: 异常枚举
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
public enum BizExceptionEnum implements ServiceExceptionEnum{
    USER_NULL(20001, "用户不存在");

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
