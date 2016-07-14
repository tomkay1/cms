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
import com.huotu.cms.manage.login.Manager;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.service.repository.SiteRepository;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hzbc on 2016/7/9.
 */
@Transactional
public class PageControllerTest extends ManageTest {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void flow() throws Exception {
        //首先确保虚拟出来的siteId 并没有存在任何页面
        Owner owner = randomOwner();
        Site site = randomSite(owner);
        long siteId = site.getSiteId();
        mockMvc.perform(get("/manage/{siteId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()").value(0));//exception:json can not be null or empty
//      Page page = randomPage();
//      String json = JSON.toJSONString(page);
//      创建一个page,page应该是从界面上put上来的,此处从测试类路劲下的page.json中获取
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("page.json");
        String pageJson = StreamUtils.copyToString(is, Charset.forName("utf-8"));

        // 保存它 save
        String pageHref = mockMvc.perform(put("/manage/pages/{siteId}", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pageJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        // 单独获取 pageID为1的原因是json中保存的
        mockMvc.perform(get("/manage/pages/{pageId}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect()
        ;

        //保存页面部分属性
        String propertyName = UUID.randomUUID().toString();
        mockMvc.perform(put("/manage/pages/{pageId}/{propertyName}", 1, propertyName)).andDo(print())
                .andExpect(status().isAccepted())
                .andReturn();


        mockMvc.perform(get("/manage/owners/{ownerId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
        // https://github.com/jayway/JsonPath
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
//        .andExpect(jsonPath("$.[0]").value(..))
        ;

        // 删除
        mockMvc.perform(delete(pageHref))
                .andExpect(status().isNoContent());
        // 现在长度应该是0
        mockMvc.perform(get("/manage/owners/{ownerId}/pages", siteId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
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
        MvcResult result = accessViaCookie(cookie, new Manager(), "/manage/widget/widgets");
        String widgetJson = result.getResponse().getContentAsString();
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

    @Before
    public void putCMSContext() {
        List<Site> sites = siteRepository.findAll();
        Site site = null;
        if (sites.size() != 0)
            site = sites.get(0);
        else
            site = randomSite(randomOwner());
        CMSContext.PutContext(request, response, site);
    }

    /**
     * 完善
     * @param cookie
     * @param login
     * @param url
     * @return
     * @throws Exception
     */
    private MvcResult accessViaCookie(Cookie cookie, Login login, String url) throws Exception {
        MvcResult result = mockMvc.perform(get(url)
                .cookie(cookie))
                .andExpect(status().isFound())
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
                return mockMvc.perform(get(url)
                        .cookie(cookie)
                        .session(session))
                        .andExpect(status().isOk())
                        .andReturn();
            }
            if (result.getResponse().getStatus() == 302)
                redirectedUrl = result.getResponse().getRedirectedUrl();
            else
                throw new IllegalStateException("why ?" + result.getResponse().getStatus());
        }
    }

}
