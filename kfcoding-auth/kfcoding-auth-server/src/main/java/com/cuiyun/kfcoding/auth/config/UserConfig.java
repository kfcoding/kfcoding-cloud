package com.cuiyun.kfcoding.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: kfcoding-cloud
 * @description: user配置类
 * @author: maple
 * @create: 2018-08-02 16:18
 **/
@Configuration
@Data
public class UserConfig {
    @Value("${jwt.token-header}")
    private String userTokenHeader;
}
