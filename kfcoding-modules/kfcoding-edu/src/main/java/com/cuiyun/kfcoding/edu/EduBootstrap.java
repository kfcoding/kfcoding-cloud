package com.cuiyun.kfcoding.edu;

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
 * @description: edu模块启动类
 * @author: maple
 * @create: 2018-08-14 20:13
 **/
@EnableFeignClients({"com.cuiyun.kfcoding.auth.client.feign", "com.cuiyun.kfcoding.edu.feign"})
@EnableScheduling
@EnableKfcodingAuthClient
@EnableTransactionManagement
@EnableKfcodingCache
@SpringCloudApplication
@MapperScan("com.cuiyun.kfcoding.basic.dao")
@EnableSwagger2Doc
public class EduBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EduBootstrap.class).web(true).run(args);
    }
}
