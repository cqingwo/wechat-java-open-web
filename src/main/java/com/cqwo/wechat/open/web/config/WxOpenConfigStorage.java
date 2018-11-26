package com.cqwo.wechat.open.web.config;

import com.cqwo.wechat.open.web.domain.WxOpenOAuth2AccessToken;
import com.cqwo.wechat.open.web.exption.WxOpenExpiresTimeException;

import java.util.concurrent.locks.Lock;

public interface WxOpenConfigStorage {


    /**
     * 设置appid
     */
    void setAppId(String appId);

    /**
     * 获取appid
     */
    String getAppId();


    /**
     * 设置密钥
     */
    void setSecret(String secret);

    /**
     * 获取密钥
     */
    String getSecret();

    /**
     * 设置密钥
     */
    void setToken(String token);

    /**
     * 获取token
     */
    String getToken();


    /**
     * 获取线程锁
     *
     * @return
     */
    Lock getAccessTokenLock();

    /**
     * 获取accessToken
     *
     * @return
     */
    String getOAuth2AccessToken() throws WxOpenExpiresTimeException;


    void setOAuth2AccessToken(WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken);


}
