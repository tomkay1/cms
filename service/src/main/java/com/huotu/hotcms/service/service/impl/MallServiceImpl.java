/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.service.MallService;
import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.BrandRestRepository;
import com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
@Service
public class MallServiceImpl implements MallService {

    @Autowired
    private CategoryRestRepository categoryRestRepository;
    @Autowired
    private BrandRestRepository brandRestRepository;

    @Override
    public List<Category> listCategories(long merchantId) throws IOException {
        return categoryRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public List<Brand> listBrands(long merchantId) throws IOException {
        return brandRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public User getLoginUser() {
        //todo
        return null;
    }

    @Override
    public boolean isLogin() {
        return getLoginUser() != null;
    }

    @Override
    public String getLoginUserName() {
        if (isLogin()) {
            return getLoginUser().getLoginName();
        }
        return null;
    }

    @Override
    public String getMallDomain(Owner owner) {
        //todo
        return "http://www.baidu.com";
    }

    @Override
    public String mallLogin(Owner owner, String username, String password) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建HttpPost
        HttpPost httppost = new HttpPost(getMallDomain(owner) + "/Mall/UserCenter/Login/" + owner.getCustomerId());
        // 创建参数队列
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("username", username));
        basicNameValuePairs.add(new BasicNameValuePair("password", password));
        return getResult(httpclient, httppost, basicNameValuePairs);
    }


    @Override
    public String mallRegister(Owner owner, String username, String password) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HttpPost
        HttpPost httpPost = new HttpPost(getMallDomain(owner) + "/Mall/UserCenter/Register" + owner.getCustomerId());
        // 创建参数队列
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("username", username));
        basicNameValuePairs.add(new BasicNameValuePair("password", password));
        basicNameValuePairs.add(new BasicNameValuePair("sourceType", "PC"));
        return getResult(httpClient, httpPost, basicNameValuePairs);
    }

    @Nullable
    private String getResult(CloseableHttpClient httpclient, HttpPost httppost, List<BasicNameValuePair> basicNameValuePairs) {
        UrlEncodedFormEntity uefEntity;
        String result = null;
        try {
            uefEntity = new UrlEncodedFormEntity(basicNameValuePairs, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + result);
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
