package com.cuiyun.kfcoding.common.exception.auth;


import com.cuiyun.kfcoding.common.exception.KfCodingException;
import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * Created by bifenglin on 2017/9/8.
 */
public class UserTokenException extends KfCodingException {
    public UserTokenException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum);
    }
}
