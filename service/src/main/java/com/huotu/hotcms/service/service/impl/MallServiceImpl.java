/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.hotcms.service.service.ConfigService;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author CJ
 */
@Service
public class MallServiceImpl implements MallService {
    private static final Log log = LogFactory.getLog(MallServiceImpl.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ConfigService configService;
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
        return apiResult("/MallApi/MallConfig/Domain/" + owner.getCustomerId()
                , null
                , JsonNode::textValue);
    }

    @Override
    public User mallLogin(Owner owner, String username, String password, HttpServletResponse response)
            throws IOException, LoginException {
        final User user = apiResult("/MallApi/Account/Login/" + owner.getCustomerId()
                , nameValuePair -> new LoginException(nameValuePair.getName())
                , json -> {
                    try {
                        log.debug("PC-API Login:" + json);
                        return userRestRepository.getOneByPK(json.asText());
                    } catch (IOException e) {
                        throw new IllegalStateException("PC-API response bad ID", e);
                    }
                }, new BasicNameValuePair("username", username)
                , new BasicNameValuePair("password"
                        , DigestUtils.md5DigestAsHex(password.getBytes("UTF-8")).toLowerCase(Locale.CHINA)));
        return makeUserLogin(owner, user, response);
    }

    private User makeUserLogin(Owner owner, User user, HttpServletResponse response) throws IOException {
        if (response == null)
            return user;
        //获取内购页地址
        String domain = getMallDomain(owner);
        // pcsite.pcpdmall.com 获取最高域名
        domain = domain.substring(domain.indexOf("."));

        while (domain.indexOf(".", 1) != domain.lastIndexOf(".")) {
            domain = domain.substring(domain.indexOf(".", 1));
        }

        Cookie cookie = new Cookie("userid_" + owner.getCustomerId(), String.valueOf(user.getId()));
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);

        cookie = new Cookie("levelid_" + owner.getCustomerId(), String.valueOf(user.getLevelId()));
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
        return user;
    }

    @Override
    public User mallRegister(Owner owner, String username, String password, HttpServletResponse response)
            throws IOException, RegisterException {
        final User user = apiResult("/MallApi/Account/Register/" + owner.getCustomerId()
                , nameValuePair -> new RegisterException(nameValuePair.getName())
                , json -> {
                    try {
                        log.debug("PC-API Register:" + json);
                        return userRestRepository.getOneByPK(json.get("userid").asText());
                    } catch (IOException e) {
                        throw new IllegalStateException("PC-API response bad ID", e);
                    }
                }, new BasicNameValuePair("username", username)
                , new BasicNameValuePair("password", password)
                , new BasicNameValuePair("sourceType", "PC"));
        return makeUserLogin(owner, user, response);
    }

    /**
     * 执行一个API并且获得其结果
     *
     * @param uri         比如 /Mall/MallConfig..
     * @param toException 当发现响应码并非2000时,我们会将响应码和消息丢给异常生成器
     * @param toResult    跟toException相反,获取到了2000响应码时,我们就会尝试解析其中的data
     * @param parameters  传入参数
     * @param <T>         期待的结果
     * @return 执行的结果
     * @throws IOException
     */
    private <T, X extends Exception> T apiResult(String uri, Function<NameValuePair, X> toException
            , Function<JsonNode, T> toResult
            , NameValuePair... parameters) throws IOException, X {
        final String domain = configService.getMallDomain();
//        final String domain = "pcpdmall.com";
        HttpUriRequest request;
        if (parameters.length == 0) {
            request = new HttpGet("http://pcsite." + domain + uri);
        } else {
            HttpPost post = new HttpPost("http://pcsite." + domain + uri);
            HttpEntity entity = EntityBuilder.create()
                    .setContentType(ContentType.create(URLEncodedUtils.CONTENT_TYPE, "UTF-8"))
                    .setParameters(parameters)
                    .build();
            post.setEntity(entity);
            request = post;
        }

        log.debug("PC-API " + request);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // 文档没有提及响应码规则 无视所有规则 直接处理内容
                try (InputStream inputStream = response.getEntity().getContent()) {
                    JsonNode root = objectMapper.readTree(inputStream);
                    JsonNode resultCode = root.get("resultCode");
                    if (resultCode.intValue() != 2000) {
                        // 走异常线路
                        throw toException.apply(new BasicNameValuePair(root.get("resultMsg").asText()
                                , resultCode.textValue()));
                    }
                    return toResult.apply(root.get("data"));
                }
            }
        }
    }


}
