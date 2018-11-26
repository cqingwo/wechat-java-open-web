package com.cqwo.wechat.open.web.impl;

import com.cqwo.wechat.open.web.WxOpenService;
import com.cqwo.wechat.open.web.config.WxOpenConfigStorage;
import com.cqwo.wechat.open.web.domain.WxOpenError;
import com.cqwo.wechat.open.web.domain.WxOpenOAuth2AccessToken;
import com.cqwo.wechat.open.web.domain.WxOpenOAuth2User;
import com.cqwo.wechat.open.web.exption.WxOpenErrorException;
import com.cqwo.wechat.open.web.exption.WxOpenExpiresTimeException;
import com.cqwo.wechat.open.web.utils.URIUtil;
import com.cqwo.wechat.open.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author cqnews
 */
public class WxOpenServiceImpl implements WxOpenService {

    private WxOpenConfigStorage defaultWxOpenConfigStorage = null;

    private Map<String, WxOpenConfigStorage> linkedHashMap = new LinkedHashMap<>();


    private Logger logger = LoggerFactory.getLogger(WxOpenServiceImpl.class);


    /**
     * 配置参数
     *
     * @param wxOpenConfigStorage 参数
     */
    @Override
    public void putWxOpenConfigStorage(WxOpenConfigStorage wxOpenConfigStorage) throws WxOpenErrorException {

        this.linkedHashMap.put(wxOpenConfigStorage.getAppId(), wxOpenConfigStorage);

        if (getDefaultWxOpenConfigStorage() == null) {
            this.defaultWxOpenConfigStorage = wxOpenConfigStorage;
        }
    }

    /**
     * 获取appId
     *
     * @return appId
     * @throws WxOpenErrorException w异常
     */
    @Override
    public String getAppId() throws WxOpenErrorException {
        return getDefaultWxOpenConfigStorage().getAppId();
    }

    /**
     * 获取appId配置参数
     *
     * @return WxOpenConfigStorage
     */
    @Override
    public WxOpenConfigStorage getDefaultWxOpenConfigStorage() throws WxOpenErrorException {

        try {
            return this.defaultWxOpenConfigStorage;
        } catch (Exception ex) {
            throw new WxOpenErrorException("参数初始化异常");
        }

    }


    /**
     * 获取appId配置参数
     *
     * @return WxOpenConfigStorage
     */
    @Override
    public WxOpenConfigStorage getWxOpenConfigStorage(String appId) throws WxOpenErrorException {

        try {
            return this.linkedHashMap.get(appId);
        } catch (Exception ex) {
            throw new WxOpenErrorException("参数初始化异常");
        }
    }

    /**
     * 获取accesstoken
     *
     * @return accesstoken
     */
    @Override
    public String getAccessToken() throws WxOpenErrorException {
        return getAccessToken(getDefaultWxOpenConfigStorage().getAppId());
    }

    /**
     * 获取accesstoken
     *
     * @param appId appId
     * @return accesstoken
     */
    @Override
    public String getAccessToken(String appId) throws WxOpenErrorException {

        WxOpenConfigStorage configStorage = getWxOpenConfigStorage(appId);

        configStorage.getAccessTokenLock().lock();

        try {
            return configStorage.getOAuth2AccessToken();

        } catch (WxOpenExpiresTimeException ex) {

            WxOpenOAuth2AccessToken oAuth2AccessToken = oauth2RefreshAccessToken(appId, ex.getRefreshToken());
            return oAuth2AccessToken.getAccessToken();

        } finally {

            configStorage.getAccessTokenLock().lock();
        }
    }

    /**
     * 获取用户code
     * https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect
     *
     * @param redirectUrl 返回地址
     * @return oauth2Url
     */
    @Override
    public String oauth2GetCodeUrl(String redirectUrl) throws WxOpenErrorException {
        return oauth2GetCodeUrl(getDefaultWxOpenConfigStorage().getAppId(), redirectUrl);
    }

    /**
     * 获取用户code
     * https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect
     *
     * @param appId       appId
     * @param redirectUrl 返回地址
     * @return oauth2Url
     */
    @Override
    public String oauth2GetCodeUrl(String appId, String redirectUrl) throws WxOpenErrorException {

        WxOpenConfigStorage configStorage = getWxOpenConfigStorage(appId);

        return String.format(CONNECT_OAUTH2_CODE_URL,
                configStorage.getAppId(),
                URIUtil.encodeURIComponent(redirectUrl),
                "snsapi_login", configStorage.getToken());
    }


