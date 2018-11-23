package com.cqwo.wechat.open.impl;

import com.cqwo.wechat.open.WxOpenService;
import com.cqwo.wechat.open.config.WxOpenConfigStorage;
import com.cqwo.wechat.open.domain.WxOpenError;
import com.cqwo.wechat.open.domain.WxOpenOAuth2AccessToken;
import com.cqwo.wechat.open.exption.WxOpenErrorException;
import com.cqwo.wechat.open.utils.URIUtil;
import com.cqwo.wechat.open.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author cqnews
 */

public class WxOpenServiceImpl implements WxOpenService {


    private WxOpenConfigStorage wxOpenConfigStorage;


    private Logger logger = LoggerFactory.getLogger(WxOpenServiceImpl.class);


    public void setWxOpenConfigStorage(WxOpenConfigStorage wxOpenConfigStorage) {
        this.wxOpenConfigStorage = wxOpenConfigStorage;
    }

    /**
     * 获取用户code
     * https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect
     *
     * @return
     */
    public String oauth2GetCodeUrl(String redirectURI) {
        return String.format(CONNECT_OAUTH2_CODE_URL,
                wxOpenConfigStorage.getAppId(),
                URIUtil.encodeURIComponent(redirectURI),
                "snsapi_login", wxOpenConfigStorage.getToken());
    }


    /**
     * 获取授权accesstoken
     * <p>
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
     *
     * @param code  code code
     * @param token token token
     * @return
     */
    public WxOpenOAuth2AccessToken oauth2GetAccessToken(String code, String token) throws WxOpenErrorException {

        String url = String.format(OAUTH2_ACCESS_TOKEN_URL,
                wxOpenConfigStorage.getAppId(),
                wxOpenConfigStorage.getSecret(),
                code);

        String responseText = null;
        try {
            responseText = WebUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }


        return WxOpenOAuth2AccessToken.fromJson(responseText);
    }

    /**
     * 刷新或续期access_token使用
     * https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s
     *
     * @param refreshToken refresh_token
     * @return
     */
    public WxOpenOAuth2AccessToken oauth2RefreshAccessToken(String refreshToken) throws WxOpenErrorException {
        String url = String.format(OAUTH2_ACCESS_REFRESH_TOKEN_URL,
                wxOpenConfigStorage.getAppId(),
                refreshToken);

        String responseText = null;
        try {
            responseText = WebUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }

        return WxOpenOAuth2AccessToken.fromJson(responseText);
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s
     *
     * @param accessToken
     * @return
     */
    public boolean oauth2CheckAccessToken(String accessToken, String openId) throws WxOpenErrorException {

        String url = String.format(OAUTH2_ACCESS_CHECK_TOKEN_URL,
                accessToken, openId);

        String responseText = null;
        try {
            responseText = WebUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WxOpenErrorException("微信微信异常");
        }

        return WxOpenError.fromJson(responseText).isCorrect();
    }


}
