package com.cuiyun.kfcoding.basic.rest;

import com.cuiyun.kfcoding.basic.biz.UserBiz;
import com.cuiyun.kfcoding.basic.model.User;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("测试Info")
    public ResponseEntity<?> getUserInfo(String token) throws Exception {
       return ResponseEntity.ok("info");
    }
}
