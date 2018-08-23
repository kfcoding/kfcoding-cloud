package com.cuiyun.kfcoding.basic.biz;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cuiyun.kfcoding.basic.dao.WorkspaceMapper;
import com.cuiyun.kfcoding.basic.model.Workspace;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: workspace业务类
 * @author: maple
 * @create: 2018-08-14 15:48
 **/
@Service
public class WorkspaceBiz extends BaseBiz<WorkspaceMapper, Workspace>{
    @Value("${kfcoding.workspace.deleteUrl}")
    private String deleteUrl;

    @Value("${kfcoding.workspace.createUrl}")
    private String createUrl;

    @Value("${kfcoding.workspace.release}")
    private String workSpaceRelease;

    @Value("${kfcoding.workspace.startUrl}")
    private String startUrl;

    @Value("${kfcoding.workspace.keepUrl}")
    private String keepUrl;

    public List<Workspace> getWorkspacesByUserId(String userId) {
        return this.baseMapper.selectList(new EntityWrapper<Workspace>().eq("user_id", userId));
    }

    @Override
    public boolean insert(Workspace entity) {
        return super.insert(entity);
    }
}
