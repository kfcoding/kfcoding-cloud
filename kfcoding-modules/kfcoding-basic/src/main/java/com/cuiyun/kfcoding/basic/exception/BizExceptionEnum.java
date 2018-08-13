package com.cuiyun.kfcoding.basic.exception;

import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * @program: kfcoding-cloud
 * @description: 异常枚举
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
public enum BizExceptionEnum implements ServiceExceptionEnum{
    /**
     * user模块异常
     */
    USER_NULL(20001, "用户不存在"),
    USER_EXIST(20001, "用户账号已存在"),
    USER_INSERT(20001, "用户添加失败"),
    USER_EMAIL_NULL(20002, "用户邮箱不存在"),
    USER_EMAIL_VALIDATE(20002, "用户邮箱格式不正确"),
    USER_PASSWORD_NULL(20002, "用户密码不存在"),

    /**
     * github模块异常
     */
    GITHUB_CANCAL_OAUTH(21001, "取消授权(code为空)"),
    GITHUB_ERROR_URL(21002, "url获取失败"),
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
