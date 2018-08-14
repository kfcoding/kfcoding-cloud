package com.cuiyun.kfcoding.basic.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * @program: kfcoding
 * @description: Kongfu状态枚举
 * @author: maple
 * @create: 2018-06-08 16:17
 **/
public enum BookStatusEnum implements IEnum{

    PRIVATE("private", "未公开"),
    PUBLIC("public", "公开");

    BookStatusEnum(String key, String decription) {
        this.key = key;
        this.decription = decription;
    }

    private String key;
    private String decription;


    @Override
    public Serializable getValue() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
