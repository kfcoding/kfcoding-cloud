package com.cuiyun.kfcoding.auth.config;

import com.cuiyun.kfcoding.auth.interceptor.ServiceAuthRestInterceptor;
import com.cuiyun.kfcoding.auth.interceptor.UserAuthRestInterceptor;
import com.cuiyun.kfcoding.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: kfcoding-cloud
 * @description: web配置类
 * @author: maple
 * @create: 2018-08-02 16:19
 **/
@Configuration("basicWebConfig")
@Primary
public class WebConfig implements WebMvcConfigurer{
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/service/**");
        registry.addInterceptor(getUserAuthRestInterceptor()).addPathPatterns("/service/**");
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
        return new ServiceAuthRestInterceptor();
    }

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

}
