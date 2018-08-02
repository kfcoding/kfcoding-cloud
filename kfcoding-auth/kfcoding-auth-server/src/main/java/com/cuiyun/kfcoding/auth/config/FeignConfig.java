package com.cuiyun.kfcoding.auth.config;

import com.cuiyun.kfcoding.auth.interceptor.ClientTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: fegin配置类
 * @author: maple
 * @create: 2018-08-02 16:05
 **/
@Configuration
public class FeignConfig {
    @Bean
    ClientTokenInterceptor getClientTokenInterceptor(){
        return new ClientTokenInterceptor();
    }

}
