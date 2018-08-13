package com.cuiyun.kfcoding.auth.feign;

import com.cuiyun.kfcoding.api.vo.authority.AuthRequest;
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
@FeignClient(value = "kfcoding-basic",configuration = FeignConfig.class)
public interface IUserService {
  @RequestMapping(value = "/api/users/validate", method = RequestMethod.POST)
  UserInfo validate(@RequestBody AuthRequest authRequest);
}
