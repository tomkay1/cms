/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于http请求的一些常用服务
 * Created by cwb on 2016/3/24.
 */
@Service
public class HttpServiceImpl implements HttpService {

    @Autowired
    private Environment environment;

    @Override
    public ApiResult<String> httpGet_prod(String scheme, String host, Integer port, String path, Map<String, Object> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port == null ? 80 : port)
                .setPath(path)
                .setParameters(nameValuePairs)
                .build();
        HttpGet httpGet = new HttpGet(uri);
        String random = String.valueOf(System.currentTimeMillis());
        String appKey = environment.getProperty("appKey");
        String appSecret = environment.getProperty("appSecret");
        httpGet.setHeader("_user_key",appKey);
        httpGet.setHeader("_user_random",random);
        httpGet.setHeader("_user_secure",createDigest(appKey,random,appSecret));
        CloseableHttpResponse response = httpClient.execute(httpGet);
        ApiResult<String> apiResult = new ApiResult();
        apiResult.setCode(response.getStatusLine().getStatusCode());
        apiResult.setMsg(response.getStatusLine().getReasonPhrase());
        apiResult.setData(EntityUtils.toString(response.getEntity()));
        return apiResult;
    }

    @Override
    public String createDigest(String appKey, String random, String secret) {
        return DigestUtils.md5Hex(DigestUtils.md5(appKey + random) + secret);
    }
}
