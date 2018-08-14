package com.cuiyun.kfcoding.basic;

import com.cuiyun.kfcoding.auth.client.EnableKfcodingAuthClient;
import com.cuiyun.kfcoding.framework.cache.EnableKfcodingCache;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: kfcoding-cloud
 * @description: user模块启动类
 * @author: maple
 * @create: 2018-08-03 12:58
 **/
@EnableFeignClients({"com.cuiyun.kfcoding.auth.client.feign", "com.cuiyun.kfcoding.basic.feign"})
@EnableScheduling
@EnableKfcodingAuthClient
@EnableTransactionManagement
@EnableKfcodingCache
@SpringCloudApplication
@MapperScan("com.cuiyun.kfcoding.basic.dao")
@EnableSwagger2Doc
public class BasicBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BasicBootstrap.class).web(true).run(args);
    }
}
