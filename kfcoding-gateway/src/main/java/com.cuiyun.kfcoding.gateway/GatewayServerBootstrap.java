package com.cuiyun.kfcoding.gateway;

import com.cuiyun.kfcoding.auth.client.EnableKfcodingAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-03 12:31
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableKfcodingAuthClient
@EnableFeignClients({"com.cuiyun.kfcoding.auth.client.feign","com.cuiyun.kfcoding.gateway.feign"})
public class GatewayServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServerBootstrap.class, args);
    }
}
