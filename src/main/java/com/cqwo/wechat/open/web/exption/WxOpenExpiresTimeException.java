package com.cqwo.wechat.open.web.exption;

/**
 * @author cqnews
 */
public class WxOpenExpiresTimeException extends WxOpenErrorException {

    private static final long serialVersionUID = 7259803141371581703L;

    private String refreshToken = "";

    public String getRefreshToken() {
        return refreshToken;
    }


    public WxOpenExpiresTimeException(String refreshToken, String message) {
        super(message);
        this.refreshToken = refreshToken;
    }

}
