package com.cuiyun.kfcoding.common.base.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import com.cuiyun.kfcoding.common.context.BaseContextHandler;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import com.cuiyun.kfcoding.common.msg.TableResultResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: 控制层基类
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
@Slf4j
public class BaseController<Biz extends BaseBiz,Entity> {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected Biz baseBiz;

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("添加对象")
    public ObjectRestResponse add(@RequestBody Entity entity){
        baseBiz.insert(entity);
        return new ObjectRestResponse<Entity>();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据id获取对象")
    public ObjectRestResponse<Entity> get(@PathVariable String id){
        ObjectRestResponse<Entity> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectById(id);
        entityObjectRestResponse.data((Entity)o);
        return entityObjectRestResponse;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("根据id修改对象")
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity){
        baseBiz.updateById(entity);
        return new ObjectRestResponse<Entity>();
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("根据id删除对象")
    public ObjectRestResponse<Entity> remove(@PathVariable String id){
        baseBiz.deleteById(id);
        return new ObjectRestResponse<Entity>();
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据条件获取所有对象")
    public List<Entity> all(Map params){
        return baseBiz.selectByMap(params);
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Entity> list(Page page){
        //查询列表数据
        Page data = baseBiz.selectPage(page);
        return new TableResultResponse<Entity>(data);
    }
    public String getCurrentUserName(){
        return BaseContextHandler.getUsername();
    }
    public String getCurrentUserId(){
        return BaseContextHandler.getUserID();
    }
}
