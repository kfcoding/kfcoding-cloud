package com.cuiyun.kfcoding.basic.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cuiyun.kfcoding.common.base.model.BaseModel;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author maple
 * @since 2018-08-06
 */
@TableName("basic_user")
public class User extends BaseModel<User> {

    private static final long serialVersionUID = 1L;

    private String account;
    private String name;
    private String email;
    @TableField("avatar_url")
    private String avatarUrl;
    private String description;
    private String password;
    private String role;
    private String city;
    private String company;
    private String profession;
    private String status;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", deleteTime=" + deleteTime +
        ", version=" + version +
        ", isDel=" + isDel +
        ", account=" + account +
        ", name=" + name +
        ", email=" + email +
        ", avatarUrl=" + avatarUrl +
        ", password=" + password +
        ", role=" + role +
        ", city=" + city +
        ", company=" + company +
        ", profession=" + profession +
        ", status=" + status +
        "}";
    }
}
