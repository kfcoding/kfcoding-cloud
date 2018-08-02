package com.cuiyun.kfcoding.auth.interceptor;

import com.cuiyun.kfcoding.auth.config.ClientConfig;
import com.cuiyun.kfcoding.auth.service.AuthClientService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by bifenglin on 2017/9/12.
 */
public class ClientTokenInterceptor implements RequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(ClientTokenInterceptor.class);
    @Autowired
    private ClientConfig clientConfig;
    @Autowired
    private AuthClientService authClientService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            requestTemplate.header(clientConfig.getClientTokenHeader(), authClientService.apply(clientConfig.getClientId(), clientConfig.getClientSecret()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
