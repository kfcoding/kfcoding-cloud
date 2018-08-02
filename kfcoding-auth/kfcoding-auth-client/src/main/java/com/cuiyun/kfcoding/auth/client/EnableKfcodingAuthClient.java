package com.cuiyun.kfcoding.auth.client;


import com.cuiyun.kfcoding.auth.client.config.AutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: kfcoding-cloud
 * @description: 开启注解
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfig.class)
@Documented
@Inherited
public @interface EnableKfcodingAuthClient {
}
