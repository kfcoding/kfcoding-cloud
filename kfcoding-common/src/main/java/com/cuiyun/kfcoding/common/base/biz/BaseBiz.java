package com.cuiyun.kfcoding.common.base.biz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * @program: kfcoding-cloud
 * @description: 业务基础类
 * @author: maple
 * @create: 2018-08-01 21:35
 **/
public abstract class BaseBiz<T, M extends BaseMapper<T>> extends ServiceImpl<M, T> {

}
