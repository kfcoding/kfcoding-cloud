package com.cuiyun.kfcoding.basic.util;

import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.hutool.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: kfcoding
 * @description: guthub授权
 * @author: maple
 * @create: 2018-05-07 10:46
 **/
public class OauthGithub{
    private static Logger log = LoggerFactory.getLogger(OauthGithub.class);
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_INFO_URL = "https://api.github.com/user";
    private static OauthGithub oauthGithub = new OauthGithub();
    @Value("${kfcoding.oauth。github.clientId}")
    private String clientId;
    @Value("${kfcoding.oauth。github.clientSecret}")
    private String clientSecret;

    /**
     * 用于链式操作
     * @return
     */
    public static OauthGithub me() {
        return oauthGithub;
    }

    public OauthGithub() {
    }

//    /**
//     * @throws UnsupportedEncodingException
//     * 获取授权url
//     * @param @return    设定文件
//     * @return String    返回类型
//     * @throws
//     */
//
//    private HttpRequest getAuthorizeUrl(String state) throws UnsupportedEncodingException {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("response_type", "code");
//        params.put("client_id", openid_github);
//        // params.put("redirect_uri", getRedirectUri());
//        if (StringUtils.isNotBlank(state)) {
//            params.put("state", state); //OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
//        }
//        return getAuthorizeUrl(AUTH_URL, params);
//    }

    /**
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * 获取token
     * @param @param code
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public String getTokenByCode(String code) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "authorization_code");
        // params.put("redirect_uri", getRedirectUri());
        String response = HttpRequest.post(TOKEN_URL).form(params)
                .header(Header.ACCEPT, "application/json")
                .execute().body();
        String accessToken = null;
        try {
            JSONObject json = JSONObject.parseObject(response);
            if (null != json) {
                accessToken = json.getString("access_token");
            }
        } catch (Exception e) {
            Matcher m = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)&refresh_token=(\\w+)$").matcher(response);
            if (m.find()) {
                accessToken = m.group(1);
            } else {
                Matcher m2 = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)$").matcher(response);
                if (m2.find()) {
                    accessToken = m2.group(1);
                }
            }
        }
        log.debug(accessToken);
        return accessToken;
    }

    /**
     *  获取用户信息
     * @param accessToken
     * @return
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public JSONObject getUserInfo(String accessToken) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
        String userInfo = HttpRequest.post(USER_INFO_URL).header("Authorization", "token " + accessToken).execute().body();
        JSONObject dataMap = JSON.parseObject(userInfo);
        log.debug(dataMap.toJSONString());
        return dataMap;
    }

    /**
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * 根据code一步获取用户信息
     * @param @param args    设定文件
     * @return void    返回类型
     * @throws
     */
    public JSONObject getUserInfoByCode(String code) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException{
        String accessToken = getTokenByCode(code);
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        JSONObject dataMap = getUserInfo(accessToken);
        dataMap.put("access_token", accessToken);
        log.debug(dataMap.toString());
        return dataMap;
    }
}
