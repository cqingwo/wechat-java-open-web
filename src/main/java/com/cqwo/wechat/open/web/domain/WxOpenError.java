package com.cqwo.wechat.open.web.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cqnews
 */
@Data
public class WxOpenError implements Serializable {

    private static final long serialVersionUID = 3768816519685938894L;

    private String errcode = "-1";

    private String errmsg = "不可预料的错误";

    WxOpenError() {
    }

    public WxOpenError(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public static WxOpenError fromJson(String json) {

        WxOpenError openError = null;
        try {
            openError = JSON.parseObject(json, WxOpenError.class);
        } catch (Exception ex2) {
            openError = new WxOpenError();
        }

        return openError;
    }

    public boolean isCorrect() {
        return "0".equals(errcode);
    }
}
