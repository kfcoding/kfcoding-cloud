package com.cuiyun.kfcoding.basic.rpc;

import cn.hutool.core.bean.BeanUtil;
import com.cuiyun.kfcoding.api.vo.authority.AuthRequest;
import com.cuiyun.kfcoding.api.vo.user.UserInfo;
import com.cuiyun.kfcoding.basic.biz.UserBiz;
import com.cuiyun.kfcoding.basic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: 用户rpc控制层
 * @author: maple
 * @create: 2018-08-06 12:32
 **/
@RestController
@RequestMapping("api/users")
public class UserRest {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public @ResponseBody
    UserInfo validate(@RequestBody AuthRequest authRequest){
        User user = userBiz.validate(authRequest);
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo);
        return userInfo;
    }

}
