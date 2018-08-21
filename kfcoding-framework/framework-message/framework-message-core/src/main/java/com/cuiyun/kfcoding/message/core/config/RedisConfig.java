package com.cuiyun.kfcoding.message.core.config;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 11:34
 **/
@Data
public class RedisConfig {

    private Boolean cluster = false;

    private Boolean sentinel = false;

    /**
     * clusterUrl   example:ip:port;ip:port.
     */
    private String clusterUrl;

    /**
     * sentinelUrl   example:ip:port;ip:port.
     */
    private String sentinelUrl;

    private String masterName;

    private String hostName;

    private int port;

    private String password;

    private int maxTotal = 8;

    private int maxIdle = 8;

    private int minIdle;

    private long maxWaitMillis = -1L;

    private long minEvictableIdleTimeMillis = 1800000L;

    private long softMinEvictableIdleTimeMillis = 1800000L;

    private int numTestsPerEvictionRun = 3;

    private Boolean testOnCreate = false;

    private Boolean testOnBorrow = false;

    private Boolean testOnReturn = false;

    private Boolean testWhileIdle = false;

    private long timeBetweenEvictionRunsMillis = -1L;

    private boolean blockWhenExhausted = true;

    private int timeOut = 10000;

}
