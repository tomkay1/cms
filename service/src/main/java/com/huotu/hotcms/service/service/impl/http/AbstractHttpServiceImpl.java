/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl.http;

import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public abstract class AbstractHttpServiceImpl implements HttpService {
    String appRoot = "http://test.api.open.huobanplus.com:8081";
    String appKey = "_demo";
    String appSecret = "1f2f3f4f5f6f7f8f";
    private Log log = LogFactory.getLog(getClass());

//    @Override
//    public ApiResult<String> httpGet_prod(String scheme, String host, Integer port, String path, Map<String, Object> params) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        List<NameValuePair> nameValuePairs = new ArrayList<>();
//        params.forEach((key,value)->{
//            if(value != null) {
//                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
//            }
//        });
//        ApiResult<String> apiResult = new ApiResult<>();
//        URI uri;
//        try {
//            uri = new URIBuilder()
//                    .setScheme(scheme)
//                    .setHost(host)
//                    .setPort(port == null ? 80 : port)
//                    .setPath(path)
//                    .setParameters(nameValuePairs)
//                    .build();
//            HttpGet httpGet = new HttpGet(uri);
//            String random = String.valueOf(System.currentTimeMillis());
//            httpGet.setHeader("_user_key", appKey);
//            httpGet.setHeader("_user_random", random);
//            httpGet.setHeader("_user_secure", createDigest(appKey, random, appSecret));
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            apiResult.setCode(response.getStatusLine().getStatusCode());
//            apiResult.setMsg(response.getStatusLine().getReasonPhrase());
//            apiResult.setData(EntityUtils.toString(response.getEntity()));
//        } catch (URISyntaxException e) {
//            System.out.println("url语法错误");
//            log.error("url语法错误");
//        }catch (IOException e) {
//            System.out.println("接口服务不可用");
//            log.error("接口服务不可用");
//        }
//        return apiResult;
//    }

    @Override
    public ApiResult<String> httpGet_prod(String path, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        ApiResult<String> apiResult = new ApiResult<>();
        URI uri;
        try {
            uri = new URIBuilder(this.appRoot)
                    .setParameters(nameValuePairs)
                    .setPath(path)
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            String random = String.valueOf(System.currentTimeMillis());
            httpGet.setHeader("_user_key", appKey);
            httpGet.setHeader("_user_random", random);
            httpGet.setHeader("_user_secure", createDigest(appKey, random, appSecret));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            apiResult.setCode(response.getStatusLine().getStatusCode());
            apiResult.setMsg(response.getStatusLine().getReasonPhrase());
            apiResult.setData(EntityUtils.toString(response.getEntity()));
        } catch (URISyntaxException e) {
            System.out.println("url语法错误");
            log.error("url语法错误");
        }catch (IOException e) {
            System.out.println("接口服务不可用");
            log.error("接口服务不可用");
        }
        return apiResult;
    }

    @Override
    public String createDigest(String appKey, String random, String secret) throws UnsupportedEncodingException {
        byte[] key1 = new byte[0];
        try {
            key1 = DigestUtils.md5Digest((appKey + random).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] key2 = Hex.decode(secret);
        byte[] key = new byte[key1.length + key2.length];
        System.arraycopy(key1, 0, key, 0, key1.length);
        System.arraycopy(key2, 0, key, key1.length, key2.length);
        return  DigestUtils.md5DigestAsHex(key);
    }
}
