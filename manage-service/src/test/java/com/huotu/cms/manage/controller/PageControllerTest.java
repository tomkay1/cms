/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.cms.manage.ManageTest;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.servlet.CMSFilter;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by hzbc on 2016/7/9.
 */
@Transactional
public class PageControllerTest extends ManageTest {

    private static final String URL = "url";
    private static final String PARAM = "param";
    private static final String CONTENT = "content";
    private static final String MEDIATYPE = "mediaType";
    private static final String METHOD="method";

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    //    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private HttpServletResponse response;
    @Autowired
    private SiteRepository siteRepository;
    private Logger logger = LoggerFactory.getLogger(PageControllerTest.class);

    @Override
    public void createMockMVC() {
        MockitoAnnotations.initMocks(this);
        // ignore it, so it works in no-web fine.
        if (context == null)
            return;
        DefaultMockMvcBuilder builder = webAppContextSetup(context);
        builder.addFilter(new CMSFilter(servletContext));
        if (springSecurityFilter != null) {
            builder = builder.addFilters(springSecurityFilter);
        }

        if (mockMvcConfigurer != null) {
            builder = builder.apply(mockMvcConfigurer);
        }
        mockMvc = builder.build();
    }

    @Test
    public void flow() throws Exception {
        //首先确保虚拟出来的siteId 并没有存在任何页面
        Owner owner = randomOwner();
        Site site = randomSite(owner);
        long siteId = site.getSiteId();
        loginAsOwner(owner);

        mockMvc.perform(
                get("/manage/" + siteId + "/pages")
                        .session(session)
        ).andDo(print());

        Cookie cookie = new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1");
        Map<String, Object> map = new HashMap<>();
        map.put(URL, "/manage/{siteId}/pages");
        map.put(PARAM, siteId);
        map.put(METHOD, HttpMethod.GET.name());
        MvcResult result = accessViaCookie(cookie, map);
        Assert.assertTrue(result.getResponse().getContentType().contains("application/json"));
        int length = JsonPath.read(result.getResponse().getContentAsString(), "$.length()");
        Assert.assertTrue(0 == length);

        //随机创建一个Page
        Page page = randomPage();
        PageInfo pageInfo = randomPageInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        String pageJson = objectMapper.writeValueAsString(page);
        map.clear();
        map.put(URL, "/manage/{siteId}/pages");
        map.put(PARAM, pageInfo.getPageId());
        map.put(CONTENT, pageJson);
        map.put(MEDIATYPE, MediaType.APPLICATION_JSON.toString());
        map.put(METHOD, HttpMethod.PUT.name());
        result = accessViaCookie(cookie, map);
        Assert.assertTrue(result.getResponse().getContentType().contains("application/json"));
        length = JsonPath.read(result.getResponse().getContentAsString(), "$.length()");
        Assert.assertTrue(length > 0);

        //获取上面保存的页面信息
        map.put(URL, "/manage/pages/{pageId}");
        map.put(PARAM, String.valueOf(pageInfo.getPageId()));
        map.put(MEDIATYPE, MediaType.APPLICATION_JSON.toString());
        map.put(METHOD, HttpMethod.GET.name());
        result = accessViaCookie(cookie, map);
        Assert.assertTrue(result.getResponse().getContentType().contains("application/json"));
        pageJson = result.getResponse().getContentAsString();
        length = JsonPath.read(pageJson, "$.length()");
        Assert.assertTrue(length > 0);

        //校验Page信息
        Page getPage = objectMapper.readValue(pageJson, Page.class);
        Assert.assertTrue(page.equals(getPage));


        //删除
        map.clear();
        map.put(URL, "/manage/pages/{pageId}");
        map.put(PARAM, pageInfo.getPageId());
        map.put(METHOD, HttpMethod.DELETE.name());
        result = accessViaCookie(cookie, map);


        // 现在长度应该是0
        map.put(URL, "/manage/pages/{pageId}");
        map.put(PARAM, pageInfo.getPageId());
        map.put(MEDIATYPE, MediaType.APPLICATION_JSON.toString());
        map.put(METHOD, HttpMethod.GET.name());
        result = accessViaCookie(cookie, map);
        Assert.assertTrue(result.getResponse().getContentType().contains("application/json"));
        pageJson = result.getResponse().getContentAsString();
        length = JsonPath.read(pageJson, "$.length()");
        Assert.assertTrue(length == 0);
    }

