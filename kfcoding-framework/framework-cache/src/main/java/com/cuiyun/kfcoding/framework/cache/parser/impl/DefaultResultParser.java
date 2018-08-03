package com.cuiyun.kfcoding.framework.cache.parser.impl;

import com.alibaba.fastjson.JSON;
import com.cuiyun.kfcoding.framework.cache.parser.ICacheResultParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 默认缓存结果解析类
 *
 * @author bifenglin
 * @description
 * @date 2018-8-3
 * @since 1.7
 */
public class DefaultResultParser implements ICacheResultParser {

    @Override
    public Object parse(String value, Type type, Class<?>... origins) {
        Object result = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (((Class) rawType).isAssignableFrom(List.class)) {
                result = JSON.parseArray(value, (Class) parameterizedType.getActualTypeArguments()[0]);
            }
        } else if (origins == null) {
            result = JSON.parseObject(value, (Class) type);
        } else {
            result = JSON.parseObject(value, origins[0]);
        }
        return result;
    }
}
