package com.cuiyun.kfcoding.framework.cache.test;

import com.cuiyun.kfcoding.framework.cache.EnableKfcodingCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by bifenglin.
 */
@SpringBootApplication
@EnableKfcodingCache
public class CacheTest {
    public static void main(String args[]) {
        SpringApplication app = new SpringApplication(CacheTest.class);
        app.run(args);
    }

}
