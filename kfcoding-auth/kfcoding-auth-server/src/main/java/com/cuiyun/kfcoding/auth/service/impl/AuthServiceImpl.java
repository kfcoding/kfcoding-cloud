package com.cuiyun.kfcoding.auth.service.impl;

import com.cuiyun.kfcoding.auth.common.util.jwt.JWTInfo;
import com.cuiyun.kfcoding.auth.feign.IUserService;
import com.cuiyun.kfcoding.auth.service.AuthService;
import com.cuiyun.kfcoding.auth.util.user.JwtAuthenticationRequest;
import com.cuiyun.kfcoding.auth.util.user.JwtTokenUtil;
import com.cuiyun.kfcoding.auth.vo.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(JwtAuthenticationRequest authenticationRequest) throws Exception {
        UserInfo info = userService.validate(authenticationRequest);
        String token = "";
        if (!StringUtils.isEmpty(info.getId())) {
            token = jwtTokenUtil.generateToken(new JWTInfo(info.getUsername(), info.getId() + "", info.getName()));
        }
        return token;
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public Boolean invalid(String token) {
        // TODO: 2018/7/11 注销token
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        // TODO: 2018/7/11 刷新token
        return null;
    }
}
