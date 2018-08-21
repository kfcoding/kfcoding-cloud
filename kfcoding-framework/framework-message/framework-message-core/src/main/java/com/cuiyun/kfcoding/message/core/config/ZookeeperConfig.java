package com.cuiyun.kfcoding.message.core.config;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 11:34
 **/
@Data
public class ZookeeperConfig {

    private String host;

    private int sessionTimeOut = 1000;

    private String rootPath = "/kfcoding";
}
