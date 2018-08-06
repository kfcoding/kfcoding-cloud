package com.cuiyun.kfcoding.basic.rpc;

import com.cuiyun.kfcoding.api.vo.user.UserInfo;
import com.cuiyun.kfcoding.basic.biz.UserBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: 用户rpc控制层
 * @author: maple
 * @create: 2018-08-06 12:32
 **/
@RequestMapping("api/users")
public class UserRest {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public @ResponseBody
    UserInfo validate(@RequestBody Map<String,String> body){
        return userBiz.validate(body.get("credenceName"),body.get("password"));
    }
}
