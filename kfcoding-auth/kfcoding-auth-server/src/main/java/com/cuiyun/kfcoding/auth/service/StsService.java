package com.cuiyun.kfcoding.auth.service;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * @program: kfcoding-cloud
 * @description: Sts服务类
 * @author: maple
 * @create: 2018-08-15 19:20
 **/
public interface StsService {
    AssumeRoleResponse getAssumeRoleResponse(String reg) throws ClientException;
}