    /**
     * 获取授权accesstoken
     * <p>
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
     *
     * @param code code
     * @return 微信用户token
     */
    @Override
    public WxOpenOAuth2AccessToken oauth2GetAccessToken(String code) throws WxOpenErrorException {
        return oauth2GetAccessToken(getDefaultWxOpenConfigStorage().getAppId(), code);
    }

    /**
     * 获取授权accesstoken
     * <p>
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
     *
     * @param code  code
     * @param appId appId
     * @return 微信用户token
     */
    @Override
    public WxOpenOAuth2AccessToken oauth2GetAccessToken(String appId, String code) throws WxOpenErrorException {

        WxOpenConfigStorage configStorage = getWxOpenConfigStorage(appId);

        String url = String.format(OAUTH2_ACCESS_TOKEN_URL,
                configStorage.getAppId(),
                configStorage.getSecret(),
                code);

        String responseText = "";
        try {
            responseText = WebUtils.get(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }

        WxOpenOAuth2AccessToken oauth2AccessToken = WxOpenOAuth2AccessToken.fromJson(responseText);

        setOAuth2AccessToken(configStorage.getAppId(), oauth2AccessToken);

        return oauth2AccessToken;
    }

    /**
     * 设置用户户token
     *
     * @param appId             appId
     * @param oauth2AccessToken token
     */
    private void setOAuth2AccessToken(String appId, WxOpenOAuth2AccessToken oauth2AccessToken) throws WxOpenErrorException {

        WxOpenConfigStorage configStorage = getWxOpenConfigStorage(appId);
        configStorage.setOAuth2AccessToken(oauth2AccessToken);

    }

    /**
     * 刷新或续期access_token使用
     * https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s
     *
     * @param refreshToken refresh_token
     */
    @Override
    public WxOpenOAuth2AccessToken oauth2RefreshAccessToken(String refreshToken) throws WxOpenErrorException {
        return oauth2RefreshAccessToken(getDefaultWxOpenConfigStorage().getAppId(), refreshToken);
    }

    /**
     * 刷新或续期access_token使用
     * https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s
     *
     * @param appId
     * @param refreshToken refresh_token
     */
    @Override
    public WxOpenOAuth2AccessToken oauth2RefreshAccessToken(String appId, String refreshToken) throws WxOpenErrorException {


        WxOpenConfigStorage configStorage = getWxOpenConfigStorage(appId);

        String url = String.format(OAUTH2_ACCESS_REFRESH_TOKEN_URL,
                configStorage.getAppId(),
                refreshToken);

        String responseText = "";
        try {
            responseText = WebUtils.get(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }

        WxOpenOAuth2AccessToken oauth2AccessToken = WxOpenOAuth2AccessToken.fromJson(responseText);

        setOAuth2AccessToken(configStorage.getAppId(), oauth2AccessToken);
        return oauth2AccessToken;
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s
     *
     * @param appId appId
     */
    @Override
    public boolean oauth2CheckAccessToken(String appId, String openId) throws WxOpenErrorException {


        String url = String.format(OAUTH2_ACCESS_CHECK_TOKEN_URL,
                getAccessToken(appId), openId);

        String responseText = "";
        try {
            responseText = WebUtils.get(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }

        return WxOpenError.fromJson(responseText).isCorrect();
    }

    /**
     * 获取用户信息
     *
     * @param openId openid
     */
    @Override
    public WxOpenOAuth2User oauth2GetUserinfo(String openId) throws WxOpenErrorException {
        return oauth2GetUserinfo(getDefaultWxOpenConfigStorage().getAppId(), openId);
    }

    /**
     * 获取用户信息
     *
     * @param appId  appId
     * @param openId openid
     */
    @Override
    public WxOpenOAuth2User oauth2GetUserinfo(String appId, String openId) throws WxOpenErrorException {

        String url = String.format(OAUTH2_ACCESS_USER_URL,
                getAccessToken(appId), openId);

        String responseText = "";
        try {

            responseText = WebUtils.get(url);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WxOpenErrorException("获取用户信息");
        }

        return WxOpenOAuth2User.fromJson(responseText);

    }


}
