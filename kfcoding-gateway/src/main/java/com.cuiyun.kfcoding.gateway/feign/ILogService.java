package com.cuiyun.kfcoding.gateway.feign;

import com.cuiyun.kfcoding.api.vo.log.LogInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-03 12:31
 **/
@FeignClient("kfcoding-log")
public interface ILogService {
  @RequestMapping(value="/api/log/save",method = RequestMethod.POST)
  void saveLog(LogInfo info);
}
