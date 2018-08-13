package com.cuiyun.kfcoding.basic.biz;

import com.cuiyun.kfcoding.basic.dao.ThirdpartMapper;
import com.cuiyun.kfcoding.basic.model.Thirdpart;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: kfcoding-cloud
 * @description: 第三方逻辑层
 * @author: maple
 * @create: 2018-08-13 10:50
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ThirdpartBiz extends BaseBiz<ThirdpartMapper, Thirdpart> {
}
