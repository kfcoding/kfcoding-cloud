package com.cuiyun.kfcoding.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: key配置类
 * @author: maple
 * @create: 2018-08-02 16:06
 **/
@Configuration
@Data
public class KeyConfig {
    @Value("${jwt.rsa-secret}")
    private String userSecret;
    @Value("${client.rsa-secret}")
    private String serviceSecret;
    private byte[] userPubKey;
    private byte[] userPriKey;
    private byte[] servicePriKey;
    private byte[] servicePubKey;
}
