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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private static final String METHOD = "method";

    @Autowired
    private WidgetFactoryService widgetFactoryService;

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
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());


        //随机创建一个Page
        Page page = randomPage();
        PageInfo pageInfo = randomPageInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        String pageJson = objectMapper.writeValueAsString(page);
       mockMvc.perform(put("/manage/{siteId}/pages", siteId)
                .session(session)
                .accept(MediaType.APPLICATION_JSON)
                .content(pageJson))
                .andExpect(status().isOk());

        //获取上面保存的页面信息

        MvcResult result=  mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        pageJson=result.getResponse().getContentAsString();
        //校验Page信息
        Page getPage = objectMapper.readValue(pageJson, Page.class);
        Assert.assertTrue(page.equals(getPage));


        //删除

        mockMvc.perform(delete("/manage/pages/{pageId}",pageInfo).session(session))
                .andExpect(status().isOk());


        // 现在长度应该是0
        mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }

    /**
     * 对widget json进行校验
     *
     * @throws Exception
     */
    @Test
    public void testGetWidgets() throws Exception {

        Owner owner = randomOwner();
        loginAsOwner(owner);

        /*先确保存在已安装的控件*/
        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
        if (installedWidgets.size() == 0) {
            widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                    , "1.0-SNAPSHOT", "picCarousel");
        }
        MvcResult result =mockMvc.perform(get("/manage/widget/widgets").session(session))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String widgetJson = result.getResponse().getContentAsString();
        logger.info("获取到的json：" + widgetJson);
        Assert.assertTrue(widgetJson != null && widgetJson.length() != 0);
        //identity的格式:<groupId>-<widgetId>:<version>
        //此处校验逻辑为：先检索出所有的identity，如果存在groupId和widgetId 一致，但有两个版本号的，视为bug！
        List<String> identities = JsonPath.read(widgetJson, "$..identity");
        Assert.assertTrue(identities.size() != 0);

        //json中不应该出现同一个组件
        for (int i = 0; i < identities.size(); i++) {
            for (int j = i + 1; j <= identities.size() - 1; j++) {
                Assert.assertEquals(identities.get(i).split(":")[0].equals(identities.get(j).split(":")[0]), false);
            }
        }
        //json组件版本不应该有高低
//        for (int i = 0; i < identities.size(); i++) {
//            for (int j = i + 1; j <= identities.size() - 1; j++) {
//                if (identities.get(i).split(":")[0].equals(identities.get(j).split(":")[0])) {
//                    //断言为真，就表明json符合要求
//                    Assert.assertEquals(identities.get(i).split(":")[1].equals(identities.get(j).split(":")[1]), true);
//                }
//            }
//        }
    }

}
