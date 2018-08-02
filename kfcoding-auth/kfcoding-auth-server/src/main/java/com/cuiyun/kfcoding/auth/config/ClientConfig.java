package com.cuiyun.kfcoding.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: client配置类
 * @author: maple
 * @create: 2018-08-02 16:05
 **/
@Configuration
public class ClientConfig {
    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;
    @Value("${client.token-header}")
    private String clientTokenHeader;

    public String getClientTokenHeader() {
        return clientTokenHeader;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

}
