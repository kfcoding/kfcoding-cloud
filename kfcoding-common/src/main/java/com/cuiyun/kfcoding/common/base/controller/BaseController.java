package com.cuiyun.kfcoding.common.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import com.cuiyun.kfcoding.common.context.BaseContextHandler;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity){
        baseBiz.insert(entity);
        return new ObjectRestResponse<Entity>();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Entity> get(@PathVariable int id){
        ObjectRestResponse<Entity> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectById(id);
        entityObjectRestResponse.data((Entity)o);
        return entityObjectRestResponse;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity){
        baseBiz.updateById(entity);
        return new ObjectRestResponse<Entity>();
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<Entity> remove(@PathVariable int id){
        baseBiz.deleteById(id);
        return new ObjectRestResponse<Entity>();
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    public List<Entity> all(){
        return baseBiz.selectList(new EntityWrapper());
    }

//    @RequestMapping(value = "/page",method = RequestMethod.GET)
//    @ResponseBody
//    public TableResultResponse<Entity> list(@RequestParam Map<String, Object> params){
//        //查询列表数据
//    }
    public String getCurrentUserName(){
        return BaseContextHandler.getUsername();
    }
}
