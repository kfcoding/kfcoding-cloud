package com.cuiyun.kfcoding.user.biz;

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
public class UserBiz {

}
