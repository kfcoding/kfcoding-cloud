package com.cuiyun.kfcoding.auth.controller;

import com.cuiyun.kfcoding.auth.config.KeyConfig;
import com.cuiyun.kfcoding.auth.service.AuthClientService;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 客户端权限控制层
 * @author: maple
 * @create: 2018-08-06 16:42
 **/
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("client")
public class ClientController {
    @Autowired
    private AuthClientService authClientService;

    @Autowired
    private KeyConfig keyConfig;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ObjectRestResponse getAccessToken(String clientId, String secret) throws Exception {
        return new ObjectRestResponse().data(authClientService.apply(clientId, secret));
    }

    @RequestMapping(value = "myClient")
    public ObjectRestResponse getAllowedClient(String serviceId, String secret) {
        return new ObjectRestResponse<List<String>>().data(authClientService.getAllowedClient(serviceId, secret));
    }

    @RequestMapping(value = "/servicePubKey", method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getServicePublickKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret){
        return new ObjectRestResponse<byte[]>().data(keyConfig.getServicePubKey());
    }

    @RequestMapping(value = "/userPubKey",method = RequestMethod.POST)
    public ObjectRestResponse<byte[]> getUserPublicKey(@RequestParam("clientId") String clientId, @RequestParam("secret") String secret) throws Exception {
        authClientService.validate(clientId, secret);
        return new ObjectRestResponse<byte[]>().data(keyConfig.getUserPubKey());
    }
}
