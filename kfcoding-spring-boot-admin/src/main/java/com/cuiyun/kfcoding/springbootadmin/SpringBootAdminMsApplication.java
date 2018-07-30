package com.cuiyun.kfcoding.springbootadmin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableAdminServer
@SpringCloudApplication
public class SpringBootAdminMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminMsApplication.class, args);
    }
}