    /**
     * 对widget json进行校验
     *
     * @throws Exception
     */
    @Test
    public void testGetWidgets() throws Exception {

        /*先确保存在已安装的控件*/
        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
        if (installedWidgets.size() == 0) {
            widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                    , "1.0-SNAPSHOT", "picCarousel");
        }
        Cookie cookie = new Cookie(CMSEnums.CookieKeyValue.RoleID.name(), "-1");
        Map<String, Object> map = new HashMap<>();
        map.put(URL, "/manage/widget/widgets");
        MvcResult result = accessViaCookie(cookie, map);
        String widgetJson = result.getResponse().getContentAsString();
        logger.info("获取到的json：" + widgetJson);
        Assert.assertTrue(widgetJson != null && widgetJson.length() != 0);
        //identity的格式:<groupId>-<widgetId>:<version>
        //此处校验逻辑为：先检索出所有的identity，如果存在groupId和widgetId 一致，但有两个版本号的，视为bug！
        List<String> identities = JsonPath.read(widgetJson, "$..identity");
        Assert.assertTrue(identities.size() != 0);
        for (int i = 0; i < identities.size(); i++) {
            for (int j = i + 1; j <= identities.size() - 1; j++) {
                if (identities.get(i).split(":")[0].equals(identities.get(j).split(":")[0])) {
                    //断言为真，就表明json符合要求
                    Assert.assertEquals(identities.get(i).split(":")[1].equals(identities.get(j).split(":")[1]), true);
                }
            }
        }
    }

//    @Before
//    public void putCMSContext() {
//        List<Site> sites = siteRepository.findAll();
//        Site site = null;
//        if (sites.size() != 0)
//            site = sites.get(0);
//        else
//            site = randomSite(randomOwner());
//        CMSContext.PutContext(request, response, site);
//    }

    /**
     * 通过伪造cookie验证权限
     *
     * @param cookie 模拟个Cookie
     * @param map    参数map
     * @return MvcResult
     * @throws Exception
     */
    private MvcResult accessViaCookie(Cookie cookie, Map<String, Object> map) throws Exception {

        String url = (String) map.get(URL);
        Object params = map.get(PARAM);
        String content = (String) map.get(CONTENT);
        if(content==null)
            content="";
        String mediaType = (String) map.get(MEDIATYPE);
        if(mediaType==null)
            mediaType=MediaType.ALL_VALUE;
        String method= (String) map.get(METHOD);
        HttpMethod httpMethod=HttpMethod.valueOf(method);

        MockHttpServletRequestBuilder builder=null;

        switch (httpMethod){
            case GET:
                builder=get(url, params).cookie(cookie);
                break;
            case POST:
                builder=post(url, params).content(content).accept(mediaType);
                break;
            case PUT:
                builder=put(url, params).content(content).accept(mediaType);
                break;
            case DELETE:
                builder=delete(url, params).content(content).accept(mediaType);
        }




        MvcResult result = mockMvc.perform(builder)
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(true);

        String redirectedUrl = mockMvc.perform(get(result.getResponse().getRedirectedUrl())
                .cookie(cookie)
                .session(session))
                .andExpect(status().isFound())
                .andReturn().getResponse().getRedirectedUrl();

        while (true) {
            result = mockMvc.perform(get(redirectedUrl).session(session)).andReturn();
            if (result.getResponse().getStatus() == 200) {
                session = (MockHttpSession) result.getRequest().getSession(true);
                return mockMvc.perform(builder.session(session))
                        .andReturn();
            }
            if (result.getResponse().getStatus() == 302)
                redirectedUrl = result.getResponse().getRedirectedUrl();
            else
                throw new IllegalStateException("why ?" + result.getResponse().getStatus());
        }
    }



}
