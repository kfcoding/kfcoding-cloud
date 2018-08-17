package com.cuiyun.kfcoding.common.exception;

/**
 * @program: kfcoding
 * @description: 封装KfCoding的异常
 * @author: maple
 * @create: 2018-05-05 20:57
 **/
public class KfCodingException extends RuntimeException {

    private int status;
    private String message;

    public KfCodingException(ServiceExceptionEnum serviceExceptionEnum){
        this.status = serviceExceptionEnum.getStatus();
        this.message = serviceExceptionEnum.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
