package com.cuiyun.kfcoding.framework.cache.annotation;

import com.cuiyun.kfcoding.framework.cache.parser.IKeyGenerator;
import com.cuiyun.kfcoding.framework.cache.parser.impl.DefaultKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解决问题：
 *
 * @author bifenglin
 * @version 1.0
 * @date 2018-8-2
 * @since 1.7
 */
@Retention(RetentionPolicy.RUNTIME)//在运行时可以获取  
@Target(value = {ElementType.METHOD, ElementType.TYPE})//作用到类，方法，接口上等
public @interface CacheClear {
    /**
     * 缓存key的前缀
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public String pre() default "";

    /**
     * 缓存key
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public String key() default "";

    /**
     * 缓存keys
     *
     * @return
     * @author bifenglin
     * @date 2018-8-3
     */
    public String[] keys() default "";

    /**
     * 键值解析类
     *
     * @return
     */
    public Class<? extends IKeyGenerator> generator() default DefaultKeyGenerator.class;
}
