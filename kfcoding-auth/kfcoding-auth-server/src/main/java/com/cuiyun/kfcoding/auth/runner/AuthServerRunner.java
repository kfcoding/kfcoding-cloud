package com.cuiyun.kfcoding.auth.runner;

import com.cuiyun.kfcoding.auth.common.util.jwt.RsaKeyHelper;
import com.cuiyun.kfcoding.auth.config.KeyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: 启动加载配置
 * @author: maple
 * @create: 2018-08-06 18:34
 **/
@Configuration
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_USER_PRI_KEY = "KF:AUTH:JWT:PRI";
    private static final String REDIS_USER_PUB_KEY = "KF:AUTH:JWT:PRI";
    private static final String REDIS_SERVICE_PRI_KEY = "KF:AUTH:CLIENT:PRI";
    private static final String REDIS_SERVICE_PUB_KEY = "KF:AUTH:CLIENT:PUB";

    @Autowired
    private KeyConfig keyConfig;

    @Override
    public void run(String... args) throws IOException, NoSuchAlgorithmException {
        if (redisTemplate.hasKey(REDIS_USER_PRI_KEY) && redisTemplate.hasKey(REDIS_USER_PUB_KEY) && redisTemplate.hasKey(REDIS_SERVICE_PRI_KEY) && redisTemplate.hasKey(REDIS_SERVICE_PUB_KEY)) {
            keyConfig.setUserPriKey(RsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_USER_PRI_KEY).toString()));
            keyConfig.setUserPubKey(RsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_USER_PUB_KEY).toString()));
            keyConfig.setServicePriKey(RsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_SERVICE_PRI_KEY).toString()));
            keyConfig.setServicePubKey(RsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_SERVICE_PUB_KEY).toString()));
        } else {
            Map<String, byte[]> keyMap = RsaKeyHelper.generateKey(keyConfig.getUserSecret());
            keyConfig.setUserPriKey(keyMap.get("pri"));
            keyConfig.setUserPubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(REDIS_USER_PRI_KEY, RsaKeyHelper.toHexString(keyMap.get("pri")));
            redisTemplate.opsForValue().set(REDIS_USER_PUB_KEY, RsaKeyHelper.toHexString(keyMap.get("pub")));
            keyMap = RsaKeyHelper.generateKey(keyConfig.getServiceSecret());
            keyConfig.setServicePriKey(keyMap.get("pri"));
            keyConfig.setServicePubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(REDIS_SERVICE_PRI_KEY, RsaKeyHelper.toHexString(keyMap.get("pri")));
            redisTemplate.opsForValue().set(REDIS_SERVICE_PUB_KEY, RsaKeyHelper.toHexString(keyMap.get("pub")));

        }
    }
}
