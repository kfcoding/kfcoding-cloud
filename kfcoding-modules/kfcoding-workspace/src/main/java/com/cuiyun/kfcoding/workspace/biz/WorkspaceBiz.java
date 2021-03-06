package com.cuiyun.kfcoding.workspace.biz;

import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @program: kfcoding-cloud
 * @description: workspace业务类
 * @author: maple
 * @create: 2018-08-14 15:48
 **/
@Service
public class WorkspaceBiz extends BaseBiz{
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

}
