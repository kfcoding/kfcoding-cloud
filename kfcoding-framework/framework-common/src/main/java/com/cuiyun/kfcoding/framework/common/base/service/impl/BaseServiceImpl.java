package com.cuiyun.kfcoding.framework.common.base.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cuiyun.kfcoding.framework.common.base.service.IBaseService;

/**
 * @program: kfcoding-cloud
 * @description: service实现类
 * @author: maple
 * @create: 2018-08-02 13:15
 **/
public class BaseServiceImpl<T> extends ServiceImpl<BaseMapper<T>, T> implements IBaseService<T> {

}
