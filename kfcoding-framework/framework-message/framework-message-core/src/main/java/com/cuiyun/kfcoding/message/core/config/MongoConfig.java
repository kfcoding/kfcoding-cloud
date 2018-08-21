package com.cuiyun.kfcoding.message.core.config;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 11:34
 **/
@Data
public class MongoConfig {
    /**
     * mongo数据库设置.
     */
    private String mongoDbName;

    /**
     * mongo数据库URL.
     */
    private String mongoDbUrl;

    /**
     * mongo数据库用户名.
     */
    private String mongoUserName;

    /**
     * mongo数据库密码.
     */
    private String mongoUserPwd;
}
