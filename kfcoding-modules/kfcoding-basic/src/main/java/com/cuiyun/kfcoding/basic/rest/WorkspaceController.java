package com.cuiyun.kfcoding.basic.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.cuiyun.kfcoding.basic.biz.WorkspaceBiz;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.Workspace;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.common.msg.ListRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-22 10:46
 **/
@RestController
@RequestMapping("/workspaces")
@CrossOrigin(origins = "*")
@Api("工作空间模块")
public class WorkspaceController extends BaseController<WorkspaceBiz, Workspace>{

    @RequestMapping(value = "/current",method = RequestMethod.GET)
    @ApiOperation("根据id获取对象")
    public ListRestResponse<Book> current(Page page) {
        List<Workspace> workspaces = baseBiz.getWorkspacesByUserId(getCurrentUserId());
        return new ListRestResponse<>().result(workspaces);
    }


}
