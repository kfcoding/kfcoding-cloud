
package com.cuiyun.kfcoding.auth.client.jwt;

import com.cuiyun.kfcoding.auth.client.config.ServiceAuthConfig;
import com.cuiyun.kfcoding.auth.client.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.auth.client.feign.ServiceAuthFeign;
import com.cuiyun.kfcoding.auth.common.util.jwt.IJWTInfo;
import com.cuiyun.kfcoding.auth.common.util.jwt.JWTHelper;
import com.cuiyun.kfcoding.common.exception.auth.ClientTokenException;
import com.cuiyun.kfcoding.common.msg.BaseResponse;
import com.cuiyun.kfcoding.common.msg.ObjectRestResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: 服务鉴权工具类
 * @author: maple
 * @create: 2018-08-02 16:31
 **/
@Configuration
@Slf4j
@EnableScheduling
public class  ServiceAuthUtil{
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    private List<String> allowedClient;
    private String clientToken;


    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            return JWTHelper.getInfoFromToken(token, serviceAuthConfig.getPubKeyByte());
        } catch (ExpiredJwtException ex) {
            throw new ClientTokenException(BizExceptionEnum.TOKEN_EXPIRED);
        } catch (SignatureException ex) {
            throw new ClientTokenException(BizExceptionEnum.TOKEN_SIGNATURE);
        } catch (IllegalArgumentException ex) {
            throw new ClientTokenException(BizExceptionEnum.TOKEN_EMPTY);
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void refreshAllowedClient() {
        log.debug("refresh allowedClient.....");
        BaseResponse resp = serviceAuthFeign.getAllowedClient(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 200) {
            ObjectRestResponse<List<String>> allowedClient = (ObjectRestResponse<List<String>>) resp;
            this.allowedClient = allowedClient.getData();
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshClientToken() {
        log.debug("refresh client token.....");
        BaseResponse resp = serviceAuthFeign.getAccessToken(serviceAuthConfig.getClientId(), serviceAuthConfig.getClientSecret());
        if (resp.getStatus() == 200) {
            ObjectRestResponse<String> clientToken = (ObjectRestResponse<String>) resp;
            this.clientToken = clientToken.getData();
        }
    }


    public String getClientToken() {
        if (this.clientToken == null) {
            this.refreshClientToken();
        }
        return clientToken;
    }

    public List<String> getAllowedClient() {
        if (this.allowedClient == null) {
            this.refreshAllowedClient();
        }
        return allowedClient;
    }
}