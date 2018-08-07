package com.cuiyun.kfcoding.gate.feign;

import com.cuiyun.kfcoding.api.vo.authority.PermissionInfo;
import com.cuiyun.kfcoding.gate.fallback.UserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-07 20:05
 **/
@FeignClient(value = "kfcoding-basic",fallback = UserServiceFallback.class)
public interface IUserService {
  @RequestMapping(value="/api/user/un/{username}/permissions",method = RequestMethod.GET)
  public List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username);
  @RequestMapping(value="/api/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getAllPermissionInfo();
}
