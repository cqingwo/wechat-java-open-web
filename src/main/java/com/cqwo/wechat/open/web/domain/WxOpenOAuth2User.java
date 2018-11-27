package com.cqwo.wechat.open.web.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.cqwo.wechat.open.web.exption.WxOpenErrorException;
import lombok.*;

import java.io.Serializable;

/**
 * @author cqnews
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxOpenOAuth2User implements Serializable {

    private static final long serialVersionUID = -4785849546352239406L;

// "openid": "oLVPpjqs9BhvzwPj5A-vTYAX3GLc",
//         "nickname": "刺猬宝宝",
//         "sex": 1,
//         "language": "简体中文",
//         "city": "深圳",
//         "province": "广东",
//         "country": "中国",
//         "headimgurl": "http://wx.qlogo.cn/mmopen/utpKYf69VAbCRDRlbUsPsdQN38DoibCkrU6SAMCSNx558eTaLVM8PyM6jlEGzOrH67hyZibIZPXu4BK1XNWzSXB3Cs4qpBBg18/0",
//         "privilege": []


    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    @JSONField(name = "openid")
    private String openId = "";

    /**
     * 用户昵称
     */
    @JSONField(name = "nickname")
    private String nickName = "";

    /**
     * 普通用户性别，1为男性，2为女性
     */
    @JSONField(name = "sex")
    private Integer sex = 0;

    /**
     * 语言
     */
    @JSONField(name = "language")
    private String language = "";

    /**
     * 国家，如中国为CN
     */
    @JSONField(name = "country")
    private String country = "";

    /**
     * 普通用户个人资料填写的省份
     */
    @JSONField(name = "province")
    private String province = "";

    /**
     * 普通用户个人资料填写的城市
     */
    @JSONField(name = "city")
    private String city = "";

    /**
     * 用户头像，最后一个数值代表正方形头像大小
     */
    @JSONField(name = "headimgurl")
    private String headimgurl = "";

    /**
     * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     */
    @JSONField(name = "privilege")
    private String privilege = "";

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    @JSONField(name = "unionid")
    private String unionId = "";

    /**
     * 用户信息
     *
     * @param json json
     * @return
     * @throws WxOpenErrorException
     */
    public static WxOpenOAuth2User fromJson(String json) throws WxOpenErrorException {

        try {

            return JSON.parseObject(json, WxOpenOAuth2User.class);

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
