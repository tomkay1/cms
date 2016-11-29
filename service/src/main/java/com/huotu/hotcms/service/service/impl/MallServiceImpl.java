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
import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.hotcms.service.service.MallService;
import com.huotu.hotcms.service.util.CookieHelper;
import com.huotu.huobanplus.common.entity.Brand;
import com.huotu.huobanplus.common.entity.Category;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.BrandRestRepository;
import com.huotu.huobanplus.sdk.common.repository.CategoryRestRepository;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
@Service
public class MallServiceImpl implements MallService {
    private static final Log log = LogFactory.getLog(MallServiceImpl.class);

    @Autowired
    private CategoryRestRepository categoryRestRepository;
    @Autowired
    private BrandRestRepository brandRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    @Override
    public List<Category> listCategories(long merchantId) throws IOException {
        return categoryRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public List<Brand> listBrands(long merchantId) throws IOException {
        return brandRestRepository.listByMerchantId(merchantId);
    }

    @Override
    public User getLoginUser(HttpServletRequest request, Owner owner) throws IOException {
        if (owner.getCustomerId() == null)
            throw new IllegalArgumentException("必须开启伙伴商城。");

        Integer id = CookieHelper.getCookieValInteger(request, "userid_" + owner.getCustomerId());
        if (id == null)
            return null;
        return userRestRepository.getOneByPK(id);
    }

    @Override
    public boolean isLogin(HttpServletRequest request, Owner owner) {
        if (owner.getCustomerId() == null)
            throw new IllegalArgumentException("必须开启伙伴商城。");

        Integer id = CookieHelper.getCookieValInteger(request, "userid_" + owner.getCustomerId());
        return id != null;
    }

    @Override
    public String getLoginUserName(HttpServletRequest request, Owner owner) throws IOException {
        return getLoginUser(request, owner).getLoginName();
    }

    @Override
    public String getMallDomain(Owner owner) throws IOException {
        //todo
        return "http://www.baidu.com";
    }

    @Override
    public User mallLogin(Owner owner, String username, String password, HttpServletResponse response) throws IOException, LoginException {
        // 创建HttpPost
        HttpPost httppost = new HttpPost(getMallDomain(owner) + "/Mall/UserCenter/Login/" + owner.getCustomerId());
        // 创建参数队列
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("username", username));
        basicNameValuePairs.add(new BasicNameValuePair("password", password));
        String result = getResult(httppost, basicNameValuePairs);
        return null;

    }


    @Override
    public User mallRegister(Owner owner, String username, String password, HttpServletResponse response) throws IOException, RegisterException {
        // 创建HttpPost
        HttpPost httpPost = new HttpPost(getMallDomain(owner) + "/Mall/UserCenter/Register" + owner.getCustomerId());
        // 创建参数队列
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("username", username));
        basicNameValuePairs.add(new BasicNameValuePair("password", password));
        basicNameValuePairs.add(new BasicNameValuePair("sourceType", "PC"));
        String result = getResult(httpPost, basicNameValuePairs);
        return null;
    }

    @Nullable
    private String getResult(HttpPost httppost, List<BasicNameValuePair> basicNameValuePairs) throws IOException {
        UrlEncodedFormEntity uefEntity;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            uefEntity = new UrlEncodedFormEntity(basicNameValuePairs, "UTF-8");
            httppost.setEntity(uefEntity);
            log.info("executing request " + httppost.getURI());
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    log.info("--------------------------------------");
                    log.info("Response content: " + result);
                    log.info("--------------------------------------");
                    return result;
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return null;
    }


}
