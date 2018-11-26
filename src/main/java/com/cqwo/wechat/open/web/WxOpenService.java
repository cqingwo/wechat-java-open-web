package com.cqwo.wechat.open.web;

import com.cqwo.wechat.open.web.config.WxOpenConfigStorage;
import com.cqwo.wechat.open.web.domain.WxOpenOAuth2AccessToken;
import com.cqwo.wechat.open.web.domain.WxOpenOAuth2User;
import com.cqwo.wechat.open.web.exption.WxOpenErrorException;

public interface WxOpenService {


    /**
     * 请求CODE
     */
    String CONNECT_OAUTH2_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    //https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect

    /**
     * code获取access_tokencode获取access_token
     */
    String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 刷新用户token,未到期不更新token
     */
    String OAUTH2_ACCESS_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";


    /**
     * 检验token是否有效
     */
    String OAUTH2_ACCESS_CHECK_TOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";

    /**
     * 检验token是否有效
     */
    String OAUTH2_ACCESS_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";


    /**
     * 配置参数
     *
     * @param wxOpenConfigStorage 参数
     */
    void putWxOpenConfigStorage(WxOpenConfigStorage wxOpenConfigStorage) throws WxOpenErrorException;

    /**
     * 获取默认微信配置
     *
     * @return WxOpenConfigStorage
     */
    WxOpenConfigStorage getDefaultWxOpenConfigStorage() throws WxOpenErrorException;

    /**
     * 获取appId
     *
     * @return appId
     * @throws WxOpenErrorException w异常
     */
    String getAppId() throws WxOpenErrorException;

    /**
     * 获取accesstoken
     *
     * @return accesstoken
     */
    String getAccessToken() throws WxOpenErrorException;

    /**
     * 获取accesstoken
     *
     * @param appId appId
     * @return accesstoken
     */
    String getAccessToken(String appId) throws WxOpenErrorException;

    /**
     * 获取用户code
     * https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect
     */
    String oauth2GetCodeUrl(String redirectUrl) throws WxOpenErrorException;

    /**
     * 获取用户code
     *
     * @param appId       appId
     * @param redirectUrl 返回地址
     *                    https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect
     */
    String oauth2GetCodeUrl(String appId, String redirectUrl) throws WxOpenErrorException;

    /**
     * 获取appId配置参数
     *
     * @param appId appId
     */
    WxOpenConfigStorage getWxOpenConfigStorage(String appId) throws WxOpenErrorException;


    /**
     * 获取授权accesstoken
     * <p>
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
     *
     * @param code code
     */
    WxOpenOAuth2AccessToken oauth2GetAccessToken(String code) throws WxOpenErrorException;

    /**
     * 获取授权accesstoken
     * <p>
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
     *
     * @param code  code
     * @param appId appId
     */
    WxOpenOAuth2AccessToken oauth2GetAccessToken(String appId, String code) throws WxOpenErrorException;

    /**
     * 刷新或续期access_token使用
     * https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s
     *
     * @param refreshToken refresh_token
     */
    WxOpenOAuth2AccessToken oauth2RefreshAccessToken(String refreshToken) throws WxOpenErrorException;

    /**
     * 刷新或续期access_token使用
     * https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s
     *
     * @param appId        appId
     * @param refreshToken refresh_token
     */
    WxOpenOAuth2AccessToken oauth2RefreshAccessToken(String appId, String refreshToken) throws WxOpenErrorException;


    /**
     * 检验授权凭证（access_token）是否有效
     * https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s
     *
     * @param accessToken accessToken
     */
    boolean oauth2CheckAccessToken(String accessToken, String openId) throws WxOpenErrorException;


    /**
     * 获取用户信息
     *
     * @param openId openid
     */
    WxOpenOAuth2User oauth2GetUserinfo(String openId) throws WxOpenErrorException;


    /**
     * 获取用户信息
     *
     * @param appId  appId
     * @param openId openid
     */
    WxOpenOAuth2User oauth2GetUserinfo(String appId, String openId) throws WxOpenErrorException;
}
