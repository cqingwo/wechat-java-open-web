package com.cqwo.wechat.open.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class WxOpenError {

    private String errcode = "-1";

    private String errmsg = "不可预料的错误";

    public WxOpenError() {
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
