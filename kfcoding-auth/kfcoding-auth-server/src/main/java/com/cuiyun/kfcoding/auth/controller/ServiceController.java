package com.cuiyun.kfcoding.auth.controller;

import com.cuiyun.kfcoding.auth.biz.ClientBiz;
import com.cuiyun.kfcoding.auth.model.Client;
import com.cuiyun.kfcoding.auth.model.ClientService;
import com.cuiyun.kfcoding.common.base.controller.BaseController;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kfcoding-cloud
 * @description: 权限服务控制层
 * @author: maple
 * @create: 2018-08-06 16:55
 **/
@RestController
@RequestMapping("service")
public class ServiceController extends BaseController<ClientBiz,Client> {

    @RequestMapping(value = "/{id}/client", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable int id, String clients){
        baseBiz.modifyClientServices(id, clients);
        return new ObjectRestResponse().rel(true);
    }

    @RequestMapping(value = "/{id}/client", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<ClientService> getUsers(@PathVariable int id){
        return new ObjectRestResponse<ClientService>().rel(true).data(baseBiz.getClientServices(id));
    }
}
