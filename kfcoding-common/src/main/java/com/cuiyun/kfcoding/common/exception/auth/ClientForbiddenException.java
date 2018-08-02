package com.cuiyun.kfcoding.common.exception.auth;

import com.cuiyun.kfcoding.common.exception.KfCodingException;
import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * @program: kfcoding-cloud
 * @description: 拒绝访问异常
 * @author: maple
 * @create: 2018-08-02 16:27
 **/
public class ClientForbiddenException extends KfCodingException{

    public ClientForbiddenException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum);
    }
}
