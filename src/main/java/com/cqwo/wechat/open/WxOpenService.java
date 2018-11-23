package com.cqwo.wechat.open;

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

}
