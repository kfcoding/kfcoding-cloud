package com.cuiyun.kfcoding.common.exception;

/**
 * @program: kfcoding
 * @description: KfCoding异常枚举
 * @author: maple
 * @create: 2018-05-05 21:05
 **/
public enum KfCodingExceptionEnum implements ServiceExceptionEnum {
    /**
     * 文件上传
     */
    FILE_READING_ERROR(400,"FILE_READING_ERROR!"),
    FILE_NOT_FOUND(400,"FILE_NOT_FOUND!"),

    /**
     * 错误的请求
     */
    REQUEST_NULL(400, "请求有错误"),
    SERVER_ERROR(500, "服务器异常");

    KfCodingExceptionEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private Integer status;

    private String message;

    @Override
    public Integer getStatus() {
        return status;
    }

    public void setCode(Integer code) {
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
