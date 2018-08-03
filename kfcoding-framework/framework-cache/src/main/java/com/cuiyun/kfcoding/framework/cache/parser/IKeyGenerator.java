package com.cuiyun.kfcoding.framework.cache.parser;

import com.cuiyun.kfcoding.framework.cache.constants.CacheScope;

/**
 * 缓存键值表达式
 *
 * @author bifenglin
 * @description
 * @date 2018-8-3
 * @since 1.7
 */
public abstract class IKeyGenerator {
    public static final String LINK = "_";

    /**
     * 获取动态key
     *
     * @param key
     * @param scope
     * @param parameterTypes
     * @param arguments
     * @return
     */
    public String getKey(String key, CacheScope scope,
                         Class<?>[] parameterTypes, Object[] arguments) {
        StringBuffer sb = new StringBuffer("");
        key = buildKey(key, scope, parameterTypes, arguments);
        sb.append(key);
        if (CacheScope.user.equals(scope)) {
            if (getUserKeyGenerator() != null)
                sb.append(LINK)
                        .append(getUserKeyGenerator().getCurrentUserAccount());
        }
        return sb.toString();
    }

    /**
     * 当前登陆人key
     *
     * @param
     */
    public abstract IUserKeyGenerator getUserKeyGenerator();

    /**
     * 生成动态key
     *
     * @param key
     * @param scope
     * @param parameterTypes
     * @param arguments
     * @return
     */
    public abstract String buildKey(String key, CacheScope scope,
                                    Class<?>[] parameterTypes, Object[] arguments);
}
