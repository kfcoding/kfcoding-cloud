package com.cuiyun.kfcoding.basic.rest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cuiyun.kfcoding.auth.client.annotation.IgnoreUserToken;
import com.cuiyun.kfcoding.basic.biz.BookBiz;
import com.cuiyun.kfcoding.basic.biz.BookTagBiz;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.BookTag;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.common.msg.ListRestResponse;
import com.cuiyun.kfcoding.common.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @IgnoreUserToken
    @Override
    public TableResultResponse<Book> list(Page page) {
        return super.list(page);
    }
}
