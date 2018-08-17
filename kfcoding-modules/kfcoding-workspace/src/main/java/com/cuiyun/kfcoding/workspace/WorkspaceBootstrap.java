package com.cuiyun.kfcoding.workspace;

import com.cuiyun.kfcoding.auth.client.EnableKfcodingAuthClient;
import com.cuiyun.kfcoding.framework.cache.EnableKfcodingCache;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: kfcoding-cloud
 * @description: workspace启动类
 * @author: maple
 * @create: 2018-08-13 19:26
 **/
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients({"com.cuiyun.kfcoding.auth.client.feign", "com.cuiyun.kfcoding.workspace.feign"})
@EnableScheduling
@EnableKfcodingAuthClient
@EnableTransactionManagement
@EnableKfcodingCache
@SpringBootApplication
@EnableSwagger2Doc
public class WorkspaceBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WorkspaceBootstrap.class).web(true).run(args);
    }

}
