package com.cuiyun.framework.cache.core;

import java.util.List;
import java.util.Set;

/**
 * Created by heyong on 2018/7/30 16:35
 * Description: 缓存基类接口
 * @author bifenglin
 */
public interface S2Cache {

    /**
     * 缓存名称
     * @return
     */
    String getName();

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    boolean exists(final String key);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Object get(final String key);

    /**
     * 获取缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(final String key, Class<T> clazz);

    /**
     * 获取缓存列表
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getList(final String key, Class<T> clazz);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @return
     */
    void set(final String key, Object value);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    void set(final String key, Object value, Long expireTime);

    /**
     * 删除缓存
     *
     * @param key
     */
    void remove(final String key);


    /**
     * 删除缓存
     *
     * @param keys
     */
    void remove(final String... keys);

    /**
     * 自增
     *
     * @param key
     * @param value
     * @return
     */
    long increment(final String key, long value);


    /**
     * 入队
     *
     * @param key
     * @param value
     * @param hashKey
     * @return
     */
    void hashPush(final String key, String hashKey, Object value);

    /**
     * 入队
     *
     * @param key
     * @param value
     * @param hashKey
     * @param expireTime
     * @return
     */
    void hashPush(final String key, String hashKey, Object value, Long expireTime);

    /**
     * 出队
     * @param key
     * @param hashKey
     * @return
     */
    Object hashPop(final String key, String hashKey);

    /**
     * 出队
     * @param key
     * @param hashKey
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T hashPop(final String key, String hashKey, Class<T> clazz);

    /**
     * 是否存在hash缓存
     *
     * @param key
     * @param hashKey
     * @return
     */
    boolean hashHasKey(final String key, String hashKey);

    /**
     * 删除hash缓存
     *
     * @param key
     * @param hashKey
     */
    void hashRemove(final String key, String hashKey);

    /**
     * key集合
     *
     * @param key
     * @return
     */
    Set<Object> hashKeys(final String key);

    /**
     * value集合
     *
     * @param key
     * @return
     */
    List<Object> hashValues(final String key);


    /**
     * 集合自增
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    long hashIncrement(final String key, String hashKey, long value);
}
