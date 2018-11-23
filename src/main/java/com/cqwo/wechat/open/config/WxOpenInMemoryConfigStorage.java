package com.cqwo.wechat.open.config;

import lombok.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Setter
@Getter
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


    protected volatile String jsapiTicket;

    protected volatile long jsapiTicketExpiresTime;

    protected Lock accessTokenLock = new ReentrantLock();

    protected Lock jsapiTicketLock = new ReentrantLock();


}
