package com.cuiyun.kfcoding.auth.service;


import com.cuiyun.kfcoding.api.vo.authority.AuthRequest;

public interface AuthService {
    String login(AuthRequest authRequest) throws Exception;
    String refresh(String oldToken);
    void validate(String token) throws Exception;
    Boolean invalid(String token);
}
