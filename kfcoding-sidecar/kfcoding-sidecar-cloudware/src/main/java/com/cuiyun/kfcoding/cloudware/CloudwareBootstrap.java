package com.cuiyun.kfcoding.cloudware;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

/**
 * @author maple
 * @date 2018
 */
@EnableSidecar
@SpringCloudApplication
public class CloudwareBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(CloudwareBootstrap.class, args);
    }
}
