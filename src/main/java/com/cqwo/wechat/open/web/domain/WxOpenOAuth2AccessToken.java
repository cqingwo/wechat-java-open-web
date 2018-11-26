package com.cqwo.wechat.open.web.domain;

import com.alibaba.fastjson.JSON;
import com.cqwo.wechat.open.web.exption.WxOpenErrorException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author cqnews
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WxOpenOAuth2AccessToken implements Serializable {


    private static final long serialVersionUID = -3281632348392344735L;

    /**
     * 接口调用凭证
     */
    private String accessToken = "";

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private int expiresIn = -1;

    /**
     * 用户刷新access_token
     */
    private String refreshToken = "";


    /**
     * 授权用户唯一标识
     */
    private String openId = "";

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope = "";

    /**
     * https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&announce_id=11513156443eZYea&version=&lang=zh_CN.
     * 本接口在scope参数为snsapi_base时不再提供unionID字段。
     */
    private String unionId = "";

    public static WxOpenOAuth2AccessToken fromJson(String json) throws WxOpenErrorException {
        try {
            return JSON.parseObject(json, WxOpenOAuth2AccessToken.class);
        } catch (Exception ex) {


            WxOpenError openError = null;
            try {
                openError = JSON.parseObject(json, WxOpenError.class);
            } catch (Exception ex2) {
                openError = new WxOpenError();
            }


            throw new WxOpenErrorException(openError, "accessToken获取失败");
        }
    }


}