package com.cuiyun.kfcoding.api.vo.authority;

import com.alibaba.fastjson.annotation.JSONField;
import com.cuiyun.kfcoding.api.vo.authority.enums.AuthTypeEnum;
import com.cuiyun.kfcoding.api.vo.authority.validator.Credence;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-09 14:48
 **/
public class AuthRequest implements Credence{
    // 验证类型
    private AuthTypeEnum authType;
    // 账号或者邮箱
    private String credenceName;
    // 密码
    private String credenceCode;

    public AuthTypeEnum getAuthType() {
        return authType;
    }

    @JSONField(name = "authType")
    public void setAuthType(String authType) {
        this.authType = AuthTypeEnum.getEnum(authType);
    }

    public void setCredenceName(String credenceName) {
        this.credenceName = credenceName;
    }

    public void setCredenceCode(String credenceCode) {
        this.credenceCode = credenceCode;
    }

    @Override
    public AuthTypeEnum getCredenceAuthType() {
        return this.authType;
    }

    @Override
    public String getCredenceName() {
        return this.credenceName;
    }

    @Override
    public String getCredenceCode() {
        return this.credenceCode;
    }
}
