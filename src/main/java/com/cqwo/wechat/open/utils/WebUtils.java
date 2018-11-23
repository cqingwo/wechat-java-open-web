package com.cqwo.wechat.open.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class WebUtils {


    /**
     * 日志
     */
    private static Logger looger = LoggerFactory.getLogger(WebUtils.class);


    /**
     * post数据提交
     *
     * @param httpUrl 请求的地址
     * @param params  请求的参数
     * @return
     * @throws Exception
     */
    public static String post(String httpUrl, MultiValueMap<String, String> params) throws Exception {

        /**
         * 客户端
         */
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8,GB2312;q=0.7,*;q=0.7")
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-cn,zh;q=0.5")
                .defaultHeader(HttpHeaders.CONNECTION, "Keep-Alive")
                .defaultHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .filter(ExchangeFilterFunctions
                        .basicAuthentication("user", "password"))
                .filter((clientRequest, next) -> {
                    looger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers()
                            .forEach((name, values) -> values.forEach(value -> looger.info("{}={}", name, value)));
                    return next.exchange(clientRequest);
                })
                .build();

        int i = 1;

        ClientResponse resp = null;

        HttpStatus status = HttpStatus.CONTINUE;


        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }


        List<Long> waitList = Arrays.asList(1L, 3L, 4L, 7L, 11L, 18L);

        while (!(status.is2xxSuccessful()) && i < waitList.size()) {


            try {
                resp = webClient.post().uri(httpUrl).body(BodyInserters.fromFormData(params)).exchange().block();
                status = resp != null ? resp.statusCode() : HttpStatus.CONTINUE;

                looger.error("第" + i + "次推送,推送结果：" + (resp != null ? resp.toString() : "无"));

            } catch (Exception ex) {

                looger.info("第" + (i + 1) + "次推送.");
            }
            TimeUnit.SECONDS.sleep(waitList.get(i));
            i += 1;
        }

        if (status.is2xxSuccessful()) {
            return Objects.requireNonNull(resp).bodyToMono(String.class).block();
        }


        //looger.info("result:{}", resp.block());
        throw new IllegalStateException("数据请求失败");

    }


    /**
     * get数据提交
     *
     * @param httpUrl 请求的地址
     */
    public static String get(String httpUrl) throws Exception {

        /**
         * 客户端
         */
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8,GB2312;q=0.7,*;q=0.7")
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-cn,zh;q=0.5")
                .defaultHeader(HttpHeaders.CONNECTION, "Keep-Alive")
                .defaultHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .filter(ExchangeFilterFunctions
                        .basicAuthentication("user", "password"))
                .filter((clientRequest, next) -> {
                    looger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers()
                            .forEach((name, values) -> values.forEach(value -> looger.info("{}={}", name, value)));
                    return next.exchange(clientRequest);
                })
                .build();

        int i = 0;

        ClientResponse resp = null;

        HttpStatus status = HttpStatus.CONTINUE;


        List<Long> waitList = Arrays.asList(1L, 3L, 4L, 7L, 11L, 18L);

        while (!(status.is2xxSuccessful()) && i < waitList.size()) {


            try {
                resp = webClient.get().uri(httpUrl).exchange().block();
                status = resp != null ? resp.statusCode() : HttpStatus.CONTINUE;
                looger.info("第" + (i + 1) + "次推送,推送结果：" + (resp != null ? resp.toString() : "无"));

            } catch (Exception ignored) {
                looger.info("第" + (i + 1) + "次推送.");
            }

            TimeUnit.SECONDS.sleep(waitList.get(i));
            i += 1;
        }

        if (status.is2xxSuccessful()) {
            return Objects.requireNonNull(resp).bodyToMono(String.class).block();
        }

        //looger.info("result:{}", resp.block());
        throw new IllegalStateException("数据请求失败");
    }

    public static void main(String[] args) throws Exception {

        String s = get("http://www.cqwo.com/");
        System.out.println(s);
    }


}
