package com.cuiyun.kfcoding.trace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
@SpringBootApplication
@EnableZipkinServer
public class TraceBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(TraceBootstrap.class,args);
    }
}
