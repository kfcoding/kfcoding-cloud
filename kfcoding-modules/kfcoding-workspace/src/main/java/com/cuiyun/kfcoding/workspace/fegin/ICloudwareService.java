package com.cuiyun.kfcoding.workspace.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: kfcoding-cloud
 * @description: cloudware接口
 * @author: maple
 * @create: 2018-08-14 20:28
 **/
@FeignClient(name = "kfcoding-sidecar-cloudware")
public interface ICloudwareService {
    @RequestMapping(path = "", method = RequestMethod.GET)
    String test();
}
