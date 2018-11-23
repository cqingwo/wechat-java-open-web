package com.cqwo.wechat.open.exption;

import com.cqwo.wechat.open.domain.WxOpenError;

/**
 * 微信异常
 *
 * @author cqnews
 */
public class WxOpenErrorException extends Exception {

    private static final long serialVersionUID = 6328689904021989135L;

    private WxOpenError wxOpenError;

    public WxOpenErrorException(WxOpenError wxOpenError) {

        super();
        this.wxOpenError = wxOpenError;
    }

    public WxOpenErrorException(WxOpenError wxOpenError, String message) {
        super(message);
        this.wxOpenError = wxOpenError;
    }

    public WxOpenErrorException(String message) {
        super(message);
        this.wxOpenError = new WxOpenError("-1","网络异常");
    }

    public WxOpenError getWxOpenError() {
        return wxOpenError;
    }

    public void setWxOpenError(WxOpenError wxOpenError) {
        this.wxOpenError = wxOpenError;
    }
}
