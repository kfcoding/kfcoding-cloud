package com.cuiyun.kfcoding.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cuiyun.kfcoding.auth.service.StsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: Sts实现类
 * @author: maple
 * @create: 2018-08-15 19:21
 **/
@Service
public class StsServiceImpl implements StsService{

    private DefaultAcsClient client;

    @Value("${kfcoding.sts.endpoint}")
    private String endpoint;

    @Value("${kfcoding.sts.accessKeySecret}")
    private String accessKeySecret;

    @Value("${kfcoding.sts.accessKeyId}")
    private String accessKeyId;

    @Value("${kfcoding.sts.roleArn}")
    private String roleArn;

    public StsServiceImpl() throws ClientException {
        DefaultProfile.addEndpoint("", "", "Sts", endpoint);
        IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        this.client = client;
    }

    public AssumeRoleResponse getAssumeRoleResponse(String reg) throws ClientException {
        Map policy = new HashMap();
        List statements = new ArrayList();
        Map statement = new HashMap();

        // 添加action
        List actions = new ArrayList();
        actions.add("oss:*");

        // 添加resource
        List resources = new ArrayList();
        resources.add("acs:oss:*:*:" + reg);

        // 整合statement
        statement.put("Effect", "Allow");
        statement.put("Action", actions);
        statement.put("Resource", resources);
        statements.add(statement);

        //整合policy
        policy.put("Version", "1");
        policy.put("Statement", statements);
        String policyString = JSON.toJSONString(policy);

        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setPolicy(policyString);
        request.setMethod(MethodType.POST);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(RandomUtil.randomString(10));
        final AssumeRoleResponse response = client.getAcsResponse(request);
        return response;
    }
}
