package com.cuiyun.kfcoding.auth.util.user;

import com.cuiyun.kfcoding.auth.common.util.jwt.IJWTInfo;
import com.cuiyun.kfcoding.auth.common.util.jwt.JWTHelper;
import com.cuiyun.kfcoding.auth.config.KeyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by bifenglin on 2017/9/10.
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.expire}")
    private int expire;
    @Autowired
    private KeyConfig keyConfig;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo, keyConfig.getUserPriKey(),expire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token, keyConfig.getUserPubKey());
    }
}
