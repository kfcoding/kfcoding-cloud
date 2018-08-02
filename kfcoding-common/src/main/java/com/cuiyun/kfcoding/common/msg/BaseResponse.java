package com.cuiyun.kfcoding.common.msg;

/**
 * @program: kfcoding-cloud
 * @description: response基础类
 * @author: maple
 * @create: 2018-08-02 17:13
 **/
public class BaseResponse {
    private int status = 200;
    private String message;

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
