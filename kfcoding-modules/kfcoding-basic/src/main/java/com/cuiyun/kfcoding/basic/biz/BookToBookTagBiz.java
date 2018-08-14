package com.cuiyun.kfcoding.basic.biz;

import com.cuiyun.kfcoding.basic.dao.BookToBookTagMapper;
import com.cuiyun.kfcoding.basic.model.BookToBookTag;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-13 16:00
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class BookToBookTagBiz extends BaseBiz<BookToBookTagMapper, BookToBookTag> {
}
