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
import com.huotu.cms.manage.service.SecurityService;
import com.huotu.hotcms.service.common.CMSEnums;
import com.huotu.hotcms.service.entity.PageInfo;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.repository.PageInfoRepository;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.page.Page;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.httpclient.Header;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
public class PageControllerTest extends ManageTest {

    @Autowired
    private PageInfoRepository pageInfoRepository;

    @Autowired
    private WidgetFactoryService widgetFactoryService;

    @Autowired
    private  OwnerRepository ownerRepository;


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
        InputStream is= this.getClass().getClassLoader().getResourceAsStream("page.json");
        String pageJson= StreamUtils.copyToString(is, Charset.forName("utf-8"));

        // 保存它 save
        String pageHref = mockMvc.perform(put("/manage/pages/{siteId}", siteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pageJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        // 单独获取 pageID为1的原因是json中保存的
        mockMvc.perform(get("/manage/pages/{pageId}",1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // TODO 更多数据校验 以确保返回的数据 是之前创建的Page
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

    @Test
    public void readJson() throws IOException {
        InputStream is= this.getClass().getClassLoader().getResourceAsStream("page.json");
        String pageJson= StreamUtils.copyToString(is, Charset.forName("utf-8"));
        ObjectMapper objectMapper=new ObjectMapper();
        Page page=objectMapper.readValue(pageJson, Page.class);
    }

    @Test
    public void testGetPage() throws Exception {
        PageInfo pageInfo=pageInfoRepository.findAll().get(0);//先查找一个已存在的PageInfo
        if(pageInfo==null) //如果不存在就随机创建一个，新创建的PageInfo已经初始化页面信息
            pageInfo=randomPageInfo();
        mockMvc.perform(get("/manage/pages/{pageId}", pageInfo.getPageId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * 对widget json进行校验
     * <i>此测试权限真心蛋疼</i>
     * @see #testJsonPath()
     * @throws Exception
     */
    @Test
    public void testGetWidgets() throws Exception {

        /*先确保存在已安装的控件*/
        List<InstalledWidget> installedWidgets= widgetFactoryService.widgetList(null);
        if(installedWidgets.size()==0){
            widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                    , "1.0-SNAPSHOT", "picCarousel");
        }
        loginAsManage();
        MvcResult result = mockMvc.perform(get("/manage/widget/widgets"))
                .andExpect(status().isFound())
                .andReturn();
        String widgetJson=result.getResponse().getContentAsString();
        //identity的格式:<groupId>-<widgetId>:<version>
        //此处校验逻辑为：先检索出所有的identity，如果存在groupId和widgetId 一致，但有两个版本号的，视为bug！
        List<String> identities=JsonPath.read(widgetJson,"$.identity[*]");

    }
    @Test
    public void testJsonPath() throws IOException {
        //直接从文件读出
        InputStream is= this.getClass().getClassLoader().getResourceAsStream("widget.json");
        String widgetJson= StreamUtils.copyToString(is, Charset.forName("utf-8"));
        List<String> identities=JsonPath.read(widgetJson,"$..identity");
        for (int i = 0; i <identities.size() ; i++) {
            for (int j = i+1; j <=identities.size() ; j++) {
                if(identities.get(i).split(":")[0].equals(identities.get(j).split(":")[0])){
                    //断言为真，就表明json符合要求
                    Assert.assertEquals(identities.get(i).split(":")[1].equals(identities.get(j).split(":")[1]),true);
                }
            }
        }
    }

}
