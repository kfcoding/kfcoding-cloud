package com.cuiyun.kfcoding.basic.rest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.auth.client.annotation.IgnoreUserToken;
import com.cuiyun.kfcoding.basic.biz.BookBiz;
import com.cuiyun.kfcoding.basic.biz.BookTagBiz;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.BookTag;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.common.msg.ListRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 书籍控制层
 * @author: maple
 * @create: 2018-08-13 15:37
 **/
@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
@Api("书籍模块")
public class BookController extends BaseController<BookBiz, Book>{

    @Autowired
    private BookTagBiz  bookTagBiz;

    @RequestMapping(path = "/tags", method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", notes="")
    @IgnoreUserToken
    public ListRestResponse tags() {
        List<BookTag> tags = bookTagBiz.selectList(new EntityWrapper<>());
        return new ListRestResponse().result(tags);
    }

}
