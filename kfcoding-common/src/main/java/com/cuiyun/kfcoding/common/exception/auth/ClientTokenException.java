package com.cuiyun.kfcoding.common.exception.auth;


import com.cuiyun.kfcoding.common.exception.KfCodingException;
import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * Created by ace on 2017/9/10.
 */
public class ClientTokenException extends KfCodingException {
    public ClientTokenException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum);
    }
}
