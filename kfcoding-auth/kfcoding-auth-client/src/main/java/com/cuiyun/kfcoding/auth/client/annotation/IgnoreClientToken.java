package com.cuiyun.kfcoding.auth.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: kfcoding-cloud
 * @description: 忽略服务鉴权
 * @author: maple
 * @create: 2018-08-02 18:26
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface IgnoreClientToken {
}
