package com.cuiyun.kfcoding.auth.util.user;

import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String credenceName;
    private String password;


    public JwtAuthenticationRequest(String credenceName, String password) {
        this.credenceName = credenceName;
        this.password = password;
    }

    public JwtAuthenticationRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCredenceName() {
        return credenceName;
    }

    public void setCredenceName(String credenceName) {
        this.credenceName = credenceName;
    }
}
