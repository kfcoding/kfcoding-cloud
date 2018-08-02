package com.cuiyun.kfcoding.common.exception.auth;

import com.cuiyun.kfcoding.common.exception.KfCodingException;
import com.cuiyun.kfcoding.common.exception.ServiceExceptionEnum;

/**
 * @program: kfcoding-cloud
 * @description: 验证异常
 * @author: maple
 * @create: 2018-08-02 17:13
 **/
public class ClientInvalidException extends KfCodingException {
    public ClientInvalidException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum);
    }
}
