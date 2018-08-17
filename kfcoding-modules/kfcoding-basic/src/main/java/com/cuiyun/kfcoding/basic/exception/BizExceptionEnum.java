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
    USER_UPDATE_ERROR(20002, "用户修改失败"),

    /**
     * github模块异常
     */
    GITHUB_CANCAL_OAUTH(21001, "取消授权(code为空)"),
    GITHUB_ERROR_URL(21002, "url获取失败"),

    /**
     * book模块异常
     */
    BOOK_ERROR(22001, "没有课程"),
    BOOK_CREAT_ERROR(22002, "创建课程失败"),
    BOOK_UPDATE(22002, "课程修改失败"),
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

    public void setStatus(Integer code) {
        this.status = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
