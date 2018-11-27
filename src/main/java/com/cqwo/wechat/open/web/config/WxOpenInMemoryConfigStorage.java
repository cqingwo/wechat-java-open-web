package com.cqwo.wechat.open.web.config;

import com.cqwo.wechat.open.web.domain.WxOpenOAuth2AccessToken;
import com.cqwo.wechat.open.web.exption.WxOpenExpiresTimeException;
import lombok.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WxOpenInMemoryConfigStorage implements WxOpenConfigStorage {

    /**
     * AppId
     */
    private String appId = "";

    /**
     * App密钥
     */
    private String secret = "";

    /**
     * token
     */
    private String token = "";

    /**
     * accessToken
     */
    private String accessToken = "";

    /**
     * 刷新token
     */
    private String refreshToken = "";


    /**
     * accessToken过期时间
     */
    private long expiresIn = -1;


    /**
     * 超时时间
     */
    private long expiresTime = 0;

    /**
     * 微信token
     */
    private WxOpenOAuth2AccessToken wxOpenOAuth2AccessToken;

    /**
     * accessToken锁
     */
    protected Lock accessTokenLock = new ReentrantLock();

    @Override
    public String getOAuth2AccessToken() throws WxOpenExpiresTimeException {

        if (expiresTime <= getUnixTimeStamp()) {
            throw new WxOpenExpiresTimeException("token过期,重新刷新", this.refreshToken);
        }

        return accessToken;
    }

    @Override
    public void setOAuth2AccessToken(WxOpenOAuth2AccessToken token) {
        this.accessToken = token.getAccessToken();
        this.expiresIn = token.getExpiresIn();
        this.expiresTime = getUnixTimeStamp() + expiresIn - 10 * 600;
        this.refreshToken = token.getRefreshToken();
        wxOpenOAuth2AccessToken = token;
    }

    /**
     * 获取unix时间戳格式
     *
     * @return
     */
    public static Integer getUnixTimeStamp() {

        return Math.toIntExact(System.currentTimeMillis() / 1000);

    }

}
