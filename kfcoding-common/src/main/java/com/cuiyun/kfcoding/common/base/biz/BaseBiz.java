package com.cuiyun.kfcoding.common.base.biz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cuiyun.kfcoding.common.context.BaseContextHandler;

/**
 * @program: kfcoding-cloud
 * @description: 业务基础类
 * @author: maple
 * @create: 2018-08-01 21:35
 **/
public abstract class BaseBiz<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    protected String getCurrentUserId(){
        return BaseContextHandler.getUserID();
    }
    protected String getCurrentUserName(){
        return BaseContextHandler.getName();
    }
}
