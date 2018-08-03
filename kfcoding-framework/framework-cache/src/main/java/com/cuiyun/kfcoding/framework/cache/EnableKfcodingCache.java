package com.cuiyun.kfcoding.framework.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author bifenglin
 * @create 2018-8-3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableKfcodingCache {
}
