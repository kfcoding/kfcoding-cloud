package com.cuiyun.kfcoding.message.core.bootstrap;

import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.service.InitService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 13:57
 **/
public class TransactionBootstrap extends AutoConfig implements ApplicationContextAware{

    private com.cuiyun.kfcoding.message.core.service.InitService InitService;

    @Autowired
    public TransactionBootstrap(final InitService InitService) {
        this.InitService = InitService;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.getInstance().setCfgContext((ConfigurableApplicationContext) applicationContext);
        start(this);
    }

    private void start(final AutoConfig AutoConfig) {
        InitService.initialization(AutoConfig);
    }
}
