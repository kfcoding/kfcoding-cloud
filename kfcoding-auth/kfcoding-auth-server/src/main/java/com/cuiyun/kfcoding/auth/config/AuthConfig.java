package com.cuiyun.kfcoding.auth.config;

import com.cuiyun.kfcoding.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: 权限配置类
 * @author: maple
 * @create: 2018-08-02 16:03
 **/
@Configuration
public class AuthConfig {
    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler(){
        return new GlobalExceptionHandler();
    }

}
