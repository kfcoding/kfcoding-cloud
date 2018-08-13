package com.cuiyun.kfcoding.basic.rest;

import com.cuiyun.kfcoding.api.vo.authority.AuthRequest;
import com.cuiyun.kfcoding.api.vo.authority.enums.AuthTypeEnum;
import com.cuiyun.kfcoding.basic.biz.UserBiz;
import com.cuiyun.kfcoding.basic.model.User;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kfcoding-cloud
 * @description: 用户控制层
 * @author: maple
 * @create: 2018-08-06 12:17
 **/
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Api("用户模块")
public class UserController extends BaseController<UserBiz, User>{

    @Value("${auth.token-url}")
    private String tokenUrl;

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ApiOperation("添加对象")
    @Override
    public ResponseEntity add(@RequestBody User user) {
        User savedUser = baseBiz.add(user);
        AuthRequest authRequest = new AuthRequest();
        authRequest.setAuthType(AuthTypeEnum.PASSWORD.getValue());
        authRequest.setCredenceCode(savedUser.getPassword());
        authRequest.setCredenceName(savedUser.getEmail());
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, tokenUrl).body(authRequest);
    }
}
