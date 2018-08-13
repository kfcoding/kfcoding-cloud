package com.cuiyun.kfcoding.api.vo.authority.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: kfcoding
 * @description: 第三方类型枚举
 * @author: maple
 * @create: 2018-05-20 12:46
 **/
public enum AuthTypeEnum {
    GITHUB("github"),
    PASSWORD("password");


    private static final Map<String, AuthTypeEnum> CODE_MAP = new HashMap<String, AuthTypeEnum>();

    static {
        for (AuthTypeEnum typeEnum : AuthTypeEnum.values()) {
            CODE_MAP.put(typeEnum.getValue().toString(), typeEnum);
        }
    }

    AuthTypeEnum(String value){
        this.value = value;
    }

    /**
     * string转enum
     */
    public static AuthTypeEnum getEnum(String value) {
        return CODE_MAP.get(value);
    }

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
