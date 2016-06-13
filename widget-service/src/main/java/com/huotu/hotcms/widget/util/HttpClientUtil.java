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
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

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
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return response;
    }


    /**
     * 下载jar 到本地
     *
     * @param url  发起下载的url路径
     * @param path path 存到本地的路径
     */
    public boolean downloadJar(String url, String path) throws IOException {

        //读取文件
        CloseableHttpResponse response = null;
        byte[] result = null;
        response = get(url, new HashMap<>());
        result = EntityUtils.toByteArray(response.getEntity());

        // 写入文件
        File f = new File(path);
        // 创建文件路径
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(path));
        bw.write(result);

        //关闭连接
        close(response);
        return true;
    }

    //提供一个关闭连接和流的方法
    //// TODO: 2016/6/7
    public void close(CloseableHttpResponse response) throws IOException {
        if (response != null) {
            EntityUtils.consume(response.getEntity());
            response.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }
}
