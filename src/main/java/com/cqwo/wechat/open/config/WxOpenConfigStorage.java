package com.cqwo.wechat.open.config;

public interface WxOpenConfigStorage {


    /**
     * 获取appid
     */
    String getAppId();


    /**
     * 获取密钥
     */
    String getSecret();

    /**
     * 获取token
     */
    String getToken();
}
