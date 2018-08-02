package com.cuiyun.kfcoding.auth.interceptor;

import com.cuiyun.kfcoding.auth.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.auth.common.util.jwt.IJWTInfo;
import com.cuiyun.kfcoding.auth.config.ClientConfig;
import com.cuiyun.kfcoding.auth.service.AuthClientService;
import com.cuiyun.kfcoding.auth.util.client.ClientTokenUtil;
import com.cuiyun.kfcoding.common.exception.auth.ClientForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bifenglin on 2017/9/12.
 */
@SuppressWarnings("ALL")
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(ServiceAuthRestInterceptor.class);

    @Autowired
    private ClientTokenUtil clientTokenUtil;
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private ClientConfig clientConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(clientConfig.getClientTokenHeader());
        IJWTInfo infoFromToken = clientTokenUtil.getInfoFromToken(token);
        String uniqueName = infoFromToken.getUniqueName();
        for(String client: authClientService.getAllowedClient(clientConfig.getClientId())){
            if(client.equals(uniqueName)){
                return super.preHandle(request, response, handler);
            }
        }
        throw new ClientForbiddenException(BizExceptionEnum.CLIENT_FORBIDDEN);
    }
}
