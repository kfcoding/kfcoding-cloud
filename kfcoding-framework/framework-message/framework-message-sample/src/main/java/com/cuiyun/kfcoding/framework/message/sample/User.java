package com.cuiyun.kfcoding.framework.message.sample;

import java.io.Serializable;

/**
 * Created by maple on 2018/8/21 15:53
 * Description:
 * @author maple
 */
public class User implements Serializable {
    private String userName;
    private String address;

    public User() {
    }

    User(String userName, String address) {
        this.userName = userName;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}