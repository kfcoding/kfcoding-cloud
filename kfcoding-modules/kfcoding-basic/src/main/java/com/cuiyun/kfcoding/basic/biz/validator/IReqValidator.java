package com.cuiyun.kfcoding.basic.biz.validator;


import com.cuiyun.kfcoding.api.vo.authority.validator.Credence;
import com.cuiyun.kfcoding.basic.model.User;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * <p>验证请求/auth接口时,请求参数的正确性</p>
 * <p>
 * <p>如果想拓展验证方法只需实现这个接口,然后在AuthenticationRestController类中注意相应实现的本接口的类即可</p>
 *
 * @author maple
 * @date 2018-08-3 11:48
 */
public interface IReqValidator {

    /**
     * 通过请求参数验证
     * @Retuen userId
     * @author maple
     * @Date 2018/8/23 11:49
     */
    User validate(Credence credence) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException;
}
