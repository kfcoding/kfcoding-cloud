package com.cuiyun.kfcoding.auth.client.jwt;

import com.cuiyun.kfcoding.auth.client.config.UserAuthConfig;
import com.cuiyun.kfcoding.auth.client.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.auth.common.util.jwt.IJWTInfo;
import com.cuiyun.kfcoding.auth.common.util.jwt.JWTHelper;
import com.cuiyun.kfcoding.common.exception.auth.UserTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: 用户鉴权兰姐类
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
@Configuration
public class UserAuthUtil {
    @Autowired
    private UserAuthConfig userAuthConfig;
    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, userAuthConfig.getPubKeyByte());
        }catch (ExpiredJwtException ex){
            throw new UserTokenException(BizExceptionEnum.TOKEN_EXPIRED);
        }catch (SignatureException ex){
            throw new UserTokenException(BizExceptionEnum.TOKEN_SIGNATURE);
        }catch (IllegalArgumentException ex){
            throw new UserTokenException(BizExceptionEnum.TOKEN_EMPTY);
        }
    }
}
