package com.cuiyun.kfcoding.framework.cache.annotation;

import com.cuiyun.kfcoding.framework.cache.constants.CacheScope;
import com.cuiyun.kfcoding.framework.cache.parser.ICacheResultParser;
import com.cuiyun.kfcoding.framework.cache.parser.IKeyGenerator;
import com.cuiyun.kfcoding.framework.cache.parser.impl.DefaultKeyGenerator;
import com.cuiyun.kfcoding.framework.cache.parser.impl.DefaultResultParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启缓存
 * <p/>
 * 解决问题：
 *
 * @author bifenglin
 * @version 1.0
 * @date 2018-8-2
 * @since 1.7
 */
@Retention(RetentionPolicy.RUNTIME)
// 在运行时可以获取
@Target(value = {ElementType.METHOD, ElementType.TYPE})
// 作用到类，方法，接口上等
public @interface Cache {
    /**
     * 缓存key menu_{0.id}{1}_type
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public String key() default "";

    /**
     * 作用域
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public CacheScope scope() default CacheScope.application;

    /**
     * 过期时间
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public int expire() default 720;

    /**
     * 描述
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public String desc() default "";

    /**
     * 返回类型
     *
     * @return
     * @author bifenglin
     * @date 2018-8-2
     */
    public Class[] result() default Object.class;

    /**
     * 返回结果解析类
     *
     * @return
     */
    public Class<? extends ICacheResultParser> parser() default DefaultResultParser.class;

    /**
     * 键值解析类
     *
     * @return
     */
    public Class<? extends IKeyGenerator> generator() default DefaultKeyGenerator.class;
}
