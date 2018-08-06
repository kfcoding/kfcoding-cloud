package com.cuiyun.kfcoding.basic.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.api.vo.user.UserInfo;
import com.cuiyun.kfcoding.basic.dao.UserMapper;
import com.cuiyun.kfcoding.basic.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.basic.model.User;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: kfcoding-cloud
 * @description: 用户业务类
 * @author: maple
 * @create: 2018-08-03 19:09
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<User, UserMapper>{

    public UserInfo validate(String credenceName, String password) {
        User user = new User();
        if (Validator.isEmail(credenceName)){
            user.setEmail(credenceName);
        } else {
            user.setAccount(credenceName);
        }
        user.setPassword(password);
        Assert.isNull(this.baseMapper.selectOne(user), BizExceptionEnum.USER_NULL.getMessage());
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo);
        return userInfo;
    }
}
