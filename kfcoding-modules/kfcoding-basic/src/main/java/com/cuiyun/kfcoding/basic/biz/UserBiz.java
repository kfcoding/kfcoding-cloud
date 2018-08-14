package com.cuiyun.kfcoding.basic.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.api.vo.authority.AuthRequest;
import com.cuiyun.kfcoding.basic.biz.validator.impl.DbValidator;
import com.cuiyun.kfcoding.basic.biz.validator.impl.GithubValidator;
import com.cuiyun.kfcoding.basic.dao.UserMapper;
import com.cuiyun.kfcoding.basic.enums.BookStatusEnum;
import com.cuiyun.kfcoding.basic.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.User;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import com.cuiyun.kfcoding.common.exception.KfCodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 用户业务类
 * @author: maple
 * @create: 2018-08-03 19:09
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User>{

    @Autowired
    GithubValidator githubValidator;
    @Autowired
    DbValidator dbValidator;
    @Autowired
    BookBiz bookBiz;

    public User validate(AuthRequest authRequest) {
        User user = null;
        switch (authRequest.getAuthType()) {
            case GITHUB:  // 若是github登陆
                user = githubValidator.validate(authRequest);
                break;
            case PASSWORD: // 若是账号密码登陆
                user = dbValidator.validate(authRequest);
                break;
        }
        return user;
    }

    @Override
    public boolean insert(User user) {
        Assert.notNull(user.getEmail(), BizExceptionEnum.USER_EMAIL_NULL.getMessage());
        Assert.notNull(user.getPassword(), BizExceptionEnum.USER_PASSWORD_NULL.getMessage());
        if (!Validator.isEmail(user.getEmail()))
            throw new KfCodingException(BizExceptionEnum.USER_EMAIL_VALIDATE);
        if (baseMapper.selectObjs(new EntityWrapper<User>().eq("email", user.getEmail())).size() > 0)
            throw new KfCodingException(BizExceptionEnum.USER_EXIST);
        // 设置name和account的默认值
        if (user.getName() == null){
            user.setName(user.getEmail());
        }
        if (user.getAccount() == null){
            user.setAccount(user.getEmail());
        }
        user.setCreateTime(new Date());
        if (baseMapper.insert(user) == 0)
            throw new KfCodingException(BizExceptionEnum.USER_INSERT);
        return true;
    }

    /**
     * 获取公开的书籍
     */
    public List<Book> listBook(String userId, BookStatusEnum bookStatusEnum) {
        List<Book> list = bookBiz.selectList(new EntityWrapper<Book>().eq("user_id", userId).eq("status", bookStatusEnum));
        return list;
    }

    @Override
    public boolean updateById(User entity) {
        User targetUser = baseMapper.selectById(entity.getId());
        if (!targetUser.getAccount().equals(entity.getAccount())){
            User param = new User();
            param.setAccount(entity.getAccount());
            param.setVersion(targetUser.getVersion());
            if (baseMapper.selectOne(param) != null){
                throw new KfCodingException(BizExceptionEnum.USER_EXIST);
            }
        }
        // 修改数据
        BeanUtil.copyProperties(entity, targetUser, entity.getIgnoreProperties());
        if (baseMapper.updateById(targetUser) == 0) {
            throw new KfCodingException(BizExceptionEnum.USER_UPDATE_ERROR);
        }
        return super.updateById(entity);
    }
}
