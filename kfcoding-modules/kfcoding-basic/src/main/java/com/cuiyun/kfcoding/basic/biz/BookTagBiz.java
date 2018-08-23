package com.cuiyun.kfcoding.basic.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.basic.dao.BookTagMapper;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.BookTag;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 书籍标签服务类
 * @author: maple
 * @create: 2018-08-13 19:13
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class BookTagBiz extends BaseBiz<BookTagMapper, BookTag>{


}
