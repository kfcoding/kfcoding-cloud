package com.cuiyun.kfcoding.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: kfcoding-cloud
 * @description: 权限控制类
 * @author: maple
 * @create: 2018-08-02 15:37
 **/
@RestController
@Slf4j
@RequestMapping("jwt")
public class AuthController {
    @Value("${jwt.token-header}")
    private String tokenHeader;


}
