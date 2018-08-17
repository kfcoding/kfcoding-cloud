package com.cuiyun.kfcoding.workspace.rest;

import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.workspace.biz.WorkspaceBiz;
import com.cuiyun.kfcoding.workspace.model.Workspace;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: kfcoding-cloud
 * @description: workspace 控制类
 * @author: maple
 * @create: 2018-08-15 20:04
 **/
@RestController
@RequestMapping("api/workspaces")
@CrossOrigin(origins = "*")
@Api(description = "工作空间相关接口")
public class WorkspaceController extends BaseController<WorkspaceBiz, Workspace> {

}
