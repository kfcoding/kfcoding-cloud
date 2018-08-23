package com.cuiyun.kfcoding.basic.rest;

import com.cuiyun.kfcoding.auth.client.annotation.IgnoreUserToken;
import com.cuiyun.kfcoding.basic.biz.UserBiz;
import com.cuiyun.kfcoding.basic.enums.BookStatusEnum;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.User;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.common.msg.ListRestResponse;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 用户控制层
 * @author: maple
 * @create: 2018-08-06 12:17
 **/
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Api("用户模块")
public class UserController extends BaseController<UserBiz, User>{

    @IgnoreUserToken
    public ObjectRestResponse add(@RequestBody User user) {
        return super.add(user);
    }

    @Override
    @IgnoreUserToken
    public ObjectRestResponse<User> get(String id) {
        return super.get(id);
    }

    @RequestMapping(path = "/{userId}/books", method = RequestMethod.GET)
    @ApiOperation(value = "用户课程列表", notes="列出该用户创建的所有公开的课程")
    @IgnoreUserToken
    public ListRestResponse listBook(@PathVariable String userId) {
        List<Book> list = baseBiz.listBook(userId, BookStatusEnum.PUBLIC);
        return new ListRestResponse().result(list);
    }

    @RequestMapping(path = "/current", method = RequestMethod.GET)
    @ApiOperation(value = "当前用户", notes="获取当前用户信息")
    public ObjectRestResponse current() {
        User user = baseBiz.selectById(getCurrentUserId());
        return new ObjectRestResponse().data(user);
    }


}
