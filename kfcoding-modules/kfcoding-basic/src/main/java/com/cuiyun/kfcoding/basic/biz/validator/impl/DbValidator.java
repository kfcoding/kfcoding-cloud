package com.cuiyun.kfcoding.basic.biz.validator.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.api.vo.authority.validator.Credence;
import com.cuiyun.kfcoding.api.vo.user.UserInfo;
import com.cuiyun.kfcoding.basic.biz.UserBiz;
import com.cuiyun.kfcoding.basic.biz.validator.IReqValidator;
import com.cuiyun.kfcoding.basic.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.basic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账号密码验证
 *
 * @author maple
 * @date 2018-08-23 12:34
 */
@Service
public class DbValidator implements IReqValidator {

    @Autowired
    UserBiz userBiz;

    @Override
    public User validate(Credence credence) {
        EntityWrapper ew = new EntityWrapper<User>();
        // 若是邮箱格式
        if (Validator.isEmail(credence.getCredenceName())){
            ew.eq("email", credence.getCredenceName());
        } else {
            ew.eq("account", credence.getCredenceName());
        }
//        ew.eq("password", MD5Util.encrypt(credence.getCredenceCode()));
        ew.eq("password", credence.getCredenceCode());

        User user = userBiz.selectOne(ew);
        Assert.notNull(user, BizExceptionEnum.USER_NULL.getMessage());
        return user;
    }
}
