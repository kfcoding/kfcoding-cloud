package com.cuiyun.kfcoding.auth.feign;

import com.cuiyun.kfcoding.api.vo.user.UserInfo;
import com.cuiyun.kfcoding.auth.config.FeignConfig;
import com.cuiyun.kfcoding.auth.util.user.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * ${DESCRIPTION}
 *
 * @author  bifenglin
 * @create 2017-06-21 8:11
 */
@FeignClient(value = "ace-admin",configuration = FeignConfig.class)
public interface IUserService {
  @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
  public UserInfo validate(@RequestBody JwtAuthenticationRequest authenticationRequest);
}
