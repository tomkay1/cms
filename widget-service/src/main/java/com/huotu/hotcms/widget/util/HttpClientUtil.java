/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作为http 请求发起的工具类
 * Created by elvis on 2016-6-7.
 */
public class HttpClientUtil {

    private static final Log log = LogFactory.getLog(HttpClientUtil.class);

    private static HttpClientUtil httpClientUtil = new HttpClientUtil();
    private CloseableHttpClient httpClient = null;

    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        return httpClientUtil;
    }

    private void initHttpClient() {
        httpClient = HttpClients.createDefault();
    }

    /**
     * @param url        post 请求的路径
     * @param requestMap 请求参数
     * @return
     * @throws IOException 不抛出异常即为成功
     */
    public CloseableHttpResponse post(String url, Map<String, Object> requestMap) throws IOException {
        initHttpClient();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        requestMap.forEach((key, value) -> {
            if (value != null) {
                nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(value)));
            }
        });
        HttpPost httpPost = new HttpPost(url);
        HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");

        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return response;
    }

    /**
     * @param url        post 请求的路径
     * @param requestMap 请求参数
     * @return
     * @throws IOException 不抛出异常即为成功
     */
    public CloseableHttpResponse get(String url, Map requestMap) throws IOException {
        initHttpClient();
        StringBuilder finalUrl = new StringBuilder(url);
        Iterator iterator = requestMap.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue() != null) {
                if (index == 0) {
                    finalUrl.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    index++;
                } else {
                    finalUrl.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                }
            }
        }
        HttpGet httpGet = new HttpGet(finalUrl.toString());
        return httpClient.execute(httpGet);
    }


    /**
     * 下载jar 到本地
     *
     * @param url          发起下载的url路径
     * @param outputStream 输出目的地
     */
    public void webGet(String url, OutputStream outputStream) throws IOException {
        try (CloseableHttpResponse response = get(url, new HashMap<>())) {
            StreamUtils.copy(response.getEntity().getContent(), outputStream);
        }
    }

}
