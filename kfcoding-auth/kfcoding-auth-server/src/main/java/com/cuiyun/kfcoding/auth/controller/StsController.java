package com.cuiyun.kfcoding.auth.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.cuiyun.kfcoding.auth.service.StsService;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kfcoding-cloud
 * @description: Sts授权控制类
 * @author: maple
 * @create: 2018-08-15 19:16
 **/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(description = "Sts授权相关接口")
public class StsController {
    @Autowired
    private StsService stsService;

    @Value("${kfcoding.bucketName.book}")
    private String bucketName;

    @RequestMapping(value = "/auth/sts/{kongfuid}", method = RequestMethod.GET)
    @ApiOperation(value = "获取sts临时身份", notes="")
    public ResponseEntity<?> getSts(@PathVariable String kongfuid) throws ClientException {
        StringBuffer sb = new StringBuffer();
        sb.append(bucketName).append("/").append(kongfuid).append("/*");
        AssumeRoleResponse response = stsService.getAssumeRoleResponse(bucketName + "/" + kongfuid + "/*");
        return ResponseEntity.ok(new ObjectRestResponse<>().data(response));
    }
}
