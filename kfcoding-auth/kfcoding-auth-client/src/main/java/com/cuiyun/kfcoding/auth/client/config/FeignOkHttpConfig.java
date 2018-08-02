package com.cuiyun.kfcoding.auth.client.config;

import com.cuiyun.kfcoding.auth.client.interceptor.OkHttpTokenInterceptor;
import com.cuiyun.kfcoding.auth.client.jwt.ServiceAuthUtil;
import com.cuiyun.kfcoding.common.context.BaseContextHandler;
import feign.Feign;
import feign.Request;
import feign.Response;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-02 18:29
 **/
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Configuration
@ConditionalOnClass(Feign.class)
public class FeignOkHttpConfig {

    @Autowired
    OkHttpTokenInterceptor okHttpLoggingInterceptor;

    private int feignOkHttpReadTimeout = 60;
    private int feignConnectTimeout = 60;
    private int feignWriteTimeout = 120;

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder().readTimeout(feignOkHttpReadTimeout, TimeUnit.SECONDS).connectTimeout(feignConnectTimeout, TimeUnit.SECONDS)
                .writeTimeout(feignWriteTimeout, TimeUnit.SECONDS).connectionPool(new ConnectionPool())
                .addInterceptor(okHttpLoggingInterceptor)
                .build();
    }
}
