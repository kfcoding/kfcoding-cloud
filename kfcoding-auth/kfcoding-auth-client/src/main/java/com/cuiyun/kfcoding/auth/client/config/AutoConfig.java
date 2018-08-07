package com.cuiyun.kfcoding.auth.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-02 18:34
 **/
@Configuration
@ComponentScan({"com.cuiyun.kfcoding.auth.client","com.cuiyun.kfcoding.auth.common.event"})
public class AutoConfig {
    @Bean
    ServiceAuthConfig getServiceAuthConfig(){
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig(){
        return new UserAuthConfig();
    }

}
