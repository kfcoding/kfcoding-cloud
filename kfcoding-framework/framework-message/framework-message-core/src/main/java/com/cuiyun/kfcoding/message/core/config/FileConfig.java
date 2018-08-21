package com.cuiyun.kfcoding.message.core.config;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 11:34
 **/
@Data
public class FileConfig {
    /**
     * 文件保存路径
     */
    private String path;

    /**
     * 文件前缀
     */
    private String prefix;
}
