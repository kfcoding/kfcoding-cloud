package com.cuiyun.kfcoding.generator.config;

import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 默认的代码生成的配置
 *
 * @author maple
 * @date 2018-10-28-下午8:27
 */
public class KfCodingGeneratorConfig extends AbstractGeneratorConfig {

    protected void globalConfig() {
        globalConfig.setOutputDir("/Users/maple/Documents/kfcoding");//写自己项目的绝对路径,注意具体到java目录
        globalConfig.setFileOverride(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setOpen(false);
        globalConfig.setAuthor("maple");
    }

    protected void dataSourceConfig() {
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setUrl("jdbc:mysql://localhost/kfcoding_user_dev?characterEncoding=utf8");
    }

    protected void strategyConfig() {
        strategyConfig.setTablePrefix(new String[]{"user_"});// 此处可以修改为您的表前缀
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
    }

    protected void packageConfig() {
        packageConfig.setParent(null);
        packageConfig.setEntity("com.cuiyun.kfcoding.basic.model");
        packageConfig.setMapper("com.cuiyun.kfcoding.basic.dao");
        packageConfig.setXml("com.cuiyun.kfcoding.basic.dao.mapping");
    }

    protected void contextConfig() {
        contextConfig.setProPackage("com.cuiyun.kfcoding.basic");
        contextConfig.setCoreBasePackage("com.cuiyun.kfcoding.core");
        contextConfig.setModuleName("user");
        contextConfig.setProjectPath("/Users/maple/Documents/kfcoding/kfcoding");//写自己项目的绝对路径

        /**
         * mybatis-plus 生成器开关
         */
        contextConfig.setEntitySwitch(true);
        contextConfig.setDaoSwitch(true);
        contextConfig.setServiceSwitch(true);
    }

    @Override
    protected void config() {
        globalConfig();
        dataSourceConfig();
        strategyConfig();
        packageConfig();
        contextConfig();
    }
}
