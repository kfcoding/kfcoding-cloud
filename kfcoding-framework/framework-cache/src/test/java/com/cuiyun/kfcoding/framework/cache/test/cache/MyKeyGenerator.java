package com.cuiyun.kfcoding.framework.cache.test.cache;

import com.cuiyun.kfcoding.framework.cache.constants.CacheScope;
import com.cuiyun.kfcoding.framework.cache.parser.IKeyGenerator;
import com.cuiyun.kfcoding.framework.cache.parser.IUserKeyGenerator;

/**
 * ${DESCRIPTION}
 *
 * @author bifenglin
 * @create 2018-05-22 14:05
 */
public class MyKeyGenerator extends IKeyGenerator {
    @Override
    public IUserKeyGenerator getUserKeyGenerator() {
        return null;
    }

    @Override
    public String buildKey(String key, CacheScope scope, Class<?>[] parameterTypes, Object[] arguments) {
        return "myKey_"+arguments[0];
    }
}
